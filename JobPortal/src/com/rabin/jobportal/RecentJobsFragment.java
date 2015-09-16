package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecentJobsFragment extends Fragment {

	private String pageTitle;
	ListView lv_endless;
	RecentJobsAdapter mAdapter;
	ArrayList<HashMap<String, String>> data, moreData;
	Context mContext;
	LinearLayout loadingView;
	int run = 1;
	String url_data = AppConstants.HOST_ADDRESS + "job_portal/get_jobs.php?";
	TextView headerView;
	static String rejected_jobs = "";
	String url_rejectedJobs = AppConstants.HOST_ADDRESS
			+ "job_portal/get_rejected_jobs.php?js_id="
			+ JobsViewPagerFragmentActivity.sm.getJsId();

	static JobSeekerSessionManager session;

	public RecentJobsFragment newInstance(String title) {

		RecentJobsFragment fragment = new RecentJobsFragment();
		fragment.pageTitle = title;
		return fragment;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdapter.notifyDataSetChanged();
		// Toast.makeText(mContext, "resumed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setTitle(pageTitle);

		View rootView = inflater.inflate(R.layout.fragment_lv_recent_jobs,
				container, false);
		session = JobsViewPagerFragmentActivity.sm;
		mContext = getActivity();

		// session.clearAppliedJobs();
		// session.clearSavedJobs();

		new AppliedJobsSubmitTask(mContext).execute("0",
				session.getAppliedJobs());
		new AppliedJobsSubmitTask(mContext)
				.execute("1", session.getSavedJobs());

		data = new ArrayList<HashMap<String, String>>();

		loadingView = (LinearLayout) rootView.findViewById(R.id.loadingView);
		loadingView.setVisibility(View.INVISIBLE);

		if (ConnectionHelper.isNetworkAvailable(mContext)) {
			// new FetchJobs(run).execute(url_data, "1");
		} else {
			Toast.makeText(mContext, "not connected to internet",
					Toast.LENGTH_LONG).show();
		}

		lv_endless = (ListView) rootView.findViewById(R.id.lv_endless);
		mAdapter = new RecentJobsAdapter(mContext, data,
				R.layout.appied_saved_jobs_list_item, getClass()
						.getSimpleName());

		headerView = (TextView) rootView.findViewById(R.id.headerView);
		headerView.setVisibility(View.GONE);

		lv_endless.setAdapter(mAdapter);

		new GetRejectedJobs().execute();

		lv_endless.setOnScrollListener(new EndlessScrollListener(loadingView) {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				new FetchJobs(run++).execute(url_data, Integer.toString(page));
				Log.e("page on scrolling", Integer.toString(page));
			}
		});

		return rootView;

	}

	private class FetchJobs extends AsyncTask<String, Void, Void> {

		ProgressDialog dialog;
		JSONObject jsonObject;
		JSONArray jsonArray;
		int success = 0;
		int numRun = 1;
		String url = "";

		public FetchJobs(int numRuns) {
			this.numRun = numRuns;
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Loading...");
			if (numRun == 1)
				dialog.show();
			loadingView.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(String... params) {
			if (session.isLoggedIn()) {
				url = params[0] + "page=" + params[1] + "&js_id="
						+ session.getJsId();
				Log.d("url li", url);
			} else {
				url = params[0] + "page=" + params[1];
				Log.d("url not li", url);
			}

			moreData = new ArrayList<HashMap<String, String>>();
			jsonObject = JSONParser.getJSONFromURL(url, mContext);
			Log.e("url calling rcjbfg", url);
			// jsonObject.toString());

			try {
				success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("jobs");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);
						map.put("job_id", jsonObject.getString("job_id"));
						map.put("employer_id",
								jsonObject.getString("employer_id"));
						map.put("title", jsonObject.getString("title"));
						map.put("company_name",
								jsonObject.getString("company_name"));
						map.put("location", jsonObject.getString("location"));
						map.put("job_cat_id",
								jsonObject.getString("job_cat_id"));
						map.put("category", jsonObject.getString("category"));
						map.put("functional_area",
								jsonObject.getString("functional_area"));
						map.put("salary", jsonObject.getString("salary"));
						map.put("experience",
								jsonObject.getString("experience"));
						map.put("body", jsonObject.getString("body"));
						map.put("extra", jsonObject.getString("extra"));
						map.put("key_skills",
								jsonObject.getString("key_skills"));
						map.put("qualifications",
								jsonObject.getString("qualifications"));
						map.put("date_posted",
								jsonObject.getString("date_posted"));
						map.put("date_ending",
								jsonObject.getString("date_ending"));

						map.put("company_category",
								jsonObject.getString("company_category"));
						map.put("company_address",
								jsonObject.getString("company_address"));
						map.put("telephone", jsonObject.getString("telephone"));
						map.put("company_description",
								jsonObject.getString("company_description"));

						if (Integer.parseInt(params[1]) == 1) {
							data.add(map);
						} else {
							moreData.add(map);
						}
					}
				}
			} catch (Exception e) {
				Log.e("Error", "s" + e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (session.isLoggedIn()) {
				headerView.setVisibility(View.VISIBLE);
			} else {
				headerView.setVisibility(View.GONE);
			}

			if (success == 0) {
				loadingView.setVisibility(View.GONE);
			}
			mAdapter.data.addAll(moreData);
			mAdapter.notifyDataSetChanged();

		}

	}

	class GetRejectedJobs extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			JSONObject jObj = JSONParser.getJSONFromURL(url_rejectedJobs,
					mContext);

			try {
				rejected_jobs = jObj.getString("rejected_jobs");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}

}

class RecentJobsAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<HashMap<String, String>> data;

	LayoutInflater inflater;
	int layoutResId;
	String className;

	public RecentJobsAdapter(Context context,
			ArrayList<HashMap<String, String>> mData, int layoutResId,
			String classname) {
		this.mContext = context;
		this.data = mData;
		this.inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutResId = layoutResId;
		this.className = classname;
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
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(layoutResId, null);

			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.category = (TextView) convertView
					.findViewById(R.id.category);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);
			holder.company = (TextView) convertView.findViewById(R.id.company);
			holder.salary = (TextView) convertView.findViewById(R.id.salary);
			holder.date_posted = (TextView) convertView
					.findViewById(R.id.date_posted);

			holder.btnApplyFront = (Button) convertView
					.findViewById(R.id.btnApplyFront);
			holder.btnRemove = (Button) convertView
					.findViewById(R.id.btnRemove);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(data.get(position).get("title") + " : "
				+ data.get(position).get("job_id"));
		holder.category.setText(data.get(position).get("functional_area"));
		holder.salary.setText(data.get(position).get("salary"));
		holder.company.setText(data.get(position).get("company_name"));
		holder.location.setText(data.get(position).get("location"));
		holder.date_posted.setText(data.get(position).get("date_posted"));

		if (className.equals("AppliedJobsActivity")) {
			holder.btnApplyFront.setVisibility(View.GONE);

			if (RecentJobsFragment.rejected_jobs.contains(data.get(position)
					.get("job_id"))) {
				holder.btnRemove
						.setBackgroundColor(Color.parseColor("#ff0000"));
				holder.btnRemove.setVisibility(View.VISIBLE);
			} else {
				holder.btnRemove.setVisibility(View.GONE);
			}
		} else if (className.equals("SavedJobsActivity")) {
			holder.btnApplyFront.setVisibility(View.VISIBLE);
			holder.btnRemove.setVisibility(View.VISIBLE);
		} else {
			holder.btnApplyFront.setVisibility(View.VISIBLE);
			holder.btnRemove.setVisibility(View.GONE);
		}

		if (RecentJobsFragment.session.isLoggedIn()
				&& RecentJobsFragment.session.idInPref(
						data.get(position).get("job_id"), 0)) {
			holder.btnApplyFront.setText("Applied");
			holder.btnApplyFront.setBackground(mContext.getResources()
					.getDrawable(R.drawable.btn_normal));
			holder.btnApplyFront.setEnabled(false);
		} else {
			holder.btnApplyFront.setText("Apply");
			holder.btnApplyFront.setBackground(mContext.getResources()
					.getDrawable(R.drawable.btn_back));
			;
			holder.btnApplyFront.setEnabled(true);
		}

		holder.btnApplyFront.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (RecentJobsFragment.session.isLoggedIn()) {

					if (!RecentJobsFragment.rejected_jobs.contains(data.get(
							position).get("job_id"))) {

						if (!className.equals("SavedJobsActivity")) {

							if (RecentJobsFragment.session.getAppliedJobCount() < 10) {

								holder.btnApplyFront.setText("applied");
								holder.btnApplyFront.setBackground(mContext
										.getResources().getDrawable(
												R.drawable.btn_normal));
								;
								holder.btnApplyFront.setEnabled(false);
								RecentJobsFragment.session.addAppliedJob(data
										.get(position).get("job_id"));
								new AppliedJobsSubmitTask(mContext).execute(
										"0", RecentJobsFragment.session
												.getAppliedJobs());
							} else {
								Toast.makeText(mContext, "10 jobs reached.",
										Toast.LENGTH_SHORT).show();
							}

						} else {
							RecentJobsFragment.session.addAppliedJob(data.get(
									position).get("job_id"));
							RecentJobsFragment.session.removeSavedJob(data.get(
									position).get("job_id"));
							new AppliedJobsSubmitTask(mContext)
									.execute("0", RecentJobsFragment.session
											.getAppliedJobs());
							new AppliedJobsSubmitTask(mContext).execute("1",
									RecentJobsFragment.session.getSavedJobs());
							data.remove(position);
							notifyDataSetChanged();
						}
					} else {
						Toast.makeText(mContext,
								"Cannot apply. This job has already been declined for you.",
								Toast.LENGTH_SHORT).show();
					}
				} else {

					Intent i = new Intent(mContext,
							JobSeekerSignInRegisterFragmentActivity.class);
					i.putExtra("job_id", data.get(position).get("job_id"));
					mContext.startActivity(i);
				}

			}
		});

		holder.btnRemove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (className.equals("AppliedJobsActivity")) {

					RecentJobsFragment.session.removeAppliedJob(data.get(
							position).get("job_id"));

					new AppliedJobsSubmitTask(mContext).execute("0",
							RecentJobsFragment.session.getAppliedJobs());
					data.remove(position);

					if (data.size() == 0) {
						Toast.makeText(mContext,
								"There are no jobs to display.\n Go back now.",
								Toast.LENGTH_LONG).show();
					}

				} else if (className.equals("SavedJobsActivity")) {

					RecentJobsFragment.session.removeSavedJob(data
							.get(position).get("job_id"));

					new AppliedJobsSubmitTask(mContext).execute("1",
							RecentJobsFragment.session.getSavedJobs());
					data.remove(position);

					if (data.size() == 0) {
						Toast.makeText(mContext,
								"There are no jobs to display.\n Go back now.",
								Toast.LENGTH_LONG).show();
					}

				}
				notifyDataSetChanged();
				//
				// if(data.size()==0){
				// Builder dialog = new AlertDialog.Builder(mContext);
				// dialog.setTitle("List Empty");
				// dialog.setMessage("\nThere are no jobs to show.\nWould you like to go back?\n");
				// dialog.setPositiveButton("Yes", new
				// DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface arg0, int arg1) {
				//
				// Intent intent = new Intent(mContext,
				// JobsViewPagerFragmentActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// mContext.startActivity(intent);
				// ((Activity) mContext).finish();
				//
				// }
				// });
				//
				// dialog.setNegativeButton("Cancel", null);
				// dialog.show();
				// }
				//

			}
		});

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!className.equals("AppliedJobsActivity")
						|| !className.equals("SavedJobsActivity")) {
					Intent intent = new Intent(mContext,
							JobDetailActivity.class);
					intent.putExtra("job", data.get(position));
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					// ((Activity) mContext).finish();
				}
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView category;
		TextView location;
		TextView salary;
		TextView company;
		TextView date_posted;
		Button btnApplyFront, btnRemove;
	}

}
