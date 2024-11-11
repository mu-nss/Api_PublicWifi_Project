package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dto.WifiDTO;
import jdbc.SQLiteManager;


public class WifiDAO {
	
    public static Connection conn;
    public static PreparedStatement pstmt;
    public static ResultSet rs;
    

    public static int insertPublicWifi(JsonArray jsonArray) {
    	
        conn = null;
        pstmt = null;
        int count = 0;

        try {
        	conn = SQLiteManager.connDB();
        	conn.setAutoCommit(false);   

            /* Insert 진행 */
            String sql = " insert into public_wifi "
                    + " ( x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, "
                    + " x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, "
                    + " x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm) "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject data = (JsonObject) jsonArray.get(i).getAsJsonObject();

                pstmt.setString(1, data.get("X_SWIFI_MGR_NO").getAsString());
                pstmt.setString(2, data.get("X_SWIFI_WRDOFC").getAsString());
                pstmt.setString(3, data.get("X_SWIFI_MAIN_NM").getAsString());
                pstmt.setString(4, data.get("X_SWIFI_ADRES1").getAsString());
                pstmt.setString(5, data.get("X_SWIFI_ADRES2").getAsString());
                pstmt.setString(6, data.get("X_SWIFI_INSTL_FLOOR").getAsString());
                pstmt.setString(7, data.get("X_SWIFI_INSTL_TY").getAsString());
                pstmt.setString(8, data.get("X_SWIFI_INSTL_MBY").getAsString());
                pstmt.setString(9, data.get("X_SWIFI_SVC_SE").getAsString());
                pstmt.setString(10, data.get("X_SWIFI_CMCWR").getAsString());
                pstmt.setString(11, data.get("X_SWIFI_CNSTC_YEAR").getAsString());
                pstmt.setString(12, data.get("X_SWIFI_INOUT_DOOR").getAsString());
                pstmt.setString(13, data.get("X_SWIFI_REMARS3").getAsString());
                pstmt.setString(14, data.get("LAT").getAsString());
                pstmt.setString(15, data.get("LNT").getAsString());
                pstmt.setString(16, data.get("WORK_DTTM").getAsString());

                pstmt.addBatch();               

                //1000개 기준으로 임시 batch 실행
                if ((i + 1) % 1000 == 0) {
                    int[] result = pstmt.executeBatch();
                    count += result.length;    //배치한 완료 개수
                    conn.commit();
                }
            }

            int[] result = pstmt.executeBatch();
            count += result.length;    //배치한 완료 개수
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();

            try {
            	conn.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        } finally {
            SQLiteManager.closeDB(conn, pstmt);
        }

        return count;
    }

    public List<WifiDTO> getNearestWifiList(String lat, String lnt) {

        conn = null;
        pstmt = null;
        rs = null;

        List<WifiDTO> list = new ArrayList<>();

        try {
            conn= SQLiteManager.connDB();

            //위도 경도 구하는 식
            String sql = " SELECT *, " +
                    " round(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT) " +
                    " -radians(?))+sin(radians(?))*sin(radians(LAT))), 4) " +
                    " AS distance " +
                    " FROM public_wifi " +
                    " ORDER BY distance " +
                    " LIMIT 20;";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, Double.parseDouble(lat));
            preparedStatement.setDouble(2, Double.parseDouble(lnt));
            preparedStatement.setDouble(3, Double.parseDouble(lat));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                        .dist(resultSet.getDouble("distance"))
                        .mgrNo(resultSet.getString("x_swifi_mgr_no"))
                        .wrdofc(resultSet.getString("x_swifi_wrdofc"))
                        .mainNm(resultSet.getString("x_swifi_main_nm"))
                        .adres1(resultSet.getString("x_swifi_adres1"))
                        .adres2(resultSet.getString("x_swifi_adres2"))
                        .instlFloor(resultSet.getString("x_swifi_instl_floor"))
                        .instlTy(resultSet.getString("x_swifi_instl_ty"))
                        .instlMby(resultSet.getString("x_swifi_instl_mby"))
                        .svcSe(resultSet.getString("x_swifi_svc_se"))
                        .cmcwr(resultSet.getString("x_swifi_cmcwr"))
                        .cnstcYear(resultSet.getString("x_swifi_cnstc_year"))
                        .inoutDoor(resultSet.getString("x_swifi_inout_door"))
                        .remaps3(resultSet.getString("x_swifi_remars3"))
                        .lat(resultSet.getString("lat"))
                        .lnt(resultSet.getString("lnt"))
                        .workDttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()))
                        .build();
                list.add(wifiDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteManager.closeConnDB(connection, preparedStatement, resultSet);
        }

        return list;
    }

   public List<WifiDTO> selectWifiList(String mgrNo, double distance) {

        connection = null;
        preparedStatement = null;
        resultSet = null;

        List<WifiDTO> list = new ArrayList<>();

        try {
            connection = SQLiteManager.connectionDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, mgrNo);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WifiDTO wifiDTO = WifiDTO.builder()
                       .dist(distance)
                       .mgrNo(resultSet.getString("x_swifi_mgr_no"))
                       .wrdofc(resultSet.getString("x_swifi_wrdofc"))
                       .mainNm(resultSet.getString("x_swifi_main_nm"))
                       .adres1(resultSet.getString("x_swifi_adres1"))
                       .adres2(resultSet.getString("x_swifi_adres2"))
                       .instlFloor(resultSet.getString("x_swifi_instl_floor"))
                       .instlTy(resultSet.getString("x_swifi_instl_ty"))
                       .instlMby(resultSet.getString("x_swifi_instl_mby"))
                       .svcSe(resultSet.getString("x_swifi_svc_se"))
                       .cmcwr(resultSet.getString("x_swifi_cmcwr"))
                       .cnstcYear(resultSet.getString("x_swifi_cnstc_year"))
                       .inoutDoor(resultSet.getString("x_swifi_inout_door"))
                       .remaps3(resultSet.getString("x_swifi_remars3"))
                       .lat(resultSet.getString("lat"))
                       .lnt(resultSet.getString("lnt"))
                       .workDttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()))
                       .build();
                list.add(wifiDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           SQLiteManager.closeConnDB(connection, preparedStatement, resultSet);
        }

       return list;
    }

    public WifiDTO selectWifi(String mgrNo) {
        WifiDTO wifiDTO = new WifiDTO();

        connection = null;
        preparedStatement = null;
        resultSet = null;

        try {
            connection = SQLiteManager.connectionDB();
            String sql = " select * from public_wifi where x_swifi_mgr_no = ? ";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, mgrNo);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                wifiDTO.setMgrNo(resultSet.getString("X_SWIFI_MGR_NO"));
                wifiDTO.setWrdofc(resultSet.getString("X_SWIFI_WRDOFC"));
                wifiDTO.setMainNm(resultSet.getString("X_SWIFI_MAIN_NM"));
                wifiDTO.setAdres1(resultSet.getString("X_SWIFI_ADRES1"));
                wifiDTO.setAdres2(resultSet.getString("X_SWIFI_ADRES2"));
                wifiDTO.setInstlFloor(resultSet.getString("X_SWIFI_INSTL_FLOOR"));
                wifiDTO.setInstlTy(resultSet.getString("X_SWIFI_INSTL_TY"));
                wifiDTO.setInstlMby(resultSet.getString("X_SWIFI_INSTL_MBY"));
                wifiDTO.setSvcSe(resultSet.getString("X_SWIFI_SVC_SE"));
                wifiDTO.setCmcwr(resultSet.getString("X_SWIFI_CMCWR"));
                wifiDTO.setCnstcYear(resultSet.getString("X_SWIFI_CNSTC_YEAR"));
                wifiDTO.setInoutDoor(resultSet.getString("X_SWIFI_INOUT_DOOR"));
                wifiDTO.setRemaps3(resultSet.getString("X_SWIFI_REMARS3"));
                wifiDTO.setLat(resultSet.getString("LAT"));
                wifiDTO.setLnt(resultSet.getString("LNT"));
                wifiDTO.setWorkDttm(String.valueOf(resultSet.getTimestamp("work_dttm").toLocalDateTime()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           SQLiteManager.closeConnDB(connection, preparedStatement, resultSet);
        }

        return wifiDTO;
    }
}