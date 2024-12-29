package Dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import Bean.BookingBean;
import DTO.BookingDTO;

public interface BookingDao {
	
	List<BookingDTO> findBookingsByPage(String searchValue, String orderByColumn, String orderDir, int start, int length);
    long countTotal();
    long countFiltered(String searchValue);
    BookingDTO findBookingById(Long bookingId);
    
	BookingBean findByHouseId(Long houseId);
	List<BookingDTO> findAllBooking();
	boolean createBooking(BookingBean booking);
	boolean deleteBooking(Long bookingId);
	boolean updateBooking(BookingBean booking);
	boolean updateBookingStatus(Long bookingId, LocalDate bookingDate, LocalTime bookingTime, Byte status);
}
