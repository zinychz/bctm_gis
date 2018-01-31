package gis.orm;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GisgraphsHibernateDao implements GisgraphsDao {

	private SessionFactory sessionFactory;

	public boolean deleteGroup(Gisgraphs group, Gisgraphs owner) {

		boolean success = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Gisgraphs g = (Gisgraphs) session.createCriteria(Gisgraphs.class).add(Restrictions.eq("id", owner.getId()))
					.setFetchMode("groups", FetchMode.JOIN).uniqueResult();

			g.getGroups().remove(group);
			session.update(g);

			tx.commit();
			success = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			// throw e;
		} finally {
			session.close();
		}

		return success;
	}

	public boolean deleteChild(Gisgraphs child, Gisgraphs owner) {
		return deleteGroup(owner, child);
	}

	public boolean addGroup(Gisgraphs group, Gisgraphs toG) {

		boolean success = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Gisgraphs g = (Gisgraphs) session.createCriteria(Gisgraphs.class).add(Restrictions.eq("id", toG.getId()))
					.setFetchMode("groups", FetchMode.JOIN).uniqueResult();

			g.getGroups().add(group);
			session.update(g);

			tx.commit();
			success = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			// throw e;
		} finally {
			session.close();
		}

		return success;
	}

	public boolean addChild(Gisgraphs child, Gisgraphs toG) {
		return addGroup(toG, child);
	}

	public Long add(Gisgraphs g) {

		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Long id = null;
		try {
			tx = session.beginTransaction();
			id = (Long) session.save(g);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			// throw e;
		} finally {
			session.close();
		}

		return id;
	}

	public boolean update(Gisgraphs g) {

		boolean success = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(g);
			tx.commit();
			success = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			// throw e;
		} finally {
			session.close();
		}

		return success;
	}

	public boolean delete(Gisgraphs g) {

		boolean success = false;
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(g);
			tx.commit();
			success = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			// throw e;
		} finally {
			session.close();
		}

		return success;
	}

	public Set<Gisgraphs> getChildren(long id) {
		Gisgraphs g = (Gisgraphs) sessionFactory.getCurrentSession().createCriteria(Gisgraphs.class)
				.add(Restrictions.eq("id", id)).setFetchMode("children", FetchMode.JOIN).uniqueResult();
		return g.getChildren();
	}

	public Set<Gisgraphs> getGroups(long id) {
		Gisgraphs g = (Gisgraphs) sessionFactory.getCurrentSession().createCriteria(Gisgraphs.class)
				.add(Restrictions.eq("id", id)).setFetchMode("groups", FetchMode.JOIN).uniqueResult();
		return g.getGroups();
	}

	public List<Gisgraphs> getByTypeByLikeName(String type, String likeName, boolean caseSensitive) {
		Query query = null;

		if (caseSensitive) {
			query = sessionFactory.getCurrentSession()
					.createSQLQuery("select * from Gisgraphs where type = ? and name like ? order by name")
					.addEntity(Gisgraphs.class);

			query.setString(0, type);
			query.setString(1, "%" + likeName + "%").list();

			return query.list();
		}

		query = sessionFactory.getCurrentSession()
				.createSQLQuery("select * from Gisgraphs where type = ? and lower(name) like ? order by name")
				.addEntity(Gisgraphs.class);

		query.setString(0, type);
		query.setString(1, "%" + likeName.toLowerCase() + "%").list();

		return query.list();
	}

	@Autowired
	public GisgraphsHibernateDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Gisgraphs getById(long id) {
		return (Gisgraphs) sessionFactory.getCurrentSession().get(Gisgraphs.class, id);
	}

	public List<Gisgraphs> getAll() {
		List<Gisgraphs> list = sessionFactory.getCurrentSession().createCriteria(Gisgraphs.class).list();
		Collections.sort(list, new Comparator<Gisgraphs>() {
			public int compare(Gisgraphs shape1, Gisgraphs shape2) {
				return shape1.getType().compareTo(shape2.getType());
			}
		});

		return list;
	}

	public List<Gisgraphs> getByLikeName(String likeName, boolean caseSensitive) {
		Query query = null;

		if (caseSensitive) {
			// query = sessionFactory.getCurrentSession()
			// .createSQLQuery("select * from Gisgraphs where name like ? order
			// by name")
			// .addEntity(Gisgraphs.class);
			// return query.setString(0, "%" + likeName + "%").list();

			// Criteria crit =
			// sessionFactory.getCurrentSession().createCriteria(Gisgraphs.class);
			// crit.add(Restrictions.ilike("name", "%" + likeName + "%"));
			// return crit.list();

			query = sessionFactory.getCurrentSession()
					.createSQLQuery("select * from Gisgraphs where name COLLATE utf8_bin like ? order by name")
					.addEntity(Gisgraphs.class);
			return query.setString(0, "%" + likeName + "%").list();
		}

		// query = sessionFactory.getCurrentSession()
		// .createSQLQuery("select * from Gisgraphs where lower(name) like ?
		// order by name")
		// .addEntity(Gisgraphs.class);
		// return query.setString(0, "%" + likeName.toLowerCase() + "%").list();

		// Criteria crit =
		// sessionFactory.getCurrentSession().createCriteria(Gisgraphs.class);
		// crit.add(Restrictions.like("name", "%" + likeName + "%"));
		// return crit.list();

		query = sessionFactory.getCurrentSession()
				.createSQLQuery("select * from Gisgraphs where name like ? order by name").addEntity(Gisgraphs.class);
		return query.setString(0, "%" + likeName + "%").list();
	}
}
