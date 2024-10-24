package rent189.houseBean;

import java.util.Map;

public interface furnitureDAO {
	//Read
	furnitureTableBean findFurnitureById(int houseId);
			
	//Create
		boolean createFurniture(furnitureTableBean furnitureTB);
		
	//Update
		boolean updateFurniture(Map<String, Object> updates, int furnitureId);
		
	//Delete
		boolean deleteFurnitureById(int furnitureId);
}
