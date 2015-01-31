package gis.factory;

import gis.shapes.AbstractShape;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory
{
	private static Map<Long, Graph> cache;
	private Map<String, AbstractShape> mapping;

	public Graph createShape(long id, String type, long priority, String name, String coord)
	{
		Graph g = cache.get(id);

		if (g == null)
		{
			AbstractShape templateShape = mapping.get(type);

			if (templateShape != null)
			{
				g = templateShape.newInstance(id, type, priority, name);
				g.parseCoordinates(coord);
				
				cache.put(id, g);
			}
		}

		return g;
	}

	public ShapeFactory(Map<String, AbstractShape> mapping)
	{
		this.mapping = mapping;
		cache = new HashMap<Long, Graph>();
	}
}
