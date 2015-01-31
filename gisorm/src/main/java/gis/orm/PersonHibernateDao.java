package gis.orm;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PersonHibernateDao implements PersonDao
{
	private SessionFactory sessionFactory;

	@Autowired
	public PersonHibernateDao(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Person getById(long id)
	{
		return (Person) sessionFactory.getCurrentSession().get(Person.class, id);
	}

	public List<Person> getAll()
	{
		List<Person> list = sessionFactory.getCurrentSession().createCriteria(Person.class).list();

		return list;
	}
}
