package net.pdp7.zqxjkcrud.dao;

import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;

import schemacrawler.schema.Catalog;

public class Dao {

	protected final CatalogRepository catalogRepository;
	protected final DSLContext dslContext;

	public Dao(CatalogRepository catalogRepository, DSLContext dslContext) {
		this.catalogRepository = catalogRepository;
		this.dslContext = dslContext;
	}

	protected Catalog getCatalog() {
		return catalogRepository.getCatalog();
	}

	public Map<String, Table> getTables() {
		return getCatalog().getTables()
				.stream()
				.filter(this::isVisibleTable)
				.collect(Collectors.toMap(t -> t.getName(), t -> new Table(t, dslContext)));
	}

	protected boolean isVisibleTable(schemacrawler.schema.Table table) {
		return !table.getName().startsWith("_");
	}
}
