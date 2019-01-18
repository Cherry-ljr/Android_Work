<%@ page language="java" import="java.sql.*,java.io.*,java.util.*" pageEncoding="UTF-8"%>
<%

  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String username = (String)request.getSession().getAttribute("username");

  if (username == null || username.equals("")) response.sendRedirect("index.jsp");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试系统</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body>
      <p align=center><%=username %>：<%=(String)request.getSession().getAttribute("message") %></p>

	  <table width="90%" border="1" align="center">
	  
<% 

  String sql="select * from jk15";
  String driverName=getServletContext().getInitParameter("driverName");  
  System.out.print(driverName);
  String url=getServletContext().getInitParameter("url");  
  Connection conn=DriverManager.getConnection(url);  
  PreparedStatement pstm = conn.prepareStatement(sql);
  ResultSet rs = pstm.executeQuery();
  ResultSetMetaData mdata = rs.getMetaData();
  
  out.print("<tr>");
  for (int i=0;i< mdata.getColumnCount();i++) out.print("<td>" + mdata.getColumnName(i+1) + "</td>");
  out.print("</tr>");

  while(rs.next()){

%>
		  <tr>
<% 
  for (int i=0;i< mdata.getColumnCount();i++) out.print("<td>" + rs.getString(i+1) + "</td>");
%>

		  </tr>
<% 
	rs.next();
   }
   rs.close();
   pstm.close();
   conn.close();
%>
		</table>
  </body>
</html>