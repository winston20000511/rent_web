package Dao;

import java.util.List;
import java.util.Map;

import Bean.houseTableBean;
import DTO.HouseDetailsDTO;
import DTO.HouseSimpleInfoDTO;

public interface houseDAO {
	
	boolean updateHouseDetailsWithoutImageAndUser(Long houseId, HouseDetailsDTO houseDetails);
    List<HouseSimpleInfoDTO> getPaginatedHouseList(int page, int pageSize, String keyword);
    HouseDetailsDTO getHouseDetailsById(Long houseId);
    byte[] getSmallestImageByHouseId(Long houseId);
}
