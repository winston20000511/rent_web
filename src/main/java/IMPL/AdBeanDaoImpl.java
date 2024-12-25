package IMPL;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.AdBean;
import Dao.AdBeanDao;
import util.HibernateUtil;

public class AdBeanDaoImpl implements AdBeanDao {

	private Session session;

	public AdBeanDaoImpl(Session session) {
		this.session = session;
	}

	// 使用 HQL 的 create 方法
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
	public AdBean getAdBeanByAdId(Integer adid) {
		return session.get(AdBean.class, adid);
	}

	@Override
	public List<AdBean> getAdBeansByUserId(Integer userid) {
		Query<AdBean> query = session.createQuery("from AdBean where userid = :userid", AdBean.class);
		query.setParameter("userid", userid);
		return query.getResultList();
	}

	@Override
	public List<AdBean> getAdBeansByHouseId(Integer houseid) {
		Query<AdBean> query = session.createQuery("from AdBean where houseid = :houseid", AdBean.class);
		query.setParameter("houseid", houseid);
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
	public AdBean updateAdBeanOnTypeAndQty(Integer adid, String adtype, Integer quantity) {
		Integer adduration = adtype.equals("A廣告") ? 30 : 60;
		Integer adprice = adtype.equals("A廣告") ? 10000 : 20000;

		AdBean adBean = getAdBeanByAdId(adid);
		adBean.setAdtype(adtype);
		adBean.setAdduration(adduration);
		adBean.setAdprice(adprice);
		adBean.setQuantity(quantity);
		adBean.setCreatedat(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Taipei")));

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
