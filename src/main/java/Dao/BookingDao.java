package Dao;

import java.util.List;

import Bean.BookingBean;

public interface BookingDao {
	BookingBean findByHouseId(Long houseId);
	List<BookingBean> findAllBooking();
	boolean createBooking(BookingBean booking);
	boolean deleteBooking(Long bookingId);
	boolean updateBooking(BookingBean booking);
	boolean updateBookingStatus(Long bookingId, Byte status);
}
