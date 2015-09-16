package com.rabin.jobportal;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class JobSeekerSessionManager
{
  private static final String IS_LOGGED_IN = "is_logged_in";
  public static final String KEY_APPLIED_JOB_ID = "applied_jobs";
  public static final String KEY_EMAIL = "email";
  public static final String KEY_JS_ID = "js_id";
  public static final String KEY_SAVED_JOB_ID = "saved_jobs";
  public static final String KEY_USER_NAME = "name";
  private static final String PREF_NAME = "JobSeekerSP";
  SharedPreferences.Editor JS_SPEditor;
  SharedPreferences JobSeekerSp;
  int PRIVATE_MODE = 0;
  Context context;
  
  public JobSeekerSessionManager(Context paramContext)
  {
    this.context = paramContext;
    this.JobSeekerSp = paramContext.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
    this.JS_SPEditor = this.JobSeekerSp.edit();
    this.JS_SPEditor.commit();
  }
  
  public void addAppliedJob(String paramString)
  {
    String str = this.JobSeekerSp.getString("applied_jobs", "");
    if (!idInPref(paramString, 0)) {
      this.JS_SPEditor.putString("applied_jobs", str + "," + paramString);
    }
    this.JS_SPEditor.commit();
  }
  
  public void addSavedJob(String paramString)
  {
    String str = this.JobSeekerSp.getString("saved_jobs", "");
    this.JS_SPEditor.putString("saved_jobs", str + "," + paramString);
    this.JS_SPEditor.commit();
  }
  
  public void checkLogin()
  {
    if (!isLoggedIn()) {
      redirect_to(JobSeekerSignInRegisterFragmentActivity.class);
    }
  }
  
  public void clearAppliedJobs()
  {
    this.JS_SPEditor.putString("applied_jobs", "");
    this.JS_SPEditor.commit();
  }
  
  public void clearSavedJobs()
  {
    this.JS_SPEditor.putString("saved_jobs", "");
    this.JS_SPEditor.commit();
  }
  
  public void createLoginSession(String paramString1, String paramString2, String paramString3)
  {
    this.JS_SPEditor.putBoolean(IS_LOGGED_IN, true);
    this.JS_SPEditor.putString("name", paramString1);
    this.JS_SPEditor.putString("email", paramString2);
    this.JS_SPEditor.putString("js_id", paramString3);
    this.JS_SPEditor.commit();
  }
  
  public String getAppliedJobs()
  {
    return this.JobSeekerSp.getString("applied_jobs", "");
  }
  
  public String getJsEmail()
  {
    return this.JobSeekerSp.getString("email", " ");
  }
  
  public String getJsId()
  {
    return this.JobSeekerSp.getString("js_id", "1");
  }
  
  public String getJsName()
  {
    return this.JobSeekerSp.getString("name", " ");
  }
  
  public String getSavedJobs()
  {
    return this.JobSeekerSp.getString("saved_jobs", "");
  }
  
  public HashMap<String, String> getUserDetails()
  {
    HashMap<String, String> localHashMap = new HashMap<String, String>();
    localHashMap.put("name", this.JobSeekerSp.getString("name", null));
    localHashMap.put("email", this.JobSeekerSp.getString("email", null));
    localHashMap.put("js_id", this.JobSeekerSp.getString("js_id", null));
    return localHashMap;
  }
  
  public boolean idInPref(String job_id, int prefType){
		boolean result=false;
		String str="";
		if(prefType == 0){
			str = JobSeekerSp.getString(KEY_APPLIED_JOB_ID, "");
		}else if(prefType ==1){
			str = JobSeekerSp.getString(KEY_SAVED_JOB_ID, "");
		}
		
		if(str != null && !str.isEmpty()){
			if(str.contains(job_id))
				result = true;
			else
				result = false;
		}
		
		return result;
	}
  
  public boolean isLoggedIn()
  {
    return this.JobSeekerSp.getBoolean(IS_LOGGED_IN, false);
  }
  
  public void logoutUser()
  {
    this.JS_SPEditor.clear();
    this.JS_SPEditor.commit();
  }
  
  	public void redirect_to(Class<?> c){
		Intent i = new Intent(context, c);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
//		((Activity) context).finish();
		
	}
  
  public void removeAppliedJob(String paramString)
  {
    String str = getAppliedJobs().replace(paramString, "").replace(",,", ",");
    int i = str.length();
    paramString = str;
    if (str.substring(i - 1, i).equals(",")) {
      paramString = str.substring(0, str.length() - 1);
    }
    this.JS_SPEditor.putString("applied_jobs", "");
    this.JS_SPEditor.putString("applied_jobs", paramString);
    this.JS_SPEditor.commit();
  }
  
  public void removeSavedJob(String paramString)
  {
    String str = getSavedJobs().replace(paramString, "").replace(",,", ",");
    int i = str.length();
    paramString = str;
    if (str.substring(i - 1, i).equals(",")) {
      paramString = str.substring(0, str.length() - 1);
    }
    this.JS_SPEditor.putString("saved_jobs", "");
    this.JS_SPEditor.putString("saved_jobs", paramString);
    this.JS_SPEditor.commit();
  }
  
  
  public int getAppliedJobCount(){
	  String jobs = getAppliedJobs();
	  int count = 0;

	  if(!jobs.equals("")){
	  jobs = jobs.substring(1);
	  String[] tokens = jobs.split(",",-1);
	  count = tokens.length;
	  }
	  return count;
	  
	  
  }
  
  public int getSavedJobCount(){
	  String jobs = getSavedJobs();
	  int count = 0;

	  jobs = jobs.substring(1);
	  String[] tokens = jobs.split(",",-1);
	  count = tokens.length;
	  
	  return count;
	  
	  
  }
  
  
}


/* Location:              C:\Users\Rabin\Desktop\job finder apk extracted\dex2jar-2.0\classes-dex2jar.jar!\com\rabin\jobportal\JobSeekerSessionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */

//package com.rabin.jobportal;
//
//import java.util.HashMap;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//
//public class JobSeekerSessionManager {
//
//	SharedPreferences JobSeekerSp;
//	Editor JS_SPEditor;
//	Context context;
//
//	int PRIVATE_MODE = 0;
//
//	// sharedpreferences name
//	private static final String PREF_NAME = "JobSeekerSP";
//	// all shared preferences key
//	private static final String IS_LOGGED_IN = IS_LOGGED_IN;
//	// user name: public to access from outside
//	public static final String KEY_USER_NAME = "name";
//	// email address
//	public static final String KEY_EMAIL = "email";
//	public static final String KEY_JS_ID = "js_id";
//
//	public static final String KEY_APPLIED_JOB_ID = "applied_jobs";
//	public static final String KEY_SAVED_JOB_ID = "saved_jobs";
//
//	public JobSeekerSessionManager(Context context) {
//		this.context = context;
//		JobSeekerSp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//		JS_SPEditor = JobSeekerSp.edit();
//		JS_SPEditor.commit();
//	}
//
//	public void createLoginSession(String name, String email, String js_id) {
//		JS_SPEditor.putBoolean(IS_LOGGED_IN, true);
//		JS_SPEditor.putString(KEY_USER_NAME, name);
//		JS_SPEditor.putString(KEY_EMAIL, email);
//		JS_SPEditor.putString(KEY_JS_ID, js_id);
//		JS_SPEditor.commit();
//
//	}
//	
//	/**
//	 * @summary add applied job to shared preferences
//	 * @param id - id of job to be added
//	 */
//	public void addAppliedJob(String id){
//		String str = JobSeekerSp.getString(KEY_APPLIED_JOB_ID, "");
//		JS_SPEditor.putString(KEY_APPLIED_JOB_ID, str+","+id);
//		JS_SPEditor.commit();
//	}
//	
//	
//	public String getAppliedJobs(){
//		return JobSeekerSp.getString(KEY_APPLIED_JOB_ID, "");
//	}
//	
//	public void clearAppliedJobs(){
//		JS_SPEditor.putString(KEY_APPLIED_JOB_ID, "");
//		JS_SPEditor.commit();
//	}
//	
//	
//	public void addSavedJob(String id){
//		String str = JobSeekerSp.getString(KEY_SAVED_JOB_ID, "");
//		JS_SPEditor.putString(KEY_SAVED_JOB_ID, str+","+id);
//		JS_SPEditor.commit();
//	}
//	
//	
//	public String getSavedJobs(){
//		return JobSeekerSp.getString(KEY_SAVED_JOB_ID, "");
//	}
//	
//	public void clearSavedJobs(){
//		JS_SPEditor.putString(KEY_SAVED_JOB_ID, "");
//		JS_SPEditor.commit();
//	}
//	
//	/**
//	 * 
//	 * @param id - id of job applied
//	 * @param prefType -- 0: applied_job 1: saved_jobs
//	 * @return
//	 */
//	public boolean idInPref(String job_id, int prefType){
//		boolean result=false;
//		String str="";
//		if(prefType == 0){
//			str = JobSeekerSp.getString(KEY_APPLIED_JOB_ID, "");
//		}else if(prefType ==1){
//			str = JobSeekerSp.getString(KEY_SAVED_JOB_ID, "");
//		}
//		
//		if(str != null && !str.isEmpty()){
//			if(str.contains(job_id))
//				result = true;
//			else
//				result = false;
//		}
//		
//		return result;
//	}
//	
//	
//	
//	public String getJsId(){
//		return JobSeekerSp.getString(KEY_JS_ID, "1");
//	}
//	public String getJsName(){
//		return JobSeekerSp.getString(KEY_USER_NAME, " ");
//	}
//	public String getJsEmail(){
//		return JobSeekerSp.getString(KEY_EMAIL, " ");
//	}
//
//	// get stored session data.. returned in a hashmap
//	public HashMap<String, String> getUserDetails() {
//		HashMap<String, String> user = new HashMap<String, String>();
//		user.put(KEY_USER_NAME, JobSeekerSp.getString(KEY_USER_NAME, null));
//		user.put(KEY_EMAIL, JobSeekerSp.getString(KEY_EMAIL, null));
//		user.put(KEY_JS_ID, JobSeekerSp.getString(KEY_JS_ID, null));
//
//		return user;
//	}
//
//	// check for logged in user
//	// get login state
//	public boolean isLoggedIn() {
//		return JobSeekerSp.getBoolean(IS_LOGGED_IN, false);
//
//	}
//	
//
//	//check whether user is logged in or not
//	//if not redirect to login page
//	//else redirect to afterLoggedInPage
//	
//	public void checkLogin(){
//		if(!this.isLoggedIn()){
//			redirect_to(JobSeekerSignInRegisterFragmentActivity.class);
//		}
//	}
//	
//	public void redirect_to(Class<?> c){
//		Intent i = new Intent(context, c);
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(i);
////		((Activity) context).finish();
//		
//	}
//	
//
//	//clear session details to log out user
//	public void logoutUser(){
//		JS_SPEditor.clear();
//		JS_SPEditor.commit();
//		//redirect to login activity page or just restrict privileged access
//	}
//
//}
