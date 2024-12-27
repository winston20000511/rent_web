package Dao;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {

	private Long bookingId;
    private String houseTitle;
    private String userName;
	private LocalDate bookingDate;
	private LocalTime bookingTime;
    private Byte status;
    

}
