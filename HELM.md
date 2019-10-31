As an exercise to learn K8S and Helm, a minimal, bad Helm Chart is included to run the demo.

To use it:

[Get Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)

```
$ minikube delete  # if you want to start fresh
$ minikube start
```

[Get Helm](https://helm.sh/docs/using_helm/#installing-helm)

```
$ helm init
$ helm install charts/zqxjkcrud -n zq

$ kubectl get pods  # wait until it shows something like:
NAME                            READY   STATUS    RESTARTS   AGE
zq-postgresql-0                 1/1     Running   0          83s
zq-zqxjkcrud-5dfcc796cd-8lxnw   0/1     Running   0          83s

# follow the instructions printed in helm install to forward a port and access
```

To clean up:

```
$ helm del --purge zq
$ kubectl delete pvc data-zq-postgresql-0
```
