package gis.orm;

import java.util.List;

public interface RouteTypeDao
{
	public RouteType getById(long id);
	public List<RouteType> getAll();
}
