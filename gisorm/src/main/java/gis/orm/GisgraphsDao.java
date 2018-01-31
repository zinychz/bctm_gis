package gis.orm;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

public interface GisgraphsDao {
	public Gisgraphs getById(long id);

	public List<Gisgraphs> getAll();

	public List<Gisgraphs> getByLikeName(String likeName, boolean caseSensitive);

	public List<Gisgraphs> getByTypeByLikeName(String type, String likeName, boolean caseSensitive);

	public Set<Gisgraphs> getGroups(long id);

	public Set<Gisgraphs> getChildren(long id);

	public Long add(Gisgraphs g);

	public boolean update(Gisgraphs g);

	public boolean delete(Gisgraphs g);

	public boolean addGroup(Gisgraphs group, Gisgraphs toG);

	public boolean addChild(Gisgraphs child, Gisgraphs toG);

	public boolean deleteGroup(Gisgraphs group, Gisgraphs owner);

	public boolean deleteChild(Gisgraphs child, Gisgraphs owner);
}
