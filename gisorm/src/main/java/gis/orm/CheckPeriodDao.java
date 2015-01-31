package gis.orm;

import java.util.List;

public interface CheckPeriodDao
{
	public CheckPeriod getById(long id);
	public List<CheckPeriod> getAll();
}
