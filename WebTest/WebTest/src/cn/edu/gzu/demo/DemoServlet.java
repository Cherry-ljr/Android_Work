package cn.edu.gzu.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

public class DemoServlet extends HttpServlet {
	
	private Connection conn = null; // 数据库连接对象
	private String driverName = null; // 数据库驱动器
	private String url = null; // 数据库地址URL
	private String Message = "";



	/**
	 * 构造方法.
	 */
	public DemoServlet() {
		super();
	}

	/**
	 * 
	 * 析构方法，关闭数据库连接对象
	 * 
	 */  
    public void destroy() {  
        super.destroy();  
        try {  
        	conn.close();  
        }catch(Exception e) {  
            System.out.println("关闭数据库错误："+e.getMessage());  
        }  
    } 


	/**
	 * 
	 * 初始化方法，取得数据库连接对象
	 * 
	 */   
    public void init(ServletConfig config) throws ServletException  
    {  
        super.init(config);  
        //本Servlet里面的设置
        //driverName=config.getInitParameter("driverName");  
        //url=config.getInitParameter("url");  
        
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
    
    /**
	 * 
	 * 处理Post请求，转发到 Get，统一处理
	 * 
	 */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
		
	}
	
	/**
	 * 
	 * 处理 Get请求
	 * 
	 */ 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(); 
		
		// 取得用户登录页面提交的数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 判断登录账号为空，则自动跳转到登录
		if (username == null || username.trim().length() == 0) {
			Message = "请输入用户名";  
			session.setAttribute("message", Message);
			response.sendRedirect("index.jsp");
			return;
		}
		// 判断登录密码为空
		if (password == null || password.trim().length() == 0) {
			Message = "请输入密码";  
			session.setAttribute("message", Message);
			response.sendRedirect("index.jsp");
			return;
		}
		// 查询数据库和跳转到登录主界面
		try {
			// 查询数据库操作

			// 简单，但是会有安全问题和系列隐患，不建议手动拼接
			// String Sql = "select * from user where username = '" + username +"'";
			// Statement st = conn.createStatement();
			// ResultSet rs = st.executeQuery(Sql);

			// 预处理模式
			String Sql = "select * from user where username = ?";
			PreparedStatement pst = conn.prepareStatement(Sql);
			// 传入参数
			pst.setString(1, username);
			// 执行查询
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					// 跳转到主界面
					Message = "登录成功";  
					session.setAttribute("message", Message);
					session.setAttribute("username", username);
					response.sendRedirect("main.jsp");
				} else {
					Message = "密码错！";  
					session.setAttribute("message", Message);   
					response.sendRedirect("index.jsp");
				}
			} else {
				Message = "不存在的用户！";  
				session.setAttribute("message", Message);    
				response.sendRedirect("index.jsp");
			}

		} catch (Exception e) {
			System.out.println("错误：" + e.getMessage());
			response.sendRedirect("index.jsp");
		}
	}

}
