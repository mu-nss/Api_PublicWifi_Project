package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *	SQLite JDBC 연결
 **/
public class SQLiteManager {
	
    // DB 연결
    public static Connection connDB() {
    	
    	String dbPath = "D:/PublicWifi_Project/ApiPublicWifi/public_wifi_sql.db";
    	String url = "jdbc:sqlite:" + dbPath;
    	
    	Connection conn = null;
    	
        try {
        		
        	Class.forName("org.sqlite.JDBC"); 
        	conn = DriverManager.getConnection(url);
        

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
 
        return conn;
    }
 
    // DB 연결종료
    public static void closeAllDB(Connection conn, PreparedStatement pstmt, ResultSet rs) {
    	 try {
             if (rs != null && ! rs.isClosed()) {
            	 rs.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         try {
             if (pstmt != null && ! pstmt.isClosed()) {
            	 pstmt.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         try {
             if (conn != null && ! conn.isClosed()) {
            	 conn.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
    }
    
    public static void closeDB(Connection conn, PreparedStatement pstmt) {
    	try {
            if (pstmt != null && ! pstmt.isClosed()) {
           	 pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null && ! conn.isClosed()) {
           	 conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
