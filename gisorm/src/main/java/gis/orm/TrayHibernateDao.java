package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrayHibernateDao implements TrayDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public TrayHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Tray getById(long id)
	{
		return (Tray) sessionFactory.getCurrentSession().get(Tray.class, id);
	}

	public List<Tray> getAll()
	{
		List<Tray> list = sessionFactory.getCurrentSession().createCriteria(Tray.class).list();

		return list;
	}
}
