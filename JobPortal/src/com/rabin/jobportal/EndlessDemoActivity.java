package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EndlessDemoActivity extends Activity {

	ListView lv_endless;
	ListViewAdapter mAdapter;
	ArrayList<HashMap<String, String>> data, moreData;
	Context mContext;
	LinearLayout loadingView;
	final String url_data = "http://10.0.2.2/job_portal/endless_demo.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_endless_demo);

		mContext = getApplicationContext();

		data = new ArrayList<HashMap<String, String>>();

		new FetchData().execute(url_data, "1");

		loadingView = (LinearLayout) findViewById(R.id.loadingView);
		loadingView.setVisibility(View.GONE);

		lv_endless = (ListView) findViewById(R.id.lv_endless);
		mAdapter = new ListViewAdapter(mContext, data);

		lv_endless.setAdapter(mAdapter);

		lv_endless.setOnScrollListener(new EndlessScrollListener(loadingView) {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				new FetchData().execute(url_data, Integer.toString(page));
			}
		});
	}

	// asynctask specific to this adapter
	// can be used for two different similar datasources
	//

	private class FetchData extends AsyncTask<String, Void, Void> {

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
			jsonObject = JSONParser.getJSONFromURL(params[0] + "?page="
					+ params[1], mContext);
			Log.e("url calling", params[0] + "?page=" + params[1]);
			// jsonObject.toString());

			try {
				success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("values");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);
						map.put("id", jsonObject.getString("id"));
						map.put("value", jsonObject.getString("value"));
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
				Toast.makeText(mContext, "End of List", Toast.LENGTH_SHORT)
						.show();
				loadingView.setVisibility(View.GONE);
			}
			mAdapter.data.addAll(moreData);
			mAdapter.notifyDataSetChanged();

		}

	}

}

// specific to this class
// simple adapter creating views
// no fancy stuffs

class ListViewAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<HashMap<String, String>> data;

	LayoutInflater inflater;

	public ListViewAdapter(Context mContext,
			ArrayList<HashMap<String, String>> mData) {
		this.mContext = mContext;
		this.data = mData;
		this.inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.lv_endless_list_item, null);

			holder.name = (TextView) convertView.findViewById(R.id.name);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(data.get(position).get("id") + " : "
				+ data.get(position).get("value"));

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext,
						"category id : " + data.get(position).get("id"),
						Toast.LENGTH_SHORT).show();

			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView name;
	}

}
