package Dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Bean.formComplationBean;



public class formDao {

	private Session session;

	public formDao(Session session) {
		this.session = session;
	}

	// search all
	public List<formComplationBean> queryAll() {
		Query<formComplationBean> query = session.createQuery("from fromComplationBean", formComplationBean.class);
		return query.list();
	}

	// search // Read
	public formComplationBean queryById(Integer complaints_id) {
		return session.get(formComplationBean.class, complaints_id);
	}

	// Create
	public boolean insert(formComplationBean frombean) {
//		session.save(frombean);
		session.persist(frombean);
		return true;
	}

	// Delete
	public boolean deleteById(formComplationBean DBean) {
		formComplationBean managedBean = session.get(formComplationBean.class, DBean.getComplaints_id());
		if (managedBean != null) {
			session.remove(managedBean);
			session.flush();
			return true;
		}
		return false;
	}

//	public boolean deleteById(fromComplationBean DBean) {
////	session.delete(DBean);
//	session.remove(DBean);
//	session.flush();
//	return true;
//	}

	// Update
	public boolean updateById(formComplationBean UpBean) {
		System.out.println("DAO Update"+ UpBean);
		if (UpBean != null) {
			session.merge(UpBean);
			session.flush();
			return true;
		}
		return false;
		// session.delete(UpBean);
	}
}