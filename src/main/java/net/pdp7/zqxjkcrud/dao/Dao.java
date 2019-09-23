package net.pdp7.zqxjkcrud.dao;

import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import net.pdp7.zqxjkcrud.dao.Update.TableUpdate;
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
				.collect(
						Collectors
								.toMap(t -> t.getName(), t -> new Table(t, dslContext)));
	}

	protected boolean isVisibleTable(schemacrawler.schema.Table table) {
		return !table.getName().startsWith("_");
	}

	public void update(Update update) {
		for (TableUpdate tableUpdate : update.tableUpdates) {
			Map<Field<Object>, Object> fields = tableUpdate.fields.entrySet()
					.stream()
					.collect(
							Collectors.toMap(
									e -> DSL.field((String) e.getKey()),
									e -> e.getValue()));
			switch (tableUpdate.action) {
			case INSERT:
				dslContext.insertInto(DSL.table(tableUpdate.table.getName()))
						.set(fields)
						.execute();
				break;
			case UPDATE:
				dslContext.update(DSL.table(tableUpdate.table.getName()))
						.set(fields)
						.where(DSL.field("_id").cast(String.class).equal(tableUpdate.id))
						.execute();
				break;
			default:
				throw new RuntimeException("Unknown operation");
			}
		}
	}
}
