package com.example.cjcx2;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import com.people.People;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Display extends Activity  {

	ListView lv;
	List<People> list = new ArrayList<People>();
	
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.display);	
	
	lv = (ListView)findViewById(R.id.display1);
	 init();
	 
	lv.setOnItemClickListener(new AdapterView.OnItemClickListener(
			) {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(Display.this,Update.class);
					Bundle bun1 = new Bundle();
					
					bun1.putString("name",list.get(arg2).getName() );
					bun1.putSerializable("chengji", list.get(arg2));
					intent.putExtras(bun1);
					startActivity(intent);
				}
	});
	}
	private void init() {
		 list = new ArrayList<People>();
		 for(int i=0;i<DataSupport.findAll(People.class).size();i++)
		 {
			 People people=DataSupport.findAll(People.class).get(i);
			// if(people.getEn()>90)
			
			 list.add(people);
		 }
		 ArrayAdapter<People> adapter = new ArrayAdapter<People>(this, android.R.layout.simple_list_item_1, list);
		 lv.setAdapter(adapter);
	}
	}

