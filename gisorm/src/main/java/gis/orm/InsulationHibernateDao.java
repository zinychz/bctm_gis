package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InsulationHibernateDao implements InsulationDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public InsulationHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Insulation getById(long id)
	{
		return (Insulation) sessionFactory.getCurrentSession().get(Insulation.class, id);
	}

	public List<Insulation> getAll()
	{
		List<Insulation> list = sessionFactory.getCurrentSession().createCriteria(Insulation.class)
				.list();

		return list;
	}
}
