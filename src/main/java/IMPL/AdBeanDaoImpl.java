package IMPL;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.AdBean;
import Dao.AdBeanDao;

public class AdBeanDaoImpl implements AdBeanDao {

	private Session session;

	public AdBeanDaoImpl(Session session) {
		this.session = session;
	}

	@Override
	public boolean createAdBean(AdBean adBean) {
		try {
			session.persist(adBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<AdBean> getAllAdBeans() {
		Query<AdBean> query = session.createQuery("from AdBean", AdBean.class);
		return query.list();
	}

	@Override
	public AdBean getAdBeanByAdId(Integer adId) {
		return session.get(AdBean.class, adId);
	}

	@Override
	public List<AdBean> getAdBeansByUserId(Integer userId) {
		Query<AdBean> query = session.createQuery("from AdBean where userId = :userId", AdBean.class);
		query.setParameter("userid", userId);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByHouseId(Integer houseId) {
		Query<AdBean> query = session.createQuery("from AdBean where houseId = :houseId", AdBean.class);
		query.setParameter("houseId", houseId);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByIsPaid(Boolean ispaid) {
		Query<AdBean> query = session.createQuery("from AdBean where ispaid = :ispaid", AdBean.class);
		query.setParameter("ispaid", ispaid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeanByAdIdAndIsPaid(Integer adid, Boolean ispaid) {
		Query<AdBean> query = session.createQuery("from AdBean where adid = :adid and ispaid = :ispaid", AdBean.class);
		query.setParameter("adid", adid);
		query.setParameter("ispaid", ispaid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByHouseIdAndIsPaid(Integer houseid, Boolean ispaid) {
		Query<AdBean> query = session.createQuery("from AdBean where houseid = :houseid and ispaid = :ispaid", AdBean.class);
		query.setParameter("houseid", houseid);
		query.setParameter("ispaid", ispaid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByUserIdAndIsPaid(Integer userid, Boolean ispaid) {
		Query<AdBean> query = session.createQuery("from AdBean where userid = :userid and ispaid = :ispaid", AdBean.class);
		query.setParameter("userid", userid);
		query.setParameter("ispaid", ispaid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByOrderId(String orderid) {
		Query<AdBean> query = session.createQuery("from AdBean where orderid = :orderid", AdBean.class);
		query.setParameter("orderid", orderid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getCanceledAdBeans() {
		// 暫無實作，這裡可以依需求填寫具體邏輯
		return null;
	}

	@Override
	public boolean updateAdBean(AdBean adBean) {
		try {
			session.update(adBean);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public AdBean updateAdBeanOnAdTypeAndPrice(Integer adid, Integer adtypeId) {
		AdBean adBean = getAdBeanByAdId(adid);
		adBean.setAdPrice(adtypeId);
//		adBean.setAdtypeId(null);
//		adBean.setAdtype(null);

		return adBean;
	}

	@Override
	public boolean deleteAdBeanByAdId(Integer adid) {
		try {
			Query<?> query = session.createQuery("delete from AdBean where adid = :adid");
			query.setParameter("adid", adid);
			int result = query.executeUpdate();
			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
