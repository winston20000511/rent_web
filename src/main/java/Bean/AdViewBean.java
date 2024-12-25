package Bean;

import java.time.ZonedDateTime;
import java.util.logging.Logger;

import lombok.Data;
import util.TimeForm;

@Data
public class AdViewBean {
	
	private Logger logger = Logger.getLogger(AdViewBean.class.getName());
	
	private Long adId;
	private Long userId;
	private Integer houseTitle;
	private String adName;
	private Integer adPrice;
	private Integer subtotal;
	private Boolean isCouponUsed;
	private String isPaid;
	private String orderId;
	private String paidDate;
	private String adDuration;

	public AdViewBean() {
		
	}
	
	public AdViewBean(Long adId, Long userId, Integer houseTitle, String adName, String adDuration,
			Integer adPrice, Integer subtotal, String isPaid, String orderId, String paidDate) {
		this.adId = adId;
		this.userId = userId;
		this.houseTitle = houseTitle;
		this.adName = adName;
		this.adDuration = adDuration;
		this.adPrice = adPrice;
		this.subtotal = subtotal;
		this.isPaid = isPaid;
		this.orderId = orderId;
		this.paidDate = paidDate;
	}

	public void setAdDuration(Integer adDuration) {
		if(adDuration==30) this.adDuration = "30天";
		else if(adDuration==60)this.adDuration = "60天";
		logger.info("資料型別有誤");
	}
	
	public void setPaidDate(ZonedDateTime paidDate) {
		this.paidDate = TimeForm.convertZonedDateTimeToString(paidDate);
	}

	public void setIsPaid(Boolean isPaid) {
		if (isPaid) this.isPaid = "已付款";
		else this.isPaid = "未付款";
	}

	public void setOrderId(String orderId) {
		this.orderId = (orderId == null)? "N/A" : orderId;
	}

}
