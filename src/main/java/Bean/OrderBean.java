package Bean;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders_table")
public class OrderBean {
	
	@Column(name = "user_id")
	private Long userId;
	
	@Id
	@Column(name = "merchantTradNo")
	private String merchantTradNo;
	
	@Column(name = "merchantTradDate")
	private ZonedDateTime merchantTradDate;
	
	@Column(name = "totalAmount")
	private Long totalAmount;
	
	@Column(name = "tradeDesc")
	private String tradeDesc;
	
	@Column(name = "itemName")
	private String itemName;
	
	@Column(name = "order_status", columnDefinition = "TINYINT")
	private Short orderStatus;
	
	@Column(name = "choosePayment")
	private String choosePayment;

	@Column(name = "third_party")
	private Short thirdParty;
	
	@Column(name = "returnValue")
	private String returnValue;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="user_id", insertable=false, updatable=false)
//	@JsonIgnore
//	private UserTableBean user;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="order", cascade = CascadeType.ALL)
	private List<AdBean> ads = new ArrayList<AdBean>();

	public OrderBean() {
	}
	
	
}

