package dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderDetailsDTO {
	
	private String orderId;
	private String paidDate;
	private Long userId;
	private String userName;
	private String paymentMethod;
	private String orderStatus;
	
	private Long houseId;
	private Long adId;
	private String adtype;
//	private String adPeriod;
	
	
}
