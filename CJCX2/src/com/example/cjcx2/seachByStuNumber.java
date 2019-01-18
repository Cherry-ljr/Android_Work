package com.example.cjcx2;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.people.People;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class seachByStuNumber extends Activity {
	
		private ListView lvCatalog;
		private List<People> peoples;   
	    
	    EditText Esco;
	    Spinner spinner_gen;
	    Spinner spinner_sub;
	
	 
	    public void onCreate(Bundle savedInstanceState)
	    {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.query2);
		    Button q_sure2=(Button)findViewById(R.id.q2_sure);
		    Esco=(EditText)findViewById(R.id.edt_grades);
		    spinner_gen=(Spinner)findViewById(R.id.s_Gen);
		    spinner_sub=(Spinner)findViewById(R.id.S_Sub);
							 
		    q_sure2.setOnClickListener(new View.OnClickListener() {
						
		    	@Override
		    	public void onClick(View arg0) {
		    		// TODO Auto-generated method stub
		    		setContentView(R.layout.display);						
		    		lvCatalog = (ListView) findViewById(R.id.display1);
		    		init();									
				}
			});
			}

	    private void init() 
	    {
					  
	    	String gender1=spinner_gen.getSelectedItem().toString();
	    	String sub1=spinner_sub.getSelectedItem().toString();
	    	int scores=Integer.parseInt(Esco.getText().toString());
	    	peoples = new ArrayList<People>();
						   					 
	    	for(int i=0;i<DataSupport.findAll(People.class).size();i++)
	    	{   
	    		People people=DataSupport.findAll(People.class).get(i);
							   
	    		if(gender1.equals("任意"))
	    		{
	    			if(sub1.equals("高数") && people.getMath()==scores)					
	    				peoples.add(people);						  
	    			if(sub1.equals("英语") && people.getEn()==scores)					
	    				peoples.add(people); 
	    			if(sub1.equals("物理") && people.getPy()==scores)					
	    				peoples.add(people);
	    		}	
	    		if(gender1.equals(people.getGender()))
	    		{
	    			if(sub1.equals("高数") && people.getMath()==scores)					
	    				peoples.add(people);						  
	    			if(sub1.equals("英语") && people.getEn()==scores)					
	    				peoples.add(people); 
	    			if(sub1.equals("物理") && people.getPy()==scores)					
	    				peoples.add(people);
	    			
	    		}
	    	}
						       						 
	    	ArrayAdapter<People> adapter = new ArrayAdapter<People>(this, android.R.layout.simple_list_item_1, peoples);
	    	lvCatalog.setAdapter(adapter);
	    }
	}
