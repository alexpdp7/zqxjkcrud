package net.pdp7.zqxjkcrud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.selenium.BrowserWebDriverContainer;

@org.testcontainers.junit.jupiter.Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = SmokeTest.Initializer.class)
public class SmokeTest {

  @Container
  public BrowserWebDriverContainer firefox =
      new BrowserWebDriverContainer("selenium/standalone-firefox:4.13.0");

  @LocalServerPort public int port;

  @Test
  public void test() {
    firefox.start();
    RemoteWebDriver driver =
        new RemoteWebDriver(firefox.getSeleniumAddress(), new FirefoxOptions());
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
