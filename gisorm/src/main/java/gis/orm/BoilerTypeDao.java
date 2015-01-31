package gis.orm;

import java.util.List;

public interface BoilerTypeDao
{
	public BoilerType getById(long id);
	public List<BoilerType> getAll();
}
