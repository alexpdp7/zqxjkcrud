package net.pdp7.zqxjkcrud.utils;

import java.util.Map;
import java.util.stream.Collectors;

public final class MapUtils {

  private MapUtils() {}

  public static <T> Map<String, T> subMap(Map<String, T> map, String prefix) {
    return map.entrySet().stream()
        .filter(e -> e.getKey().startsWith(prefix))
        .collect(Collectors.toMap(e -> e.getKey().substring(prefix.length()), e -> e.getValue()));
  }
}
