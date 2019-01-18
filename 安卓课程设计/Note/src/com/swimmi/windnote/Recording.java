package com.swimmi.windnote;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
public class Recording extends Activity implements OnClickListener {
	 private Button start;             //录音开始
	 private Button stop;              //录音结束
	 private ListView listView;        //显示
	 private int color;					//当前皮肤颜色
	 private SharedPreferences sp;		//数据存储
	 private LinearLayout recording;			//布局容器

	 
	 // 录音文件播放
	 private MediaPlayer myPlayer;
	 // 录音
	 private MediaRecorder myRecorder;
	 // 音频文件保存地址
	 private String path;
	 private String paths = path;
	 private File saveFilePath;
	 // 所录音的文件
	 String[] listFile = null;
	 ShowRecorderAdpter showRecord;
	 AlertDialog aler = null;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.recording);
	  start = (Button) findViewById(R.id.start);
	  stop = (Button) findViewById(R.id.stop);
	  listView = (ListView) findViewById(R.id.list);
      sp = getSharedPreferences("setting", 0);		//获取设置数据
      color=sp.getInt("color", getResources().getColor(R.color.blue));		//获取皮肤颜色
      recording=(LinearLayout)findViewById(R.id.recording);
      recording.setBackgroundColor(color);//设置背景颜色

	  myPlayer = new MediaPlayer();//播放音频
	  myRecorder = new MediaRecorder();//声音源，输出文件格式，编码格式
	  // 从麦克风源进行录音
	  myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//默认音频源
	  // 设置输出格式
	  myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
	  // 设置编码格式
	  myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	  showRecord = new ShowRecorderAdpter();
	  if (Environment.getExternalStorageState().equals(
	    Environment.MEDIA_MOUNTED)) {//判断SD卡的状态
	   try {
	    path = Environment.getExternalStorageDirectory()//返回文件获取外部存储（SD）卡
	      .getCanonicalPath().toString()//getCanonicalPath()返回一个绝对路径，getPath()返回一个相对路径
	      + "/XIONGRECORDERS";
	    File files = new File(path);
	    if (!files.exists()) {//如果文件路径不存在就创造一个
	     files.mkdir();
	    }
	    listFile = files.list();
	   } catch (IOException e) {
	    e.printStackTrace();
	   }
	  }
	  start.setOnClickListener(this);
	  stop.setOnClickListener(this);
	  if (listFile != null) {//判断文件是否为空
	   listView.setAdapter(showRecord);
	  }
	 }
	
	 class ShowRecorderAdpter extends BaseAdapter {
	  @Override
	  public int getCount() {//返回Adapter 连接/绑定 的数据集合的长度，也是对应容器View（如ListView）的项的个数。
	   return listFile.length;
	  }
	  @Override
	  public Object getItem(int arg0) {//点击选择处理事件中方便地调用
	   return arg0;
	  }
	  @Override
	  public long getItemId(int arg0) {//返回的是该postion对应item的id
	   return arg0;
	  }
	  @Override
	  public View getView(final int postion, View arg1, ViewGroup arg2) {//返回parent中每个项（如ListView中的每一行）的View。
	   View views = LayoutInflater.from(Recording.this).inflate(//LayoutInflater是用来找res/layout/下的xml布局文件
	     R.layout.list_show_filerecorder, null);
	   TextView filename = (TextView) views
	     .findViewById(R.id.show_file_name);
	   Button plays = (Button) views.findViewById(R.id.bt_list_play);//播放
	   Button stop = (Button) views.findViewById(R.id.bt_list_stop);//停止
	   filename.setText(listFile[postion]);
	   // 播放录音
	   plays.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
	     try {
	      myPlayer.reset();//重置播放
	      myPlayer.setDataSource(path + "/" + listFile[postion]);
	      if (!myPlayer.isPlaying()) {//文件没有正在播放
	       myPlayer.prepare();//准备
	       myPlayer.start();//开始
	      } else {
	       myPlayer.pause();//暂停
	      }
	     } catch (IOException e) {
	      e.printStackTrace();
	     }
	    }
	   });
	   // 停止播放
	   stop.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
	     if (myPlayer.isPlaying()) {//播放
	      myPlayer.stop();//停止
	     }
	    }
	   });
	   return views;
	  }
	 }
	 @Override
	 public void onClick(View v) {//事件
	  switch (v.getId()) {
	  case R.id.start:
	   final EditText filename = new EditText(this);//文件名
	   Builder alerBuidler = new Builder(this);
	   alerBuidler
	     .setTitle("请输入要保存的文件名")
	     .setView(filename)
	      .setNegativeButton("取消",null)
	     .setPositiveButton("确定",
	       new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog,
	          int which) {
	         String text = filename.getText().toString();
	         try {
	          paths = path
	            + "/"
	            + text
	            + new SimpleDateFormat(
	              "yyyyMMddHHmmss").format(System
	              .currentTimeMillis())
	            + ".amr";
	          saveFilePath = new File(paths);
	          myRecorder.setOutputFile(saveFilePath
	            .getAbsolutePath());
	          saveFilePath.createNewFile();
	          myRecorder.prepare();
	          // 开始录音
	          myRecorder.start();
	          start.setText("正在录音中…………");//button显示正在录音
	          start.setEnabled(false);//start显示灰色，现在无法使用
	          aler.dismiss();
	          // 重新读取 文件
	          File files = new File(path);
	          listFile = files.list();
	          // 刷新ListView
	          showRecord.notifyDataSetChanged();
	         } catch (Exception e) {
	          e.printStackTrace();
	         }
	        }
	       });
	   aler = alerBuidler.create();
	   aler.setCanceledOnTouchOutside(false);
	   aler.show();
	   break;
	  case R.id.stop:
	   if (saveFilePath.exists() && saveFilePath != null) {
	    myRecorder.stop();
	    myRecorder.release();
	    // 判断是否保存 如果不保存则删除
	    new AlertDialog.Builder(this)
	      .setTitle("是否保存该录音")
	      .setPositiveButton("确定", null)
	      .setNegativeButton("取消",
	        new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog,
	           int which) {
	          saveFilePath.delete();
	          // 重新读取 文件
	          File files = new File(path);
	          listFile = files.list();
	          // 刷新ListView
	          showRecord.notifyDataSetChanged();
	         }
	        }).show();
	   }
	   start.setText("录音");
	   start.setEnabled(true);
	  default:
	   break;
	  }
	 }
	 @Override
	 protected void onDestroy() {
	  // 释放资源
	  if (myPlayer.isPlaying()) {//播放
	   myPlayer.stop();//停止
	   myPlayer.release();//释放
	  }
	  myPlayer.release();
	  myRecorder.release();
	  super.onDestroy();
	 }
	}
