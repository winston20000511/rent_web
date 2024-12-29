package DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {

	private Long bookingId;
	private Long houseId;
	private String houseTitle; 
	private String houseAddress;
	private Integer housePrice;
	private Long houseOwnerId;
	private String houseOwnerName;
	private String houseOwnerEmail;
	private String houseOwnerphone;
	private Long userId;
	private String userName;
	private String userEmail;
	private String userphone;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;
	private LocalDate bookingDate;
	private LocalTime bookingTime;
	private Byte status;

}
