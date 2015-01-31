package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BoilerTypeHibernateDao implements BoilerTypeDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public BoilerTypeHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public BoilerType getById(long id)
	{
		return (BoilerType) sessionFactory.getCurrentSession().get(BoilerType.class, id);
	}

	public List<BoilerType> getAll()
	{
		List<BoilerType> list = sessionFactory.getCurrentSession().createCriteria(BoilerType.class)
				.list();

		return list;
	}
}
