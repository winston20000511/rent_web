package Bean;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="ads_table")
public class AdBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= " ad_id")
	private Long adId;
	
//	@Column(name = "user_id")
//	private Long userId;
	
	@Column(name = "house_id")
	private Long houseId;
	
	@Column(name = "ad_price")
	private Integer adPrice;
	
	@Column(name = "is_paid")
	private Boolean isPaid;
	
	@Column(name="order_id", insertable=false, updatable=false)
	private String orderId;
	
	@Column(name = "paid_date")
	private ZonedDateTime paidDate;
	
	@Column(name = "coupon")
	private Integer isCouponUsed;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	@JsonIgnore
	private OrderBean order;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adtype_id")
    @JsonIgnore
    private AdtypeBean adtype;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserTableBean user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="house_id", insertable=false, updatable=false)
	@JsonIgnore
	private HouseTableBean house;
	
	public AdBean() {
	}
	
}
