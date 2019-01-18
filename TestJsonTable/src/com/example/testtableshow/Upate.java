package com.example.testtableshow;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

public class Upate extends Activity {
	private EditText stuText;
	private EditText nameText;
	private EditText genderText;

	private Spinner spinner_sub;
	private EditText scoresText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		Button sure = (Button) findViewById(R.id.U_sure);	
	
		stuText=(EditText)findViewById(R.id.U_stu);
		nameText=(EditText)findViewById(R.id.U_name);
		genderText=(EditText)findViewById(R.id.U_gender);	   
	    spinner_sub=(Spinner)findViewById(R.id.U_subjects);
	    scoresText=(EditText)findViewById(R.id.u_scores);		
		
		ButtonListener buttonListener = new ButtonListener();
		sure.setOnClickListener(buttonListener);
	}
	
	class ButtonListener implements OnClickListener {

		public void onClick(View v) {
			 
			 new Thread(runnable).start();
		     Toast.makeText(Upate.this, "修改成功", Toast.LENGTH_SHORT).show(); 
		     finish();  	
			}
	}
		
	/*Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String json = data.getString("value");
			//if (json != null && json.length() > 0)
				//initContent(json);
		}
	};*/

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			String stu =stuText.getText().toString();
			String name =nameText.getText().toString();
			String gender =genderText.getText().toString();
			String scores =scoresText.getText().toString();
			String sub =spinner_sub.getSelectedItem().toString();
			//Log.d("tag", stu);
			/*Message msg = new Message();
			Bundle data = new Bundle();*/
			
			
			String ret = "";
			try {
				// Android 模拟器访问 pc，pc的默认地址 10.0.2.2 
				String BaseUrl = "http://10.0.2.2:8080/WebTest/DemoServletJson.do?username=test&password=test6test&";
						
				 doGet(BaseUrl + "action=update" + "&stu="+stu+ "&name="+ name
														+ "&gender="+gender
														+ "&Sub="+sub
														+"&scores="+scores
														, "UTF-8");							
			} catch (Exception e) {
				e.printStackTrace();
			}
			
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
