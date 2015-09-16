package com.rabin.jobportal;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
	
	  private static final String USER_TYPE = "user_type";
	  private static final String PREF_NAME = "Session";
	  SharedPreferences.Editor SPEditor;
	  SharedPreferences sharedPref;
	  int PRIVATE_MODE = 0;
	  Context context;
	  
	  public SessionManager(Context mContext)
	  {
	    this.context = mContext;
	    this.sharedPref = mContext.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
	    this.SPEditor = this.sharedPref.edit();
	    this.SPEditor.commit();
	  }
	  
	  
	  public void loginJobSeeker(){
		  this.SPEditor.putString(USER_TYPE, "0");
		  this.SPEditor.commit();
	  }
	  
	  
	  public void loginEmployer(){
		  this.SPEditor.putString(USER_TYPE, "1");
		  this.SPEditor.commit();
	  }
	  
	  public void logoutUser(){
		  this.SPEditor.putString(USER_TYPE, "0");
		  this.SPEditor.commit();
	  }
	  
	  
	  
	  public String getUserType(){
		  return this.sharedPref.getString(USER_TYPE, "0");
	  }

}
