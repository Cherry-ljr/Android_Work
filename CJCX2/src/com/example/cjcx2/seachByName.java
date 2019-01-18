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

public class seachByName extends Activity {
	
	 private ListView lvCatalog;
	    private List<People> peoples;
	    EditText EName;
	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.query1);
		  EName=(EditText)findViewById(R.id.edt_name);

		 Button q1_sure=(Button)findViewById(R.id.q1_sure);
		 
		 q1_sure.setOnClickListener(new View.OnClickListener() {
				
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
			 String name=EName.getText().toString();
			 peoples = new ArrayList<People>();	 				
			 peoples=DataSupport.where("name like ?","%"+name+"%").find(People.class);								
			 ArrayAdapter<People> adapter = new ArrayAdapter<People>(this, android.R.layout.simple_list_item_1, peoples);
			 lvCatalog.setAdapter(adapter);
								 
			}
			 
			 
		}

