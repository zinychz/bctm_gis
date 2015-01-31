package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MeasureHibernateDao implements MeasureDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public MeasureHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Measure getById(long id)
	{
		return (Measure) sessionFactory.getCurrentSession().get(Measure.class, id);
	}

	public List<Measure> getAll()
	{
		List<Measure> list = sessionFactory.getCurrentSession().createCriteria(Measure.class)
				.list();

		return list;
	}
}
