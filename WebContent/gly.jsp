<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
    <%@page import = "java.sql.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>12#202С˵��Դ��</title>
</head>
<body background="E:\JAVAworksprace\xiaoshuowang\WebContent\image\14.jpg">
<%
if(session.getAttribute("access") !="y"){
	out.println("<script>alert('û�з���Ȩ��');window.location = '../xiaoshuowang/glydl.jsp';</script>");
}
else{
	out.println("<b>��ã�" + session.getAttribute("accessid") + "<b/>");
	out.println("<a href = 'glydl.jsp'>ע��</a>");
}
out.println("<a href='../xiaoshuowang/shangchuan.jsp'>�ϴ�����˴�</a>");
%>
<form>
<table align="center">
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
<table border ="1" align="center">
<%
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
Connection conn1 = DriverManager.getConnection("jdbc:odbc:user2");
Statement smt1 = conn1.createStatement();
String Search = request.getParameter("search");
String Type = request.getParameter("type");
String str="aa";
String isauthor = "select * from books where author like'%" + Search + "'";
String isbook = "select * from books where book like'%" + Search + "'";
out.println("<tr>" + "<th>���</th><th>����</th><th>����</th><th>����</th><th>ɾ��</th></tr>");	
if (Search !=null && Type.compareTo(str) > 0){
	ResultSet rs1 = smt1.executeQuery(isbook);
	while(rs1.next()){
		String id = rs1.getString(3);
		%>
		<tr>
		<td><%= rs1.getString(1) %></td>
		<td><%= id %></td>
		<td><%= rs1.getString(4) %></td>
		<td><a href=<%= rs1.getString(5) %>>����</a></td>
		<td><a href="../xiaoshuowang/deletdata.jsp?st=<%= id %>">ɾ��</a></td>
		</tr>
		<%
	}
}
else{
	ResultSet rs2 = smt1.executeQuery(isauthor);
	while(rs2.next()){
		String id = rs2.getString(3);
		%>
		<tr>
		<td><%= rs2.getString(1) %></td>
		<td><%= id %></td>
		<td><%= rs2.getString(4) %></td>
		<td><a href=<%= rs2.getString(5) %>>����</a></td>
		<td><a href="../xiaoshuowang/deletdata.jsp?st=<%= id %>">ɾ��</a></td>
		</tr>
		<%
	}
}
%>
</table>
</body>
</html>