package gis.orm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InventoriesHibernateDao implements InventoriesDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public InventoriesHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Inventories getById(long id)
	{
		return (Inventories) sessionFactory.getCurrentSession().get(Inventories.class, id);
	}

	public List<Inventories> getAll()
	{
		List<Inventories> list = sessionFactory.getCurrentSession()
				.createCriteria(Inventories.class).list();

		return list;
	}
}
