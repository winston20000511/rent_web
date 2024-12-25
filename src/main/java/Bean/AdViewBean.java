package Bean;

import java.time.ZonedDateTime;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import util.TimeForm;

@Data
public class AdViewBean {
	
	@JsonIgnore
//	private Logger logger = Logger.getLogger(AdViewBean.class.getName());
	
	private Long adId;
	private Long userId;
	private String userName;
	private Long houseId;
	private String adType;
	private Integer adPrice;
	private Integer subtotal;
	private Boolean isCouponUsed;
	private String isPaid;
	private String orderId;
	private String paidDate;

	public AdViewBean() {
		
	}
	
	public AdViewBean(Long adId, Long userId, String userName, Long houseId, String adType,
			Integer adPrice, Integer subtotal, String isPaid, String orderId, String paidDate) {
		this.adId = adId;
		this.userId = userId;
		this.userName = userName;
		this.houseId = houseId;
		this.adType = adType;
		this.adPrice = adPrice;
		this.subtotal = subtotal;
		this.isPaid = isPaid;
		this.orderId = orderId;
		this.paidDate = paidDate;
	}
	
	public void setPaidDate(ZonedDateTime paidDate) {
		if(paidDate == null) this.paidDate = null;
		else this.paidDate = TimeForm.convertZonedDateTimeToString(paidDate);
	}

	public void setIsPaid(Boolean isPaid) {
		if (isPaid) this.isPaid = "已付款";
		else this.isPaid = "未付款";
	}

	public void setOrderId(String orderId) {
		this.orderId = (orderId == null)? "N/A" : orderId;
	}

}
