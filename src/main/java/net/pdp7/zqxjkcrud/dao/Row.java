package net.pdp7.zqxjkcrud.dao;

import java.util.HashMap;
import java.util.Map;
import org.jooq.Record;

public abstract class Row {

  public abstract String getId();

  public abstract String getDisplay();

  public abstract Map<String, Object> getFields();

  public static class RecordRow extends Row {
    protected final Record record;

    public RecordRow(Record record) {
      this.record = record;
    }

    public String getId() {
      Object id = record.get("_id");
      assert id != null;
      return id.toString();
    }

    public String getDisplay() {
      return String.valueOf(record.get("_display"));
    }

    public Map<String, Object> getFields() {
      return record.intoMap();
    }
  }

  public static class TransientRow extends Row {

    @Override
    public String getId() {
      return null;
    }

    @Override
    public String getDisplay() {
      return "New";
    }

    @Override
    public Map<String, Object> getFields() {
      return new HashMap<>();
    }
  }
}
