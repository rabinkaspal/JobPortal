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
import android.os.AsyncTask;
import android.os.Bundle;
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


public class SetJobAlert extends DrawerActivity {
	
	public ArrayList<HashMap<String, String>> categories;
	JSONObject jsonObject;
	JSONArray jsonArray;
	final String url_categories = AppConstants.HOST_ADDRESS
			+ "job_portal/get_all_categories.php";

	final String url_searchJobs = AppConstants.HOST_ADDRESS
			+ "job_portal/search_jobs.php";
	ArrayList<NameValuePair> search_params;

	Button btnSelect, btnAddJobAlert;
	static int selectedPos = 0;
	String cat_id = "0";
	TextView salaryMin, salaryMax, expMin, expMax, salaryRange, expRange;

	EditText etKeyskills, etLocation;
	TextView tvJobAlert;

	String strKeyword, strLocation, strCategory = "";
	int minSal = 5000, maxSal = 100000, minExp = 0, maxExp = 12;
	int rowCount;
	String jobWord="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.add_job_alert);
		super.onCreate(savedInstanceState);
		
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnAddJobAlert = (Button) findViewById(R.id.btnAddJobAlert);
		
		tvJobAlert = (TextView) findViewById(R.id.tvJobAlert);

		salaryMin = (TextView) findViewById(R.id.salaryMin);
		salaryMax = (TextView) findViewById(R.id.salaryMax);
		salaryRange = (TextView) findViewById(R.id.salaryRange);
		expMin = (TextView) findViewById(R.id.expMin);
		expMax = (TextView) findViewById(R.id.expMax);
		expRange = (TextView) findViewById(R.id.expRange);

		etKeyskills = (EditText) findViewById(R.id.etKeyWord);
		etLocation = (EditText) findViewById(R.id.etLocation);

		categories = new ArrayList<HashMap<String, String>>();

		new DownloadCategories().execute();
		new GetSavedAlert().execute();

		// create RangeSeekBar as Integer range between 20 and 75
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(5000, 100000,
				SetJobAlert.this);
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
		ViewGroup layout1 = (ViewGroup) findViewById(R.id.layout1);
		layout1.removeView((View) layout1.getParent());
		layout1.addView(seekBar);

		// create RangeSeekBar as Integer range between 20 and 75
		RangeSeekBar<Integer> seekBar2 = new RangeSeekBar<Integer>(0, 10,
				SetJobAlert.this);
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
		ViewGroup layout2 = (ViewGroup) findViewById(R.id.layout2);
		layout2.removeView((View) layout2.getParent());
		layout2.addView(seekBar2);



		btnSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				View dialogView;
				LayoutInflater inflater = (LayoutInflater) SetJobAlert.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				dialogView = inflater.inflate(
						R.layout.fragment_job_category_listings, null);

				ListView lv = (ListView) dialogView
						.findViewById(R.id.lv_joblisting);
				lv.setAdapter(new SpinnerDataAdapterSetJobAlert(SetJobAlert.this, categories));

				AlertDialog.Builder alert = new AlertDialog.Builder(
						SetJobAlert.this);
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

		btnAddJobAlert.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// edt.setBackgroundColor(Color.parseColor("#FA8072"));
				// edt.setError("Should be 10 digits.");

				strCategory = categories.get(selectedPos).get("category_name");

				strKeyword = etKeyskills.getText().toString().trim()
						.replace(",", " ");
				strLocation = etLocation.getText().toString().trim()
						.replace(",", " ");

				if (strKeyword.equalsIgnoreCase("")
						|| strLocation.equalsIgnoreCase("")) {
					Toast.makeText(SetJobAlert.this, "Fill in all the fields",
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

					jobWord = strKeyword + "/" + minSal
							+ "/" + maxSal + "/" + minExp + "/"	+ maxExp;
					
//					Toast.makeText(getApplicationContext(), jobWord, Toast.LENGTH_LONG).show();
					
					AlertDialog.Builder alert = new AlertDialog.Builder(SetJobAlert.this);
					alert.setCancelable(false).setTitle("Information")
					.setMessage("\nYour job description will be saved now.\n\nYou will be notified when something relevant comes up.\n")
					.setPositiveButton("Cool", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							
							new SaveJobAlertTask().execute(sm.getJsId(),jobWord);
							
							finish();
							onBackPressed();
						}
					}).setNegativeButton("Not Now", null).show();
					
				}

			}
		});

	}
	
private class GetSavedAlert extends AsyncTask<Void, Void, Void>{
		
		ProgressDialog dialog;
		String url_savedJobAlert = AppConstants.HOST_ADDRESS + "job_portal/get_saved_job_alert.php?js_id="+sm.getJsId();
		JSONObject jsonObject;
		String setJobAlert = "";
		String message;
		
		int success=0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(SetJobAlert.this);
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Loading...");
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... args) {

			
			jsonObject = JSONParser.getJSONFromURL(url_savedJobAlert, getApplicationContext());
			
			try {
				success = jsonObject.getInt("success");
				
				if(success==1){
					String a = jsonObject.getString("keyword");
					String b = jsonObject.getString("minSal");
					String c = jsonObject.getString("maxSal");
					String d = jsonObject.getString("minExp");
					String e = jsonObject.getString("maxExp");
					
					setJobAlert = "You have a job alert for\nKeyword: 	\t\t\t\t"+a+"\nMin.Salary: \t\t\tRs."+b+"\nMax. Salary: \t\t\tRs."+c+"\nMin. Experience: \t"+d+" years\nMax. Experience: \t"+e+" years";
				}else{
					message = jsonObject.getString("message");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if(success==1){
				tvJobAlert.setText(setJobAlert);
			}else{
				tvJobAlert.setText(message);
			}
			super.onPostExecute(result);
		}
		
	}
	
	
	
	
	private class SaveJobAlertTask extends AsyncTask<String, Void, Void>{
		
		ProgressDialog dialog;
		String url_saveJobAlert = AppConstants.HOST_ADDRESS + "job_portal/save_job_alert.php";
		ArrayList<NameValuePair> params;
		JSONObject jsonObject;
		String message = "";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(SetJobAlert.this);
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Saving Job Description....");
			dialog.show();
		}
		
		@Override
		protected Void doInBackground(String... args) {

			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("js_id", args[0]));
			params.add(new BasicNameValuePair("desc", args[1]));
			
			jsonObject = JSONParser.makeHttpRequest(url_saveJobAlert, "GET", params);
			
			try {
				message = jsonObject.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
	}
	
	
	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_categories,
					getApplicationContext());
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			btnSelect.setText(categories.get(0).get("category_name"));
		}

	}

}

class SpinnerDataAdapterSetJobAlert extends BaseAdapter {

	ArrayList<HashMap<String, String>> data;
	Context mContext;
	LayoutInflater inflater;

	public SpinnerDataAdapterSetJobAlert(Context context,
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

		if (position == SetJobAlert.selectedPos) {
			holder.rb_category.setChecked(true);
		}

		holder.category.setText(data.get(position).get("category_name"));

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SetJobAlert.selectedPos = position;
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
