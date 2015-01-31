package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RouteTypeHibernateDao implements RouteTypeDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public RouteTypeHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public RouteType getById(long id)
	{
		return (RouteType) sessionFactory.getCurrentSession().get(RouteType.class, id);
	}

	public List<RouteType> getAll()
	{
		List<RouteType> list = sessionFactory.getCurrentSession().createCriteria(RouteType.class)
				.list();

		return list;
	}
}
