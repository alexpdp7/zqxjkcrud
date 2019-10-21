package net.pdp7.zqxjkcrud.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

import schemacrawler.schema.Column;

public class Field {

	protected final Table table;
	protected final Column column;

	public Field(Table table, Column column) {
		this.table = table;
		this.column = column;
	}

	public String getName() {
		return column.getName();
	}

	public String getWidgetName() {
		String widgetType = column.getType().getName();
		Map<String, Object> columnInfo = table.getColumnInfo(this.column);
		String widgetInfo = (String) columnInfo.get("widget");
		if (widgetInfo != null) {
			widgetType = widgetInfo;
		}
		switch (widgetType) {
		case "timestamptz":
			return "widget-timestamp";
		case "date":
			return "widget-date";
		case "bool":
			return "widget-boolean";
		case "textarea":
			return "widget-textarea";
		default:
			return "widget-default";
		}
	}

	public Object convert(Map<String, String[]> form) {
		switch (column.getType().getName()) {
		case "numeric":
			return new BigDecimal(form.get("value")[0]);
		case "timestamptz":
			return Timestamp.valueOf(form.get("value/date")[0]
				+ " " + form.get("value/time")[0]
				+ ":" + form.get("value/s")[0]
				+ "." + form.get("value/ms")[0]);
		case "date":
			return LocalDate.parse(form.get("value")[0]);
		case "bool":
			return form.get("value") != null;
		default:
			return form.get("value")[0];
		}
	}
}
