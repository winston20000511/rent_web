package rent189.houseBean;

import java.sql.Connection;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rent189.config.ConnectionUtil;

public class furnitureIMPL implements furnitureDAO{
	
	public furnitureTableBean findFurnitureById(int houseId) {
		String sql ="select*from  [dbo].[furniture_table] WHERE [house_id] = ?";
		try (Connection conn = ConnectionUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        
		        pstmt.setInt(1, houseId);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            furnitureTableBean furnitureTableBean = new furnitureTableBean();
		            furnitureTableBean.setFurnitureId(rs.getInt("furniture_id"));
		            furnitureTableBean.setHouseId(rs.getInt("house_id"));
		            furnitureTableBean.setWashingMachine(rs.getBoolean("washingMachine"));
		            furnitureTableBean.setAirConditioner(rs.getBoolean("airConditioner"));
		            furnitureTableBean.setNetwork(rs.getBoolean("network"));
		            furnitureTableBean.setBedstead(rs.getBoolean("bedstead"));
		            furnitureTableBean.setMattress(rs.getBoolean("mattress"));
		            furnitureTableBean.setRefrigerator(rs.getBoolean("refrigerator"));
		            furnitureTableBean.setEwaterHeater(rs.getBoolean("ewaterHeater"));
		            furnitureTableBean.setGwaterHeater(rs.getBoolean("gwaterHeater"));
		            furnitureTableBean.setTelevision(rs.getBoolean("television"));
		            furnitureTableBean.setChannel4(rs.getBoolean("channel4"));
		            furnitureTableBean.setSofa(rs.getBoolean("sofa"));
		            furnitureTableBean.setTables(rs.getBoolean("tables"));
		            
		            return furnitureTableBean;
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
	public boolean createFurniture(furnitureTableBean furnitureTB){
		String sql = "INSERT INTO [dbo].[furniture_table]\r\n"
				+ " ([house_id],[washingMachine],[airConditioner],[network],[bedstead],[mattress]\r\n"
				+ " ,[refrigerator],[ewaterHeater],[gwaterHeater],[television],[channel4],[sofa]\r\n"
				+ " ,[tables])\r\n"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try (Connection conn = ConnectionUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);){
	        pstmt.setInt(1, furnitureTB.getHouseId());
	        pstmt.setBoolean(2, furnitureTB.getWashingMachine());
	        pstmt.setBoolean(3, furnitureTB.getAirConditioner());
	        pstmt.setBoolean(4, furnitureTB.getNetwork());
	        pstmt.setBoolean(5, furnitureTB.getBedstead());
	        pstmt.setBoolean(6, furnitureTB.getMattress());
	        pstmt.setBoolean(7, furnitureTB.getRefrigerator());
	        pstmt.setBoolean(8, furnitureTB.getEwaterHeater());
	        pstmt.setBoolean(9, furnitureTB.getGwaterHeater());
	        pstmt.setBoolean(10,furnitureTB.getTelevision());
	        pstmt.setBoolean(11,furnitureTB.getChannel4());
	        pstmt.setBoolean(12,furnitureTB.getSofa());
	        pstmt.setBoolean(13,furnitureTB.getTables());


	        int updateCount = pstmt.executeUpdate();
	        System.out.println("已新增" + updateCount + "筆資料");
	        
	        return updateCount > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}

	@Override
	public boolean updateFurniture(Map<String, Object> updates, int furnitureId) {
	    // 構建動態 SQL 更新語句
	    StringBuilder sql = new StringBuilder("UPDATE [dbo].[furniture_table] SET ");
	    boolean first = true;
	    for (String column : updates.keySet()) {
	        if (!first) {
	            sql.append(", ");
	        }
	        sql.append(column).append(" = ?");
	        first = false;
	    }
	    sql.append(" WHERE furniture_id = ?;");

	    try (Connection conn = ConnectionUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

	        // 根據 Map 中的值設置參數
	        int index = 1;
	        for (Object value : updates.values()) {
	            if (value instanceof String) {
	                pstmt.setString(index, (String) value);
	            } else if (value instanceof Integer) {
	                pstmt.setInt(index, (Integer) value);
	            } else if (value instanceof Boolean) {
	                pstmt.setBoolean(index, (Boolean) value);
	            } else {
	                throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
	            }
	            index++;
	        }

	        // 設置 furniture_id 參數
	        pstmt.setInt(index, furnitureId);

	        int updateCount = pstmt.executeUpdate();
	        System.out.println("已修改了" + updateCount + "筆");

	        return updateCount > 0; // 返回更新是否成功
	    } catch (SQLException e) {
	        e.printStackTrace(); // 錯誤處理
	    }

	    return false; // 更新失敗
	}
	
	@Override
	public boolean deleteFurnitureById(int furnitureId) {
		String sql = "delete from [dbo].[furniture_table] where furniture_id=?";
		try (Connection conn = ConnectionUtil.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,furnitureId);
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
