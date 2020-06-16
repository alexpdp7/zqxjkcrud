package net.pdp7.zqxjkcrud;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pdp7.zqxjkcrud.dao.CatalogRepository;
import net.pdp7.zqxjkcrud.dao.Dao;
import net.pdp7.zqxjkcrud.security.UserDetailsServiceImpl;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;

@SpringBootApplication
@Configuration
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
		return SchemaCrawlerOptionsBuilder.builder()
				.withLimitOptions(LimitOptionsBuilder.builder().includeSchemas(s -> s.equals(zqxjkSchema)).toOptions())
				.toOptions();
	}

	@Bean
	public CatalogRepository catalogRepository() {
		return new CatalogRepository(dslContext, schemaCrawlerOptions());
	}

	@Bean
	public Dao dao() {
		return new Dao(catalogRepository(), dslContext);
	}

	public static void main(String[] args) {
		SpringApplication.run(ZqxjkCrudApp.class, args);
	}
}
