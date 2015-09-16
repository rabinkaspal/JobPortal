package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JobByCategory extends DrawerActivity {

	String category_name;
	String cat_id;
	TextView txt_category_name;
	ListView lv_jobs;
	RecentJobsAdapter mAdapter;
	
	ArrayList<HashMap<String, String>> data, moreData;
	Context mContext;
	LinearLayout loadingView;
	final String url_data =  AppConstants.HOST_ADDRESS + "job_portal/get_jobs_by_category.php?";
	//page=1&cat_id=15
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdapter.notifyDataSetChanged();
		//Toast.makeText(mContext, "resumed", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.lv_job_by_category);
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		
		Intent intent = getIntent();
		category_name = intent.getStringExtra("category_name");
		cat_id = intent.getStringExtra("cat_id");
		
		
		txt_category_name = (TextView) findViewById(R.id.category_name);
		txt_category_name.setText(category_name + " Jobs");

		
		mContext = getApplicationContext();

		Toast.makeText(mContext, cat_id, Toast.LENGTH_SHORT).show();
		
		
		data = new ArrayList<HashMap<String, String>>();

		if (ConnectionHelper.isNetworkAvailable(mContext)) {
		//	new FetchJobs().execute(url_data, "1", cat_id);
		}else{
			Toast.makeText(mContext, "no wifi", Toast.LENGTH_SHORT).show();
		}
		
		loadingView = (LinearLayout) findViewById(R.id.loadingView);
		loadingView.setVisibility(View.GONE);

		
		lv_jobs = (ListView) findViewById(R.id.lv_jobs);
		mAdapter = new RecentJobsAdapter(mContext, data,R.layout.appied_saved_jobs_list_item, getClass().getSimpleName());
		
		lv_jobs.setAdapter(mAdapter);
		
		lv_jobs.setOnScrollListener(new EndlessScrollListener(loadingView) {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				new FetchJobs().execute(url_data, Integer.toString(page), cat_id);
			}
		});
		
	}

	
	
	private class FetchJobs extends AsyncTask<String, Void, Void> {

		ProgressDialog dialog;
		JSONObject jsonObject;
		JSONArray jsonArray;
		int success = 0;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Loading...");
		}

		@Override
		protected Void doInBackground(String... params) {

			moreData = new ArrayList<HashMap<String, String>>();
			jsonObject = JSONParser.getJSONFromURL(params[0] + "page="
					+ params[1] + "&cat_id=" + params[2], mContext);
			Log.e("url calling", params[0] + "page=" + params[1] + "&cat_id=" + params[2]);
			// jsonObject.toString());

			try {
				success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("jobs");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);
						map.put("job_id", jsonObject.getString("job_id"));
						map.put("employer_id", jsonObject.getString("employer_id"));
						map.put("title", jsonObject.getString("title"));
						map.put("company_name", jsonObject.getString("company_name"));
						map.put("location", jsonObject.getString("location"));
						map.put("job_cat_id", jsonObject.getString("job_cat_id"));
						map.put("category", jsonObject.getString("category"));
						map.put("functional_area", jsonObject.getString("functional_area"));
						map.put("salary", jsonObject.getString("salary"));
						map.put("experience", jsonObject.getString("experience"));
						map.put("body", jsonObject.getString("body"));
						map.put("extra", jsonObject.getString("extra"));
						map.put("key_skills", jsonObject.getString("key_skills"));
						map.put("qualifications", jsonObject.getString("qualifications"));
						map.put("date_posted", jsonObject.getString("date_posted"));
						map.put("date_ending", jsonObject.getString("date_ending"));
						
						map.put("company_category", jsonObject.getString("company_category"));
						map.put("company_address", jsonObject.getString("company_address"));
						map.put("telephone", jsonObject.getString("telephone"));
						map.put("company_description", jsonObject.getString("company_description"));
						
						
						if (Integer.parseInt(params[1]) == 1) {
							data.add(map);
						} else {
							moreData.add(map);
						}
					}
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (success == 0) {
//				Toast.makeText(mContext, "End of List", Toast.LENGTH_SHORT)
//						.show();
				loadingView.setVisibility(View.GONE);
			}
			mAdapter.data.addAll(moreData);
			mAdapter.notifyDataSetChanged();

		}

	}
	
}
