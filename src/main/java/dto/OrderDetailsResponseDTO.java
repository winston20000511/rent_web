package dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderDetailsResponseDTO {
	
	private Long userId;
	private String orderId;
	private String paidDate;
	private String userName;
	private String paymentMethod;
	private String orderStatus;
	private Long totalAmount;
	
	private List<Long> houseIds;
	private List<String> houseTitles;
	private List<Long> adIds;
	private List<Long> adCouponApplied; // 存有 coupn 的 ad id
	private List<String> adtypes;
	private List<Integer> adOriginalPrice;
	private List<Integer> adDiscounts;
	private List<String> adPeriods;

	public void setOrderStatus(Short orderStatus) {
		if(orderStatus == (short)0) this.orderStatus = "已取消";
		if(orderStatus == (short)1) this.orderStatus =  "一般訂單";
	}
}
