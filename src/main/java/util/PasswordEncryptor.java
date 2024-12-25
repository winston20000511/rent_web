package util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import Bean.AdminBean;

public class PasswordEncryptor {
    public static void main(String[] args) {
        Transaction transaction = null;

  
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<AdminBean> admins = session.createQuery("FROM AdminBean", AdminBean.class).getResultList();
            for (AdminBean admin : admins) {
                String plainPassword = admin.getAdminPassword();
                if (!plainPassword.startsWith("$2a$")) { 
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    admin.setAdminPassword(hashedPassword);
                    session.update(admin);
                }
            }
            transaction.commit();
            System.out.println("所有密碼已成功加密並更新。");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
