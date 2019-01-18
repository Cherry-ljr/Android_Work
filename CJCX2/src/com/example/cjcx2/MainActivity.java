package com.example.cjcx2;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 Read.readExecl(MainActivity.this);
		 Button add=(Button)findViewById(R.id.addButton);
	     Button query=(Button)findViewById(R.id.searchButton);
	     Button update=(Button)findViewById(R.id.updateButton);
	     Button delete=(Button)findViewById(R.id.deleteBUtton);
	     ButtonListener buttonListener =new ButtonListener();
	     
	     add.setOnClickListener(buttonListener);
	     query.setOnClickListener(buttonListener);
	     update.setOnClickListener(buttonListener);
	     delete.setOnClickListener(buttonListener);
	}
	
	
	
	 class ButtonListener implements OnClickListener {
			public void onClick(View v) {
				int id = v.getId();
				Intent intent = new Intent();
				switch (id) {
				
				case R.id.addButton:
					intent.setClass(MainActivity.this, Add.class);
					MainActivity.this.startActivity(intent);
					break;
					
				case R.id.searchButton:
					intent.setClass(MainActivity.this, AllSearch.class);		
					MainActivity.this.startActivity(intent);
					break;
					
				case R.id.deleteBUtton:					
					intent.setClass(MainActivity.this, Delete.class);
					MainActivity.this.startActivity(intent);
					break;
				case R.id.updateButton:					
					intent.setClass(MainActivity.this, Display.class);
					MainActivity.this.startActivity(intent);
					break;	
					}
				}
			}
	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
