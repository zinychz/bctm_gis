package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MaterialHibernateDao implements MaterialDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public MaterialHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Material getById(long id)
	{
		return (Material) sessionFactory.getCurrentSession().get(Material.class, id);
	}

	public List<Material> getAll()
	{
		List<Material> list = sessionFactory.getCurrentSession().createCriteria(Material.class)
				.list();

		return list;
	}
}
