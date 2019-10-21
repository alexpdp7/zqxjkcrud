package net.pdp7.zqxjkcrud.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.pdp7.zqxjkcrud.utils.MapUtils;

public class Update {

	public final List<TableUpdate> tableUpdates;

	public Update(Dao dao, Map<String, String[]> form) {
		Map<String, String[]> data = MapUtils.subMap(form, "data/");
		tableUpdates = data.keySet()
				.stream()
				.map(k -> k.substring(0, k.indexOf("/")))
				.map(Integer::parseInt)
				.collect(Collectors.toSet())
				.stream()
				.sorted()
				.map(i -> MapUtils.subMap(data, i.toString() + "/"))
				.map(f -> new TableUpdate(dao, f))
				.collect(Collectors.toList());
	}

	public static final class TableUpdate {
		public final Table table;
		public final String id;
		public final TableAction action;
		public final Map<String, Object> fields;

		private TableUpdate(Dao dao, Map<String, String[]> form) {
			table = dao.getTables().get(form.remove("_table")[0]);
			action = TableAction.valueOf(form.remove("_action")[0]);
			id = form.remove("_id")[0];
			fields = table.getFields()
					.entrySet()
					.stream()
					.collect(
							Collectors.toMap(
									e -> e.getKey(),
									e -> convertField(
											e.getValue(),
											MapUtils.subMap(form, e.getKey() + "/"))));
		}

		private Object convertField(Field field, Map<String, String[]> form) {
			return field.convert(form);
		}
	}

	public enum TableAction {
		INSERT, UPDATE;
	}
}
