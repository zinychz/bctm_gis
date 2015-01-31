package gis.orm;

import java.util.List;

public interface RouteItemDao
{
	public RouteItem getById(ItemPk itemPk);
	public List<RouteItem> getAll();
	public List<RouteItem> getByInvId(long invId);
}
