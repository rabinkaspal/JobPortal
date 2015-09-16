package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class JobCategoryFragment extends Fragment{

	ListView lv_joblistings;
	static ArrayList<HashMap<String, String>> categories;
	CategoryListViewAdapter mAdapter;

	JSONObject jsonObject;
	JSONArray jsonArray;
	

	final String url_categories =  AppConstants.HOST_ADDRESS + "job_portal/get_all_categories.php";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_job_category_listings, container, false);
		
		categories = new ArrayList<HashMap<String, String>>();
		
			new DownloadCategories().execute();
		
		lv_joblistings = (ListView) rootView.findViewById(R.id.lv_joblisting);
		mAdapter = new CategoryListViewAdapter(getActivity().getApplicationContext(),
				categories);

		return rootView;
	}
	
	
	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_categories,
					getActivity());
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

class CategoryListViewAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> data;
	Context mContext;
	LayoutInflater inflater;

	public CategoryListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.mContext = context;
		this.data = data;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size() - 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.joblisting_list_item, null);

			holder.category = (TextView) convertView
					.findViewById(R.id.name);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

			holder.category.setText(data.get(position).get("category_name"));
		
		
		
		
		
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Toast.makeText(mContext,
//						"category id : " + data.get(position).get("cat_id"),
//						Toast.LENGTH_SHORT).show();
				Intent jobCategoryIntent = new Intent(mContext, JobByCategory.class);
				jobCategoryIntent.putExtra("cat_id", data.get(position).get("cat_id"));
				jobCategoryIntent.putExtra("category_name", data.get(position).get("category_name"));
				jobCategoryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(jobCategoryIntent);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView category;
	}

}

