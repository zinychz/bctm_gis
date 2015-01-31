package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CheckPeriodHibernateDao implements CheckPeriodDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public CheckPeriodHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public CheckPeriod getById(long id)
	{
		return (CheckPeriod) sessionFactory.getCurrentSession().get(CheckPeriod.class, id);
	}

	public List<CheckPeriod> getAll()
	{
		List<CheckPeriod> list = sessionFactory.getCurrentSession()
				.createCriteria(CheckPeriod.class).list();

		return list;
	}
}
