package IMPL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.HouseDetailsDTO;
import DTO.HouseSimpleInfoDTO;
import Dao.houseDAO;
import util.ConnectionUtil;

public class houseIMPL implements houseDAO {

	@Override
	public List<HouseSimpleInfoDTO> getPaginatedHouseList(int page, int pageSize, String keyword) {
	    List<HouseSimpleInfoDTO> houses = new ArrayList<>();
	    String query = "SELECT h.house_id, h.title, h.price, h.address, h.status, " +
	                   "u.user_id, u.name AS user_name, u.email AS user_email " +
	                   "FROM house_table h JOIN user_table u ON h.user_id = u.user_id " +
	                   "WHERE u.name LIKE ? OR h.title LIKE ? " +
	                   "ORDER BY h.status ASC, h.house_id " +
	                   "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	    try (Connection conn = ConnectionUtil.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        String searchKeyword = (keyword != null && !keyword.isEmpty()) ? "%" + keyword + "%" : "%%";
	        stmt.setString(1, searchKeyword);
	        stmt.setString(2, searchKeyword);
	        stmt.setInt(3, (page - 1) * pageSize); // OFFSET
	        stmt.setInt(4, pageSize);             // FETCH NEXT

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            HouseSimpleInfoDTO house = new HouseSimpleInfoDTO();
	            house.setHouseId(rs.getLong("house_id"));
	            house.setTitle(rs.getString("title"));
	            house.setPrice(rs.getInt("price"));
	            house.setAddress(rs.getString("address"));
	            house.setStatus(rs.getByte("status"));
	            house.setUserId(rs.getLong("user_id"));
	            house.setUserName(rs.getString("user_name"));
	            house.setUserEmail(rs.getString("user_email"));
	            houses.add(house);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Database query error");
	    }
	    return houses;
	}



	
	@Override
	public byte[] getSmallestImageByHouseId(Long houseId) {
		byte[] image = null;
		String query = "SELECT TOP 1 image_url\r\n" + "FROM image_table\r\n" + "WHERE house_id = ?\r\n"
				+ "ORDER BY image_id ASC";

		try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setLong(1, houseId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				image = rs.getBytes("image_url");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return image;
	}

	@Override
	public int getTotalRecordCount(String keyword) {
	    String query = "SELECT COUNT(*) AS total " +
	                   "FROM house_table h " +
	                   "JOIN user_table u ON h.user_id = u.user_id " +
	                   "WHERE u.name LIKE ? OR h.title LIKE ?";

	    try (Connection conn = ConnectionUtil.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        String searchKeyword = (keyword != null && !keyword.isEmpty()) ? "%" + keyword + "%" : "%%";
	        stmt.setString(1, searchKeyword);
	        stmt.setString(2, searchKeyword);

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("total");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error fetching total record count");
	    }
	    return 0;
	}
	
	@Override
	public boolean updateHouseStatus(Long houseId, byte status) {
	    String query = "UPDATE house_table SET status = ? WHERE house_id = ?";

	    try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setByte(1, status);
	        stmt.setLong(2, houseId);
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to update house status");
	    }
	}
}
