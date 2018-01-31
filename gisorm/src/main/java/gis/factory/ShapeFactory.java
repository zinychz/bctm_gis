package gis.factory;

import gis.shapes.AbstractShape;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShapeFactory {
	private static Map<Long, Graph> cache;
	private Map<String, AbstractShape> mapping;

	public Graph createShape(long id, String type, long priority, String name, String coord, boolean useCache) {

		Graph g = null;
		if (useCache) {
			g = cache.get(id);
		}

		if (g == null) {
			AbstractShape templateShape = mapping.get(type);

			if (templateShape != null) {
				g = templateShape.newInstance(id, type, priority, name);
				g.parseCoordinates(coord);

				if (useCache) {
					cache.put(id, g);
				}
			}
		}

		return g;
	}

	public void removeFromCache(long id) {
		cache.remove(id);
	}

	public Graph createCustomShape(Class shapeClass, Map<String, Object> properties) {

		if (!Graph.class.isAssignableFrom(shapeClass))
			return null;

		Object object = null;
		try {
			object = shapeClass.newInstance();
		} catch (Exception e) {
			return null;
		}

		if (properties != null) {

			List<Field> fields = new ArrayList<Field>();
			fields = getAllFields(fields, object.getClass());

			for (Map.Entry<String, Object> entry : properties.entrySet()) {

				String fieldName = entry.getKey();
				Object value = entry.getValue();

				for (Field f : fields) {
					if (f.getName().equals(fieldName)) {
						boolean accessible = f.isAccessible();
						f.setAccessible(true);
						try {
							f.set(object, value);
						} catch (Exception e) {
						}
						break;
					}
				}
			}
		}

		return (Graph) object;
	}

	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}

	public ShapeFactory(Map<String, AbstractShape> mapping) {
		this.mapping = mapping;
		cache = new ConcurrentHashMap<Long, Graph>();
	}

	public Map<String, AbstractShape> getMapping() {
		return mapping;
	}
}
