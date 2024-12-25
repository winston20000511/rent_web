package IMPL;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import Bean.AdminBean;
import Dao.AdminDao;
import util.HibernateUtil;

public class AdminDaoImpl implements AdminDao {

	@Override
	public AdminBean registerAccount(String adminName, String adminEmail, String adminPassword, String phoadminPhonene,
			String role) {
		Transaction transaction = null;
		AdminBean admin = null;

//	    System.out.println("adminName"+adminName);
//	    System.out.println("adminEmail"+adminEmail);
//	    System.out.println("adminPassword"+adminPassword);
//	    System.out.println("phoadminPhonene"+phoadminPhonene);
//	    System.out.println("role"+role);

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {

			transaction = session.beginTransaction();

			String hql = "FROM AdminBean WHERE adminEmail = :email";
			Query<AdminBean> query = session.createQuery(hql, AdminBean.class);
			query.setParameter("email", adminEmail);

			AdminBean existingAdmin = query.uniqueResult();
			if (existingAdmin != null) {
				System.out.println("Email 已存在，無法註冊新的帳戶");
				return null;
			}

			admin = new AdminBean();
			admin.setAdminName(adminName);
			admin.setAdminEmail(adminEmail);
			String hashedPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
			admin.setAdminPassword(hashedPassword);
//	        admin.setAdminPassword(adminPassword); 
			admin.setAdminPhone(phoadminPhonene);
			admin.setRole(role);

			System.out.println(admin.getAdminName());
			System.out.println(admin.getAdminEmail());
			System.out.println(admin.getAdminPassword());
			System.out.println(admin.getAdminPhone());
			System.out.println(admin.getRole());

			session.persist(admin);

			transaction.commit();

			System.out.println("註冊成功：" + admin);
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

		return admin;
	}

	@Override
	public AdminBean checkAccount(String email) {
	    AdminBean admin = null;
	    Transaction transaction = null;

	    String hql = "FROM AdminBean WHERE adminEmail = :email";

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();

	        Query<AdminBean> query = session.createQuery(hql, AdminBean.class);
	        query.setParameter("email", email);
	        admin = query.uniqueResult();

	        transaction.commit(); 
	    } catch (Exception e) {
	        if (transaction != null) {
	            try {
	                transaction.rollback(); 
	            } catch (Exception rollbackException) {
	                rollbackException.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	    }
	    return admin;
	}

	@Override
	public AdminBean confirmAccount(String email, String password) {

		AdminBean admin = null;
		Transaction transaction = null;

//		String sql ="SELECT * FROM admin_table WHERE adminEmail = ? AND adminPassword = ?";
		String hql = "FROM AdminBean WHERE adminEmail = :email AND adminPassword = :password";

		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {

			transaction = session.beginTransaction();
			Query<AdminBean> query = session.createQuery(hql, AdminBean.class);

			query.setParameter("email", email);
			query.setParameter("password", password);

			admin = query.uniqueResult();

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public boolean updateAdmin(AdminBean admin) {
		boolean updateResult = false;
		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			transaction = session.beginTransaction();

			session.merge(admin);

			transaction.commit();
			updateResult = true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return updateResult;
	}
}
