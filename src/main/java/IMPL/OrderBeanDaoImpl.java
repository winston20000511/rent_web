package IMPL;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.OrderBean;
import Dao.OrderBeanDao;
import DTO.OrderDetailsDTO;

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
	public List<OrderDetailsDTO> getOrderTableData() {
		
		String hqlstr = 
			    "select o.userId, o.merchantTradNo, o.merchantTradDate, o.choosePayment, o.orderStatus, " +
			    "u.name, a.adId, adt.adName " +
			    "from OrderBean o " +
			    "join o.user u " +
			    "join o.ads a " +
			    "join a.adtype adt";
		Query<OrderDetailsDTO> query = session.createQuery(hqlstr);
		return query.list();
	}


	@Override
	public List<OrderDetailsDTO> getOrderTableDataByTradNo(String tradNo) {
		String hqlstr = 
			    "select o.userId, o.merchantTradNo, o.merchantTradDate, o.choosePayment, o.orderStatus, " +
			    "u.name, a.adId, adt.adName " +
			    "from OrderBean o " +
			    "join o.user u " +
			    "join o.ads a " +
			    "join a.adtype adt";
		
		Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
		query.setParameter("merchantTradNo", tradNo);
		
		return query.list();
	}
	
	@Override
	public List<OrderDetailsDTO> getOrderTableDataByUserId(Integer userId) {
	    String hqlstr = 
	        "select o.userId, o.merchantTradNo, o.merchantTradDate, o.orderStatus "
	        + "from OrderBean o where o.userId = :userId order by o.merchantTradDate";
	    
	    Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
	    query.setParameter("userId", userId);
	    
	    return query.list();
	}

	// 用 DTO 接
	@Override
	public List<OrderDetailsDTO> getOrderTableDataByOrderStatus(Short orderStatus) {
	    String hqlstr = 
	        "select o.userId, o.merchantTradNo, o.merchantTradDate, o.orderStatus "
	        + "from OrderBean o where o.orderStatus = :orderStatus order by o.merchantTradDate";
	    
	    try {
	        Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
	        query.setParameter("orderStatus", orderStatus);
	        return query.list();
	    } catch (Exception exception) {
	        exception.printStackTrace();
	        logger.severe("get order table data by order status error");
	    }
	    
	    return null;
	}

	@Override
	public List<OrderDetailsDTO> getOrderTableDataByTradNoAndOrderStatus(String tradNo, Integer orderStatus) {
	    String hqlstr = 
	            "select o.userId, o.merchantTradNo, o.merchantTradDate, o.orderStatus "
	            + "from OrderBean o where o.merchantTradNo = :merchantTradNo and o.orderStatus = :orderStatus "
	            + "order by o.merchantTradDate";
	    
	    try {
	        Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
	        query.setParameter("merchantTradNo", tradNo);
	        query.setParameter("orderStatus", orderStatus);
	        return query.list();
	    } catch (Exception exception) {
	        exception.printStackTrace();
	        logger.severe("get order table data by order status error");
	    }
	    
	    return null;
	}

	@Override
	public List<OrderDetailsDTO> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus) {
	    String hqlstr = 
	            "select o.userId, o.merchantTradNo, o.merchantTradDate, o.orderStatus "
	            + "from OrderBean o where o.userId = :userId and o.orderStatus = :orderStatus "
	            + "order by o.merchantTradDate";
	    
	    try {
	        Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
	        query.setParameter("userId", userId);
	        query.setParameter("orderStatus", orderStatus);
	        return query.list();
	    } catch (Exception exception) {
	        exception.printStackTrace();
	        logger.severe("get order table data by user id and order status error");
	    }
	    
	    return null;
	}

	
	@Override
	public OrderDetailsDTO getOrderAdCombinedDataByTradNo(String tradNo){
	    String hqlstr = "select o.userid, o.merchanttradno, o.merchanttraddate, "
                + "o.choosepayment, o.orderstatus, o.totalamount, "
                + "a.username, a.houseid, a.adid, a.adtype, "
                + "a.adduration, a.adprice, a.quantity "
                + "from OrderBean o join o.ads a "
                + "where o.merchanttradno = :merchanttradno";
	    
	    Query<OrderDetailsDTO> query = session.createQuery(hqlstr, OrderDetailsDTO.class);
	    query.setParameter("merchanttradno", tradNo);

	    return query.getSingleResult();
	}


	@Override
	public OrderBean cancelOrderStatusByTradNo(String tradNo) {
		OrderBean orderBean = getOrderBeanByTradNo(tradNo);
		orderBean.setOrderStatus((short)0);
		return orderBean;
	}

}
