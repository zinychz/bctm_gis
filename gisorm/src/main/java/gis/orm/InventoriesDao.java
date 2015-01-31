package gis.orm;

import java.util.List;
import java.util.Set;

public interface InventoriesDao
{
	public Inventories getById(long id);
	public List<Inventories> getAll();
	// public List<Inventories> getByLikeName(String likeName, boolean
	// caseSensitive);
}
