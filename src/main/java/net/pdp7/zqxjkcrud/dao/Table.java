package net.pdp7.zqxjkcrud.dao;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

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

	public Result<Record> list() {
		return dslContext.select().from(getName()).fetch();
	}
}
