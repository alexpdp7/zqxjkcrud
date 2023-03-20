package net.pdp7.zqxjkcrud.dao;

import javax.sql.DataSource;
import org.springframework.cache.annotation.Cacheable;
import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.datasource.DatabaseConnectionSources;

public class CatalogRepository {

  protected final SchemaCrawlerOptions schemaCrawlerOptions;
  protected final DataSource dataSource;

  public CatalogRepository(DataSource dataSource, SchemaCrawlerOptions schemaCrawlerOptions) {
    this.schemaCrawlerOptions = schemaCrawlerOptions;
    this.dataSource = dataSource;
  }

  @Cacheable(cacheNames = "catalogs")
  public Catalog getCatalog() {
    return SchemaCrawlerUtility.getCatalog(
        DatabaseConnectionSources.fromDataSource(dataSource), schemaCrawlerOptions);
  }
}
