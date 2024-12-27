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
@Table(name="condition_table")
public class ConditionTableBean {

	@Id
	private Long houseId;

	private Boolean pet;

	private Boolean parkingSpace;

	private Boolean elevator;

	private Boolean balcony;

	private Boolean shortTerm;

	private Boolean cooking;

	private Boolean waterDispenser;

	private Boolean managementFee;

	private Byte genderRestrictions;
}
