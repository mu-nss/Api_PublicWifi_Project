<%@ page import="dto.WifiDTO"%>
<%@ page import="dao.WifiDAO"%>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<html>
<head>
    <title>와이파이 정보 구하기</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="../css/style.css">
</head>
<body>
	<h1>와이파이 정보 구하기</h1>
	<%@ include file="header.jsp" %>
	
    <%
        String lat = "";
    	String lnt = "";
    
    	if(request.getParameter("lat") == null) {
    		lat = "0.0";
    	} else {
    		request.getParameter("lat");
    	}
        
    	if(request.getParameter("lnt") == null) {
    		lnt = "0.0";
    	} else {
    		request.getParameter("lnt");
    	}
    %>

    <div class="input">
        <span>LAT:</span>
        <input type="text" id="lat" value="<%=lat%>">

        <span>LNT:</span>
        <input type="text" id="lnt" value="<%=lnt%>">

        <button id="btn_getPosition"><span>내 위치 가져오기</span></button>
        <button id="btn_listNearest"><span>근처 WIFI 정보 보기</span></button>
    </div>

    <div>
    	<table>
            <thead>
	            <tr>
	                <th>거리(Km)</th>
	                <th>관리번호</th>
	                <th>자치구</th>
	                <th>와이파이명</th>
	                <th>도로명주소</th>
	                <th>상세주소</th>
	                <th>설치위치(층)</th>
	                <th>설치기관</th>
	                <th>설치유형</th>
	                <th>서비스구분</th>
	                <th>망종류</th>
	                <th>설치년도</th>
	                <th>실내외구분</th>
	                <th>WIFI접속환경</th>
	                <th>X좌표</th>
	                <th>Y좌표</th>
	                <th>작업일자</th>
	            </tr>
			</thead>
            <tbody>
            	<%
                    if (!("0.0").equals(lat) && !("0.0").equals(lnt)) {
                        WifiDAO wifiDAO = new WifiDAO();
                        List<WifiDTO> list = wifiDAO.getListWifi(lat, lnt);

                        if (list != null) {
                            for (WifiDTO wifiDTO : list) {
                %>
            	<tr>
					<td>${wifi.dist}</td>
					<td>${wifi.mgrNo}</td>
					<td>${wifi.wrdofc}</td>
					<td>
						<a href="wifi?action=getWifiDetails&id=${wifi.id}">${wifi.mainNm}</a>
					</td>
					<td>${wifi.adres1}</td>
					<td>${wifi.adres2}</td>
					<td>${wifi.instlFloor}</td>
					<td>${wifi.instlTy}</td>
					<td>${wifi.instlMby}</td>
					<td>${wifi.svcSe}</td>
					<td>${wifi.cmcwr}</td>
					<td>${wifi.cnstcYear}</td>
					<td>${wifi.inoutDoor}</td>
					<td>${wifi.remars3}</td>
					<td>${wifi.lat}</td>
					<td>${wifi.lnt}</td>
					<td>${wifi.workDttm}</td>
				</tr>
				    <% } %>
                <% } %>
                <% } else { %>
                    <td colspan='17'> 위치 정보를 입력하신 후에 조회해 주세요. </td>
                <% } %>
			</tbody>
        </table>
    </div>

    <script>
        let getPosition = document.getElementById("btn_getPosition");
        let getNearestWifi = document.getElementById("btn_listNearest");

        let lat = null;
        let lnt = null;

        window.onload = () => {
            lat = document.getElementById("lat").value;
            lnt = document.getElementById("lnt").value;
        }

        getPosition.addEventListener("click", function () {
            if ('geolocation' in navigator) {
                navigator.geolocation.getPosition(function (position){
                    let latitude = position.coords.latitude;
                    let longitude = position.coords.longitude;
                    
                    document.getElementById("lat").value = latitude;
                    document.getElementById("lnt").value = longitude;
                })
            } 
        });

        getNearestWifi.addEventListener("click", function (){
            let latitude = document.getElementById("lat").value;
            let longitude = document.getElementById("lnt").value;

            if (latitude !== "" || longitude !== "") {
                window.location.assign("index.jsp?lat=" + latitude + "&lnt=" + longitude);
            }
        })
    </script>

</body>
</html>