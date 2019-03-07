package net.pdp7.zqxjkcrud.dao;

import java.util.Iterator;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

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
		return dslContext.select().from(getName()).fetch(r -> new Row.RecordRow(r));
	}

	public Iterator<Field> getFields() {
		return table.getColumns()
				.stream()
				.filter(c -> !c.getName().startsWith("_"))
				.map(c -> new Field(c))
				.iterator();
	}

	public Row getRow(String id) {
		return dslContext.select()
				.from(getName())
				.where(DSL.field("_id").cast(String.class).equal(id))
				.fetchOne(r -> new Row.RecordRow(r));
	}
}
