package rent189.adOrder.adBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import rent189.adOrder.util.TimeForm;
import rent189.config.ConnectionUtil;

public class AdBeanDaoImpl implements AdBeanDao {

	// 使用者會用到 create
	@Override
	public boolean createAdBean(AdBean adBean) {
		StringBuilder sqlCreate = new StringBuilder("insert into ads_table "
				+ "(user_id, user_name, house_id, ad_type, ad_duration, ad_price, quantity, "
				+ "created_at, is_paid, order_id, paid_date, expires_at) values(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		try(Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlCreate.toString());) {
			pstmt.setInt(1, adBean.getAdId());
			pstmt.setString(2, adBean.getUserName());
			pstmt.setInt(3, adBean.getHouseId());
			pstmt.setString(4, adBean.getAdType());
			pstmt.setInt(5, adBean.getAdDuration());
			pstmt.setInt(6, adBean.getAdPrice());
			pstmt.setInt(7, adBean.getQuantity());
			pstmt.setTimestamp(8, TimeForm.convertZoneDateTimeToTimestamp(adBean.getCreatedAt()));
			pstmt.setBoolean(9, adBean.getIsPaid());
			pstmt.setString(10, adBean.getOrderId());
			pstmt.setTimestamp(11, TimeForm.convertZoneDateTimeToTimestamp(adBean.getPaidDate()));
			pstmt.setTimestamp(12, TimeForm.convertZoneDateTimeToTimestamp(adBean.getExpiresAt()));
			
			int row = pstmt.executeUpdate();
			System.out.println(row + " data have been updated.");
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public ArrayList<AdBean> getAdBeans() {
		StringBuilder sqlSelect = new StringBuilder("select*from ads_table");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}

			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AdBean getAdBeanByAdId(Integer adId) {
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " + "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table " + "where ad_id = ?");
		
		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			pstmt.setInt(1, adId);
			ResultSet resultSet = pstmt.executeQuery();

			// resultSet 一開始指標在index 0 之前 => 要 next 只到實際有值的位置
			if (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				return adBean;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<AdBean> getAdBeansByUserId(Integer userId) {
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " + "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table " + "where user_id = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			pstmt.setInt(1, userId);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<AdBean> getAdBeansByHouseId(Integer houseId) {
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " + "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table " + "where house_id = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			pstmt.setInt(1, houseId);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<AdBean> getAdBeansByIsPaid(Boolean isPaid) {
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " + "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table " + "where is_paid = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			int paidStatus = 0;
			if (isPaid) paidStatus = 1;

			pstmt.setInt(1, paidStatus);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	// get by combined conditions: 1. select adId, houseId, userId  2. is_paid
	@Override
	public ArrayList<AdBean> getAdBeanByAdIdAndIsPaid(Integer adId, Boolean isPaid){
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " 
				+ "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table "
				+ "where ad_id = ? and is_paid = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			
			pstmt.setInt(1, adId);
			
			int paidStatus = 0;
			if (isPaid) paidStatus = 1;

			pstmt.setInt(2, paidStatus);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public ArrayList<AdBean> getAdBeanByHouseIdAndIsPaid(Integer houseId, Boolean isPaid){
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " 
				+ "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table "
				+ "where house_id = ? and is_paid = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			
			pstmt.setInt(1, houseId);
			
			int paidStatus = 0;
			if (isPaid) paidStatus = 1;

			pstmt.setInt(2, paidStatus);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	};
	
	@Override
	public ArrayList<AdBean> getAdBeanByUserIdAndIsPaid(Integer userId, Boolean isPaid){
		StringBuilder sqlSelect = new StringBuilder("select ad_id, user_id, user_name, "
				+ "house_id, ad_type, ad_duration, ad_price, " 
				+ "quantity, created_at, is_paid, order_id, "
				+ "paid_date, expires_at from ads_table "
				+ "where user_id = ? and is_paid = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");

			
			pstmt.setInt(1, userId);
			
			int paidStatus = 0;
			if (isPaid) paidStatus = 1;

			pstmt.setInt(2, paidStatus);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	};
	
	public ArrayList<AdBean> getAdBeansByOrderId(String orderId){
		StringBuilder sqlSelect = new StringBuilder("select* from ads_table where order_id = ?");
		ArrayList<AdBean> adBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setString(1, orderId);
			ResultSet resultSet = pstmt.executeQuery();

			while (resultSet.next()) {
				AdBean adBean = setAdBean(resultSet);
				adBeanList.add(adBean);
			}
			return adBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	@Override
	public ArrayList<AdBean> getCanceledAdBeans() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public AdBean updateAdBeanByAdId(Integer adId, List<String> receivedData) {
		
		// get adBean
		AdBean adBean = getAdBeanByAdId(adId);
		
		// collect different types of value from database
		List<Object> list = new ArrayList<>();
		
		StringBuilder sqlUpdate = new StringBuilder("update ads_table set "); 
		
		// check if it's the first condition
		boolean isFirst = true;
		
		// 0. ad id 不更新
		// user id 資料庫撈資料就會一起更新	
		// user name & house id 同上
	
		// 1. ad type
		String adType = receivedData.get(1).equals("a") ? "A廣告":"B廣告";
		String newAdType = adType.isEmpty() ? null : adType;
		if(newAdType != null) {
			adBean.setAdType(newAdType);
			
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("ad_type = ?");
			list.add(adBean.getAdType());
			isFirst = false;
		}

		// 2. ad duration
		String duration = receivedData.get(1).equals("a") ? "30" : "60";
		String newDuration = duration.isEmpty() ? null : duration;
		if(newDuration != null) {
			adBean.setAdDuration(Integer.parseInt(newDuration));
			
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("ad_duration = ?");
			list.add(adBean.getAdDuration());
			isFirst = false;
		}

		// 3. ad price
		String price = receivedData.get(1).equals("a") ? "10000" : "20000";
		String newPrice = price.isEmpty() ? null : price;
		if(newPrice != null) {
			adBean.setAdPrice(Integer.parseInt(newPrice));
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("ad_price = ?");
			list.add(adBean.getAdPrice());
			isFirst = false;
		}
		
		// 4. quantity
		String quantity = receivedData.get(4);
		String newQuantity = quantity.isEmpty() ? null : quantity;
		if(newQuantity != null) {
			adBean.setQuantity(Integer.parseInt(newQuantity));
			
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("quantity = ?");
			list.add(adBean.getQuantity());
			isFirst = false;
		}
		
		// 5. created at; time is automatically generated
		ZonedDateTime newDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Taipei"));
		if(newDateTime != null) {
			adBean.setCreatedAt(newDateTime);
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("created_at =?");
			list.add(TimeForm.convertZoneDateTimeToTimestamp(adBean.getCreatedAt()));
			
			isFirst = false;
		}
		
		
		// 6. is paid
		String isPaid = receivedData.get(6);
		String newIsPaid = isPaid.isEmpty() ? null : isPaid;
		if(newIsPaid != null) {
			adBean.setIsPaid(Boolean.valueOf(newIsPaid));
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("is_paid = ?");
			
			int paidStatus = 0;
			if(adBean.getIsPaid() == true) paidStatus = 1;
			else paidStatus = 0;
			list.add(paidStatus);
			isFirst = false;
		}
		
		
		// 7. order id
		String orderId = receivedData.get(7);
		String newOrderId = orderId.isEmpty() ? null : orderId;
		if(newOrderId != null) {
			adBean.setOrderId(newOrderId);
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("order_id = ?");
			list.add(adBean.getOrderId());
			isFirst = false;
		}
		
		// 8. paid date
		String paidDate = receivedData.get(8);
		String newPaidDate = paidDate.isEmpty() ? null : paidDate;
		if(newPaidDate != null) {
			ZonedDateTime zonedPaidDate = TimeForm.convertStringToZonedDateTime(newPaidDate);
			adBean.setPaidDate(zonedPaidDate);
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("paid_date = ?");
			list.add(TimeForm.convertZoneDateTimeToTimestamp(adBean.getPaidDate()));
			isFirst = false;
		}
		
		// 9. expires at
		String expiresDate = receivedData.get(9);
		String newExpiresDate = expiresDate.isEmpty() ? null : expiresDate;
		if(newExpiresDate != null) {
			ZonedDateTime zonedExpiresDate = TimeForm.convertStringToZonedDateTime(newExpiresDate);
			adBean.setExpiresAt(zonedExpiresDate);
			if(!isFirst) {
				sqlUpdate.append(", ");
			}
			sqlUpdate.append("expires_at = ?");
			list.add(TimeForm.convertZoneDateTimeToTimestamp(adBean.getExpiresAt()));

			isFirst = false;
		}
		
		
		// where
		sqlUpdate.append("where ad_id = ?");
		list.add(adBean.getAdId());
		
		System.out.println(list.toString());
		
		try(Connection conn = ConnectionUtil.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sqlUpdate.toString());){
			
			System.out.println("connected");
			
			for(int i = 0; i < list.size(); i++) {
				pstmt.setObject(i+1, list.get(i));
			}
			
			int row = pstmt.executeUpdate();
			System.out.println(row + "data have been updated.");
			
			return adBean;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	@Override
	public boolean deleteAdBeanByAdId(Integer adId) {
		StringBuilder sqlDelete = new StringBuilder("delete from ads_table where ad_id=?");
		
		try(Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlDelete.toString());) {
			
			System.out.println("connected");
			
			pstmt.setInt(1, adId);
			int row = pstmt.executeUpdate();
			System.out.println(row + " data have been deleted.");
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// inner class
	private AdBean setAdBean(ResultSet resultSet) throws SQLException {
		AdBean adBean = new AdBean();
		adBean.setAdId(resultSet.getInt(1));
		adBean.setUserId(resultSet.getInt(2));
		adBean.setUserName(resultSet.getString(3));
		adBean.setHouseId(resultSet.getInt(4));
		adBean.setAdType(resultSet.getString(5));
		adBean.setAdDuration(resultSet.getInt(6));
		adBean.setAdPrice(resultSet.getInt(7));
		adBean.setQuantity(resultSet.getInt(8));

		Timestamp sqlTimestamp = resultSet.getTimestamp(9);
		ZonedDateTime zonedDateTime = TimeForm.convertTimestampToZoneDateTime(sqlTimestamp);

		adBean.setCreatedAt(zonedDateTime);
		adBean.setIsPaid(resultSet.getBoolean(10));
		
		
		adBean.setOrderId(resultSet.getString(11));
		adBean.setPaidDate(TimeForm.convertTimestampToZoneDateTime(resultSet.getTimestamp(12)));
		
		
		adBean.setExpiresAt(TimeForm.convertTimestampToZoneDateTime(resultSet.getTimestamp(13)));

		return adBean;
	}

}
