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

public class CompaniesFragment extends Fragment {

	
	ListView lv_joblistings;
	ArrayList<HashMap<String, String>> companies;
	CompanyListViewAdapter mAdapter;

	JSONObject jsonObject;
	JSONArray jsonArray;

	final String url_companies =  AppConstants.HOST_ADDRESS + "job_portal/get_employers_list.php";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_company_listings, container, false);
		
		
		companies = new ArrayList<HashMap<String, String>>();

		new DownloadCategories().execute();

		lv_joblistings = (ListView) rootView.findViewById(R.id.lv_joblisting);
		mAdapter = new CompanyListViewAdapter(getActivity().getApplicationContext(),
				companies);
		
		
		return rootView;
	}
	
	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_companies,
					getActivity());
			Log.e("companies", jsonObject.toString());

			try {
				int success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("companies");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);
						map.put("company_name",
								jsonObject.getString("company_name"));
						map.put("emp_id", jsonObject.getString("emp_id"));
						map.put("location", jsonObject.getString("location"));
						companies.add(map);
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

class CompanyListViewAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> data;
	Context mContext;
	LayoutInflater inflater;

	public CompanyListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.mContext = context;
		this.data = data;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
			convertView = inflater.inflate(R.layout.companies_list_item, null);

			holder.company = (TextView) convertView
					.findViewById(R.id.name);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

			holder.company.setText(data.get(position).get("company_name"));
			holder.location.setText(data.get(position).get("location"));
		
		
		
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Toast.makeText(mContext,
//						"employer id : " + data.get(position).get("emp_id"),
//						Toast.LENGTH_SHORT).show();
				Intent jobCategoryIntent = new Intent(mContext, JobByCompany.class);
				jobCategoryIntent.putExtra("emp_id", data.get(position).get("emp_id"));
				jobCategoryIntent.putExtra("company_name", data.get(position).get("company_name"));
				jobCategoryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(jobCategoryIntent);

			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView company,location;
	}

}

