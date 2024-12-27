package Bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "furniture_table")
public class FurnitureTableBean {

	@Id
	private Long houseId;
	private Boolean washingMachine;

	private Boolean airConditioner;

	private Boolean network;

	private Boolean bedstead;

	private Boolean mattress;

	private Boolean refrigerator;

	private Boolean ewaterHeater;
	
	private Boolean gwaterHeater;

	private Boolean television;

	private Boolean channel4;

	private Boolean sofa;

	private Boolean tables;


}