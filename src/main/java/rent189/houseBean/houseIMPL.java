package rent189.houseBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rent189.config.ConnectionUtil;



public class houseIMPL implements houseDAO {

	@Override
	public houseTableBean findHouseById(int houseId) {
		String sql = "select*from [RentDB].[dbo].[house_table] where [house_id] = ?";
		try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, houseId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			houseTableBean houseBean = new houseTableBean();
			houseBean.setHouseId(rs.getInt("house_id"));
			houseBean.setUserId(rs.getInt("user_id"));
			houseBean.setTitle(rs.getString("title"));
			houseBean.setPrice(rs.getInt("price"));
			houseBean.setDescription(rs.getString("description"));
			houseBean.setSize(rs.getInt("size"));
			houseBean.setCity(rs.getString("city"));
			houseBean.setTownship(rs.getString("township"));
			houseBean.setStreet(rs.getString("street"));
			houseBean.setRoom(rs.getByte("room"));
			houseBean.setBathroom(rs.getByte("bathroom"));
			houseBean.setLivingroom(rs.getByte("livingroom"));
			houseBean.setKitchen(rs.getByte("kitchen"));
			houseBean.setHousetype(rs.getByte("housetype"));
			houseBean.setFloor(rs.getByte("floor"));
			houseBean.setAtticAddition(rs.getBoolean("atticAddition"));

			return houseBean;

		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createHouse(houseTableBean houseTB) {
		String sql = "INSERT INTO [RentDB].[dbo].[house_table] \r\n"
				+ "([user_id],[title],[price],[description],[size],[city],[township] ,[street]\r\n"
				+ ",[room],[bathroom],[livingroom],[kitchen],[housetype],[floor],[atticAddition])\r\n"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, houseTB.getUserId());
			pstmt.setString(2, houseTB.getTitle());
			pstmt.setInt(3, houseTB.getPrice());
			pstmt.setString(4, houseTB.getDescription());
			pstmt.setInt(5, houseTB.getSize());
			pstmt.setString(6, houseTB.getCity());
			pstmt.setString(7, houseTB.getTownship());
			pstmt.setString(8, houseTB.getStreet());
			pstmt.setByte(9, houseTB.getRoom());
			pstmt.setByte(10, houseTB.getBathroom());
			pstmt.setByte(11, houseTB.getLivingroom());
			pstmt.setByte(12, houseTB.getKitchen());
			pstmt.setByte(13, houseTB.getHousetype());
			pstmt.setByte(14, houseTB.getFloor());
			pstmt.setBoolean(15, houseTB.getAtticAddition());
			int updateCount = pstmt.executeUpdate();
			System.out.println("已新增" + updateCount + "筆資料");

			return updateCount > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateHouse(Map<String, Object> updates, int houseId) {
		// 構建 SQL 更新語句，使用佔位符
		StringBuilder sql = new StringBuilder("UPDATE [RentDB].[dbo].[house_table] SET ");

		// 動態生成 SQL 語句的 SET 子句
		boolean first = true;
		for (String column : updates.keySet()) {
			if (!first) {
				sql.append(", ");
			}
			sql.append(column).append(" = ?");
			first = false;
		}
		sql.append(" WHERE house_id = ?;"); // 添加條件

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

			// 設置更新的欄位值
			int index = 1;
			for (Object value : updates.values()) {
				if (value instanceof String) {
					pstmt.setString(index++, (String) value);
				} else if (value instanceof Integer) {
					pstmt.setInt(index++, (Integer) value);
				} else if (value instanceof Boolean) {
					pstmt.setBoolean(index++, (Boolean) value);
				} else {
					throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
				}
			}

			// 設置 house_id
			pstmt.setInt(index, houseId);

			int updateCount = pstmt.executeUpdate();
			System.out.println("已修改了" + updateCount + "筆");

			return updateCount > 0; // 返回更新是否成功
		} catch (SQLException e) {
			e.printStackTrace(); // 錯誤處理
		}

		return false; // 更新失敗
	}

	@Override
	public boolean deleteHouseById(int houseId) {
		String deleteHouseTablesql = "delete from [RentDB].[dbo].[house_table] where house_id=?";
		String deleteFurnitureTablesql = "delete from [RentDB].[dbo].[furniture_table] where house_id=?";
		String deleteConditionTablesql = "delete from [RentDB].[dbo].[condition_table] where house_id=?";
		String deleteImageTablesql = "delete from [RentDB].[dbo].[image_table] where house_id=?";
		String deleteAdsTablesql = "delete from [RentDB].[dbo].[ads_table] where house_id=?";
		String deleteReviewTablesql = "delete from [RentDB].[dbo].[review_table] where house_id=?";
		String deleteBookingTablesql = "delete from [RentDB].[dbo].[booking_table] where house_id=?";
		
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement pstmtH = conn.prepareStatement(deleteHouseTablesql);
			PreparedStatement pstmtF = conn.prepareStatement(deleteFurnitureTablesql);
			PreparedStatement pstmtC = conn.prepareStatement(deleteConditionTablesql);
			PreparedStatement pstmtI = conn.prepareStatement(deleteImageTablesql);		
			PreparedStatement pstmtA = conn.prepareStatement(deleteAdsTablesql);		
			PreparedStatement pstmtR = conn.prepareStatement(deleteReviewTablesql);		
			PreparedStatement pstmtB = conn.prepareStatement(deleteBookingTablesql);		
	
			pstmtR.setInt(1, houseId);
			pstmtR.executeUpdate();

			pstmtA.setInt(1, houseId);
			pstmtA.executeUpdate();

			pstmtI.setInt(1, houseId);
			pstmtI.executeUpdate();

			pstmtC.setInt(1, houseId);
			pstmtC.executeUpdate();

			pstmtB.setInt(1, houseId);
			pstmtB.executeUpdate();
			
			
			pstmtF.setInt(1, houseId);
			pstmtF.executeUpdate();
			
			pstmtH.setInt(1, houseId);
			int updateCount = pstmtH.executeUpdate();
			System.out.println("已刪除了" + updateCount + "筆");
			return updateCount > 0;
	    } catch (SQLException e) {
	        System.err.println("SQL Exception: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Exception: " + e.getMessage());
	    }
	    return false;
	}

	public List<houseBACKBean> getAllHouses() {
	    List<houseBACKBean> list = new ArrayList<>();
	    String Sql1 = "SELECT [house_id], [user_id], [title], [price], [city], [township], [street] FROM [RentDB].[dbo].[house_table]";

	    try (Connection conn = ConnectionUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(Sql1);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            int userId = rs.getInt("user_id");
	            String userIdSql = "SELECT [user_id],[name] FROM [RentDB].[dbo].[user_table] WHERE user_id = ?";
	            
	            try (PreparedStatement userPstmt = conn.prepareStatement(userIdSql)) {
	                userPstmt.setInt(1, userId);
	                ResultSet userRs = userPstmt.executeQuery();
	                String userName = null;
	                if (userRs.next()) {
	                    userName = userRs.getString("name");
	                  
	                }

	                String address = String.format("%s, %s, %s", rs.getString("city"), rs.getString("township"), rs.getString("street"));
	                
	                houseBACKBean house = new houseBACKBean(rs.getInt("house_id"), rs.getString("title"),rs.getInt("user_Id"), userName, address, rs.getInt("price"));
	                list.add(house);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle exceptions appropriately
	        
	    }
	    
	    return list;
	}
}
