<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="java.sql.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"> 
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
<title>12#202С˵��Դ��</title>
</head>
<body  background="E:\JAVAworksprace\xiaoshuowang\WebContent\image\14.jpg">
<form>
<a href="../xiaoshuowang/glydl.jsp">����Ա��¼</a>
<a href="../xiaoshuowang/yonghujiemian.jsp">
<p align="center">
<img src="E:\nethomework\zhuyetubiao.jpg">
</p>
</a>
<table align="center" width="500">
<tr>
<td><input type="text" name="search" size="50"></td>
<td><select name="type">
<option value="aaa">����</option>
<option value="a">������</option>
</select></td>
<td><input type = "submit" value = "����" name = "loginbutton"></td>
</tr>
</table>
</form>
<table border ="3" align="center" width="1000">
<%
request.setCharacterEncoding("gbk");
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
Connection conn = DriverManager.getConnection("jdbc:odbc:user2");
Statement smt = conn.createStatement();
String Search = request.getParameter("search");
String Type = request.getParameter("type");
String str="aa";
out.print("��ʹ�á�Ŀ�����Ϊ����������");
String isauthor = "select * from books where author like'%" + Search + "'";
String isbook = "select * from books where book like'%" + Search + "'";
out.println("<tr height='60'>" + "<th>���</th><th>����</th><th>����</th><th>����</th></tr>");	
if (Search !=null && Type.compareTo(str) > 0){
	ResultSet rs1 = smt.executeQuery(isbook);
	while(rs1.next()){
		%>
		<tr align="center">
		<td><%= rs1.getString(1) %></td>
		<td><%= rs1.getString(3) %></td>
		<td><%= rs1.getString(4) %></td>
		<td><a href=<%= rs1.getString(5) %>>����</a></td>
		</tr>
		<%
	}
}
else{
	ResultSet rs2 = smt.executeQuery(isauthor);
	while(rs2.next()){
		%>
		<tr align="center">
		<td><%= rs2.getString(1) %></td>
		<td><%= rs2.getString(3) %></td>
		<td><%= rs2.getString(4) %></td>
		<td><a href=<%= rs2.getString(5) %>>����</a></td>
		</tr>
		<%
	}
}
%>
</table>
</body>
</html>