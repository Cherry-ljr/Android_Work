package com.example.cjcx2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class AllSearch extends Activity {
	
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.query);
			
			 Button sort=(Button)findViewById(R.id.part_query);
		     Button query=(Button)findViewById(R.id.Special_query);
		    
		    
		     
		     sort.setOnClickListener(queryButtonListener);
		     query.setOnClickListener(sortButtonListener);
		    
		}
	    OnClickListener queryButtonListener = new OnClickListener() 	
	    {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
			       intent.setClass(AllSearch.this, Sort.class);
			       AllSearch.this.startActivity(intent);
			}
	    	
	    };
	    
	    
	    OnClickListener sortButtonListener = new OnClickListener() 	
	    {

			@Override
			public void onClick(View v) {	
				new AlertDialog.Builder(AllSearch.this).setTitle("点击你想查询的标准：")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[]{"按姓名", "按分数"}, 0, 
						new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub							
						dialog.dismiss();	
						if(which==0){
							Intent intent = new Intent();
						       intent.setClass(AllSearch.this, seachByName.class);
						       AllSearch.this.startActivity(intent);
							
						}
						if(which==1){
							
							Intent intent = new Intent();
						       intent.setClass(AllSearch.this, seachByStuNumber.class);
						       AllSearch.this.startActivity(intent);
							
						}
                   }					
				}).setNegativeButton("取消", null).show();			
			}
	    	
	    };
	    
	    
	    
	    
	    
	    
	

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

	}

