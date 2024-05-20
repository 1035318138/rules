package threadLocal.utils;

import java.util.*;


/**
 * ThreadLocal Utils
 * 
 * @author efun
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ThreadLocalUtil {

	private static ThreadLocal<Map<String, Object>> holder = new ThreadLocal() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap(8);
		}
	};

	private ThreadLocalUtil() {

	}

	public static Map<String, Object> getThreadLocal() {
		return holder.get();
	}

	public static <T> T get(String key) {
		Map map = holder.get();
		return (T) map.get(key);
	}

	public static <T> T get(String key, T defaultValue) {
		Map map = holder.get();
		return (T) map.get(key) == null ? defaultValue : (T) map.get(key);
	}

	public static void set(String key, Object value) {
	    if (key == null || "".equals(key)) {
	        throw new IllegalArgumentException("key cannot be null");
	    }
		Map map = holder.get();
		map.put(key, value);
	}

	public static void set(Map<String, Object> keyValueMap) {
	    if (keyValueMap == null || keyValueMap.isEmpty()) {
	        throw new IllegalArgumentException("map cannot be null");
	    }
		Map map = holder.get();
		map.putAll(keyValueMap);
	}

	public static void removeAll() {
		holder.remove();
	}

	public static <T> Map<String, T> fetchVarsByPrefix(String prefix) {
		Map<String, T> vars = new HashMap<>(16);
		if (prefix == null) {
			return vars;
		}
		Map map = holder.get();
		Set<Map.Entry> set = map.entrySet();

		for (Map.Entry entry : set) {
			Object key = entry.getKey();
			if (key instanceof String && ((String) key).startsWith(prefix)) {
				vars.put((String) key, (T) entry.getValue());
			}
		}
		return vars;
	}

	public static <T> T remove(String key) {
		Map map = holder.get();
		return (T) map.remove(key);
	}

	public static void clear(String prefix) {
		if (prefix == null) {
			return;
		}
		Map map = holder.get();
		Set<Map.Entry> set = map.entrySet();
		List<String> removeKeys = new ArrayList<>();

		for (Map.Entry entry : set) {
			Object key = entry.getKey();
			if (key instanceof String && ((String) key).startsWith(prefix)) {
				removeKeys.add((String) key);
			}
		}
		for (String key : removeKeys) {
			map.remove(key);
		}
	}

}