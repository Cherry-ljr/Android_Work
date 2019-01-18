package com.example.testtableshow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	MyHorizontalScrollView view_1;
	MyHorizontalScrollView view_2;
	TableLayout id_table_layout;
	TableLayout data_table_layout;
	int order = 1;
	private ArrayList<HashMap<String, Object>> datalist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view_1 = (MyHorizontalScrollView) this
				.findViewById(R.id.HorizontalScrollView_1);
		view_2 = (MyHorizontalScrollView) this
				.findViewById(R.id.HorizontalScrollView_2);
		view_1.setScrollView(view_2);
		view_2.setScrollView(view_1);
		init();
		new Thread(runnable).start();
	}

	public void init() {
		id_table_layout = (TableLayout) findViewById(R.id.left_table);
		data_table_layout = (TableLayout) findViewById(R.id.data_table);
		datalist = new ArrayList<HashMap<String, Object>>();
	}

	public void initContent(String json) {

		datalist.clear();
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

		}

		int count = datalist.size();
		view_1.removeAllViews();
		id_table_layout.removeAllViews();
		data_table_layout.removeAllViews();

		for (int i = 0; i < count; i++) {
			id_table_layout.addView(getIdRow(i));
			data_table_layout.addView(getDataRow(i));
		}
		view_1.addView(getHead());
	}

	public TableRow getIdRow(int n) {
		TableRow row = new TableRow(this);
		View v = this.getLayoutInflater().inflate(R.layout.data_xml, null);
		TextView id_text = (TextView) v.findViewById(R.id.id_text);
		id_text.setText("" + datalist.get(n).get("m1"));
		row.addView(v);
		return row;
	}

	public TableRow getDataRow(int n) {
		TableRow row = new TableRow(this);
		View v = this.getLayoutInflater().inflate(R.layout.item, null);
		TextView t1 = (TextView) v.findViewById(R.id.t1);
		t1.setText((String) datalist.get(n).get("m2"));
		TextView t2 = (TextView) v.findViewById(R.id.t2);
		t2.setText((String) datalist.get(n).get("m3"));
		TextView t3 = (TextView) v.findViewById(R.id.t3);
		t3.setText((String) datalist.get(n).get("m4"));
		TextView t4 = (TextView) v.findViewById(R.id.t4);
		t4.setText((String) datalist.get(n).get("m5"));
		TextView t5 = (TextView) v.findViewById(R.id.t5);
		t5.setText((String) datalist.get(n).get("m6"));
		TextView t6 = (TextView) v.findViewById(R.id.t6);
		t6.setText((String) datalist.get(n).get("m7"));
		
		row.addView(v);
		return row;
	}

	public View getHead() {
		View v = this.getLayoutInflater().inflate(R.layout.item, null);
		TextView t1 = (TextView) v.findViewById(R.id.t1);
		t1.setText("学号");
		t1.setTextColor(Color.BLACK);
		t1.setBackgroundResource(R.drawable.text_bg);
		TextView t2 = (TextView) v.findViewById(R.id.t2);
		t2.setText("姓名");
		t2.setTextColor(Color.BLACK);
		t2.setBackgroundResource(R.drawable.text_bg);
		TextView t3 = (TextView) v.findViewById(R.id.t3);
		t3.setText("性别");
		t3.setTextColor(Color.BLACK);
		t3.setBackgroundResource(R.drawable.text_bg);
		TextView t4 = (TextView) v.findViewById(R.id.t4);
		t4.setText("高数");
		t4.setTextColor(Color.BLACK);
		t4.setBackgroundResource(R.drawable.text_bg);
		TextView t5 = (TextView) v.findViewById(R.id.t5);
		t5.setText("英语");
		t5.setTextColor(Color.BLACK);
		t5.setBackgroundResource(R.drawable.text_bg);
		TextView t6 = (TextView) v.findViewById(R.id.t6);
		t6.setText("物理");
		t6.setTextColor(Color.BLACK);
		t6.setBackgroundResource(R.drawable.text_bg);
		
		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.m1:        //按学号降序排序
			order = 1;
			new Thread(runnable).start();
			break;
		case R.id.m2:        //按高数降序排序
			order = 2;
			new Thread(runnable).start();
			break;
		case R.id.m3:       //增添
			intent.setClass(MainActivity.this, Add.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.m4:       //删除
			intent.setClass(MainActivity.this, delete.class);
			MainActivity.this.startActivity(intent);
			break;        
		case R.id.m5:        //修改
			intent.setClass(MainActivity.this, Upate.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.m6:       //查询
			intent.setClass(MainActivity.this, Search.class);
			MainActivity.this.startActivity(intent);
			break; 
		case R.id.m7:       //刷新
			finish();
			intent.setClass(MainActivity.this, MainActivity.class);
			MainActivity.this.startActivity(intent);
			break; 
		}
		return super.onOptionsItemSelected(item);
	}

	private void showSettings() {
		final Intent settingsIntent = new Intent(
				android.provider.Settings.ACTION_SETTINGS);
		settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(settingsIntent);
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

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			String ret = "";
			try {
				// Android 模拟器访问 pc，pc的默认地址 10.0.2.2 
				String BaseUrl = "http://10.0.2.2:8080/WebTest/DemoServletJson.do?username=test&password=test6test&";
				switch (order) {
				case 1:
					ret = doGet(BaseUrl + "action=queryone", "UTF-8");
					break;
				case 2:
					ret = doGet(BaseUrl + "action=querytwo", "UTF-8");
					break;
				default:
					break;
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
