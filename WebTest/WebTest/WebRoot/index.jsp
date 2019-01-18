<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message = (String) session.getAttribute("message");
if (message == null) message="";
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

	  <table width="80%" border="0" align="center">
		  <tr>
		    <td align="center">	
          	<form action="DemoServlet.do">
          	<table width="500" height="80%" border="2" bordercolor="#12A0F5"  bgcolor="#dfeaf1">
               <tr>
                 <td align="center"><br />登录 <br/><br/>
       				用户名：<input type="text" name="username" size="19" maxlength="19" /><br/><br/>
       				密　码：<input type="password" name="password" size="19" maxlength="19" /><br /><br />
                	<input type="submit" name="Submit" value="登录"/>    
                	<input type="reset" name="Submit" value="重置" /><br/> <br/>
                	<%=message %><br/>                          		
                </td>
              </tr>
          </table>
          </form>
          </td>
		</table>
	  </div>
  </body>
</html>