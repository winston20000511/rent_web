package Dao;

import java.util.List;
import java.util.Map;

import Bean.houseTableBean;

public interface houseDAO {
	
	// Read
	houseTableBean findHouseById(int houseId);
	List<houseTableBean> getAllHouses();


	// Create
	boolean createHouse(houseTableBean houseTB);

	// Update
	boolean updateHouse(Map<String, Object> updates, int houseId);

	// Delete
	boolean deleteHouseById(int houseId);
}
