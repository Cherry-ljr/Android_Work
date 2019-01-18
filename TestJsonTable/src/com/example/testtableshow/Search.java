package com.example.testtableshow;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {
	
	private Spinner spinner_gen;
	private Spinner spinner_sub;
	private EditText editScores;
	private TextView  labelView;;
	private ArrayList<HashMap<String, Object>> datalist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query2);
		Button sure = (Button) findViewById(R.id.q2_sure);	
	
		editScores=(EditText)findViewById(R.id.edt_grades);
		   
	    spinner_gen=(Spinner)findViewById(R.id.s_Gen);
	    spinner_sub=(Spinner)findViewById(R.id.S_Sub);
	    labelView = (TextView)findViewById(R.id.tv_display);
		
		ButtonListener buttonListener = new ButtonListener();
		sure.setOnClickListener(buttonListener);
	}
	
	class ButtonListener implements OnClickListener {

		public void onClick(View v) {
			datalist = new ArrayList<HashMap<String, Object>>();
			 new Thread(runnable).start();

		        	
			}
	}
		
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String json = data.getString("value");
		    if (json != null && json.length() > 0)
				initContent(json);
		}
	};
	
	public void initContent(String json) {

	//	datalist.clear();
		if (json != null && json.length() > 0) {
			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(json);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					// 初始化map数组对象
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("m1", jsonObject.getString("ID"));
					map.put("m2", jsonObject.getString("学号"));
					map.put("m3", jsonObject.getString("姓名"));
					map.put("m4", jsonObject.getString("性别"));
					map.put("m5", jsonObject.getString("高数"));
					map.put("m6", jsonObject.getString("英语"));
					map.put("m7", jsonObject.getString("物理"));
					
					datalist.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String id[]=new String[datalist.size()];
			String stu[]=new String[datalist.size()];
			String name[]=new String[datalist.size()];
			String gender[]=new String[datalist.size()];
			String math[]=new String[datalist.size()];
			String en[]=new String[datalist.size()];
			String py[]=new String[datalist.size()];
			if(datalist.size()!=0)
			{for(int i=0;i<datalist.size();i++)
			{
			 id[i]=(String) datalist.get(i).get("m1");
			 stu[i]=(String) datalist.get(i).get("m2");
			 name[i]=(String) datalist.get(i).get("m3");
			 gender[i]=(String) datalist.get(i).get("m4");
			 math[i]=(String) datalist.get(i).get("m5");
			 en[i]=(String) datalist.get(i).get("m6");
			 py[i]=(String) datalist.get(i).get("m7");
	//		labelView.setText("id="+id+" "+"学号="+stu+" "+"姓名="+name+" "+"性别="+gender+" "+"高数="+math+" "+"英语="+en+" "+"物理="+py);
			//msg += peoples[i].toString()+"\n";
			}
			String msg="";
			for(int i=0;i<datalist.size();i++)
				msg+="id="+id[i]+" "+"学号="+stu[i]+" "+"姓名="+name[i]+" "+"性别="+gender[i]+" "+"高数="+math[i]+" "+"英语="+en[i]+" "+"物理="+py[i]+"\n";						
			labelView.setText(msg);}
			else return;
		}
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			
			String score =editScores.getText().toString();
			String gen =spinner_gen.getSelectedItem().toString();
			String sub =spinner_sub.getSelectedItem().toString();
			
			Message msg = new Message();
			Bundle data = new Bundle();
	
			String ret = "";
			try {
				// Android 模拟器访问 pc，pc的默认地址 10.0.2.2 
				String BaseUrl = "http://10.0.2.2:8080/WebTest/DemoServletJson.do?username=test&password=test6test&";
				if(sub.equals("高数")){
				ret = doGet(BaseUrl + "action=searchone" + "&gender="+gen
						+ "&math="+ score , "UTF-8");}
				
				else if(sub.equals("英语")){
					ret = doGet(BaseUrl + "action=searchtwo" + "&gender="+URLEncoder.encode(gen,"UTF-8")
							+ "&en="+score, "UTF-8");
				}
				else{
					ret = doGet(BaseUrl + "action=searchthree" + "&gender="+URLEncoder.encode(gen,"UTF-8")
							+ "&py="+ score,"UTF-8");
				}				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.putString("value", ret);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	public static String doGet(String url, String encode) throws Exception {
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), encode), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

}
