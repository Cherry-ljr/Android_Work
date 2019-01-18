package cn.edu.gzu.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class DemoServletJson extends HttpServlet {

	private Connection conn = null; // 数据库连接对象
	private String driverName = null; // 数据库驱动器
	private String url = null; // 数据库地址URL
	private String Message = "";

	public DemoServletJson() {
		super();
	}

	public void destroy() {
        super.destroy();  
        try {  
        	conn.close();  
        }catch(Exception e) {  
            System.out.println("关闭数据库错误："+e.getMessage());  
        }  
	}

	public void init() throws ServletException {
        //全局contont里面的设置
        driverName=getServletContext().getInitParameter("driverName");  
        url=getServletContext().getInitParameter("url"); 
          
        try {  
            Class.forName(driverName);  
            conn=DriverManager.getConnection(url);  
        } catch(Exception e) {  
            System.out.println("数据库连接错误："+e.getMessage());  
        }  
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 取得用户登录页面提交的数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String action = request.getParameter("action");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject jsonObj = new JSONObject(); 
		
		// 判断登录账号为空，则自动跳转到登录
		if (username == null || username.trim().length() == 0) {
			jsonObj.put("ret", 1);
			jsonObj.put("message", "请输入用户名");
			out.print(jsonObj.toString());
			return;
		}
		// 判断登录密码为空
		if (password == null || password.trim().length() == 0) {
			jsonObj.put("ret", 1);
			jsonObj.put("message", "请输入密码");
			out.print(jsonObj.toString());
			return;
		}

		try {

			// 预处理模式
			String Sql = "select * from user where username = ?";
			PreparedStatement pst = conn.prepareStatement(Sql);
			// 传入参数
			pst.setString(1, username);
			// 执行查询
			ResultSet rs = pst.executeQuery();
			
			// 用户验证
			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					
				} else {
					jsonObj.put("ret", 1);
					jsonObj.put("message", "密码错误");
					out.print(jsonObj.toString());
					return;
				}
			} else {
				jsonObj.put("ret", 1);
				jsonObj.put("message", "用户错误");
				out.print(jsonObj.toString());
				return;
			}
			rs.close();
			
			if (action.equals("login")) {
				jsonObj.put("ret", 0);
				jsonObj.put("message", "登录成功");
				out.print(jsonObj.toString());
				return;
			}
			if (action.equals("queryone")) {
				Sql = "select * from [jk15] order by [学号]  desc";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("querytwo")) {
				Sql = "select * from [jk15] order by [高数]  desc";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			
			if (action.equals("delete")) {//删除
				String stu=new String(request.getParameter("stu"));
				Sql="delete from jk15 where 学号='"+stu+" '";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("add")) {//添加
				String stu=new String(request.getParameter("stu"));
				String name=new String(request.getParameter("name"));
				String gender=new String(request.getParameter("gender"));
				String math=new String(request.getParameter("math"));
				String en=new String(request.getParameter("en"));
				String py=new String(request.getParameter("py"));
				
				Sql = "INSERT INTO [jk15] ([学号],[姓名] ,[性别],[高数],[英语],[物理]) VALUES ('" + stu
				+ "', '" + name + "', '" + gender + "', '" + math+ "', '" + en+ "', '" + py
				+ "')";
								
				pst = conn.prepareStatement(Sql);
				pst.executeUpdate();
				
				return;
			}
			if (action.equals("update")) {//修改
				String stu=new String(request.getParameter("stu"));
				String name=new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
				String gender=new String(request.getParameter("gender").getBytes("ISO-8859-1"), "UTF-8");			
				String sub=new String(request.getParameter("Sub").getBytes("ISO-8859-1"), "UTF-8");
				String scores=new String(request.getParameter("scores"));
			
				if(sub.equals("高数"))				
				{
					Sql = "update [jk15] set [姓名]='"+name+"',[性别]='"+gender+"',[高数]='"+scores+"' where [学号]='"+stu+"'";
					}
				if(sub.equals("英语"))				
				{
					Sql = "update [jk15] set [姓名]='"+name+"',[性别]='"+gender+"',[英语]='"+scores+"' where [学号]='"+stu+"'";
					}
				if(sub.equals("物理"))				
				{
				
					Sql = "update [jk15] set [姓名]='"+name+"',[性别]='"+gender+"',[物理]='"+scores+"' where [学号]='"+stu+"'";
				}
								
				pst = conn.prepareStatement(Sql);
				pst.executeUpdate();
				return;
		       
			}
			
			if (action.equals("searchone")) {//查询1
				
				String math=new String(request.getParameter("math"));
				String gen=new String(request.getParameter("gender").getBytes("ISO-8859-1"), "UTF-8");
				if(gen.equals("男")||gen.equals("女"))
				Sql="select * from [jk15] where"+ "[高数]='"+ math+"'and [性别]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[高数]='"+ math+"'";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("searchtwo")) {//查询2
				
				String en=new String(request.getParameter("en"));
				String gen=new String(request.getParameter("gender"));	
				if(gen.equals("男")||gen.equals("女"))
				Sql="select * from [jk15] where"+ "[英语]='"+ en+"'and [性别]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[英语]='"+ en+"'";	
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
					
			
			if (action.equals("searchthree")) {//查询3

				String py=new String(request.getParameter("py"));
				String gen=new String(request.getParameter("gender"));	
				if(gen.equals("男")||gen.equals("女"))
				Sql="select * from [jk15] where"+ "[物理]='"+ py+"'and [性别]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[物理]='"+ py+"'";	
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			jsonObj.put("ret", 2);
			jsonObj.put("message", "系统异常");
			out.print(jsonObj.toString());
			return;
		}
	}

	public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
	{  
	   // json数组  
	   JSONArray array = new JSONArray();  
	    
	   // 获取列数  
	   ResultSetMetaData metaData = rs.getMetaData();  
	   int columnCount = metaData.getColumnCount();  
	    
	   // 遍历ResultSet中的每条数据  
	    while (rs.next()) {  
	        JSONObject jsonObj = new JSONObject();  
	         
	        // 遍历每一列  
	        for (int i = 1; i <= columnCount; i++) {  
	            String columnName =metaData.getColumnLabel(i);  
	            String value = rs.getString(columnName);  
	            jsonObj.put(columnName, value);  
	        }   
	        array.add(jsonObj);   
	    }  
	    
	   return array.toString();  
	}  
}
