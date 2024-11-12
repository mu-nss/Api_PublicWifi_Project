package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dto.WifiDTO;
import jdbc.SQLiteManager;


public class WifiDAO {
	
    public static  Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    
    public WifiDAO() {}
    
    // 공공 와이파이 정보 가져오기
    public static int insertWifi(JsonArray jsonArray) {
    	
        conn = null;
        pstmt = null;
        int count = 0;

        try {
        	conn = SQLiteManager.connDB();
        	conn.setAutoCommit(false);   

            String sql = " insert into public_wifi ( "
	                    + "   x_swifi_mgr_no, "
	                    + "	  x_swifi_wrdofc, "
	                    + "   x_swifi_main_nm, "
	                    + "   x_swifi_adres1, "
	                    + "   x_swifi_adres2, "
	                    + "   x_swifi_instl_floor, "
	                    + "   x_swifi_instl_ty, "
	                    + "   x_swifi_instl_mby, "
	                    + "   x_swifi_svc_se, "
	                    + "   x_swifi_cmcwr, "
	                    + "   x_swifi_cnstc_year, "
	                    + "   x_swifi_inout_door, "
	                    + "   x_swifi_remars3, "
	                    + "   lat, "
	                    + "   lnt, "
	                    + "   work_dttm ) "
	                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject data = (JsonObject) jsonArray.get(i).getAsJsonObject();
                
                pstmt.setString(1, data.get("x_swifi_mgr_no").getAsString());
                pstmt.setString(2, data.get("x_swifi_wrdofc").getAsString());
                pstmt.setString(3, data.get("x_swifi_main_nm").getAsString());
                pstmt.setString(4, data.get("x_swifi_adres1").getAsString());
                pstmt.setString(5, data.get("x_swifi_adres2").getAsString());
                pstmt.setString(6, data.get("x_swifi_instl_floor").getAsString());
                pstmt.setString(7, data.get("x_swifi_instl_ty").getAsString());
                pstmt.setString(8, data.get("x_swifi_instl_mby").getAsString());
                pstmt.setString(9, data.get("x_swifi_svc_se").getAsString());
                pstmt.setString(10, data.get("x_swifi_cmcwr").getAsString());
                pstmt.setString(11, data.get("x_swifi_cnstc_year").getAsString());
                pstmt.setString(12, data.get("x_swifi_inout_door").getAsString());
                pstmt.setString(13, data.get("x_swifi_remars3").getAsString());
                pstmt.setString(14, data.get("lat").getAsString());
                pstmt.setString(15, data.get("lnt").getAsString());
                pstmt.setString(16, data.get("work_dttm").getAsString());
                pstmt.addBatch();               
                pstmt.clearParameters(); 
                
                //1000개 기준으로 임시 batch 실행
                if ((i + 1) % 1000 == 0) {
                    int[] result = pstmt.executeBatch();
                    count += result.length; //배치한 완료 개수
                    conn.commit();
                }
            }

            int[] result = pstmt.executeBatch();
            count += result.length; //배치한 완료 개수
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();

            try {
            	conn.rollback();
            } catch (SQLException e2) {
            	e2.printStackTrace();
            }

        } finally {
            SQLiteManager.closeDB(conn, pstmt);
        }

        return count;
    }
    
    // 내 위치 정보를 입력하면 가까운 위치에 있는 와이파이 정보 20개를 보여주는 기능 
    public List<WifiDTO> getListWifi(String lat, String lnt) {

        conn = null;
        pstmt = null;
        rs = null;

        List<WifiDTO> nearestList = new ArrayList<WifiDTO>();

        try {
            conn= SQLiteManager.connDB();

            String sql = " select *, " +
	                    " 	round(6371 * acos(cos(radians(?)) * cos(radians(LAT)) * cos(radians(LNT) - " +
	                    " 	radians(?)) + sin(radians(?)) * sin(radians(LAT))), 4) as dist " +
	                    " from public_wifi " +
	                    " order by dist " +
	                    " limit 20;";

            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, Double.parseDouble(lat));
            pstmt.setDouble(2, Double.parseDouble(lnt));
            pstmt.setDouble(3, Double.parseDouble(lat));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                        .mgrNo(rs.getString("x_swifi_mgr_no"))
                        .wrdofc(rs.getString("x_swifi_wrdofc"))
                        .mainNm(rs.getString("x_swifi_main_nm"))
                        .adres1(rs.getString("x_swifi_adres1"))
                        .adres2(rs.getString("x_swifi_adres2"))
                        .instlFloor(rs.getString("x_swifi_instl_floor"))
                        .instlTy(rs.getString("x_swifi_instl_ty"))
                        .instlMby(rs.getString("x_swifi_instl_mby"))
                        .svcSe(rs.getString("x_swifi_svc_se"))
                        .cmcwr(rs.getString("x_swifi_cmcwr"))
                        .cnstcYear(rs.getString("x_swifi_cnstc_year"))
                        .inoutDoor(rs.getString("x_swifi_inout_door"))
                        .remaps3(rs.getString("x_swifi_remars3"))
                        .lat(rs.getString("lat"))
                        .lnt(rs.getString("lnt"))
                        .workDttm(String.valueOf(rs.getTimestamp("work_dttm").toLocalDateTime()))
                        .build();
                nearestList.add(wifiDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            SQLiteManager.closeAllDB(conn, pstmt, rs);
        }

        return nearestList;
    }

   public List<WifiDTO> selectList(String mgrNo, double distance) {

	   	conn = null;
        pstmt = null;
        rs = null;

        List<WifiDTO> list = new ArrayList<WifiDTO>();

        try {
        	conn = SQLiteManager.connDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mgrNo);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                       .dist(distance)
                       .mgrNo(rs.getString("x_swifi_mgr_no"))
                       .wrdofc(rs.getString("x_swifi_wrdofc"))
                       .mainNm(rs.getString("x_swifi_main_nm"))
                       .adres1(rs.getString("x_swifi_adres1"))
                       .adres2(rs.getString("x_swifi_adres2"))
                       .instlFloor(rs.getString("x_swifi_instl_floor"))
                       .instlTy(rs.getString("x_swifi_instl_ty"))
                       .instlMby(rs.getString("x_swifi_instl_mby"))
                       .svcSe(rs.getString("x_swifi_svc_se"))
                       .cmcwr(rs.getString("x_swifi_cmcwr"))
                       .cnstcYear(rs.getString("x_swifi_cnstc_year"))
                       .inoutDoor(rs.getString("x_swifi_inout_door"))
                       .remaps3(rs.getString("x_swifi_remars3"))
                       .lat(rs.getString("lat"))
                       .lnt(rs.getString("lnt"))
                       .workDttm(String.valueOf(rs.getTimestamp("work_dttm").toLocalDateTime()))
                       .build();
                list.add(wifiDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           SQLiteManager.closeAllDB(conn, pstmt, rs);
        }

       return list;
    }
   
   // 와이파이 상세 정보보기 - 추가기능
    public WifiDTO detailWifi(String mgrNo) {
        WifiDTO wifiDTO = new WifiDTO();

        conn = null;
        pstmt = null;
        rs = null;

        try {
            conn = SQLiteManager.connDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, mgrNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                wifiDTO.setMgrNo(rs.getString("X_SWIFI_MGR_NO"));
                wifiDTO.setWrdofc(rs.getString("X_SWIFI_WRDOFC"));
                wifiDTO.setMainNm(rs.getString("X_SWIFI_MAIN_NM"));
                wifiDTO.setAdres1(rs.getString("X_SWIFI_ADRES1"));
                wifiDTO.setAdres2(rs.getString("X_SWIFI_ADRES2"));
                wifiDTO.setInstlFloor(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifiDTO.setInstlTy(rs.getString("X_SWIFI_INSTL_TY"));
                wifiDTO.setInstlMby(rs.getString("X_SWIFI_INSTL_MBY"));
                wifiDTO.setSvcSe(rs.getString("X_SWIFI_SVC_SE"));
                wifiDTO.setCmcwr(rs.getString("X_SWIFI_CMCWR"));
                wifiDTO.setCnstcYear(rs.getString("X_SWIFI_CNSTC_YEAR"));
                wifiDTO.setInoutDoor(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifiDTO.setRemaps3(rs.getString("X_SWIFI_REMARS3"));
                wifiDTO.setLat(rs.getString("LAT"));
                wifiDTO.setLnt(rs.getString("LNT"));
                wifiDTO.setWorkDttm(String.valueOf(rs.getTimestamp("work_dttm").toLocalDateTime()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           SQLiteManager.closeAllDB(conn, pstmt, rs);
        }

        return wifiDTO;
    }
}