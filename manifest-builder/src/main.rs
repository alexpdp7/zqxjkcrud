use std::collections::BTreeMap;

use k8s_openapi::api::apps::v1::Deployment;
use k8s_openapi::api::core::v1::{ConfigMap, Namespace, Secret, Service};
use k8s_openapi::api::networking::v1::Ingress;
use krust_manifesto::core::*;
use krust_manifesto::meta::*;
use krust_manifesto::util::{combine_yamls, to_yaml};
use krust_manifesto::workload::*;
use serde::{Deserialize, Serialize};

struct Zqxjkcrud {
    namespace: Namespace,
    deployment: Deployment,
    service: Service,
    ingress: Ingress,
    config_map: ConfigMap,
    secret: Secret,
}

impl Zqxjkcrud {
    fn create(configuration: Configuration) -> Zqxjkcrud {
        let namespace = namespace(&configuration.namespace);
        let (deployment, services) = deployment(
            &namespace,
            &configuration.instance,
            &configuration
                .image
                .unwrap_or("quay.io/alexpdp7/zqxjkcrud:openid".into()),
            vec![container_port(8080, Protocol::TCP)],
        );
        let service = &services[0];
        let ingress = service.ingress(&configuration.host_name);
        let mut configs = BTreeMap::from([
            (
                "SPRING_DATASOURCE_URL".into(),
                configuration.database.as_jdbc_url(),
            ),
            (
                "SPRING_DATASOURCE_DRIVER_CLASS_NAME".into(),
                configuration.database.get_driver_class_name(),
            ),
            ("ZQXJK_SCHEMA".into(), configuration.database.schema),
            ("SERVER_FORWARD_HEADERS_STRATEGY".into(), "NATIVE".into()),
        ]);
        let mut secrets: BTreeMap<String, String> = BTreeMap::new();
        for i in configuration.ipsilon_oidc.unwrap_or(vec![]) {
            configs.extend([
                (
                    format!(
                        "SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_{}_CLIENT_ID",
                        i.id.to_ascii_uppercase(),
                    ),
                    i.client_id,
                ),
                (
                    format!(
                        "SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_{}_SCOPE",
                        i.id.to_ascii_uppercase(),
                    ),
                    "openid,profile".into(),
                ),
                (
                    format!(
                        "SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_{}_REDIRECT_URI",
                        i.id.to_ascii_uppercase(),
                    ),
                    format!(
                        "https://{}/login/oauth2/code/login-client",
                        configuration.host_name
                    ),
                ),
                (
                    format!(
                        "SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_{}_ISSUER_URI",
                        i.id.to_ascii_uppercase(),
                    ),
                    format!("{}/openidc/", i.url),
                ),
            ]);

            secrets.extend([(
                format!(
                    "SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_{}_CLIENT_SECRET",
                    i.id.to_ascii_uppercase(),
                ),
                i.client_secret,
            )]);
        }
        let config_map = config_map(&namespace, &configuration.instance, configs);
        let secret = secret(&namespace, &configuration.instance, secrets);
        let deployment = deployment.set_env_from_config_map(&config_map);
        let deployment = deployment.set_env_from_secret(&secret);
        Zqxjkcrud {
            namespace,
            deployment,
            service: service.clone(),
            ingress,
            config_map,
            secret,
        }
    }

    fn yamls(&self) -> Vec<String> {
        vec![
            to_yaml(&self.namespace),
            to_yaml(&self.deployment),
            to_yaml(&self.service),
            to_yaml(&self.ingress),
            to_yaml(&self.config_map),
            to_yaml(&self.secret),
        ]
    }

    fn as_yaml(&self) -> String {
        combine_yamls(self.yamls())
    }
}

#[derive(Debug, PartialEq, Serialize, Deserialize)]
enum DatabaseKind {
    Postgresql,
}

#[derive(Debug, PartialEq, Serialize, Deserialize)]
struct DatabaseConfiguration {
    kind: DatabaseKind,
    host_name: String,
    database: String,
    user: String,
    schema: String,
}

impl DatabaseConfiguration {
    fn as_jdbc_url(&self) -> String {
        match self.kind {
            DatabaseKind::Postgresql => {
                format!(
                    "jdbc:postgresql://{}/{}?user={}&currentSchema={}",
                    self.host_name, self.database, self.user, self.schema
                )
            }
        }
    }

    fn get_driver_class_name(&self) -> String {
        match self.kind {
            DatabaseKind::Postgresql => "org.postgresql.Driver".into(),
        }
    }
}

#[derive(Debug, PartialEq, Serialize, Deserialize)]
struct IpsilonOidcConfiguration {
    id: String,
    client_id: String,
    client_secret: String,
    url: String,
}

#[derive(Debug, PartialEq, Serialize, Deserialize)]
struct Configuration {
    namespace: String,
    host_name: String,
    instance: String,
    image: Option<String>,
    database: DatabaseConfiguration,
    ipsilon_oidc: Option<Vec<IpsilonOidcConfiguration>>,
}

pub(crate) fn main() {
    let config = serde_yaml::from_reader(std::io::stdin()).unwrap();
    let check = Zqxjkcrud::create(config);
    print!("{}", check.as_yaml());
}

#[test]
fn test() {
    let sample_config =
        serde_yaml::from_str(&std::fs::read_to_string("sample-definition.yaml").unwrap()).unwrap();
    let generated = Zqxjkcrud::create(sample_config).as_yaml();
    let expected = std::fs::read_to_string("sample-manifests.yaml").unwrap();
    assert_eq!(generated, expected);
}
