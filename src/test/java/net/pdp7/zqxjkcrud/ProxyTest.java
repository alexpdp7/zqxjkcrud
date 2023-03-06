package net.pdp7.zqxjkcrud;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.NginxContainer;
import org.testcontainers.utility.MountableFile;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = ProxyTest.Initializer.class)
public class ProxyTest {

  @LocalServerPort public int port;

  @Test
  public void test() throws Exception {
    Network network = Network.newNetwork();
    try (BrowserWebDriverContainer<?> firefox = new BrowserWebDriverContainer<>();
        NginxContainer<?> nginx = new NginxContainer<>()) {

      firefox.withCapabilities(new FirefoxOptions()).withNetwork(network);
      nginx.withNetwork(network).withNetworkAliases("nginx");

      Testcontainers.exposeHostPorts(port);
      String nginxConf =
          "events {}\n"
              + "http {\n"
              + "    server {\n"
              + "        listen 80;\n"
              + "        location / {\n"
              + "            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n"
              + "            proxy_set_header X-Forwarded-Proto $scheme;\n"
              + "            proxy_set_header X-Forwarded-Port 80;\n"
              + "            proxy_set_header Host $host;\n"
              + "            proxy_pass http://host.testcontainers.internal:"
              + port
              + ";\n"
              + "        }\n"
              + "    }\n"
              + "}\n";
      Path nginxConfPath =
          Files.createTempFile(
              "nginx",
              "conf",
              PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------")));
      Files.writeString(nginxConfPath, nginxConf, StandardOpenOption.CREATE);
      MountableFile nginxConfFile = MountableFile.forHostPath(nginxConfPath, 0600);
      nginx
          .withCopyFileToContainer(nginxConfFile, "/etc/nginx/nginx.conf")
          .withExposedPorts(80)
          .start();
      firefox.start();
      RemoteWebDriver driver = firefox.getWebDriver();
      driver.get("http://nginx/");
      assertEquals("Please sign in", driver.getTitle());
      driver.findElement(By.id("username")).sendKeys("admin");
      driver.findElement(By.id("password")).sendKeys("admin");
      driver.findElement(By.tagName("button")).click();
      assertEquals("admin", driver.findElement(By.tagName("span")).getText());
    }
  }

  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      applicationContext.addApplicationListener(
          (ApplicationListener<WebServerInitializedEvent>)
              event -> {
                Testcontainers.exposeHostPorts(event.getWebServer().getPort());
              });
    }
  }
}
