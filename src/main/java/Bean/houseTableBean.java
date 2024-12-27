package Bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "house_table")
public class houseTableBean {

	@Id
	@Column(name = "house_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long houseId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserTableBean user;
	private String title;
	private Integer price;
	private String description;
	private Integer size;
	private String address;
	private Double lat;
	private Double lng;
	private Byte room;
	private Byte bathroom;
	private Byte livingroom;
	private Byte kitchen;
	private String houseType;
	private Byte floor;
	private Boolean atticAddition;
	private Byte status;
	private Integer clickCount;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "houseBean", cascade = CascadeType.ALL)
	private List<BookingBean> bookings = new ArrayList<BookingBean>();

}