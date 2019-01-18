package com.example.cjcx2;


import android.content.Context;
import android.util.Log;
import org.litepal.crud.DataSupport;

import com.people.People;

import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

	public class Read{
	
	    private static final String TAG = "Read";

	    public static void readExecl(Context context)
	    {
	        try
	        {
	            InputStream is = context.getAssets().open("chengji.xls");
	            Workbook workbook = Workbook.getWorkbook(is);
	            workbook.getNumberOfSheets();
	            
	            Sheet sheet = workbook.getSheet(0);
	            
	            int rows = sheet.getRows();
	            for(int i = 1; i < rows; i++)
	            {
	                String stuNumber = sheet.getCell(0,i).getContents();
	                String name = sheet.getCell(1,i).getContents();
	                String gender = sheet.getCell(2,i).getContents();
	                String math = sheet.getCell(3,i).getContents();
	                String en = sheet.getCell(4,i).getContents();
	                String py = sheet.getCell(5,i).getContents();
	             
	                People people = new People();
	                people.setStuNumber(stuNumber);
	                people.setName(name);
	                people.setGender(gender);
	                people.setMath(Integer.parseInt(math));
	                people.setEn(Integer.parseInt(en));
	                people.setPy(Integer.parseInt(py));
	              
	               
	                if(DataSupport.where("StuNumber = ?", stuNumber).find(People.class).isEmpty())
	                {
	                    
	                    people.save();
	                    
	                }
	                else    
	                {
	                    people.updateAll("StuNumber = ?", stuNumber);
	                }

	                Log.d(TAG, i + "row" + stuNumber + name + gender + math + en + py + "\n");
	            }
	            workbook.close();
	        }
	        catch (Exception e)
	        {
	            Log.e(TAG, e.getMessage());
	        }
	    }
	}
