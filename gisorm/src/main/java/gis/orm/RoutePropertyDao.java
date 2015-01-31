package gis.orm;

import java.util.List;

public interface RoutePropertyDao
{
	public RouteProperty getById(long id);
	public List<RouteProperty> getAll();
}
