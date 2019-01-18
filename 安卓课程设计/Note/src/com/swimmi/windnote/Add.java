package com.swimmi.windnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
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

public class Add extends Activity {

	private LinearLayout add;			//布局容器
	private EditText noteTxt;			//内容框
	private EditText titleTxt;			//标题框
	private ImageButton backBtn;		//返回
	private ImageButton photoBtn;		//图片
	private ImageButton recordBtn;		//录音
	private ImageButton saveBtn;		//保存
	private TextView lengthTxt;			//长度标签
	private Button clearBtn;			//清空标签
	
	private boolean lock=false;			//是否加锁
	private int n_time=-1;				//限制保存天数
	private int n_count=-1;				//限制浏览次数
	private int color;					//当前皮肤颜色

	private SharedPreferences sp;		//数据存储
	private SQLiteDatabase wn;			//数据库
	
	//记录editText中的图片，用于单击时判断单击的是那一个图片
		private List<Map<String,String>> imgList = new ArrayList<Map<String,String>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		
		wn=Database(R.raw.windnote);		//连接数据库
        sp = getSharedPreferences("setting", 0);		//获取设置数据
        color=sp.getInt("color", getResources().getColor(R.color.blue));		//获取皮肤颜色
        
		add=(LinearLayout)findViewById(R.id.add);
		add.setBackgroundColor(color);//设置背景颜色，可以改变
		noteTxt=(EditText)findViewById(R.id.note_txt);//记事框
		titleTxt=(EditText)findViewById(R.id.title_txt);
		lengthTxt=(TextView)findViewById(R.id.length_txt);
		clearBtn=(Button)findViewById(R.id.clear_btn);
		noteTxt.addTextChangedListener(change);
		if(getIntent().hasExtra("title"))		//还原未保存的数据
		{
			Bundle data=getIntent().getExtras();
			if(data.containsKey("title"))
				titleTxt.setText(data.getString("title"));
			if(data.containsKey("content"))
				noteTxt.setText(data.getString("content"));
			if(data.containsKey("lock"))
				lock=data.getBoolean("lock");
		}
        
		backBtn = (ImageButton)findViewById(R.id.back_btn);
		saveBtn = (ImageButton)findViewById(R.id.save_btn);
		photoBtn = (ImageButton)findViewById(R.id.pho_btn);
		recordBtn = (ImageButton)findViewById(R.id.record_btn);

		clearBtn.setOnClickListener(new OnClickListener(){		//清空内容事件
			@Override
			public void onClick(View view) {
				View deleteView = View.inflate(Add.this, R.layout.deletenote, null);
				final Dialog clearDialog=new Dialog(Add.this,R.style.dialog);
				clearDialog.setContentView(deleteView);
				Button yesBtn=(Button)deleteView.findViewById(R.id.delete_yes);
				yesBtn.setText(R.string.clear_note);
				Button noBtn=(Button)deleteView.findViewById(R.id.delete_no);
				noBtn.setText(R.string.clear_cancel);
				yesBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						titleTxt.setText("");
						noteTxt.setText("");
						clearDialog.dismiss();
					}
				});
				noBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {
						clearDialog.dismiss();
					}
				});
				clearDialog.show();
			}
		});
		ImageButton[] btns={backBtn,photoBtn,recordBtn,saveBtn};
		for(ImageButton btn:btns)		//添加按钮事件监听
			btn.setOnClickListener(click);
		setLock(lock);
	}
	
	public void setLock(Boolean b){			//加锁（解锁）
      //  lockBtn.setImageResource(b==true?R.drawable.unlock:R.drawable.lock);
        EditText[] txts={titleTxt,noteTxt};
        for(EditText txt:txts){
    		focus(txt,!b);
	        txt.setTextColor(b==true?getResources().getColor(R.color.darkgray):color);
	        txt.setBackgroundResource(b==true?R.color.gray:R.color.white);
        }
        lengthTxt.setTextColor(b==true?getResources().getColor(R.color.darkgray):color);
        lengthTxt.setBackgroundResource(b==true?R.color.gray:R.color.white);
        clearBtn.setTextColor(b==true?getResources().getColor(R.color.darkgray):color);
        clearBtn.setEnabled(!b);
        clearBtn.setBackgroundResource(b==true?R.color.gray:R.color.white);
	}
	public static void focus(EditText view,Boolean b){		//失去（得到）焦点
		view.setCursorVisible(b);
		view.setFocusable(b);
	    view.setFocusableInTouchMode(b);
	    if(b==true)
	    	view.requestFocus();
	    else
	    	view.clearFocus();
		Spannable text = (Spannable)view.getText();
		Selection.setSelection(text, text.length());
	}
	private void save()			//保存记事
	{
		String n_title=titleTxt.getText().toString().trim();
		if(n_title.length()==0)
			n_title="无标题";
		String n_content=noteTxt.getText().toString().trim();
		Boolean n_lock=lock;
		if(n_content.trim().length()>0){
			wn.execSQL("insert into notes(n_title,n_content,n_time,n_count,n_lock) values(?,?,?,?,?)",new Object[]{n_title,n_content,n_time,n_count,n_lock});
			Toast.makeText(Add.this, R.string.note_saved, Toast.LENGTH_SHORT).show();
			sp.edit().remove("time").commit();
			sp.edit().remove("count").commit();
			Intent intent=new Intent(Add.this,Main.class);
			startActivity(intent);
			finish();
		}
		else
			Toast.makeText(Add.this, R.string.note_null, Toast.LENGTH_SHORT).show();
	}
	private void back(){		//返回主界面
		Intent intent=new Intent(Add.this,Main.class);
		String title=titleTxt.getText().toString().trim();
		String content=noteTxt.getText().toString().trim();
		if(title.length()>0||content.length()>0)		//传递未保存的数据
		{
			Bundle data=new Bundle();
			data.putString("title",title);
			data.putString("content",content);
			data.putBoolean("lock", lock);
			intent.putExtras(data);
		}
		startActivity(intent);
		finish();
	}
	private void recording(){		//录音界面
		Intent intent = new Intent(Add.this,Recording.class);
		startActivityForResult(intent, 2);
		
	}
	private void add_pho(){		//添加图片
	
		Intent intent = new Intent();
		//设定类型为image
		intent.setType("image/*");
		//设置action
		intent.setAction(Intent.ACTION_GET_CONTENT);
		//选中相片后返回本Activity
		startActivityForResult(intent, 1);	
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			//取得数据
			
			Uri uri = data.getData();
			ContentResolver cr = Add.this.getContentResolver();
			Bitmap bitmap = null;
			Bundle extras = null;
			//如果是选择照片
			if(requestCode == 1){
				//取得选择照片的路径
				
				String[] proj = { MediaStore.Images.Media.DATA };   
	            Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);   
				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
				actualimagecursor.moveToFirst();
	            String path = actualimagecursor.getString(actual_image_column_index);  
				try {
					//将对象存入Bitmap中 
					bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//插入图片 
				InsertBitmap(bitmap,480,path);
			}
			 if(requestCode == 2){
				 
				  extras = data.getExtras();
					String path = extras.getString("audio");
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.record_icon);
					//插入录音图标
					InsertBitmap(bitmap,200,path); 
				 				 
				 
			 }
			
			
	
		}	
		}
	

	//将图片等比例缩放到合适的大小并添加在EditText中
	void InsertBitmap(Bitmap bitmap,int S,String imgPath){
		
		bitmap = resize(bitmap,S);
		//添加边框效果
		bitmap = getBitmapHuaSeBianKuang(bitmap);
		//bitmap = addBigFrame(bitmap,R.drawable.line_age);
		final ImageSpan imageSpan = new ImageSpan(this,bitmap);
		SpannableString spannableString = new SpannableString(imgPath);
		spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
		//光标移到下一行
		//et_Notes.append("\n");
		Editable editable = noteTxt.getEditableText();
		int selectionIndex = noteTxt.getSelectionStart();
		spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
		
		//将图片添加进EditText中
		editable.insert(selectionIndex, spannableString);
		//添加图片后自动空出两行 
		noteTxt.append("\n");
		
		//用List记录该录音的位置及所在路径，用于单击事件
        Map<String,String> map = new HashMap<String,String>();
        map.put("location", selectionIndex+"-"+(selectionIndex+spannableString.length()));
        map.put("path", imgPath);
        imgList.add(map);
        
		
		//用List记录该录音的位置及所在路径，用于单击事件
        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("location", selectionIndex+"-"+(selectionIndex+spannableString.length()));
        map1.put("path", imgPath);
        imgList.add(map);
	}
	//等比例缩放图片
	private Bitmap resize(Bitmap bitmap,int S){
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		double partion = imgWidth*1.0/imgHeight;
		double sqrtLength = Math.sqrt(partion*partion + 1);
		//新的缩略图大小
		double newImgW = S*(partion / sqrtLength);
		double newImgH = S*(1 / sqrtLength);
		float scaleW = (float) (newImgW/imgWidth);
		float scaleH = (float) (newImgH/imgHeight);
		
		Matrix mx = new Matrix();
		//对原图片进行缩放
		mx.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
		return bitmap;
	}
	
	//给图片加边框，并返回边框后的图片
	public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();
 
        // 用来做底图
        Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
 
        // 设置底图为画布
        Canvas canvas = new Canvas(bitmapbg);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
 
        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
                / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
                / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);
 
        // 对相片大小处理(减去边框的大小)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
 
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);
 
        // 绘制底图边框
        canvas.drawRect(
                new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
                paint);
        // 绘制灰色边框
        paint.setColor(Color.BLUE);
        canvas.drawRect(
                new Rect((int) (frameSize), (int) (frameSize), bitmapbg
                        .getWidth() - (int) (frameSize), bitmapbg.getHeight()
                        - (int) (frameSize)), paint);
 
        canvas.drawBitmap(bitmap, frameSize + 1, frameSize + 1, paint);
 
        return bitmapbg;
	}
	
	
	
	
	public SQLiteDatabase Database(int raw_id) {		//数据库连接，从raw中读取文件
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
	private TextWatcher change=new TextWatcher(){		//文本改变监听
		@Override
		public void afterTextChanged(Editable s) {
			if(s.length()>0){
				lengthTxt.setVisibility(View.VISIBLE);
				clearBtn.setVisibility(View.VISIBLE);
				lengthTxt.setText(String.valueOf(s.length()));
			}
			else{
				lengthTxt.setVisibility(View.GONE);
				clearBtn.setVisibility(View.GONE);
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)		//返回事件重写
	{
		if(keyCode==KeyEvent.KEYCODE_BACK){
			back();
			return true;
		}
		return false;
	}
	private OnClickListener click=new OnClickListener(){		//点击事件监听

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.back_btn:
				back();
				break;
			case R.id.save_btn:
				save();
				break;
			case R.id.pho_btn:
				add_pho();
				break;
			case R.id.record_btn:
				recording();
				break;
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
