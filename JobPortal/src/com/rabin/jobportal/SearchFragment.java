package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rabin.jobportal.RangeSeekBar.OnRangeSeekBarChangeListener;

public class SearchFragment extends Fragment {

	public ArrayList<HashMap<String, String>> categories;
	JSONObject jsonObject;
	JSONArray jsonArray;
	final String url_categories = AppConstants.HOST_ADDRESS
			+ "job_portal/get_all_categories.php";

	final String url_searchJobs = AppConstants.HOST_ADDRESS
			+ "job_portal/search_jobs.php";
	ArrayList<NameValuePair> search_params;

	Button btnSelect, btnSearchJobs;
	static int selectedPos = 0;
	String cat_id = "0";
	TextView salaryMin, salaryMax, expMin, expMax, salaryRange, expRange;

	EditText etKeyskills, etLocation;

	String strKeyword, strLocation, strCategory = "";
	int minSal = 5000, maxSal = 100000, minExp = 0, maxExp = 12;
	int rowCount;
	String strUrlSearchJobs="";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_activity_search,
				container, false);

		btnSelect = (Button) rootView.findViewById(R.id.btnSelect);
		btnSearchJobs = (Button) rootView.findViewById(R.id.btnSearchJob);

		salaryMin = (TextView) rootView.findViewById(R.id.salaryMin);
		salaryMax = (TextView) rootView.findViewById(R.id.salaryMax);
		salaryRange = (TextView) rootView.findViewById(R.id.salaryRange);
		expMin = (TextView) rootView.findViewById(R.id.expMin);
		expMax = (TextView) rootView.findViewById(R.id.expMax);
		expRange = (TextView) rootView.findViewById(R.id.expRange);

		etKeyskills = (EditText) rootView.findViewById(R.id.etKeyskill);
		etLocation = (EditText) rootView.findViewById(R.id.etLocation);

		categories = new ArrayList<HashMap<String, String>>();

		new DownloadCategories().execute();

		// create RangeSeekBar as Integer range between 20 and 75
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(5000, 100000,
				getActivity());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				salaryMin.setText(Integer.toString(Math.round(minValue / 1000) * 1000));
				salaryMax.setText(Integer.toString(Math.round(maxValue / 1000) * 1000));

				salaryRange.setText("Rs. "
						+ Integer.toString(Math.round(minValue / 1000) * 1000)
						+ " - "
						+ Integer.toString(Math.round(maxValue / 1000) * 1000));

				minSal = Math.round(minValue / 1000) * 1000;
				maxSal = Math.round(maxValue / 1000) * 1000;

			}
		});

		// add RangeSeekBar to pre-defined layout
		ViewGroup layout1 = (ViewGroup) rootView.findViewById(R.id.layout1);
		layout1.removeView((View) layout1.getParent());
		layout1.addView(seekBar);

		// create RangeSeekBar as Integer range between 20 and 75
		RangeSeekBar<Integer> seekBar2 = new RangeSeekBar<Integer>(0, 10,
				getActivity());
		seekBar2.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				expMin.setText(Integer.toString(minValue));
				expMax.setText(Integer.toString(maxValue));
				expRange.setText(Integer.toString(minValue) + " - "
						+ Integer.toString(maxValue) + " years");

				minExp = minValue;
				maxExp = maxValue;

			}
		});

		// add RangeSeekBar to pre-defined layout
		ViewGroup layout2 = (ViewGroup) rootView.findViewById(R.id.layout2);
		layout2.removeView((View) layout2.getParent());
		layout2.addView(seekBar2);

		/*
		 * // create RangeSeekBar as Date range between 1950-12-01 and now Date
		 * minDate = null; try { minDate = new
		 * SimpleDateFormat("yyyy-MM-dd").parse("1950-12-01"); } catch
		 * e.printStackTrace(); } Date maxDate = new Date(); RangeSeekBar<Long>
		 * seekBar2 = new RangeSeekBar<Long>(minDate.getTime(),
		 * maxDate.getTime(), getActivity());
		 * seekBar2.setOnRangeSeekBarChangeListener(new
		 * OnRangeSeekBarChangeListener<Long>() {
		 * 
		 * @Override public void onRangeSeekBarValuesChanged(RangeSeekBar<?>
		 * bar, Long minValue, Long maxValue) { // handle changed range values
		 * Log.i("seekbar2", "User selected new date range: MIN=" + new
		 * Date(minValue) + ", MAX=" + new Date(maxValue)); } });
		 * 
		 * // add RangeSeekBar to pre-defined layout ViewGroup layout2 =
		 * (ViewGroup) rootView.findViewById(R.id.layout2);
		 * layout2.removeView((View) layout2.getParent());
		 * layout2.addView(seekBar2);
		 */

		btnSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				View dialogView;
				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				dialogView = inflater.inflate(
						R.layout.fragment_job_category_listings, null);

				ListView lv = (ListView) dialogView
						.findViewById(R.id.lv_joblisting);
				lv.setAdapter(new SpinnerDataAdapter(getActivity(), categories));

				AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				alert.setCancelable(true).setTitle("Choose an item");
				alert.setView(dialogView);

				alert.setCancelable(false);
				alert.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								btnSelect.setText(categories.get(selectedPos)
										.get("category_name"));
								cat_id = categories.get(selectedPos).get(
										"cat_id");
								dialog.cancel();
							}
						});

				alert.show();
			}
		});

		btnSearchJobs.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// edt.setBackgroundColor(Color.parseColor("#FA8072"));
				// edt.setError("Should be 10 digits.");

				strCategory = categories.get(selectedPos).get("category_name");

				strKeyword = etKeyskills.getText().toString().trim()
						.replace(",", " ");
				strLocation = etLocation.getText().toString().trim()
						.replace(",", " ");

				if (strKeyword.equalsIgnoreCase("")
						|| strLocation.equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Fill in all the fields",
							Toast.LENGTH_SHORT).show();
				} else {
					
					strCategory = strCategory.replace("/", " ");
					
					strKeyword = strKeyword + " " + strLocation + " " + strCategory;
//					Toast.makeText(
//							getActivity(),
//							"Search String: " + strKeyword + "\nLocation: "
//									+ strLocation + "\n Category: "
//									+ strCategory + "\nsalary range: " + minSal
//									+ " - " + maxSal + "\n experience range: "
//									+ minExp + " - " + maxExp + "",
//							Toast.LENGTH_SHORT).show();

					strUrlSearchJobs = url_searchJobs + "?strKeyword="
							+ strKeyword + "&minSal=" + minSal
							+ "&maxSal=" + maxSal + "&minExp=" + minExp + "&maxExp="
							+ maxExp;
					
//					Toast.makeText(getActivity(), strUrlSearchJobs, Toast.LENGTH_SHORT).show();
					
					new SearchJobTask().execute(strKeyword, Integer.toString(minSal), Integer.toString(maxSal),
							Integer.toString(minExp), Integer.toString(maxExp));
				}

			}
		});

		return rootView;
	}

	private class SearchJobTask extends AsyncTask<String, Void, Void> {

		ProgressDialog dialog;
		JSONArray jsonArrayJobs;
		JSONObject jsonObjectJobs;
		ArrayList<HashMap<String, String>> searchedJobs;
		HashMap<String, String> map;
		int success = 0;
		String message;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Searching Jobs. Please wait...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();

		}

		// new SearchJobTask().execute(strKeyword, strLocation, strCategory,
		// minSal, maxSal, minExp, maxExp);

		@Override
		protected Void doInBackground(String... params) {
			search_params = new ArrayList<NameValuePair>();
			search_params.add(new BasicNameValuePair("strKeyword", params[0]));
			search_params.add(new BasicNameValuePair("minSal", params[1]));
			search_params.add(new BasicNameValuePair("maxSal", params[2]));
			search_params.add(new BasicNameValuePair("minExp", params[3]));
			search_params.add(new BasicNameValuePair("maxExp", params[4]));

			searchedJobs = new ArrayList<HashMap<String, String>>();

			jsonObjectJobs = JSONParser.makeHttpRequest(url_searchJobs, "GET",
					search_params);
			//Log.e("searched jobs", jsonObjectJobs.toString());

			try {
				success = jsonObjectJobs.getInt("success");
				if (success == 1) {
					rowCount = jsonObjectJobs.getInt("rowCount");
					jsonArrayJobs = jsonObjectJobs.getJSONArray("jobs");

					for (int i = 0; i < jsonArrayJobs.length(); i++) {
						map = new HashMap<String, String>();

						jsonObjectJobs = jsonArrayJobs.getJSONObject(i);
						map.put("job_id", jsonObjectJobs.getString("job_id"));
						map.put("employer_id",
								jsonObjectJobs.getString("employer_id"));
						map.put("title", jsonObjectJobs.getString("title"));
						map.put("company_name",
								jsonObjectJobs.getString("company_name"));
						map.put("location", jsonObjectJobs.getString("location"));
						map.put("job_cat_id",
								jsonObjectJobs.getString("job_cat_id"));
						map.put("category", jsonObjectJobs.getString("category"));
						map.put("functional_area",
								jsonObjectJobs.getString("functional_area"));
						map.put("salary", jsonObjectJobs.getString("salary"));
						map.put("experience",
								jsonObjectJobs.getString("experience"));
						map.put("body", jsonObjectJobs.getString("body"));
						map.put("extra", jsonObjectJobs.getString("extra"));
						map.put("key_skills",
								jsonObjectJobs.getString("key_skills"));
						map.put("qualifications",
								jsonObjectJobs.getString("qualifications"));
						map.put("date_posted",
								jsonObjectJobs.getString("date_posted"));
						map.put("date_ending",
								jsonObjectJobs.getString("date_ending"));

						map.put("company_category",
								jsonObjectJobs.getString("company_category"));
						map.put("company_address",
								jsonObjectJobs.getString("company_address"));
						map.put("telephone", jsonObjectJobs.getString("telephone"));
						map.put("company_description",
								jsonObjectJobs.getString("company_description"));

						searchedJobs.add(map);
					}
				} else if (success == 0) {
					message = jsonObjectJobs.getString("message");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.cancel();

			if (success == 1) {
			//	Toast.makeText(getActivity(), searchedJobs.toString(),
			//			Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getActivity(), SearchResults.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("searched_jobs", searchedJobs);
				intent.putExtra("rowCount", rowCount);
				intent.putExtra("urlSearchJobs", strUrlSearchJobs);
				startActivity(intent);
				// getActivity().finish();
			} else {
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
						.show();
			}

		}

	}

	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_categories,
					getActivity());
			Log.e("search fragment", jsonObject.toString());

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
			super.onPostExecute(result);
			btnSelect.setText(categories.get(0).get("category_name"));
		}

	}

}

class SpinnerDataAdapter extends BaseAdapter {

	ArrayList<HashMap<String, String>> data;
	Context mContext;
	LayoutInflater inflater;

	public SpinnerDataAdapter(Context context,
			ArrayList<HashMap<String, String>> data) {
		this.mContext = context;
		this.data = data;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size() - 1;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.cat_list_item, null);

			holder.category = (TextView) convertView.findViewById(R.id.name);
			holder.rb_category = (RadioButton) convertView
					.findViewById(R.id.rb_category);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.rb_category.setChecked(false);
		holder.rb_category.setClickable(false);

		if (position == SearchFragment.selectedPos) {
			holder.rb_category.setChecked(true);
		}

		holder.category.setText(data.get(position).get("category_name"));

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SearchFragment.selectedPos = position;
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView category;
		RadioButton rb_category;
	}

}