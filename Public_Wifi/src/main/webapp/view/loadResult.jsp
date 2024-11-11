<%@page import="service.ApiService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
</head>
<body>
	<%
		ApiService apiService = new ApiService();
		int cnt = apiService.getPublicWifiJson();
	%>

	<div style="text-align: center;">
		<h2><%=cnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h2>
		<a href = "index.jsp">홈으로 가기</a>
	</div>
</body>
</html>