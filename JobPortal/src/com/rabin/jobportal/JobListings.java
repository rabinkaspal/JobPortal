package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class JobListings extends BaseActivity {

	ListView lv_joblistings;
	ArrayList<HashMap<String, String>> categories;
	CategoryListViewAdapter mAdapter;

	JSONObject jsonObject;
	JSONArray jsonArray;

	final String url_categories = "http://10.0.2.2/job_portal/get_all_categories.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_job_category_listings);

		categories = new ArrayList<HashMap<String, String>>();

		new DownloadCategories().execute();

		lv_joblistings = (ListView) findViewById(R.id.lv_joblisting);
		mAdapter = new CategoryListViewAdapter(getApplicationContext(),
				categories);

		

	}

	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_categories,
					getApplicationContext());
			Log.e("asdf", jsonObject.toString());

			try {
				int success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("categories");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);
						map.put("category_name",
								jsonObject.getString("cat_name"));
						map.put("cat_id", jsonObject.getString("cat_id"));
						categories.add(map);
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lv_joblistings.setAdapter(mAdapter);
		}

	}

}

