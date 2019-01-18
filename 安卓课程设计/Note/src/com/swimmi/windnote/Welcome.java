package com.swimmi.windnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Welcome extends Activity {

	private LinearLayout welcome;		//布局
	private TextView quoteTxt;			//引言标签
	private int color;

	private SharedPreferences sp;
	private Dialog keyDialog;
	private EditText keyTxt;
	private Boolean needKey=true;		//是否需要密码
	private SQLiteDatabase wn;
	private Handler welcomeHand;		//欢迎页停留
	private Runnable welcomeShow;        //Runnable只是一个接口，它里面只有一个run()方法，没有start()方法
	                                     //      所以，即使实现了Runnable接口，那也无法启动线程，必须依托其他类。
	private String quote;
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.welcome);
	    
	    wn=Database(R.raw.windnote);
	    sp = getSharedPreferences("setting", 0);
	    String content=getResources().getString(R.string.hello_world);		//引言内容
	    String author=getResources().getString(R.string.app_name);			//引言作者
	    String type=getResources().getString(R.string.app_name);			//引言类型
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    if(!sp.getString("today","2012-12-21").equals(sdf.format(new Date())))		//控制每天显示一则引言
	    {
		    Cursor cursor=wn.rawQuery("select * from quotes order by q_count,id limit 1", null);
		    if(cursor.moveToFirst())
		    {
		    	content=cursor.getString(cursor.getColumnIndex("q_content"));
		    	author=cursor.getString(cursor.getColumnIndex("q_author"));
		    	type=cursor.getString(cursor.getColumnIndex("q_type"));
				sp.edit().putString("q_content",content).commit();
				sp.edit().putString("q_author", author).commit();
				sp.edit().putString("q_type", type).commit();
		    	quote=content;
		    	int id=cursor.getInt(cursor.getColumnIndex("id"));
		    	wn.execSQL("update quotes set q_count=q_count+1 where id="+id);
		    	sp.edit().putString("today", sdf.format(new Date())).commit();
		    }
		    cursor.close();
	    }
	    else
	    {
	    	content=sp.getString("q_content", "");
	    	author=sp.getString("q_author", "");
	    	type=sp.getString("q_type", "");
	    	quote=content;
	    }
	    
	    color=sp.getInt("color", getResources().getColor(R.color.blue));
		welcome=(LinearLayout)findViewById(R.id.welcome);
		welcome.setBackgroundColor(color);//设置欢迎界面颜色
		welcome.setOnClickListener(new OnClickListener(){		//点击屏幕跳过引言
			@Override
			public void onClick(View arg0) {
				welcome();
	        	welcomeHand.removeCallbacks(welcomeShow);//removeCallbacks方法是删除指定的Runnable对象，使线程对象停止运行。
			}
		});
		quoteTxt=(TextView)findViewById(R.id.quote_txt);
		quoteTxt.setTextColor(color);
		quoteTxt.setText(content);
        
		welcomeHand = new Handler();
		welcomeShow=new Runnable()
	    {
	        @Override
	        public void run()
	        {  
	        	welcome();
	        }
	    };
		welcomeHand.postDelayed(welcomeShow, (quote.length()+7)*100); 
	}
	private void welcome(){		//欢迎界面
		Intent data=getIntent();
    	needKey=data.getBooleanExtra("needKey", true);//设置需要密码为true
    	if(needKey&&sp.contains("key"))//输入密码
    		enterKey();
       	else
    	{
            Intent intent=new Intent(Welcome.this,Main.class);
            startActivity(intent);
            finish();
    	}
	}
	public SQLiteDatabase Database(int raw_id) {
        try {
        	int BUFFER_SIZE = 100000;
        	String DB_NAME = "windnote.db"; 
        	String PACKAGE_NAME = "com.swimmi.windnote";
        	String DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + PACKAGE_NAME+"/databases/";
        	File destDir = new File(DB_PATH);
        	  if (!destDir.exists()) {
        	   destDir.mkdirs();
        	  }
        	String file=DB_PATH+DB_NAME;
        	if (!(new File(file).exists())) {
                InputStream is = this.getResources().openRawResource(
                        raw_id);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file,null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
	private void enterKey()			//输入密码
	{
		View keyView = View.inflate(this, R.layout.cancelkey, null);
		keyDialog=new Dialog(this,R.style.dialog);
		keyDialog.setContentView(keyView);
		keyTxt=(EditText)keyView.findViewById(R.id.key_old);
		keyTxt.addTextChangedListener(change);
		keyDialog.show();
	}
	
	public TextWatcher change = new TextWatcher() {//对EditText的时时监听
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String okey=keyTxt.getText().toString();
			String rkey=sp.getString("key", "");
			if(okey.equals(rkey))
			{
				needKey=false;
				keyDialog.dismiss();
	            Intent intent=new Intent(Welcome.this,Main.class);
	            startActivity(intent);
	            finish();
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		/** 
	       * This method is called to notify you that, within s, the count characters 
	       *  beginning at start have just replaced old text that had length before 
	       *  在s中，从start处开始的count个字符刚刚替换了原来长度为before的文本 
	       *  s 为变化后的内容； 
	       *  start 为开始变化位置的索引，从0开始计数； 
	       *  before 为被取代的老文本的长度，比如s由1变为12，before为0，由12变为1，before为1； 
	       *  count 为将要发生变化的字符数 
	       */ 
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(needKey){
				finish();
				System.exit(0);
			}
			else
			{return true;}
		}
		if(keyCode==KeyEvent.KEYCODE_MENU)
		{
			welcome();
        	welcomeHand.removeCallbacks(welcomeShow);
		}
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
}
