package net.pdp7.zqxjkcrud.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

import schemacrawler.schema.Column;

public class Field {

	protected final Column column;

	public Field(Column column) {
		this.column = column;
	}

	public String getName() {
		return column.getName();
	}

	public String getWidgetName() {
		switch (column.getType().getName()) {
		case "timestamptz":
			return "widget-timestamp";
		case "date":
			return "widget-date";
		default:
			return "widget-default";
		}
	}

	public Object convert(Map<String, String[]> form) {
		switch (column.getType().getName()) {
		case "numeric":
			return new BigDecimal(form.get("value")[0]);
		case "timestamptz":
			return Timestamp.valueOf(
					form.get("value/date")[0] + " " +
					form.get("value/time")[0] + ":" +
					form.get("value/s")[0] + "." +
					form.get("value/ms")[0]);
		case "date":
			return LocalDate.parse(form.get("value")[0]);
		default:
			return form.get("value")[0];
		}
	}
}
