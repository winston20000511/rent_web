package Dao;

import java.util.List;

import dto.HouseDetailsDTO;
import dto.HouseSimpleInfoDTO;

public interface houseDAO {
	
	boolean updateHouseDetailsWithoutImageAndUser(Long houseId, HouseDetailsDTO houseDetails);
    List<HouseSimpleInfoDTO> getPaginatedHouseList(int page, int pageSize, String keyword);
    HouseDetailsDTO getHouseDetailsById(Long houseId);
    byte[] getSmallestImageByHouseId(Long houseId);
}