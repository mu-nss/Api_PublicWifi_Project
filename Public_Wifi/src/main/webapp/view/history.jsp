<%@page import="dto.HistoryDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.HistoryDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="../css/style.css">
	<title>와이파이 정보구하기</title>
</head>
<body>

	<h1>위치 히스토리 목록</h1>
	<%@ include file="header.jsp"%>
	
	<%
		HistoryDAO historyDAO = new HistoryDAO();
		List<HistoryDTO> list = historyDAO.getListHistory();
		
		String id = request.getParameter("id");
	%>
	
	<div>
	    <table>
	        <thead>
	            <tr>
	                <th>ID</th>
	                <th>x좌표</th>
	                <th>y좌표</th>
	                <th>조회일자</th>
	                <th>비고</th>
	            </tr>
	        </thead>
			<tbody>
				<%
					if(id != null && !id.isEmpty()) {
						for(HistoryDTO history : list) {
				%>
				<tr>
					<td><%=history.getId()%></td>
					<td><%=history.getLat()%></td>
					<td><%=history.getLnt()%></td>
					<td><%=history.getSearchTime()%></td>
					<td><button onclick="deleteHistory(<%=history.getId()%>)"></button></td>
				</tr>
				<%
						}
					} else {
				%>
				<tr>
					<td colspan="5">저장된 정보가 없습니다.</td>
				</tr>
				<% } %>
			</tbody>
	    </table>
	</div>

</body>
</html>