package rent189.adOrder.orderBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import rent189.adOrder.util.TimeForm;
import rent189.config.ConnectionUtil;

public class OrderBeanDaoImpl implements OrderBeanDao{

	@Override
	public boolean createOrderBean(OrderBean orderBean) {
		StringBuilder sqlCreate = new StringBuilder(
				"insert into orders_table "
				+ "(user_id, merchantTradNo, merchantTradDate, totalAmount, tradeDesc, "
				+ "itemName,returnUrl, choosePayment, checkMacValue) "
				+ "values (?,?,?,?,?,?,?,?,?)");
		
		try(Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlCreate.toString());) {
			
			System.out.println("connected");
			
			pstmt.setInt(1, orderBean.getUserId());
			pstmt.setString(2, orderBean.getMerchantTradNo());
			pstmt.setTimestamp(3, TimeForm.convertZoneDateTimeToTimestamp(orderBean.getMerchantTradDate()));
			pstmt.setInt(4, orderBean.getTotalAmount());
			pstmt.setString(5, orderBean.getTradeDesc());
			pstmt.setString(6, orderBean.getItemName());
			pstmt.setString(7, orderBean.getReturnUrl());
			pstmt.setString(8, orderBean.getChoosePayment());
			pstmt.setString(9, orderBean.getCheckMacValue());
			
			int row = pstmt.executeUpdate();
			System.out.println(row + " data have been updated.");
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public ArrayList<OrderBean> getOrderBeans() {
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table");
		ArrayList<OrderBean> orderBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				orderBeanList.add(orderBean);
			}

			return orderBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public OrderBean getOrderBeansByTradNo(String tradNo) {
		
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table where merchantTradNo = ?");

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setString(1, tradNo);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				return orderBean;
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<OrderBean> getOrderBeansByUserId(Integer userId) {
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table where user_id = ?");
		ArrayList<OrderBean> orderBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setInt(1, userId);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				orderBeanList.add(orderBean);
			}

			return orderBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<OrderBean> getOrderBeanByOrderStatus(Boolean orderStatus){
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table where order_status = ?");
		ArrayList<OrderBean> orderBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setBoolean(1, orderStatus);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				orderBeanList.add(orderBean);
			}

			return orderBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	public ArrayList<OrderBean> getOrderByTradNoAndOrderStatus(String tradeNo, Boolean orderStatus){
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table where merchantTradNo = ? and order_status = ?");
		ArrayList<OrderBean> orderBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setString(1, tradeNo);
			pstmt.setBoolean(2, orderStatus);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				orderBeanList.add(orderBean);
			}

			return orderBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<OrderBean> getOrderByUserIdAndOrderStatus(Integer userId, Boolean orderStatus){
		StringBuilder sqlSelect = new StringBuilder("select*from orders_table where user_id = ? and order_status = ?");
		ArrayList<OrderBean> orderBeanList = new ArrayList<>();

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlSelect.toString());) {

			System.out.println("connected");
			
			pstmt.setInt(1, userId);
			pstmt.setBoolean(2, orderStatus);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				OrderBean orderBean = setOrderBean(resultSet);
				orderBeanList.add(orderBean);
			}

			return orderBeanList;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Override
	public boolean updateOrderStatusByTradNo(String tradNo) {
		StringBuilder sqlUpdate = new StringBuilder("update orders_table set order_status = 0 where merchantTradNo = ?");
		try(Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate.toString());) {
			
			System.out.println("connected");
			pstmt.setString(1, tradNo);
			int row = pstmt.executeUpdate();
			System.out.println(row + "data have been updated");
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public boolean deleteOrderBeanByTradNo(String tradNo) {
		StringBuilder sqlDelete = new StringBuilder("delete from orders_table where merchantTradNo = ?");

		try (Connection conn = ConnectionUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlDelete.toString());) {

			System.out.println("connected");
			
			pstmt.setString(1, tradNo);
			int row = pstmt.executeUpdate();
			System.out.println(row + " data have been deleted.");
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	
	
	// inner methods
	private OrderBean setOrderBean(ResultSet resultSet) throws SQLException {
		OrderBean orderBean = new OrderBean();
		orderBean.setUserId(resultSet.getInt("user_id"));
		orderBean.setMerchantId(resultSet.getString("merchant_id"));
		orderBean.setMerchantTradNo(resultSet.getString("merchantTradNo"));
		orderBean.setMerchantTradDate(TimeForm.convertTimestampToZoneDateTime(resultSet.getTimestamp("merchantTradDate")));
		orderBean.setPaymentType(resultSet.getString("paymentType"));
		orderBean.setTotalAmount(resultSet.getInt("totalAmount"));
		orderBean.setTradeDesc(resultSet.getString("tradeDesc"));
		orderBean.setItemName(resultSet.getString("itemName"));
		orderBean.setOrderStatus(resultSet.getBoolean("order_status"));
		orderBean.setReturnUrl(resultSet.getString("returnUrl"));
		orderBean.setChoosePayment(resultSet.getString("choosePayment"));
		orderBean.setCheckMacValue(resultSet.getString("checkMacValue"));
		orderBean.setEncryptType(resultSet.getInt("encryptType"));
		
		return orderBean;
	}


}
