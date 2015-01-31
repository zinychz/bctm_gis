package gis.orm;

import java.util.List;

public interface BoilerItemDao
{
	public BoilerItem getById(Long id);
	public List<BoilerItem> getAll();
	public List<BoilerItem> getByInvId(long invId);
}
