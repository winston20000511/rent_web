package rent189.dao;

import rent189.model.AdminBean;

public interface AdminDao {
	AdminBean confirmAccount(String email, String password);
	boolean updateAdmin(AdminBean admin); 
}
