package com.rabin.jobportal;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class AppliedJobsSubmitTask extends AsyncTask<String, Void, Void>{
	
	Context mContext;
	
	public AppliedJobsSubmitTask(Context context){
		this.mContext = context;
	}
	

	JSONObject jsonObject;
	int success;
	String url_saveAppliedJobs = AppConstants.HOST_ADDRESS + "job_portal/saveAppliedJobs.php?js_id="+RecentJobsFragment.session.getJsId();
	String url_saveSavedJobs = AppConstants.HOST_ADDRESS + "job_portal/saveSavedJobs.php?js_id="+RecentJobsFragment.session.getJsId();
	
	@Override
	protected Void doInBackground(String... param) {
		if (Integer.parseInt(param[0]) == 0) {
			jsonObject = JSONParser.getJSONFromURL(url_saveAppliedJobs + "&jobs="+param[1], mContext);
		}else if (Integer.parseInt(param[0]) == 1) {
			jsonObject = JSONParser.getJSONFromURL(url_saveSavedJobs + "&jobs="+param[1], mContext);
		}
		
		try {
			success = jsonObject.getInt("success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (success == 1) {
			//Toast.makeText(mContext, "applied", Toast.LENGTH_SHORT).show();
		}
	}
	
}