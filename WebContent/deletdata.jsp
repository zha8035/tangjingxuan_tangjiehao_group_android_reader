<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
    <%@page import="java.sql.*" %>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>delete</title>
</head>
<body>
<%
request.setCharacterEncoding("gb2312");
String id = (String)request.getParameter("st");
Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
Connection conn = DriverManager.getConnection("jdbc:odbc:user2");
Statement smt = conn.createStatement();
smt.executeUpdate("delete * from books where book='"+id+"'");
out.println("<script>alert('删除成功');window.location = '../xiaoshuowang/gly.jsp';</script>");
smt.close();
conn.close();
%>
</body>
</html>