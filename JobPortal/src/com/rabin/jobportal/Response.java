package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Response extends DrawerActivity {

	ListView responseLv;
	ArrayList<HashMap<String, String>> responses;
	ResponseListViewAdpater mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.lv_responses);
		super.onCreate(savedInstanceState);
		
		new FetchResponseTask().execute();
		
		responses = new ArrayList<HashMap<String,String>>();
		
		responseLv = (ListView) findViewById(R.id.lv_responses);
		mAdapter = new ResponseListViewAdpater(getApplicationContext(), responses);
		responseLv.setAdapter(mAdapter);
		 Log.d("respose "	, responses.toString());
	
	}
	
	
	class FetchResponseTask extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog dialog;
		JSONObject jsonObject;
		JSONArray jsonArray;
		int success=0;
		
		String url_responses = AppConstants.HOST_ADDRESS + "job_portal/get_response.php?js_id="+RecentJobsFragment.session.getJsId();
		String message="";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Response.this);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setMessage("Loading responses....");
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = JSONParser.getJSONFromURL(url_responses, getApplicationContext());
			try {
				success = jsonObject.getInt("success");
			
					if (success==1) {
						jsonArray = jsonObject.getJSONArray("job_response");
						
						for (int i = 0; i < jsonArray.length(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							 
							 jsonObject = jsonArray.getJSONObject(i);
							 
							 map.put("job_id", jsonObject.getString("job_id"));
							 map.put("job_title", jsonObject.getString("job_title"));
							 map.put("status", jsonObject.getString("status"));
							 map.put("responseMsg", jsonObject.getString("responseMsg"));
							 
							 responses.add(map);
							 
							 Log.e("map", map.toString());
							
							 
						}
						
					}else{
						message=jsonObject.getString("message");
					}
			
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(success==0){
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
}




class ResponseListViewAdpater extends BaseAdapter{
	ArrayList<HashMap<String, String>> responseList;
	Context mContext;
	LayoutInflater inflater;
	public ResponseListViewAdpater(Context context, ArrayList<HashMap<String, String>> resp){
		this.mContext = context;
		this.responseList = resp;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return responseList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return responseList.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.response_list_item, null);

			holder.title = (TextView) convertView.findViewById(R.id.tvJobTitle);
			holder.responseMsg = (TextView) convertView.findViewById(R.id.tvResponseMsg);
			holder.status = (TextView) convertView.findViewById(R.id.tvStatus);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(responseList.get(position).get("job_title"));
		holder.responseMsg.setText(responseList.get(position).get("responseMsg"));
		
		int status = Integer.parseInt(responseList.get(position).get("status"));
		if(status==0){
			holder.status.setText("Declined");
			holder.status.setBackground(mContext.getResources()
					.getDrawable(R.drawable.response_declined_bg));
		}
		else{
			holder.status.setText("Accepted");
		holder.status.setBackground(mContext.getResources()
				.getDrawable(R.drawable.response_accepted_bg));
	}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView title,responseMsg,status;
	}
}
