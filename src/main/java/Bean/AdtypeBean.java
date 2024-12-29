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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "adtype_table")
public class AdtypeBean {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private Integer adtypeId;
	
	@Column(name = "adname")
	private String adName;
	
	@Column(name = "adprice")
	private Integer adPrice;
	
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adtype", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AdBean> ads = new ArrayList<>();

	public AdtypeBean() {
		
	}

	
}