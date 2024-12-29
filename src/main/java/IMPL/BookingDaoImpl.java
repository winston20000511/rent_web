package IMPL;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Bean.BookingBean;
import DTO.BookingDTO;
import Dao.BookingDao;
import util.HibernateUtil;

public class BookingDaoImpl implements BookingDao {
	
	@Override
	public List<BookingDTO> findBookingsByPage(String searchValue, String orderByColumn, String orderDir, int start, int length) {
	    String hql = "SELECT new DTO.BookingDTO(b.bookingId, h.houseId, h.title, h.address, h.price, hu.userId, "
	            + "hu.name, hu.email, hu.phone, ru.userId, ru.name, ru.email, ru.phone, b.createDate, "
	            + "b.bookingDate, b.bookingTime, b.status) "
	            + "FROM BookingBean b "
	            + "JOIN b.house h "
	            + "JOIN h.user hu "
	            + "JOIN b.rentUser ru "
	            + "WHERE h.title LIKE :search OR hu.name LIKE :search OR ru.name LIKE :search "
	            + "ORDER BY " + orderByColumn + " " + orderDir;
	    
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Query<BookingDTO> query = session.createQuery(hql, BookingDTO.class);
	        query.setParameter("search", "%" + searchValue + "%");
	        query.setFirstResult(start);
	        query.setMaxResults(length);

	        return query.getResultList();
	    }
	}
	
	@Override
	public long countTotal() {
	    String hql = "SELECT COUNT(b.bookingId) FROM BookingBean b";
	    
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Query<Long> query = session.createQuery(hql, Long.class);
	        return query.uniqueResult();
	    }
	}
	
	@Override
	public long countFiltered(String searchValue) {
	    String hql = "SELECT COUNT(b.bookingId) FROM BookingBean b "
	    		+ "JOIN b.house h "
	    		+ "JOIN h.user hu "
	    		+ "JOIN b.rentUser ru "
	            + "WHERE h.title LIKE :search OR hu.name LIKE :search OR ru.name LIKE :search ";
	    
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Query<Long> query = session.createQuery(hql, Long.class);
	        query.setParameter("search", "%" + searchValue + "%");
	        return query.uniqueResult();
	    }
	}
	
	
	@Override
    public BookingDTO findBookingById(Long bookingId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = """
                SELECT new BookingDTO(
                    b.id AS bookingId,
                    h.id AS houseId,
                    h.title AS houseTitle,
                    h.address AS houseAddress,
                    h.price AS housePrice,
                    o.id AS houseOwnerId,
                    o.name AS houseOwnerName,
                    o.email AS houseOwnerEmail,
                    o.phone AS houseOwnerphone,
                    u.id AS userId,
                    u.name AS userName,
                    u.email AS userEmail,
                    u.phone AS userphone,
                    b.createDate AS createDate,
                    b.bookingDate AS bookingDate,
                    b.bookingTime AS bookingTime,
                    b.status AS status
                )
                FROM Booking b
                JOIN b.house h
                JOIN h.owner o
                JOIN b.user u
                WHERE b.id = :bookingId
            """;

            // 執行查詢並返回結果
            return session.createQuery(hql, BookingDTO.class)
                          .setParameter("bookingId", bookingId)
                          .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving booking by ID", e);
        }
    }
	
	
	
	//以下是舊的////////////////////////
	
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
	public List<BookingDTO> findAllBooking() {
		List<BookingDTO> dtoList = new ArrayList<>();

		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			String hql = "SELECT new BookingDTO( " +
	                "b.bookingId, " +
	                "b.houseId, " +
	                "h.title, " +
	                "h.address, " +
	                "h.price, " +
	                "hu.userId, " +
	                "hu.name, " +
	                "hu.email, " +
	                "hu.phone, " +
	                "ru.userId, " +
	                "ru.name, " +
	                "ru.email, " +
	                "ru.phone, " +
	                "b.createDate, " +
	                "b.bookingDate, " +
	                "b.bookingTime, " +
	                "b.status) " +
	                "FROM BookingBean b " +
	                "JOIN b.house h " +
	                "JOIN h.user hu " +
	                "JOIN b.rentUser ru " +
	                "ORDER BY b.bookingId DESC";
			
			Query<BookingDTO> query = session.createQuery(hql, BookingDTO.class);
			dtoList = query.getResultList();
			

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtoList;
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
	public boolean updateBookingStatus(Long bookingId, LocalDate bookingDate, LocalTime bookingTime, Byte status) {
		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			BookingBean bookingToUpdate = session.get(BookingBean.class, bookingId);
			if (bookingToUpdate != null) {
				bookingToUpdate.setStatus(status);
				bookingToUpdate.setBookingDate(bookingDate);
				bookingToUpdate.setBookingTime(bookingTime);
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
