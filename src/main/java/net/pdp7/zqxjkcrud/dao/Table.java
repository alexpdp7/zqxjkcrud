package net.pdp7.zqxjkcrud.dao;

public class Table {

	protected final schemacrawler.schema.Table table;

	public Table(schemacrawler.schema.Table table) {
		this.table = table;
	}

	public String getName() {
		return table.getName();
	}

}
