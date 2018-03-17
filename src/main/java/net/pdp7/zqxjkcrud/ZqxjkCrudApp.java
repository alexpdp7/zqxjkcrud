package net.pdp7.zqxjkcrud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.pdp7.zqxjkcrud.security.UserDetailsServiceImpl;

@SpringBootApplication
@Controller
public class ZqxjkCrudApp {

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@Bean
	public UserDetailsServiceImpl userDetailsService(NamedParameterJdbcTemplate jdbcTemplate) {
		return new UserDetailsServiceImpl(jdbcTemplate);
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(ZqxjkCrudApp.class, args);
	}
}
