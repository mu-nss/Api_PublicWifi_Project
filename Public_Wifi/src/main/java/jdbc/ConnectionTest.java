package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionTest {
	public static void main(String[] args) {
		
    	Connection conn = null;
    	Statement st = null;
    	ResultSet rs = null;

    	String dbPath = "D:/PublicWifi_Project/ApiPublicWifi/public_wifi_sql.db";
    	String url = "jdbc:sqlite:" + dbPath;
    	    	
        try {
        	Class.forName("org.sqlite.JDBC"); 
        	conn = DriverManager.getConnection(url);
        	
        	System.out.println("SQLite DB Connected");
        
        	st = conn.createStatement();
        	rs = st.executeQuery(" select * from testDB; ");
        	st.executeUpdate(" insert into testDB(no, id) values(1, 'test'); ");
        	
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
	}
}
