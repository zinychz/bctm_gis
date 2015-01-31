package gis.orm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoutePropertyHibernateDao implements RoutePropertyDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public RoutePropertyHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public RouteProperty getById(long id)
	{
		return (RouteProperty) sessionFactory.getCurrentSession().get(RouteProperty.class, id);
	}

	public List<RouteProperty> getAll()
	{
		List<RouteProperty> list = sessionFactory.getCurrentSession()
				.createCriteria(RouteProperty.class).list();

		return list;
	}
}
