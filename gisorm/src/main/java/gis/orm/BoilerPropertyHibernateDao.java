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
public class BoilerPropertyHibernateDao implements BoilerPropertyDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public BoilerPropertyHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public BoilerProperty getById(long id)
	{
		return (BoilerProperty) sessionFactory.getCurrentSession().get(BoilerProperty.class, id);
	}

	public List<BoilerProperty> getAll()
	{
		List<BoilerProperty> list = sessionFactory.getCurrentSession()
				.createCriteria(BoilerProperty.class).list();

		return list;
	}
}
