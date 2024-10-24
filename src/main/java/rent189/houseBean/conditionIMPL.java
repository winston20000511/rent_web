package rent189.houseBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import rent189.config.ConnectionUtil;

public class conditionIMPL implements conditionDAO {

	@Override
	public conditionTableBean findConditionById(int houseId) {
	    String sql = "SELECT * FROM [RentDB].[dbo].[condition_table] WHERE [house_id] = ?";
	    
	    try (Connection conn = ConnectionUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, houseId);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            conditionTableBean conditionTableBean = new conditionTableBean();
	            conditionTableBean.setConditionId(rs.getInt("condition_id"));
	            conditionTableBean.setHouseId(rs.getInt("house_id"));
	            conditionTableBean.setPet(rs.getBoolean("pet"));
	            conditionTableBean.setParkingSpace(rs.getBoolean("parkingSpace"));
	            conditionTableBean.setElevator(rs.getBoolean("elevator"));
	            conditionTableBean.setBalcony(rs.getBoolean("balcony"));
	            conditionTableBean.setShortTern(rs.getBoolean("shortTern"));
	            conditionTableBean.setCooking(rs.getBoolean("cooking"));
	            conditionTableBean.setWaterDispenser(rs.getBoolean("waterDispenser"));
	            conditionTableBean.setManagementFee(rs.getBoolean("fee"));
	            conditionTableBean.setGenderRestrictions(rs.getBoolean("genderRestrictions"));
	            
	            return conditionTableBean;
	        } else {
	            System.out.println("No records found for house_id: " + houseId);
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error: " + e.getMessage());
	    }
	    
	    return null; // 如果沒有找到資料，回傳 null
	}

	@Override
	public boolean createCondition(conditionTableBean conditionTB){
		String sql = "INSERT INTO [RentDB].[dbo].[condition_table]\r\n"
				+ "  ([house_id],[pet],[parkingSpace],[elevator],[balcony],[shortTern],[cooking],[waterDispenser]\r\n"
				+ "	  ,[fee],[genderRestrictions])\r\n"
				+ "   VALUES(?,?,?,?,?,?,?,?,?,?);";
		try (Connection conn = ConnectionUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, conditionTB.getHouseId());
	        pstmt.setBoolean(2, conditionTB.getPet());
	        pstmt.setBoolean(3, conditionTB.getParkingSpace());
	        pstmt.setBoolean(4, conditionTB.getElevator());
	        pstmt.setBoolean(5, conditionTB.getBalcony());
	        pstmt.setBoolean(6, conditionTB.getShortTern());
	        pstmt.setBoolean(7, conditionTB.getCooking());
	        pstmt.setBoolean(8, conditionTB.getWaterDispenser());
	        pstmt.setBoolean(9, conditionTB.getManagementFee());
	        pstmt.setBoolean(10, conditionTB.getGenderRestrictions());

	        int updateCount = pstmt.executeUpdate();
	        System.out.println("已新增" + updateCount + "筆資料");
	        
	        return updateCount > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}

	@Override
	public boolean updateCondition(Map<String, Object> updates,int conditionId) {
	    // 構建 SQL 更新語句，使用佔位符
		StringBuilder sql = new StringBuilder("UPDATE [RentDB].[dbo].[condition_table] SET ");
		 // 動態生成 SQL 語句的 SET 子句
	    boolean first = true;
	    for (String column : updates.keySet()) {
	        if (!first) {
	            sql.append(", ");
	        }
	        sql.append(column).append(" = ?");
	        first = false;
	    }
	    sql.append(" WHERE condition_id = ?;"); // 添加條件
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
	        pstmt.setInt(index, conditionId);

	        int updateCount = pstmt.executeUpdate();
	        System.out.println("已修改了" + updateCount + "筆");

	        return updateCount > 0; // 返回更新是否成功
	    } catch (SQLException e) {
	        e.printStackTrace(); // 錯誤處理
	    }

	    return false; // 更新失敗
	}
	
	@Override
	public boolean deleteConditionById(int conditionId) {
		String sql = "delete from [RentDB].[dbo].[condition_table] where condition_id = ?";
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,conditionId);
			int updateCount = pstmt.executeUpdate();
			System.out.println("已刪除了" + updateCount + "筆");
			if (updateCount > 0)
				return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
