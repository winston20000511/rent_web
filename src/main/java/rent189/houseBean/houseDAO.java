package rent189.houseBean;

import java.util.List;
import java.util.Map;

public interface houseDAO {
	
	// Read
	houseTableBean findHouseById(int houseId);
	List<houseBACKBean> getAllHouses();


	// Create
	boolean createHouse(houseTableBean houseTB);

	// Update
	boolean updateHouse(Map<String, Object> updates, int houseId);

	// Delete
	boolean deleteHouseById(int houseId);
}
