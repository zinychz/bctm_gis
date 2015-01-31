package gis.orm;

import java.util.List;

public interface DivisionDao
{
	public Division getById(long id);
	public List<Division> getAll();
}
