package gis.orm;

import java.util.List;

public interface MaterialDao
{
	public Material getById(long id);
	public List<Material> getAll();
}
