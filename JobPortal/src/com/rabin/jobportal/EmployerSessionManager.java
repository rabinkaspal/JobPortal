package com.rabin.jobportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class EmployerSessionManager
{
  private static final String IS_LOGGED_IN = "is_logged_in";
  private static final String PREF_NAME = "EmployerSP";
  SharedPreferences.Editor EMP_SPEditor;
  SharedPreferences EmployerSp;
  int PRIVATE_MODE = 0;
  Context context;
  
  public EmployerSessionManager(Context mContext)
  {
    this.context = mContext;
    this.EmployerSp = mContext.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
    this.EMP_SPEditor = this.EmployerSp.edit();
    this.EMP_SPEditor.commit();
  }
  
  public void checkLogin()
  {
    if (!isLoggedIn()) {
      redirect_to(JobSeekerSignInRegisterFragmentActivity.class);
    }
  }
  public void createLoginSession()
  {
    this.EMP_SPEditor.putBoolean(IS_LOGGED_IN, true);
    this.EMP_SPEditor.commit();
  }
  
  
  public boolean isLoggedIn()
  {
    return this.EmployerSp.getBoolean(IS_LOGGED_IN, false);
  }
  
  public void logoutUser()
  {
    this.EMP_SPEditor.clear();
    this.EMP_SPEditor.commit();
  }
  
  	public void redirect_to(Class<?> c){
		Intent i = new Intent(context, c);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
//		((Activity) context).finish();
		
	}
}