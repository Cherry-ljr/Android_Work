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

	private Connection conn = null; // ���ݿ����Ӷ���
	private String driverName = null; // ���ݿ�������
	private String url = null; // ���ݿ��ַURL
	private String Message = "";

	public DemoServletJson() {
		super();
	}

	public void destroy() {
        super.destroy();  
        try {  
        	conn.close();  
        }catch(Exception e) {  
            System.out.println("�ر����ݿ����"+e.getMessage());  
        }  
	}

	public void init() throws ServletException {
        //ȫ��contont���������
        driverName=getServletContext().getInitParameter("driverName");  
        url=getServletContext().getInitParameter("url"); 
          
        try {  
            Class.forName(driverName);  
            conn=DriverManager.getConnection(url);  
        } catch(Exception e) {  
            System.out.println("���ݿ����Ӵ���"+e.getMessage());  
        }  
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ȡ���û���¼ҳ���ύ������
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String action = request.getParameter("action");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		JSONObject jsonObj = new JSONObject(); 
		
		// �жϵ�¼�˺�Ϊ�գ����Զ���ת����¼
		if (username == null || username.trim().length() == 0) {
			jsonObj.put("ret", 1);
			jsonObj.put("message", "�������û���");
			out.print(jsonObj.toString());
			return;
		}
		// �жϵ�¼����Ϊ��
		if (password == null || password.trim().length() == 0) {
			jsonObj.put("ret", 1);
			jsonObj.put("message", "����������");
			out.print(jsonObj.toString());
			return;
		}

		try {

			// Ԥ����ģʽ
			String Sql = "select * from user where username = ?";
			PreparedStatement pst = conn.prepareStatement(Sql);
			// �������
			pst.setString(1, username);
			// ִ�в�ѯ
			ResultSet rs = pst.executeQuery();
			
			// �û���֤
			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					
				} else {
					jsonObj.put("ret", 1);
					jsonObj.put("message", "�������");
					out.print(jsonObj.toString());
					return;
				}
			} else {
				jsonObj.put("ret", 1);
				jsonObj.put("message", "�û�����");
				out.print(jsonObj.toString());
				return;
			}
			rs.close();
			
			if (action.equals("login")) {
				jsonObj.put("ret", 0);
				jsonObj.put("message", "��¼�ɹ�");
				out.print(jsonObj.toString());
				return;
			}
			if (action.equals("queryone")) {
				Sql = "select * from [jk15] order by [ѧ��]  desc";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("querytwo")) {
				Sql = "select * from [jk15] order by [����]  desc";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			
			if (action.equals("delete")) {//ɾ��
				String stu=new String(request.getParameter("stu"));
				Sql="delete from jk15 where ѧ��='"+stu+" '";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("add")) {//���
				String stu=new String(request.getParameter("stu"));
				String name=new String(request.getParameter("name"));
				String gender=new String(request.getParameter("gender"));
				String math=new String(request.getParameter("math"));
				String en=new String(request.getParameter("en"));
				String py=new String(request.getParameter("py"));
				
				Sql = "INSERT INTO [jk15] ([ѧ��],[����] ,[�Ա�],[����],[Ӣ��],[����]) VALUES ('" + stu
				+ "', '" + name + "', '" + gender + "', '" + math+ "', '" + en+ "', '" + py
				+ "')";
								
				pst = conn.prepareStatement(Sql);
				pst.executeUpdate();
				
				return;
			}
			if (action.equals("update")) {//�޸�
				String stu=new String(request.getParameter("stu"));
				String name=new String(request.getParameter("name").getBytes("ISO-8859-1"), "UTF-8");
				String gender=new String(request.getParameter("gender").getBytes("ISO-8859-1"), "UTF-8");			
				String sub=new String(request.getParameter("Sub").getBytes("ISO-8859-1"), "UTF-8");
				String scores=new String(request.getParameter("scores"));
			
				if(sub.equals("����"))				
				{
					Sql = "update [jk15] set [����]='"+name+"',[�Ա�]='"+gender+"',[����]='"+scores+"' where [ѧ��]='"+stu+"'";
					}
				if(sub.equals("Ӣ��"))				
				{
					Sql = "update [jk15] set [����]='"+name+"',[�Ա�]='"+gender+"',[Ӣ��]='"+scores+"' where [ѧ��]='"+stu+"'";
					}
				if(sub.equals("����"))				
				{
				
					Sql = "update [jk15] set [����]='"+name+"',[�Ա�]='"+gender+"',[����]='"+scores+"' where [ѧ��]='"+stu+"'";
				}
								
				pst = conn.prepareStatement(Sql);
				pst.executeUpdate();
				return;
		       
			}
			
			if (action.equals("searchone")) {//��ѯ1
				
				String math=new String(request.getParameter("math"));
				String gen=new String(request.getParameter("gender").getBytes("ISO-8859-1"), "UTF-8");
				if(gen.equals("��")||gen.equals("Ů"))
				Sql="select * from [jk15] where"+ "[����]='"+ math+"'and [�Ա�]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[����]='"+ math+"'";
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			if (action.equals("searchtwo")) {//��ѯ2
				
				String en=new String(request.getParameter("en"));
				String gen=new String(request.getParameter("gender"));	
				if(gen.equals("��")||gen.equals("Ů"))
				Sql="select * from [jk15] where"+ "[Ӣ��]='"+ en+"'and [�Ա�]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[Ӣ��]='"+ en+"'";	
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
					
			
			if (action.equals("searchthree")) {//��ѯ3

				String py=new String(request.getParameter("py"));
				String gen=new String(request.getParameter("gender"));	
				if(gen.equals("��")||gen.equals("Ů"))
				Sql="select * from [jk15] where"+ "[����]='"+ py+"'and [�Ա�]='" +gen+"'";
				else 	Sql="select * from [jk15] where"+ "[����]='"+ py+"'";	
				pst = conn.prepareStatement(Sql);
				rs = pst.executeQuery();
				out.print(this.resultSetToJson(rs));
				rs.close();
				return;
			}
			

		} catch (Exception e) {
			System.out.println("����" + e.getMessage());
			jsonObj.put("ret", 2);
			jsonObj.put("message", "ϵͳ�쳣");
			out.print(jsonObj.toString());
			return;
		}
	}

	public String resultSetToJson(ResultSet rs) throws SQLException,JSONException  
	{  
	   // json����  
	   JSONArray array = new JSONArray();  
	    
	   // ��ȡ����  
	   ResultSetMetaData metaData = rs.getMetaData();  
	   int columnCount = metaData.getColumnCount();  
	    
	   // ����ResultSet�е�ÿ������  
	    while (rs.next()) {  
	        JSONObject jsonObj = new JSONObject();  
	         
	        // ����ÿһ��  
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
