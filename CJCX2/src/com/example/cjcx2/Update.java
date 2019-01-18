package com.example.cjcx2;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.people.People;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Update extends Activity {
	 private ListView lvCatalog;
	 private List<People> peoples;

	TextView tv_Name ; 
	TextView tv_Stu;
 	TextView tv_Gender;
 	
 	TextView tv_score1;
	TextView tv_sumandave1;
	TextView tv_score2;
	TextView tv_sumandave2;
	TextView tv_score3;
	TextView tv_sumandave3;
	
	EditText edt_newscore;
 	Spinner spi_subjects;
	Button btn_update;
 	
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.update);	
	
	Bundle intentTogetinfo = this.getIntent().getExtras();
	final People stu = (People) this.getIntent().getSerializableExtra("chengji");
	final String name = intentTogetinfo.getString("name");
	
	tv_Name = (TextView)findViewById(R.id.show_name);
	tv_Stu=(TextView)findViewById(R.id.show_stu);
	tv_Gender = (TextView)findViewById(R.id.show_gender);
	
	tv_score1= (TextView)findViewById(R.id.show_grades);
	tv_sumandave1= (TextView)findViewById(R.id.show_sumAndave);
	spi_subjects=(Spinner)findViewById(R.id.choose_subject);
	
	edt_newscore= (EditText)findViewById(R.id.edt_score);
	btn_update= (Button)findViewById(R.id.btn_update);

	tv_Name.setText("姓名："+name);
	tv_Stu.setText("学号:"+stu.getStuNumber());
	tv_Gender.setText("性别:"+stu.getGender());
	tv_score1.setText("高数:"+stu.getMath()+"英语:"+stu.getEn()+"物理:"+stu.getPy());
	tv_sumandave1.setText("总分:"+stu.getsum()+"平均分:"+stu.getaverage());
	
	btn_update.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
		
			// TODO Auto-generated method stub
			setContentView(R.layout.display);						
    		lvCatalog = (ListView) findViewById(R.id.display1);
    		
			final int newscore=Integer.parseInt(edt_newscore.getText().toString());
			String sub=spi_subjects.getSelectedItem().toString();
			
			setContentView(R.layout.display);						
			lvCatalog = (ListView) findViewById(R.id.display1);
			
			peoples = new ArrayList<People>();			
			
			 for(int i=0;i<DataSupport.findAll(People.class).size();i++)
			 {
				 People people=DataSupport.findAll(People.class).get(i);
				 
				 if(sub.equals("高数")&&people.getStuNumber().equals(stu.getStuNumber()))
				 { 
					 people.setMath(newscore);
					 people.updateAll("StuNumber = ?", people.getStuNumber());
					
				 }
				 if(sub.equals("英语")&&people.getStuNumber().equals(stu.getStuNumber()))
				 { 
					 people.setEn(newscore);	
					 people.updateAll("StuNumber=?",people.getStuNumber());
					
				 }
				 if(sub.equals("物理")&&people.getStuNumber().equals(stu.getStuNumber()))
				 {  
					 people.setPy(newscore);
					 people.updateAll("StuNumber=?",people.getStuNumber());
					 
				 }
				 peoples.add(people);
			 }		
			init();	
			
			 }
						
		});
	
		}
			
	public void init(){
		 
		 ArrayAdapter<People> adapter = new ArrayAdapter<People>(this, android.R.layout.simple_list_item_1, peoples);
		 lvCatalog.setAdapter(adapter);
	}

}

