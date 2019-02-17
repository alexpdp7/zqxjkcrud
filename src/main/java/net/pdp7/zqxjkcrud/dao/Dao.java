package net.pdp7.zqxjkcrud.dao;

import java.util.Map;
import java.util.stream.Collectors;

import schemacrawler.schema.Catalog;

public class Dao {

	protected final CatalogRepository catalogRepository;

	public Dao(CatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}

	protected Catalog getCatalog() {
		return catalogRepository.getCatalog();
	}

	public Map<String, Table> getTables() {
		return getCatalog().getTables()
				.stream()
				.filter(this::isVisibleTable)
				.collect(Collectors.toMap(t -> t.getName(), Table::new));
	}

	protected boolean isVisibleTable(schemacrawler.schema.Table table) {
		return !table.getName().startsWith("_");
	}
}
