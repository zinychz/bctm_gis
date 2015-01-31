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
public class BoilerItemHibernateDao implements BoilerItemDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public BoilerItemHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public BoilerItem getById(Long id)
	{
		return (BoilerItem) sessionFactory.getCurrentSession().get(BoilerItem.class, id);
	}

	public List<BoilerItem> getAll()
	{
		List<BoilerItem> list = sessionFactory.getCurrentSession().createCriteria(BoilerItem.class)
				.list();

		return list;
	}

	public List<BoilerItem> getByInvId(long invId)
	{
		return sessionFactory.getCurrentSession().createCriteria(BoilerItem.class)
				.add(Restrictions.eq("invid", invId)).list();
	}
}
