package IMPL;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Bean.BookingBean;
import Dao.BookingDao;
import util.HibernateUtil;

public class BookingDaoImpl implements BookingDao {
	
	
	@Override
	public BookingBean findByHouseId(Long houseId) {
		String hql = "FROM BookingBean WHERE house_id = :houseId";
		BookingBean booking = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<BookingBean> query = session.createQuery(hql, BookingBean.class);
			query.setParameter("houseId", houseId);

			booking = query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return booking;
	}

	@Override
	public List<BookingBean> findAllBooking() {
//		String hql = "FROM BookingBean b "
//				+ "JOIN b.userBean u " + "JOIN b.houseBean h " + "ORDER BY b.bookingId DESC";
		List<BookingBean> list = new ArrayList<BookingBean>();

		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			
			Query<BookingBean> query = session.createQuery("FROM BookingBean", BookingBean.class);
			
			list = query.getResultList();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean createBooking(BookingBean booking) {
		Transaction transaction = null;
		boolean createResult = false;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(booking);
			transaction.commit();
			createResult = true;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		}
		return createResult;
	}

	@Override
	public boolean deleteBooking(Long bookingId) {
		Transaction transaction = null;
		boolean deleteResult = false;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			BookingBean bookingToDelete = session.get(BookingBean.class, bookingId);
			if (bookingToDelete != null) {
				session.remove(bookingToDelete);
				deleteResult = true;
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		}
		return deleteResult;
	}

	@Override
	public boolean updateBooking(BookingBean booking) {
		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(booking);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateBookingStatus(Long bookingId, Byte status) {
		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			BookingBean bookingToUpdate = session.get(BookingBean.class, bookingId);
			if (bookingToUpdate != null) {
				bookingToUpdate.setStatus(status);
				session.merge(bookingToUpdate);
				transaction.commit();
				return true;
			}
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		}
		return false;
	}

}
