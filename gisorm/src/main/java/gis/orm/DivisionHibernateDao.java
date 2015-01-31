package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DivisionHibernateDao implements DivisionDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public DivisionHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Division getById(long id)
	{
		return (Division) sessionFactory.getCurrentSession().get(Division.class, id);
	}

	public List<Division> getAll()
	{
		List<Division> list = sessionFactory.getCurrentSession().createCriteria(Division.class)
				.list();

		return list;
	}
}
