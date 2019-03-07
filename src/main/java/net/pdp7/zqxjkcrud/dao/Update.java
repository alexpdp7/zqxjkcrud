package net.pdp7.zqxjkcrud.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.pdp7.zqxjkcrud.utils.MapUtils;

public class Update {

	public final List<TableUpdate> tableUpdates;

	public Update(Map<String, String[]> form) {
		Map<String, String[]> data = MapUtils.subMap(form, "data/");
		tableUpdates = data.keySet()
				.stream()
				.map(k -> k.substring(0, k.indexOf("/")))
				.map(Integer::parseInt)
				.collect(Collectors.toSet())
				.stream()
				.sorted()
				.map(i -> MapUtils.subMap(data, i.toString() + "/"))
				.map(TableUpdate::new)
				.collect(Collectors.toList());
	}

	public static final class TableUpdate {
		public final String table;
		public final String id;
		public final TableAction action;
		public final Map<String, Object> fields;

		private TableUpdate(Map<String, String[]> form) {
			table = form.remove("_table")[0];
			action = TableAction.valueOf(form.remove("_action")[0]);
			id = form.remove("_id")[0];
			fields = form.keySet()
					.stream()
					.map(k -> k.substring(0, k.indexOf("/")))
					.collect(
							Collectors
									.toMap(k -> k, k -> convertField(MapUtils.subMap(form, k + "/"))));
		}

		private Object convertField(Map<String, String[]> field) {
			return field.get("value")[0];
		}
	}

	public enum TableAction {
		INSERT, UPDATE;
	}
}
