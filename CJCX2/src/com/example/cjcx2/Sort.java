package com.example.cjcx2;


import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.people.People;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Sort extends Activity {
	  private ListView lvCatalog;
	    private List<People> peoples;
	    
	    Spinner spinner_gender;
		Spinner spinner_subject;
		Spinner spinner_sort;
		EditText edt_Minscore;
		EditText edt_Maxscore;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.sort);	
		 
		    spinner_gender=(Spinner)findViewById(R.id.spin_g);
			spinner_subject=(Spinner)findViewById(R.id.spin_S);
			spinner_sort=(Spinner)findViewById(R.id.spin_stuu);
			edt_Minscore=(EditText)findViewById(R.id.e_minscore);
			edt_Maxscore=(EditText)findViewById(R.id.e_maxscore);

		 
		 
		 Button sure1=(Button)findViewById(R.id.S_sure);   
		 sure1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				 setContentView(R.layout.display);						
					lvCatalog = (ListView) findViewById(R.id.display1);
					 init();
				 
				}
			});	 
		 }
		 private void init() {
			 
				String gender=spinner_gender.getSelectedItem().toString();
				String subject=spinner_subject.getSelectedItem().toString();
				String sort=spinner_sort.getSelectedItem().toString();
				int min=Integer.parseInt(edt_Minscore.getText().toString());
				int max=Integer.parseInt(edt_Maxscore.getText().toString());
				
			   peoples = new ArrayList<People>();
			   
			   if(sort.equals("升序")){
				   for(int i=0;i<DataSupport.findAll(People.class).size();i++)
				   {   People people=DataSupport.order("StuNumber asc").find(People.class).get(i);
				       if(gender.equals("任意"))				   
				       {if(subject.equals("高数")&&people.getMath()>=min&&people.getMath()<=max)
		 				  peoples.add(people);
				        if(subject.equals("英语")&&people.getEn()>=min&&people.getEn()<=max)
		 				   peoples.add(people);
				        if(subject.equals("物理")&&people.getPy()>=min&&people.getPy()<=max)
		 				   peoples.add(people);
				        }
				     if(gender.equals(people.getGender()))
				       {if(subject.equals("高数")&&people.getMath()>=min&&people.getMath()<=max)
		 				  peoples.add(people);
				        if(subject.equals("英语")&&people.getEn()>=min&&people.getEn()<=max)
		 				   peoples.add(people);
				        if(subject.equals("物理")&&people.getPy()>=min&&people.getPy()<=max)
		 				   peoples.add(people);
				        }
				     }
			      } 
			   
		   
				   if(sort.equals("降序"))
				   {
					   for(int i=0;i<DataSupport.findAll(People.class).size();i++)
						   
					   {   People people=DataSupport.order("StuNumber desc").find(People.class).get(i);
						  // People people=DataSupport.findAll(People.class).get(i);
					    if(gender.equals("任意"))				   
				          {if(subject.equals("高数")&&people.getMath()>=min&&people.getMath()<=max)
		 				      peoples.add(people);
				           if(subject.equals("英语")&&people.getEn()>=min&&people.getEn()<=max)
		 				      peoples.add(people);
				           if(subject.equals("物理")&&people.getPy()>=min&&people.getPy()<=max)
		 				      peoples.add(people);
				         }
					   
					   
					   if(gender.equals(people.getGender()))
					   {
					   if(subject.equals("高数")&&people.getMath()>=min&&people.getMath()<=max)
			 				 peoples.add(people);
					   if(subject.equals("英语")&&people.getEn()>=min&&people.getEn()<=max)
			 				 peoples.add(people);
					   if(subject.equals("物理")&&people.getPy()>=min&&people.getPy()<=max)
			 				 peoples.add(people);
					   }
				     } 			  			   
				   }		   
			   			 
			  
								 
			 ArrayAdapter<People> adapter = new ArrayAdapter<People>(this, android.R.layout.simple_list_item_1, peoples);
			 lvCatalog.setAdapter(adapter);
			
			 
		
	}
}
