package net.pdp7.zqxjkcrud.dao;

import java.util.stream.Stream;

import schemacrawler.schema.Catalog;

public class Dao {

	protected final CatalogRepository catalogRepository;

	public Dao(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	protected Catalog getCatalog() {
		return catalogRepository.getCatalog();
	}

	protected Stream<String> getTables() {
		return getCatalog().getTables().stream().map(t -> t.getName());
	}

	public Stream<String> getViewableTables() {
		return getTables().filter(Dao::isViewableTable);
	}

	public static boolean isViewableTable(String tableName) {
		return !tableName.startsWith("_");
	}
}
