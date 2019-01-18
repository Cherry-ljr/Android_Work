package com.example.cjcx2;



import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;


public class Search extends Activity {	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.qurry);	
		 Button Name=(Button)findViewById(R.id.AnName);
		 Button Stu=(Button)findViewById(R.id.AnStuNumber);
		 Name.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Search.this, seachByName.class);
					Search.this.startActivity(intent);
									
					}
				});
		 Stu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Search.this, seachByStuNumber.class);
					Search.this.startActivity(intent);
									
					}
				});
	
}
					
	}

	     