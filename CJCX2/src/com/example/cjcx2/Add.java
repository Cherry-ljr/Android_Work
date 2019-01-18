package com.example.cjcx2;


import org.litepal.crud.DataSupport;

import com.people.People;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Add extends Activity {
	private EditText nameText;
	private EditText stuNumberText;
	private EditText mathText;
	private EditText genderText;
	private EditText EnText;
	private EditText PyText;
	
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	            
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.add); 
	        
	        nameText = (EditText)findViewById(R.id.NAME);
	        stuNumberText = (EditText)findViewById(R.id.add_Stu);     
	        genderText = (EditText)findViewById(R.id.EditE1);
	        EnText=(EditText)findViewById(R.id.add_en);
	        PyText=(EditText)findViewById(R.id.add_py); 
	        mathText=(EditText)findViewById(R.id.add_Mach);
	        Button add = (Button)findViewById(R.id.add);
			add.setOnClickListener(ButtonListener);

}
	   OnClickListener ButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				People people = new People();
				
				people.Name = nameText.getText().toString();
				people.StuNumber = stuNumberText.getText().toString();
				people.Gender = genderText.getText().toString();
				people.Math=Integer.parseInt(mathText.getText().toString());
				people.En = Integer.parseInt(EnText.getText().toString());
				people.Py = Integer.parseInt(PyText.getText().toString());
				
				if (people.Name.equals("") || people.StuNumber.equals ("")
						|| people.Gender .equals("")|| people.En==0|| people.Py ==0||people.Math==0) {
					new Builder(Add.this).setMessage("信息不能为空!").show();
				}
			
				 else{
					 people.save();				 											
					 finish();				 		
				}
			};	    	

	    };
	 
	    
	    OnClickListener ButtonReturnListener = new OnClickListener() {
	    	@Override
			public void onClick(View v) {
	    		finish(); 	
	    }
	    };
	    }
	 
