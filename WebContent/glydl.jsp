<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
String user = request.getParameter("user");
String pass = request.getParameter("pass");
if(user !=null && pass !=null){
	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	Connection conn = DriverManager.getConnection("jdbc:odbc:user2");
	Statement smt = conn.createStatement();
	String gettabledata = "select * from gly where user='" + user + "' and pass='" + pass + "'";
	ResultSet result = smt.executeQuery(gettabledata);
	if(result.next()){
		session.setAttribute("access", "y");
		session.setAttribute("accessid",request.getParameter("user"));
		session.setMaxInactiveInterval(300);
		response.sendRedirect("gly.jsp");
	}
	else
		out.println("<script>alert('账号、密码输入错误！');window.location = '../xiaoshuowang/glydl.jsp';</script>");
}
%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
    <%@page import = "java.sql.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>管理员登陆</title>
</head>
<body background="E:\JAVAworksprace\xiaoshuowang\WebContent\image\14.jpg">
<form>
<b><font size = "5">管理员登录<hr><br></font></b>
账号：<input type = "text" name = "user" size = "20"><br>
密码：<input type = "password" name = "pass" size = "20"><br>
<input type = "submit" value = "登录" name = "loginbutton">
<a href="../xiaoshuowang/yonghujiemian.jsp">返回用户页面</a>
</form>
</body>
</html>