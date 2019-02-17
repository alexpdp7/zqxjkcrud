package net.pdp7.zqxjkcrud.dao;

import schemacrawler.schema.Column;

public class Field {

	protected final Column column;

	public Field(Column column) {
		this.column = column;
	}

	public String getName() {
		return column.getName();
	}
}
