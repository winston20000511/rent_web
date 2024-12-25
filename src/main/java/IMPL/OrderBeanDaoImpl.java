package IMPL;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.OrderBean;
import Dao.OrderBeanDao;

@SuppressWarnings("rawtypes")
public class OrderBeanDaoImpl implements OrderBeanDao{
	
	private Session session;
	
	public OrderBeanDaoImpl(Session session) {
		this.session = session;
	}

	
	@Override
	public List<OrderBean> getAllOrderBeans() {
		Query<OrderBean> orderBeans = session.createQuery("from OrderBean", OrderBean.class);
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
		String hqlstr = "select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);
		return query.list();
	}


	@Override
	public List<Map> getOrderTableDataByTradNo(String tradNo) {
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where merchanttradno=:merchanttradno order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);
		query.setParameter("merchanttradno", tradNo);
		return query.list();
	}
	
	@Override
	public List<Map> getOrderTableDataByUserId(Integer userId) {
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where userid=:userid order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);
		query.setParameter("userid", userId);
		return query.list();
	}

	@Override
	public List<Map> getOrderTableDataByOrderStatus(Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where orderstatus=:orderstatus order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);
		query.setParameter("orderstatus", orderStatus);
		return query.list();
	}

	@Override
	public List<Map> getOrderTableDataByTradNoAndOrderStatus(String tradNo, Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where merchanttradno=:merchanttradno and orderstatus=:orderstatus order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);		
		query.setParameter("merchanttradno", tradNo);
		query.setParameter("orderstatus", orderStatus);
		return query.list();
	}
	
	@Override
	public List<Map> getOrderTableDataByUserIdAndOrderStatus(Integer userId, Integer orderStatus){
		String hqlstr = 
				"select new map(userid as userId, merchanttradno as merchantTradNo, merchanttraddate as merchantTradDate, orderstatus as orderStatus) "
				+ "from OrderBean where userid=:userid and orderstatus=:orderstatus order by merchanttraddate";
		Query<Map> query = session.createQuery(hqlstr, Map.class);		
		query.setParameter("userid", userId);
		query.setParameter("orderstatus", orderStatus);
		return query.list();
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
		orderBean.setOrderstatus(0);
		return orderBean;
	}

}
