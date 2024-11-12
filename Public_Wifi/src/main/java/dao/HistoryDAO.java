package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dto.HistoryDTO;
import jdbc.SQLiteManager;


public class HistoryDAO {
	
    public static  Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    
    public HistoryDAO() {}
    
    // 입력 위치 정보 조회시 DB 저장
    public static void insertHistory(String lat, String lnt) {
    	
        conn = null;
        pstmt = null;

        try {
        	conn = SQLiteManager.connDB();
        	conn.setAutoCommit(false);   

            String sql = " insert into search_history(lat, lnt, search_time) "
	                   + " values (?, ?, ?); ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lat);
            pstmt.setString(2, lnt);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
         

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            SQLiteManager.closeDB(conn, pstmt);
        }
    }
    
    
    // 검색기록 리스트
    public List<HistoryDTO> getListHistory() {

        conn = null;
        pstmt = null;
        rs = null;

        List<HistoryDTO> searchWifi = new ArrayList<HistoryDTO>();

        try {
            conn= SQLiteManager.connDB();

            String sql = " select * from search_history order by id desc ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                HistoryDTO historyDTO = new HistoryDTO();
                historyDTO.setId(rs.getInt("id"));
                historyDTO.setLat(rs.getString("lat"));
                historyDTO.setLnt(rs.getString("lnt"));
                historyDTO.setSearchTime(rs.getTimestamp("search_history"));
                
                searchWifi.add(historyDTO);
                		
            }

        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            SQLiteManager.closeAllDB(conn, pstmt, rs);
        }
        return searchWifi;
    }

    
   // 검색기록 삭제
   public void deleteHistory(int id) {

	   	conn = null;
        pstmt = null;
        
        try {
        	conn = SQLiteManager.connDB();
            String sql = " delete from search_history where id = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           SQLiteManager.closeDB(conn, pstmt);
        }
    }
 
}