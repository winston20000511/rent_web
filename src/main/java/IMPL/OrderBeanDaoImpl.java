package IMPL;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.OrderBean;
import Dao.OrderBeanDao;

public class OrderBeanDaoImpl implements OrderBeanDao{
	
	private Logger logger = Logger.getLogger(OrderBeanDaoImpl.class.getName());
	private Session session;
	
	public OrderBeanDaoImpl(Session session) {
		this.session = session;
	}

	
	@Override
	public List<OrderBean> getAllOrderBeans() {
		Query<OrderBean> orderBeans = session.createQuery("from OrderBean", OrderBean.class);
		
		List<OrderBean> orders = orderBeans.list();
		for(OrderBean order : orders) {
			logger.info("取得的order beans: " + order.getMerchantTradNo());
		}
		
		return orderBeans.list();
	}
	
	@Override
	public OrderBean getOrderBeanByTradNo(String tradeNo) {
		Query<OrderBean> query = session.createQuery("from OrderBean where merchanttradno=:merchanttradno", OrderBean.class);
		query.setParameter("merchanttradno", tradeNo);
		return query.getSingleResult();
	}

	@Override
	public List<Map> getOrderTableData() {
		
		try {
			
			String hqlstr = "select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
					+ "from OrderBean order by merchanttraddate";
			Query<Map> query = session.createQuery(hqlstr, Map.class);
			return query.list();
			
		}catch(Exception exception) {
			
			exception.getStackTrace();
			logger.severe("get order table data MAP 錯誤");
		}
		
		return null;
	}


	@Override
	public List<Map> getOrderTableDataByTradNo(String tradNo) {
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where merchanttradno=:merchanttradno order by merchanttraddate";
		
		try {
			Query<Map> query = session.createQuery(hqlstr, Map.class);
			query.setParameter("merchanttradno", tradNo);
			return query.list();
		} catch(Exception exception) {
			
			exception.getStackTrace();
			logger.severe("get order table by tradNo MAP 錯誤");
		}
		
		return null;
		
	}
	
	@Override
	public List<Map> getOrderTableDataByUserId(Integer userId) {
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where userid=:userid order by merchanttraddate";
		
		try {
			Query<Map> query = session.createQuery(hqlstr, Map.class);
			query.setParameter("userid", userId);
			return query.list();
			
		} catch(Exception exception) {
			
			exception.getStackTrace();
			logger.severe("get order table data by user id MAP 錯誤");
		}
		
		return null;
	}

	@Override
	public List<Map> getOrderTableDataByOrderStatus(Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where orderstatus=:orderstatus order by merchanttraddate";
		
		try {
			
			Query<Map> query = session.createQuery(hqlstr, Map.class);
			query.setParameter("orderstatus", orderStatus);
			return query.list();
			
		} catch(Exception exception) {
			
			exception.getStackTrace();
			logger.severe("get order table data by order status MAP 錯誤");
		}
		
		return null;
		
	}

	@Override
	public List<Map> getOrderTableDataByTradNoAndOrderStatus(String tradNo, Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where merchanttradno=:merchanttradno and orderstatus=:orderstatus order by merchanttraddate";
		
		try {
			
			Query<Map> query = session.createQuery(hqlstr, Map.class);		
			query.setParameter("merchanttradno", tradNo);
			query.setParameter("orderstatus", orderStatus);
			return query.list();
			
		} catch(Exception exception) {
			exception.getStackTrace();
			logger.severe("get order table data by order status MAP 錯誤");
		}
		
		return null;
		
	}
	
	@Override
	public List<Map> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where userid=:userid and orderstatus=:orderstatus order by merchanttraddate";
		
		try {
			
			Query<Map> query = session.createQuery(hqlstr, Map.class);		
			query.setParameter("userid", userId);
			query.setParameter("orderstatus", orderStatus);
			return query.list();
			
		} catch(Exception exception) {
			exception.getStackTrace();
			logger.severe("get order table data by user id and order status MAP 錯誤");
		}
		
		return null;
	}
	
	@Override
	public List<Object[]> getOrderAdCombinedDataByTradNo(String tradNo){
	    String hqlstr = "select o.userid, o.merchanttradno, o.merchanttraddate, "
                + "o.choosepayment, o.orderstatus, o.totalamount, "
                + "a.username, a.houseid, a.adid, a.adtype, "
                + "a.adduration, a.adprice, a.quantity "
                + "from OrderBean o join o.ads a "
                + "where o.merchanttradno = :merchanttradno";
	    
	    Query<Object[]> query = session.createQuery(hqlstr, Object[].class);
	    query.setParameter("merchanttradno", tradNo);

	    List<Object[]> results = query.list();
	    
	    return results;
	}


	@Override
	public OrderBean cancelOrderStatusByTradNo(String tradNo) {
		OrderBean orderBean = getOrderBeanByTradNo(tradNo);
		orderBean.setOrderStatus((short)0);
		return orderBean;
	}

}
