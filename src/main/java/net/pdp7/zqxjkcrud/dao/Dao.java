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

	public Stream<Table> getTables() {
		return getCatalog().getTables().stream().filter(t -> !t.getName().startsWith("_")).map(t -> new Table(t));
	}
}
