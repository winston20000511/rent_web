package Dao;

import java.util.List;

import DTO.HouseDetailsDTO;
import DTO.HouseSimpleInfoDTO;

public interface houseDAO {
	
    List<HouseSimpleInfoDTO> getPaginatedHouseList(int page, int pageSize, String keyword);

    byte[] getSmallestImageByHouseId(Long houseId);
    int getTotalRecordCount(String keyword);
    boolean updateHouseStatus(Long houseId, byte status);
}
