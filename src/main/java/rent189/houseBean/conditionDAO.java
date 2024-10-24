package rent189.houseBean;

import java.util.Map;

public interface conditionDAO {
	//Read
	conditionTableBean findConditionById(int houseId);
				
		//Create
			boolean createCondition(conditionTableBean conditionTB);
			
		//Update
			boolean updateCondition(Map<String, Object> updates,int conditionId);
			
		//Delete
			boolean deleteConditionById(int conditionId);
}
