package rent189.dao;

import java.util.List;

import rent189.model.BookingBean;

public interface BookingDao {
	BookingBean findByHouseId(int houseId);
	List<BookingBean> findAllBooking();
	boolean createBooking(BookingBean booking);
	boolean deleteBooking(int bookingId);
	boolean updateBooking(BookingBean booking);
	List<BookingBean> findLatestBookings(int limit);
}
