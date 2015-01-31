package gis.orm;

import java.util.List;
import java.util.Set;

public interface GisgraphsDao
{
	public Gisgraphs getById(long id);
	public List<Gisgraphs> getAll();
	public List<Gisgraphs> getByLikeName(String likeName, boolean caseSensitive);
	
	public List<Gisgraphs> getByTypeByLikeName(String type, String likeName, boolean caseSensitive);
	public Set<Gisgraphs> getGroups(long id);
	public Set<Gisgraphs> getChildren(long id);
}
