package net.pdp7.zqxjkcrud.security;

import com.github.ulisesbocchio.spring.boot.security.saml.annotation.EnableSAMLSSO;
import com.github.ulisesbocchio.spring.boot.security.saml.configurer.ServiceProviderConfigurer;
import com.github.ulisesbocchio.spring.boot.security.saml.configurer.ServiceProviderConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Profile("saml")
@EnableSAMLSSO
public class SamlConfiguration extends WebSecurityConfigurerAdapter {
  @Bean
  public ServiceProviderConfigurer serviceProviderConfigurer() {
    return new ServiceProviderConfigurerAdapter() {
      @Override
      public void configure(HttpSecurity http) {
        try {
          http.csrf().ignoringAntMatchers("/saml/**");
          http.anonymous().disable();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}
