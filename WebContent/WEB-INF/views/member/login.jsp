<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">

	.tit{
	   display:block; 
	   width:150px;
	}
	.valid-msg{
		color:red;   
		font-size:0.5vw;
	}

</style>
</head>
<body>
<h1>로그인</h1>
   <c:if test="${not empty param.err}">
   	<span class='valid-msg'>아이디나 비밀번호를 잘못 입력하였습니다.</span>
   </c:if>
   <hr>
   
   <form action="/member/login" method="post">
   
      <span class='tit'>ID : </span>
      <input type="text" name="userId" id="userId">
      <span class='tit'>Password : </span>
      <input type="password" name="password" id="password">
      <button>로그인</button>
   </form>
   
   
   
</body>
</html>