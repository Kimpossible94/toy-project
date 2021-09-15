<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
<h1 style='text-align: center;'>PCLASS TOY PROJECT</h1>

<c:if test="${empty sessionScope.authentication }">
	<h2><a href="member/login-form">login</a></h2>
	<h2><a href="member/join-form">sign up</a></h2>
</c:if>

<c:if test="${not empty sessionScope.authentication }">
	<h2><a href="member/logout">logout</a></h2>
	<h2><a href="member/mypage">my page</a></h2>
</c:if>

</body>
</html>