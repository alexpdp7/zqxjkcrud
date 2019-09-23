package net.pdp7.zqxjkcrud.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public Map<String, Field> getFields() {
		return table.getColumns()
				.stream()
				.filter(c -> !c.getName().startsWith("_"))
				.collect(Collectors.toMap(c -> c.getName(), c -> new Field(c)));
	}

	public Row getRow(String id) {
		return dslContext.select()
				.from(getName())
				.where(DSL.field("_id").cast(String.class).equal(id))
				.fetchOne(r -> new Row.RecordRow(r));
	}
}
