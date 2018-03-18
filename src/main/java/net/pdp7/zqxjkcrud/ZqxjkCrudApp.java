package net.pdp7.zqxjkcrud;

import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.ImmutableMap;

import net.pdp7.zqxjkcrud.dao.CatalogRepository;
import net.pdp7.zqxjkcrud.dao.Dao;
import net.pdp7.zqxjkcrud.security.UserDetailsServiceImpl;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;

@SpringBootApplication
@Controller
public class ZqxjkCrudApp {

	@Autowired
	public DSLContext dslContext;

	@Bean
	public UserDetailsServiceImpl userDetailsService() {
		return new UserDetailsServiceImpl(dslContext);
	}

	@Value("${zqxjk.schema}")
	public String zqxjkSchema;

	@Bean
	public SchemaCrawlerOptions schemaCrawlerOptions() {
		SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		options.setSchemaInclusionRule(s -> s.equals(zqxjkSchema));
		return options;
	}

	@Bean
	public CatalogRepository catalogRepository() {
		return new CatalogRepository(dslContext, schemaCrawlerOptions());
	}

	@Bean
	public Dao dao() {
		return new Dao(catalogRepository());
	}

	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index", ImmutableMap.<String, Object>builder()
				.put("tables", dao().getTables().iterator()).build());
	}

	public static void main(String[] args) {
		SpringApplication.run(ZqxjkCrudApp.class, args);
	}
}
