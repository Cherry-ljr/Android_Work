package com.example.cjcx2;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;

public class Delete extends Activity {
	
	private EditText stuNumber;
	  private List<People> peoples;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete);
		stuNumber = (EditText) findViewById(R.id.Ustu);
		Button delete = (Button) findViewById(R.id.add);	
		delete.setOnClickListener(new ButtonListener());
		
}
	class ButtonListener implements OnClickListener {		
		public void onClick(View v) {
			String stu = stuNumber.getText().toString();
			   if (stu.equals("")) {
				new Builder(Delete.this).setMessage("学号不能为空").show();
			   }
			   else{	 				
					DataSupport.deleteAll(People.class, "StuNumber=?",stu);	
									  
					for(int i=0;i<DataSupport.findAll(People.class).size();i++)
			    	{   
			    		People people=DataSupport.findAll(People.class).get(i);
			    		if(people.getStuNumber()==stu)
			    			buildDialog1();	
			    		else buildDialog2();	
			    	}				   
								   
				   }
			   
	       }
		private void buildDialog2() {
			Builder builder = new Builder(Delete.this);
			builder.setTitle("删除成功！");
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							   finish();
						}
					});
			builder.setPositiveButton("继续删除", null);
			builder.show();
		}
		private void buildDialog1() {
			Builder builder = new Builder(Delete.this);
			builder.setTitle("删除失败？");
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							finish();
						}

					});
		}
}
}
