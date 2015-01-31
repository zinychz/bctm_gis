package gis.orm;

import java.util.List;

public interface MeasureDao
{
	public Measure getById(long id);
	public List<Measure> getAll();
}
