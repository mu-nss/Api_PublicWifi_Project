<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="../css/style.css">
	<title>와이파이 정보 구하기</title>
</head>
<body>

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
				<th>설치유형</th>
				<th>설치기관</th>
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
			<c:forEach var="wifi" items="${list}">
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
			</c:forEach>
		</tbody>
	</table>


</body>
</html>