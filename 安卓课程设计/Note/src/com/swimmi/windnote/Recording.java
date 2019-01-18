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
	 private Button start;             //¼����ʼ
	 private Button stop;              //¼������
	 private ListView listView;        //��ʾ
	 private int color;					//��ǰƤ����ɫ
	 private SharedPreferences sp;		//���ݴ洢
	 private LinearLayout recording;			//��������

	 
	 // ¼���ļ�����
	 private MediaPlayer myPlayer;
	 // ¼��
	 private MediaRecorder myRecorder;
	 // ��Ƶ�ļ������ַ
	 private String path;
	 private String paths = path;
	 private File saveFilePath;
	 // ��¼�����ļ�
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
      sp = getSharedPreferences("setting", 0);		//��ȡ��������
      color=sp.getInt("color", getResources().getColor(R.color.blue));		//��ȡƤ����ɫ
      recording=(LinearLayout)findViewById(R.id.recording);
      recording.setBackgroundColor(color);//���ñ�����ɫ

	  myPlayer = new MediaPlayer();//������Ƶ
	  myRecorder = new MediaRecorder();//����Դ������ļ���ʽ�������ʽ
	  // ����˷�Դ����¼��
	  myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//Ĭ����ƵԴ
	  // ���������ʽ
	  myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
	  // ���ñ����ʽ
	  myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	  showRecord = new ShowRecorderAdpter();
	  if (Environment.getExternalStorageState().equals(
	    Environment.MEDIA_MOUNTED)) {//�ж�SD����״̬
	   try {
	    path = Environment.getExternalStorageDirectory()//�����ļ���ȡ�ⲿ�洢��SD����
	      .getCanonicalPath().toString()//getCanonicalPath()����һ������·����getPath()����һ�����·��
	      + "/XIONGRECORDERS";
	    File files = new File(path);
	    if (!files.exists()) {//����ļ�·�������ھʹ���һ��
	     files.mkdir();
	    }
	    listFile = files.list();
	   } catch (IOException e) {
	    e.printStackTrace();
	   }
	  }
	  start.setOnClickListener(this);
	  stop.setOnClickListener(this);
	  if (listFile != null) {//�ж��ļ��Ƿ�Ϊ��
	   listView.setAdapter(showRecord);
	  }
	 }
	
	 class ShowRecorderAdpter extends BaseAdapter {
	  @Override
	  public int getCount() {//����Adapter ����/�� �����ݼ��ϵĳ��ȣ�Ҳ�Ƕ�Ӧ����View����ListView������ĸ�����
	   return listFile.length;
	  }
	  @Override
	  public Object getItem(int arg0) {//���ѡ�����¼��з���ص���
	   return arg0;
	  }
	  @Override
	  public long getItemId(int arg0) {//���ص��Ǹ�postion��Ӧitem��id
	   return arg0;
	  }
	  @Override
	  public View getView(final int postion, View arg1, ViewGroup arg2) {//����parent��ÿ�����ListView�е�ÿһ�У���View��
	   View views = LayoutInflater.from(Recording.this).inflate(//LayoutInflater��������res/layout/�µ�xml�����ļ�
	     R.layout.list_show_filerecorder, null);
	   TextView filename = (TextView) views
	     .findViewById(R.id.show_file_name);
	   Button plays = (Button) views.findViewById(R.id.bt_list_play);//����
	   Button stop = (Button) views.findViewById(R.id.bt_list_stop);//ֹͣ
	   filename.setText(listFile[postion]);
	   // ����¼��
	   plays.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
	     try {
	      myPlayer.reset();//���ò���
	      myPlayer.setDataSource(path + "/" + listFile[postion]);
	      if (!myPlayer.isPlaying()) {//�ļ�û�����ڲ���
	       myPlayer.prepare();//׼��
	       myPlayer.start();//��ʼ
	      } else {
	       myPlayer.pause();//��ͣ
	      }
	     } catch (IOException e) {
	      e.printStackTrace();
	     }
	    }
	   });
	   // ֹͣ����
	   stop.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
	     if (myPlayer.isPlaying()) {//����
	      myPlayer.stop();//ֹͣ
	     }
	    }
	   });
	   return views;
	  }
	 }
	 @Override
	 public void onClick(View v) {//�¼�
	  switch (v.getId()) {
	  case R.id.start:
	   final EditText filename = new EditText(this);//�ļ���
	   Builder alerBuidler = new Builder(this);
	   alerBuidler
	     .setTitle("������Ҫ������ļ���")
	     .setView(filename)
	      .setNegativeButton("ȡ��",null)
	     .setPositiveButton("ȷ��",
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
	          // ��ʼ¼��
	          myRecorder.start();
	          start.setText("����¼���С�������");//button��ʾ����¼��
	          start.setEnabled(false);//start��ʾ��ɫ�������޷�ʹ��
	          aler.dismiss();
	          // ���¶�ȡ �ļ�
	          File files = new File(path);
	          listFile = files.list();
	          // ˢ��ListView
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
	    // �ж��Ƿ񱣴� �����������ɾ��
	    new AlertDialog.Builder(this)
	      .setTitle("�Ƿ񱣴��¼��")
	      .setPositiveButton("ȷ��", null)
	      .setNegativeButton("ȡ��",
	        new DialogInterface.OnClickListener() {
	         @Override
	         public void onClick(DialogInterface dialog,
	           int which) {
	          saveFilePath.delete();
	          // ���¶�ȡ �ļ�
	          File files = new File(path);
	          listFile = files.list();
	          // ˢ��ListView
	          showRecord.notifyDataSetChanged();
	         }
	        }).show();
	   }
	   start.setText("¼��");
	   start.setEnabled(true);
	  default:
	   break;
	  }
	 }
	 @Override
	 protected void onDestroy() {
	  // �ͷ���Դ
	  if (myPlayer.isPlaying()) {//����
	   myPlayer.stop();//ֹͣ
	   myPlayer.release();//�ͷ�
	  }
	  myPlayer.release();
	  myRecorder.release();
	  super.onDestroy();
	 }
	}
