package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static String url="jdbc:sqlserver://localhost:1433;trustServerCertificate=true;databaseName=";
	private static String defaultDatabase="RentDB";
	private static String user="sa";
	private static String pwd="!QAZ2wsx";
	
	static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url+defaultDatabase, user, pwd);
	}
}
