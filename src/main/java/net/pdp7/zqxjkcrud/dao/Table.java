package net.pdp7.zqxjkcrud.dao;

import java.util.List;

import org.jooq.DSLContext;

public class Table {

	protected final schemacrawler.schema.Table table;
	protected final DSLContext dslContext;

	public Table(schemacrawler.schema.Table table, DSLContext dslContext) {
		this.table = table;
		this.dslContext = dslContext;
	}

	public String getName() {
		return table.getName();
	}

	public List<Row> getRows() {
		return dslContext.select().from(getName()).fetch(r -> new Row(r));
	}
}
