package net.pdp7.zqxjkcrud.dao;

import org.jooq.Record;

public class Row {

	protected final Record record;

	public Row(Record record) {
		this.record = record;
	}

	public String getId() {
		return record.get("_id").toString();
	}

	public String getDisplay() {
		return record.get("_display").toString();
	}
}
