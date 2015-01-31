package gis.orm;

import java.util.List;

public interface PersonDao
{
	public Person getById(long id);
	public List<Person> getAll();
}
