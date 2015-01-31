package gis.orm;

import java.util.List;

public interface TrayDao
{
	public Tray getById(long id);
	public List<Tray> getAll();
}
