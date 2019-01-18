package com.people;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

public class People  extends DataSupport implements Serializable{
	public String Name;
	public String StuNumber;
	//public String Terms;
	public String Gender;
	public int Math;
	public int En;
	public int Py;
	
	public People(){
		
	}
	
	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getStuNumber() {
		return StuNumber;
	}


	public void setStuNumber(String stuNumber) {
		StuNumber = stuNumber;
	}


	public String getGender() {
		return Gender;
	}


	public void setGender(String gender) {
		Gender = gender;
	}


	public int getMath() {
		return Math;
	}


	public void setMath(int math) {
		Math = math;
	}


	public int getEn() {
		return En;
	}


	public void setEn(int en) {
		En = en;
	}


	public int getPy() {
		return Py;
	}


	public void setPy(int py) {
		Py = py;
	}
	
	@Override
	public String toString(){
		
		return StuNumber + "  " + Name + "  " + Gender+" "+Math+" "+" "+ En +" "+Py;
	
}
	public int getsum(){
		int sum = 0;	
		sum = Math+En+Py;			
		return sum;
	}
	
	public float getaverage(){
		float average;
		int sum = getsum();
		average = sum/3;
		return average;
	}
	
}
