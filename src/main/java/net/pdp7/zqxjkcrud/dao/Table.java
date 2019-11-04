package net.pdp7.zqxjkcrud.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import net.pdp7.zqxjkcrud.ZqxjkCrudException;
import schemacrawler.schema.Column;

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
		return dslContext.select().from(getName())
				.orderBy(getOrdering())
				.limit(100)
				.fetch(r -> new Row.RecordRow(r));
	}

	protected Collection<OrderField<?>> getOrdering() {
		List<OrderField<?>> ordering = new ArrayList<OrderField<?>>();
		String[] orderingStrs = (String[]) getTableInfo().getOrDefault("default_sort", new String[0]);
		for (int i = 0; i < orderingStrs.length; i += 2) {
			ordering.add(createSortField(orderingStrs[i], orderingStrs[i + 1]));
		}
		return ordering;
	}

	private SortField<Object> createSortField(String fieldName, String ordering) {
		org.jooq.Field<Object> field = DSL.field(fieldName);
		SortField<Object> sortField;
		switch (ordering) {
		case "asc":
			sortField = field.asc();
			break;
		case "desc":
			sortField = field.desc();
			break;
		default:
			throw new UnknownOrderingException(getName(), ordering);
		}
		return sortField;
	}

	protected Map<String, Object> getTableInfo() {
		Map<String, Object> tableInfo = dslContext.select().from("_tables").where(DSL.field("name").equal(getName())).fetchOneMap();
		if (tableInfo == null) {
			tableInfo = new HashMap<String, Object>();
		}
		return tableInfo;
	}

	public Map<String, Object> getColumnInfo(Column column) {
		Map<String, Object> columnInfo = dslContext.select()
				.from("_columns")
				.where(DSL.field("table_name").equal(getName()))
				.and(DSL.field("name").equal(column.getName()))
				.fetchOneMap();
		if (columnInfo == null) {
			columnInfo = new HashMap<String, Object>();
		}
		return columnInfo;
	}

	public Map<String, Field> getFields() {
		return table.getColumns()
				.stream()
				.filter(c -> !c.getName().startsWith("_"))
				.collect(Collectors.toMap(c -> c.getName(), c -> new Field(this, c), (o1, o2) -> o1, LinkedHashMap::new));
	}

	public Row getRow(String id) {
		return dslContext.select()
				.from(getName())
				.where(DSL.field("_id").cast(String.class).equal(id))
				.fetchOne(r -> new Row.RecordRow(r));
	}

	public static class UnknownOrderingException extends ZqxjkCrudException {

		public UnknownOrderingException(String table, String ordering) {
			super("Unknown ordering " + ordering + " on table " + table);
		}
	}

}
