<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
    <%@page import="com.jspsmart.upload.*" %>
    <jsp:useBean id="theSmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload"/>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title></title>
</head>
<body>
<%
theSmartUpload.initialize(pageContext);
theSmartUpload.setTotalMaxFileSize(5*1024*1024);
theSmartUpload.upload();
String savePath="E:\\nethomework\\";
int fileCount=theSmartUpload.save(savePath);
out.println("123:"+fileCount);
%>
</body>
</html>