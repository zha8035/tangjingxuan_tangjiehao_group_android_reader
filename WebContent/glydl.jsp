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
		out.println("<script>alert('�˺š������������');window.location = '../xiaoshuowang/glydl.jsp';</script>");
}
%>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
    <%@page import = "java.sql.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>����Ա��½</title>
</head>
<body background="E:\JAVAworksprace\xiaoshuowang\WebContent\image\14.jpg">
<form>
<b><font size = "5">����Ա��¼<hr><br></font></b>
�˺ţ�<input type = "text" name = "user" size = "20"><br>
���룺<input type = "password" name = "pass" size = "20"><br>
<input type = "submit" value = "��¼" name = "loginbutton">
<a href="../xiaoshuowang/yonghujiemian.jsp">�����û�ҳ��</a>
</form>
</body>
</html>