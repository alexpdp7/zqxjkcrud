package net.pdp7.zqxjkcrud;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = SmokeTest.Initializer.class)
public class SmokeTest {

  @Rule
  public BrowserWebDriverContainer<?> firefox =
      new BrowserWebDriverContainer<>().withCapabilities(new FirefoxOptions());

  @LocalServerPort public int port;

  @Test
  public void test() {
    Testcontainers.exposeHostPorts(port);
    RemoteWebDriver driver = firefox.getWebDriver();
    driver.get("http://host.testcontainers.internal:" + port + "/");
    assertEquals("Please sign in", driver.getTitle());
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).sendKeys("admin");
    driver.findElement(By.tagName("button")).click();
    assertEquals("admin", driver.findElement(By.tagName("span")).getText());
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
