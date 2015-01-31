package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RouteItemHibernateDao implements RouteItemDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public RouteItemHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public RouteItem getById(ItemPk itemPk)
	{
		return (RouteItem) sessionFactory.getCurrentSession().get(RouteItem.class, itemPk);
	}

	public List<RouteItem> getAll()
	{
		List<RouteItem> list = sessionFactory.getCurrentSession().createCriteria(RouteItem.class)
				.list();

		return list;
	}

	public List<RouteItem> getByInvId(long invId)
	{
		return sessionFactory.getCurrentSession().createCriteria(RouteItem.class)
				.add(Restrictions.eq("itemPk.invid", invId)).list();
	}
}
