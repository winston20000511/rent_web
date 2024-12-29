package dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString()
public class AdDetailResponseDTO {

	private Long adId;
	private Long userId;
	private String userName;
	private Long houseId;
	private String houseTitle;
	private String adtypeName;
	private Integer subtotal;
	private Integer adPrice;
	private Boolean isPaid;
	private String orderId;
	private ZonedDateTime paidDate;
	
}
