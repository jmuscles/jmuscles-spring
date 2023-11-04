/**
 * @author manish goel
 *
 *//*
	 * package com.jmuscles.props;
	 * 
	 * import java.lang.reflect.Field; import java.util.HashMap; import
	 * java.util.List; import java.util.Map;
	 * 
	 * import com.jmuscles.util.Util;
	 * 
	 * public abstract class MapConvertible {
	 * 
	 * public Map<String, Object> objectToMap() { Map<String, Object> map = new
	 * HashMap<>(); Class<?> clazz = this.getClass(); Field[] fields =
	 * clazz.getDeclaredFields(); for (Field field : fields) {
	 * field.setAccessible(true); try { map.put(field.getName(), field.get(this)); }
	 * catch (IllegalAccessException e) { e.printStackTrace(); } } return map; }
	 * 
	 * public void mapToObject(Map<String, Object> map) { Class<?> clazz =
	 * this.getClass(); Field[] fields = clazz.getDeclaredFields(); for (Field field
	 * : fields) { field.setAccessible(true); if (map.containsKey(field.getName()))
	 * { try { field.set(this, map.get(field.getName())); } catch
	 * (IllegalAccessException e) { e.printStackTrace(); } } } }
	 * 
	 * public void resolveFieldValue(Field field, Map<String, Object> map) { Object
	 * value = map.get(field.getName()); if (field.getType().isPrimitive()) {
	 * field.set(this, Util.handlePrimitive(value, field.getType())); } else if
	 * (Util.isWrapperClass(field.getType())) { field.set(this,
	 * Util.handleWrapper(value, field.getType())); }
	 * 
	 * else if (value instanceof Map && field.getType() == Map.class) { // Handle
	 * Map fields field.set(obj, value); } else if (value instanceof Map) { //
	 * Handle other nested classes field.set(obj, mapToObject((Map<String, Object>)
	 * value, field.getType())); } else if (field.getType() == List.class && value
	 * instanceof String) { // Handle List<String> fields field.set(obj,
	 * handleList(fieldName, value)); } else if (field.getType() == String.class &&
	 * value instanceof String) { // Handle String fields field.set(obj, value); }
	 * else { field.set(obj, value); } }
	 * 
	 * private static Object handle
	 * 
	 * }
	 */