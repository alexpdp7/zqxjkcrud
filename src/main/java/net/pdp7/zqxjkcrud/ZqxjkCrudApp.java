package net.pdp7.zqxjkcrud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import net.pdp7.zqxjkcrud.security.UserDetailsService;

@SpringBootApplication
@Controller
public class ZqxjkCrudApp {

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@Bean
	public UserDetailsService userDetailsService(NamedParameterJdbcTemplate jdbcTemplate) {
		return new UserDetailsService(jdbcTemplate);
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ZqxjkCrudApp.class, args);
	}
}
