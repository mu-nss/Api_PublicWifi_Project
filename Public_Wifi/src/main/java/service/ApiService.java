package service;

import java.io.IOException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static dao.WifiDAO.insertWifi;

/**
 * OpenAPI JSON 파싱
 **/
public class ApiService {
    private static final String API_URL = "http://openapi.seoul.go.kr:8088/6271436d5a6d736a31313445644f4572/json/TbPublicWifiInfo/";
    private static final OkHttpClient okHttpClient = new OkHttpClient();
  
    //Wi-fi 갯수 구하기
    public static int totalCnt() throws IOException{         
        int cnt = 0;

        URL url = new URL(API_URL + "1/1");

        //URL 요청
        Request.Builder builder = new Request.Builder().url(url).get();

        //URL 응답
        Response response = okHttpClient.newCall(builder.build()).execute();

        try {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    JsonElement jsonElement = JsonParser.parseString(responseBody.string());

                    cnt = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                                     .getAsJsonObject().get("list_total_count")
                                     .getAsInt();

                    System.out.println("FIND WIFI CNT - " + cnt);
                }

            } else {
                System.out.println("FAIL API CONNECT - " + response.code());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public static int getPublicWifiJson() throws IOException {
        int totalCnt = totalCnt();
        int start = 1;
        int end = 1;
        int count = 0;

        try {
            for (int i = 0; i <= totalCnt / 1000; i++) {
                start = 1 + (1000 * i);
                end = (i + 1) * 1000;

                URL url = new URL(API_URL + start + "/" + end);

                Request.Builder builder = new Request.Builder().url(url).get();
                Response response = okHttpClient.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();

                    if (responseBody != null) {
                        JsonElement jsonElement = JsonParser.parseString(responseBody.string());

                        JsonArray jsonArray = jsonElement.getAsJsonObject().get("TbPublicWifiInfo")
                                                         .getAsJsonObject().get("row")
                                                         .getAsJsonArray();

                        count += insertWifi(jsonArray); // 로드된 데이터 총개수

                    } else {
                        System.out.println("FAIL API CONNECT - " + response.code());
                    }
                } else {
                    System.out.println("FAIL API CONNECT - " + response.code());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
    
}