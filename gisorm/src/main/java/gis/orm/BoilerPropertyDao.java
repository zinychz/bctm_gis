package gis.orm;

import java.util.List;

public interface BoilerPropertyDao
{
	public BoilerProperty getById(long id);
	public List<BoilerProperty> getAll();
}
