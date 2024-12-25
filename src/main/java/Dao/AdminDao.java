package Dao;

import Bean.AdminBean;

public interface AdminDao {
	AdminBean confirmAccount(String email, String password);
	
	//check email then if match password(hash) 
	AdminBean checkAccount(String email );
	
	
	boolean updateAdmin(AdminBean admin); 
	
	AdminBean registerAccount
	(String name ,String email, String password ,String phone ,String role);
	
}
