package com.swimmi.windnote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.support.v4.app.ActivityCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Note extends Activity {

	private LinearLayout note;		//����
	private TextView titleTxt;		//������
	private EditText noteTxt;		//�����
	
	private ImageButton backBtn;	//����
	private ImageButton shareBtn;	//����
	private ImageButton deleteBtn;	//ɾ��
	private ImageButton confirmBtn;	//ȷ��
	
	private Dialog delDialog;		//ɾ���Ի���
	private Integer s_id;			//����ID
	private String title;			//����
	private String content;			//����
	private Boolean lock;			//����״̬
	private int color;				//��ǰƤ����ɫ
	private SharedPreferences sp;
	private SQLiteDatabase wn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note); 
		
        wn=Database(R.raw.windnote);//���ݿ�
        sp = getSharedPreferences("setting", 0);//��ȡ��������
        color=sp.getInt("color", getResources().getColor(R.color.blue));//������ɫΪ��ɫ�����Ըı���ɫ
        
		note=(LinearLayout)findViewById(R.id.note);
		note.setBackgroundColor(color);//������ɫ
		
        titleTxt=(TextView)findViewById(R.id.title_note);
        noteTxt=(EditText)findViewById(R.id.note_txt); 
        backBtn=(ImageButton)findViewById(R.id.back_btn);
        shareBtn=(ImageButton)findViewById(R.id.share_btn);
        deleteBtn=(ImageButton)findViewById(R.id.delete_btn);
        confirmBtn=(ImageButton)findViewById(R.id.confirm_btn);
        
        Intent intent=getIntent();		//�ָ�δ��������
        @SuppressWarnings("unchecked")
		HashMap<String, Object> map=(HashMap<String, Object>) intent.getSerializableExtra("data");
        title=(String) map.get("title");
        content=(String) map.get("content");
        lock=(Boolean)map.get("lock");
        s_id=(Integer)map.get("id");
        titleTxt.setText(title);
        //noteTxt.setText(content);
        loadData(content);

        setLock(lock);
        ImageButton[] btns={backBtn,shareBtn,deleteBtn,confirmBtn};
		for(ImageButton btn:btns)
			btn.setOnClickListener(click);
	}
	
private void loadData(String context){
						
	
	
			//ȡ�����ݿ�����Ӧ���ֶ�����
		//	String context = cursor.getString(cursor.getColumnIndex("context"));
			
			//����������ʽ������ƥ��·��
		    Pattern p=Pattern.compile("/([^\\.]*)\\.\\w{3}"); 
		    Matcher m=p.matcher(context);
		    int startIndex = 0;
		    
		    while(m.find()){
		    	//ȡ��·��ǰ������
		    	if(m.start() > 0){
		    		noteTxt.append(context.substring(startIndex, m.start()));
		    	}
		    	
		        SpannableString ss = new SpannableString(m.group().toString());
		        
		        //ȡ��·��
		    	String path = m.group().toString();
		    	//ȡ��·���ĺ�׺
		    	String type = path.substring(path.length() - 3, path.length());
		    	Bitmap bm = null;
		    	Bitmap rbm = null;
		    	
			        bm = BitmapFactory.decodeFile(m.group());
			        //����ͼƬ
			        rbm = resize(bm,480);
		    	
		        //ΪͼƬ��ӱ߿�Ч��
		        rbm = getBitmapHuaSeBianKuang(rbm);
		       
		        ImageSpan span = new ImageSpan(this, rbm);
		        ss.setSpan(span,0, m.end() - m.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        System.out.println(m.start()+"-------"+m.end());
		        noteTxt.append(ss);
		        startIndex = m.end();
		        /*
		        //��List��¼��¼����λ�ü�����·�������ڵ����¼�
		        Map<String,String> map = new HashMap<String,String>();
		        map.put("location", m.start()+"-"+m.end());
		        map.put("path", path);
		        imgList.add(map);*/
		    }
		    //�����һ��ͼƬ֮������������TextView�� 
		    noteTxt.append(context.substring(startIndex,context.length()));
			//dop.close_db();
		}		
	
	
//�ȱ�������ͼƬ
	private Bitmap resize(Bitmap bitmap,int S){
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		double partion = imgWidth*1.0/imgHeight;
		double sqrtLength = Math.sqrt(partion*partion + 1);
		//�µ�����ͼ��С
		double newImgW = S*(partion / sqrtLength);
		double newImgH = S*(1 / sqrtLength);
		float scaleW = (float) (newImgW/imgWidth);
		float scaleH = (float) (newImgH/imgHeight);
		
		Matrix mx = new Matrix();
		//��ԭͼƬ��������
		mx.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
		return bitmap;
	}
	
	//��ͼƬ�ӱ߿򣬲����ر߿���ͼƬ
	public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
      float frameSize = 0.2f;
      Matrix matrix = new Matrix();

      // ��������ͼ
      Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
              bitmap.getHeight(), Bitmap.Config.ARGB_8888);

      // ���õ�ͼΪ����
      Canvas canvas = new Canvas(bitmapbg);
      canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
              | Paint.FILTER_BITMAP_FLAG));

      float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
              / (bitmap.getWidth());
      float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
              / (bitmap.getHeight());
      matrix.reset();
      matrix.postScale(scale_x, scale_y);

      // ����Ƭ��С����(��ȥ�߿�Ĵ�С)
      bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
              bitmap.getHeight(), matrix, true);

      Paint paint = new Paint();
      paint.setColor(Color.WHITE);
      paint.setStrokeWidth(1);
      paint.setStyle(Style.FILL);

      // ���Ƶ�ͼ�߿�
      canvas.drawRect(
              new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
              paint);
      // ���ƻ�ɫ�߿�
      paint.setColor(Color.BLUE);
      canvas.drawRect(
              new Rect((int) (frameSize), (int) (frameSize), bitmapbg
                      .getWidth() - (int) (frameSize), bitmapbg.getHeight()
                      - (int) (frameSize)), paint);

      canvas.drawBitmap(bitmap, frameSize + 1, frameSize + 1, paint);

      return bitmapbg;
	}
	
	
	
	
	
	public void setLock(Boolean b){		//������������
		focus(noteTxt,!b);
        noteTxt.setTextColor(b==true?getResources().getColor(R.color.darkgray):color);
        noteTxt.setBackgroundResource(b==true?R.color.gray:R.color.white);
	}
	public void share(){  
		
        Intent intent=getIntent();	
        @SuppressWarnings("unchecked")
		HashMap<String, Object> map=(HashMap<String, Object>) intent.getSerializableExtra("data");
        title=(String) map.get("title");
        content=(String) map.get("content");
        
    	
    		String share_content=title+'\n'+content; 		
    		Intent textIntent = new Intent(Intent.ACTION_SEND);
    		textIntent.setType("text/plain");
    		textIntent.putExtra(Intent.EXTRA_TEXT, share_content);
    		startActivity(Intent.createChooser(textIntent, "����"));
    	} 
	public void focus(EditText view,Boolean b){
		view.setCursorVisible(b);
		view.setFocusable(b);
	    view.setFocusableInTouchMode(b);
	    if(b==true)
	    	view.requestFocus();
	    else
	    	view.clearFocus();
		Spannable text = (Spannable)view.getText();
		Selection.setSelection(text, b?text.length():0);
	}
	private OnClickListener click=new OnClickListener(){//����¼�����
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.back_btn:
				back();
				break;
			case R.id.share_btn:
				share();
				break;
			case R.id.delete_btn:
				delete();
				break;
			case R.id.confirm_btn:
				save();
				break;
			}
		}
		
	};
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK){
			back();
			return true;
		}
		return false;
	}
	private void delete(){		//ɾ������
		View deleteView = View.inflate(this, R.layout.deletenote, null);
		delDialog=new Dialog(this,R.style.dialog);
		delDialog.setContentView(deleteView);
		Button yesBtn=(Button)deleteView.findViewById(R.id.delete_yes);
		Button noBtn=(Button)deleteView.findViewById(R.id.delete_no);
		TextView goneTimeTxt=(TextView)deleteView.findViewById(R.id.gone_time);
		TextView goneCountTxt=(TextView)deleteView.findViewById(R.id.gone_count);
		Cursor cursor=wn.rawQuery("select n_time,n_count from notes where id="+s_id,null);
		while(cursor.moveToNext()){
			int time=cursor.getInt(cursor.getColumnIndex("n_time"));
			int count=cursor.getInt(cursor.getColumnIndex("n_count"));
			String time_txt=time>0?String.valueOf(time):"n";
			String count_txt=count>0?String.valueOf(count):"n";
			goneTimeTxt.setText(R.string.left_txt);
			goneCountTxt.setText(time_txt+getResources().getString(R.string.word_time)+count_txt+getResources().getString(R.string.word_count));
		}
		yesBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				wn.execSQL("delete from notes where id="+s_id);
				Toast.makeText(Note.this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
				delDialog.dismiss();
				Intent intent=new Intent(Note.this,Main.class);
				startActivity(intent);
				finish();
			}
		});
		noBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				delDialog.dismiss();
			}
		});
		delDialog.show();
	}
	private void save()			//�������
	{
		String n_content=noteTxt.getText().toString().trim();
		Boolean n_lock=lock;
		if(n_content.trim().length()>0)
		{
			wn.execSQL("update notes set n_content=?,n_lock=? where id=?",new Object[]{n_content,n_lock,s_id});
			if(!n_content.equals(content))
			{
				Toast.makeText(Note.this, R.string.note_saved, Toast.LENGTH_SHORT).show();
			}
			Intent intent=new Intent(Note.this,Main.class);
			startActivity(intent);
			finish();
		}
		else
			Toast.makeText(Note.this, R.string.note_null, Toast.LENGTH_SHORT).show();
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
	private void back(){
		Intent intent=new Intent(Note.this,Main.class);
		startActivity(intent);
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}


