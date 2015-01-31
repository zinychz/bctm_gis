package gis.orm;

import java.util.List;

public interface InsulationDao
{
	public Insulation getById(long id);
	public List<Insulation> getAll();
}
