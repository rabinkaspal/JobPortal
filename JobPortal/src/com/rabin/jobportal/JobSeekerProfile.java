package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class JobSeekerProfile extends BaseActivity {

	int selectedPos = 0;

	TextView tvEditDetails, tvEditDetailsDiscard, js_name, js_email,
			js_location, js_prev_company, js_prev_company_position,
			js_prev_company_from, js_course1, js_institution1, js_grad_passed,
			js_course2, js_institution2, js_post_grad_passed, txtEmployment,
			txtEducation, txtAddPostGraduation, txtRemovePostGraduation;

	LinearLayout layoutJobSeekerDetails, layoutEditDetails, layoutGraduation,
			layoutPostGraduation;
	ScrollView layoutEmploymentDetails, layoutEducationDetails;

	EditText etCurrEmployerName, etCurrCompanyPosition, etInstitutionBachelors,
			etInstitutionMasters;

	Button btnFromYearSelect, btnFromMonthSelect, btnSaveEmployment,
			btnSelectCourse, btnFromYearSelect2, btnFromMonthSelect2,
			btnSelectCourse2, btnFromYearSelect3, btnFromMonthSelect3,
			btnSaveEducation;

	String currEmployerName, currCompanyPosition, institutionBachelors,
			institutionMasters;

	String year1, month1, course1, year2, month2, course2, year3, month3;

	boolean hasPostGrad = false;

	String[] year_array, month_array, course_bachelors, course_masters;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.jobseeker_profile);
		super.onCreate(arg0);

		tvEditDetails = (TextView) findViewById(R.id.tvEditDetails);
		tvEditDetailsDiscard = (TextView) findViewById(R.id.tvEditDetailsDiscard);

		txtAddPostGraduation = (TextView) findViewById(R.id.txtAddPostGraduation);
		txtRemovePostGraduation = (TextView) findViewById(R.id.txtRemovePostGraduation);

		txtEmployment = (TextView) findViewById(R.id.txtEmployment);
		txtEducation = (TextView) findViewById(R.id.txtEducation);

		js_name = (TextView) findViewById(R.id.js_name);
		js_email = (TextView) findViewById(R.id.js_email);
		js_location = (TextView) findViewById(R.id.js_location);
		js_prev_company = (TextView) findViewById(R.id.js_prev_company);
		js_prev_company_position = (TextView) findViewById(R.id.js_prev_company_position);
		js_prev_company_from = (TextView) findViewById(R.id.js_prev_company_from);
		js_course1 = (TextView) findViewById(R.id.js_course1);
		js_institution1 = (TextView) findViewById(R.id.js_institution1);
		js_grad_passed = (TextView) findViewById(R.id.js_grad_passed);
		js_course2 = (TextView) findViewById(R.id.js_course2);
		js_institution2 = (TextView) findViewById(R.id.js_institution2);
		js_post_grad_passed = (TextView) findViewById(R.id.js_post_grad_passed);

		etCurrEmployerName = (EditText) findViewById(R.id.etCurrEmployerName);
		etCurrCompanyPosition = (EditText) findViewById(R.id.etCurrCompanyPosition);
		etInstitutionBachelors = (EditText) findViewById(R.id.etInstitutionBachelors);
		etInstitutionMasters = (EditText) findViewById(R.id.etInstitutionMasters);

		btnFromYearSelect = (Button) findViewById(R.id.btnFromYearSelect);
		btnFromMonthSelect = (Button) findViewById(R.id.btnFromMonthSelect);
		btnSaveEmployment = (Button) findViewById(R.id.btnSaveEmployment);
		btnSelectCourse = (Button) findViewById(R.id.btnSelectCourse);
		btnFromYearSelect2 = (Button) findViewById(R.id.btnFromYearSelect2);
		btnFromMonthSelect2 = (Button) findViewById(R.id.btnFromMonthSelect2);
		btnSelectCourse2 = (Button) findViewById(R.id.btnSelectCourse2);
		btnFromYearSelect3 = (Button) findViewById(R.id.btnFromYearSelect3);
		btnFromMonthSelect3 = (Button) findViewById(R.id.btnFromMonthSelect3);
		btnSaveEducation = (Button) findViewById(R.id.btnSaveEducation);

		layoutGraduation = (LinearLayout) findViewById(R.id.layoutGraduation);
		layoutPostGraduation = (LinearLayout) findViewById(R.id.layoutPostGraduation);

		layoutPostGraduation.setVisibility(View.GONE);

		layoutJobSeekerDetails = (LinearLayout) findViewById(R.id.layoutJobSeekerDetails);
		layoutEditDetails = (LinearLayout) findViewById(R.id.layoutEditDetails);

		layoutEmploymentDetails = (ScrollView) findViewById(R.id.layoutEmploymentDetails);
		layoutEducationDetails = (ScrollView) findViewById(R.id.layoutEducationDetails);

		layoutEditDetails.setVisibility(View.GONE);
		layoutEducationDetails.setVisibility(View.GONE);

		tvEditDetailsDiscard.setVisibility(View.GONE);

		new FetchJobSeekerDetails().execute();

		txtAddPostGraduation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				layoutPostGraduation.setVisibility(View.VISIBLE);
				txtAddPostGraduation.setVisibility(View.GONE);
				hasPostGrad = true;
			}
		});

		txtRemovePostGraduation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutPostGraduation.setVisibility(View.GONE);
				txtAddPostGraduation.setVisibility(View.VISIBLE);
				hasPostGrad = false;
			}
		});

		tvEditDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutJobSeekerDetails.setVisibility(View.GONE);
				layoutEditDetails.setVisibility(View.VISIBLE);
				tvEditDetailsDiscard.setVisibility(View.VISIBLE);
				tvEditDetails.setVisibility(View.GONE);

			}
		});

		tvEditDetailsDiscard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutJobSeekerDetails.setVisibility(View.VISIBLE);
				layoutEditDetails.setVisibility(View.GONE);
				tvEditDetailsDiscard.setVisibility(View.GONE);
				tvEditDetails.setVisibility(View.VISIBLE);
				new FetchJobSeekerDetails().execute();
			}
		});

		txtEmployment.setEnabled(false);
		txtEmployment.setBackground(getResources().getDrawable(
				R.drawable.btn_normal));

		txtEmployment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				layoutEmploymentDetails.setVisibility(View.VISIBLE);
				layoutEducationDetails.setVisibility(View.GONE);
				txtEmployment.setEnabled(false);
				txtEducation.setEnabled(true);
				txtEmployment.setBackground(getResources().getDrawable(
						R.drawable.btn_normal));
				txtEducation.setBackground(getResources().getDrawable(
						R.drawable.btn_back));
			}
		});

		txtEducation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutEmploymentDetails.setVisibility(View.GONE);
				layoutEducationDetails.setVisibility(View.VISIBLE);
				txtEducation.setEnabled(false);
				txtEmployment.setEnabled(true);
				txtEmployment.setBackground(getResources().getDrawable(
						R.drawable.btn_back));
				txtEducation.setBackground(getResources().getDrawable(
						R.drawable.btn_normal));
			}
		});

		year_array = getResources().getStringArray(R.array.year);
		month_array = getResources().getStringArray(R.array.months);
		course_bachelors = getResources().getStringArray(
				R.array.course_graduation);
		course_masters = getResources().getStringArray(
				R.array.course_post_graduation);

		btnFromYearSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromYearSelect, year_array);
			}
		});
		btnFromYearSelect2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromYearSelect2, year_array);
			}
		});
		btnFromYearSelect3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromYearSelect3, year_array);
			}
		});

		btnFromMonthSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromMonthSelect, month_array);
			}
		});
		btnFromMonthSelect2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromMonthSelect2, month_array);
			}
		});
		btnFromMonthSelect3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnFromMonthSelect3, month_array);
			}
		});

		btnSelectCourse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnSelectCourse, course_bachelors);
			}
		});
		btnSelectCourse2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectedPos = 0;
				showDialog(btnSelectCourse2, course_masters);
			}
		});

		btnSaveEmployment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				currEmployerName = etCurrEmployerName.getText().toString()
						.trim();
				currCompanyPosition = etCurrCompanyPosition.getText()
						.toString().trim();
				year1 = btnFromYearSelect.getText().toString().trim();
				month1 = btnFromMonthSelect.getText().toString().trim();

				if (currEmployerName.length() <= 0) {
					Toast.makeText(JobSeekerProfile.this,
							"Enter your current employer.", Toast.LENGTH_SHORT).show();
					etCurrEmployerName.setError("Cannot be empty");
				} else if (currEmployerName.length() <= 5) {
					Toast.makeText(JobSeekerProfile.this, "Enter valid name.",
							Toast.LENGTH_SHORT).show();
					etCurrEmployerName.setError("More than 5 characters.");
				} else if (currCompanyPosition.length() <= 0) {
					Toast.makeText(JobSeekerProfile.this,
							"Enter your current position.", Toast.LENGTH_SHORT).show();
					etCurrEmployerName.setError("Cannot be empty.");
				} else if (currCompanyPosition.length() < 4) {
					Toast.makeText(JobSeekerProfile.this, "Enter valid name.",
							Toast.LENGTH_SHORT).show();
					etCurrEmployerName.setError("More than 4 characters.");
				} else if (year1.equals("Select Year")) {
					Toast.makeText(JobSeekerProfile.this, "select year", Toast.LENGTH_SHORT)
							.show();
				} else if (month1.equals("Select Month")) {
					Toast.makeText(JobSeekerProfile.this, "select month", Toast.LENGTH_SHORT)
							.show();
				} else {
					String emp_json = "{\"company\":\""
							+ currEmployerName
							+ "\",\"position\":\""
							+ currCompanyPosition
							+ "\",\"fromYear\":\""
							+ year1
							+ "\",\"fromMonth\":\""
							+ month1
							+ "\",\"toYear\":\"present\",\"toMonth\":\"present\"}";
					// Toast.makeText(JobSeekerProfile.this, emp_json,
					// 2000).show();
					new SaveEmploymentEducation().execute("0", emp_json);
				}

			}
		});

		btnSaveEducation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String edu_json = "";

				course1 = btnSelectCourse.getText().toString().trim();
				year2 = btnFromYearSelect2.getText().toString().trim();
				month2 = btnFromMonthSelect2.getText().toString().trim();
				institutionBachelors = etInstitutionBachelors.getText()
						.toString().trim();

				if (hasPostGrad) {
					course2 = btnSelectCourse2.getText().toString().trim();
					year3 = btnFromYearSelect3.getText().toString().trim();
					month3 = btnFromMonthSelect3.getText().toString().trim();
					institutionMasters = etInstitutionMasters.getText()
							.toString().trim();
				}

				if (course1.equals("Select Course")) {
					Toast.makeText(JobSeekerProfile.this, "Select Course", Toast.LENGTH_SHORT)
							.show();
				} else if (year2.equals("Select Year")) {
					Toast.makeText(JobSeekerProfile.this, "Select year", Toast.LENGTH_SHORT)
							.show();
				} else if (month2.equals("Select Month")) {
					Toast.makeText(JobSeekerProfile.this, "Select month", Toast.LENGTH_SHORT)
							.show();
				} else if (institutionBachelors.length() <= 0) {
					Toast.makeText(JobSeekerProfile.this,
							"Enter Institution Name.", Toast.LENGTH_SHORT).show();
					etInstitutionBachelors.setError("Cannot be empty");
				} else if (institutionBachelors.length() <= 5) {
					Toast.makeText(JobSeekerProfile.this, "Enter valid name.",
							Toast.LENGTH_SHORT).show();
					etInstitutionBachelors.setError("More than 5 characters.");
				} else {

					if (hasPostGrad) {
						if (course2.equals("Select Course")) {
							Toast.makeText(JobSeekerProfile.this,
									"Select Course", Toast.LENGTH_SHORT).show();
						} else if (year3.equals("Select Year")) {
							Toast.makeText(JobSeekerProfile.this,
									"Select year", Toast.LENGTH_SHORT).show();
						} else if (month3.equals("Select Month")) {
							Toast.makeText(JobSeekerProfile.this,
									"Select month", Toast.LENGTH_SHORT).show();
						} else if (institutionMasters.length() <= 0) {
							Toast.makeText(JobSeekerProfile.this,
									"Enter Institution Name.", Toast.LENGTH_SHORT).show();
							etInstitutionMasters.setError("Cannot be empty");
						} else if (institutionMasters.length() <= 5) {
							Toast.makeText(JobSeekerProfile.this,
									"Enter valid name.", Toast.LENGTH_SHORT).show();
							etInstitutionMasters
									.setError("More than 5 characters.");
						} else {
							edu_json = "{\"course1\":\"" + course1
									+ "\",\"year1\":\"" + year2
									+ "\",\"month1\":\"" + month2
									+ "\",\"institution1\":\""
									+ institutionBachelors
									+ "\",\"course2\":\"" + course2
									+ "\",\"year2\":\"" + year3
									+ "\",\"month2\":\"" + month3
									+ "\",\"institution2\":\""
									+ institutionMasters + "\"}";
							
							new SaveEmploymentEducation().execute("1", edu_json);
							
						}
					}else{
						edu_json = "{\"course1\":\"" + course1 + "\",\"year1\":\""
								+ year2 + "\",\"month1\":\"" + month2
								+ "\",\"institution1\":\"" + institutionBachelors
								+ "\",\"course2\":\"" + "" + "\",\"year2\":\"" + ""
								+ "\",\"month2\":\"" + ""
								+ "\",\"institution2\":\"" + "" + "\"}";
						
						new SaveEmploymentEducation().execute("1", edu_json);
					}
//					Toast.makeText(JobSeekerProfile.this, edu_json, Toast.LENGTH_SHORT)
//					.show();
				}
			}

		});

	}

	public void showDialog(final Button v, final String[] data) {
		View dialogView;
		LayoutInflater inflater = (LayoutInflater) JobSeekerProfile.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		dialogView = inflater.inflate(R.layout.fragment_job_category_listings,
				null);

		ListView lv = (ListView) dialogView.findViewById(R.id.lv_joblisting);
		lv.setAdapter(new JobProfileSpinnerAdapter(JobSeekerProfile.this, data));

		AlertDialog.Builder alert = new AlertDialog.Builder(
				JobSeekerProfile.this);
		alert.setCancelable(true).setTitle("Choose an item");
		alert.setView(dialogView);

		alert.setCancelable(false);
		alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				v.setText(data[selectedPos]);
				dialog.cancel();
			}
		});

		alert.show();
	}

	class JobProfileSpinnerAdapter extends BaseAdapter {

		String[] data;
		Context mContext;
		LayoutInflater inflater;

		public JobProfileSpinnerAdapter(Context context, String[] mData) {
			this.mContext = context;
			this.data = mData;
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			return data[position];
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.cat_list_item, null);

				holder.category = (TextView) convertView
						.findViewById(R.id.name);
				holder.rb_category = (RadioButton) convertView
						.findViewById(R.id.rb_category);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.rb_category.setChecked(false);
			holder.rb_category.setClickable(false);

			if (position == selectedPos) {
				holder.rb_category.setChecked(true);
			}

			holder.category.setText(data[position]);

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					selectedPos = position;
					notifyDataSetChanged();
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView category;
			RadioButton rb_category;
		}

	}

	class SaveEmploymentEducation extends AsyncTask<String, Void, Void> {
		JSONObject jObj;
		int success = 0;
		String message = "";
		String url_saveDetails;
		ArrayList<NameValuePair> queryString;

		@Override
		protected Void doInBackground(String... params) {

			queryString = new ArrayList<NameValuePair>();
			queryString.add(new BasicNameValuePair("js_id", RecentJobsFragment.session.getJsId()));
			queryString.add(new BasicNameValuePair("detail_string", params[1]));

			if (params[0].equals("0"))
				url_saveDetails = AppConstants.HOST_ADDRESS
						+ "job_portal/save_jobseeker_employment.php";
			else if (params[0].equals("1"))
				url_saveDetails = AppConstants.HOST_ADDRESS
						+ "job_portal/save_jobseeker_education.php";
		
			jObj = JSONParser.makeHttpRequest(url_saveDetails, "GET",
					queryString);

			try {
				success = jObj.getInt("success");
				message = jObj.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (success == 1) {
				Toast.makeText(JobSeekerProfile.this, message, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(JobSeekerProfile.this, message, Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}
	}

	class FetchJobSeekerDetails extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		HashMap<String, String> map;
		String url_jsDetails = AppConstants.HOST_ADDRESS
				+ "job_portal/get_jobseeker_profile_details.php?js_id="+RecentJobsFragment.session.getJsId();

		JSONObject jObj;
		int success = 0;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(JobSeekerProfile.this);
			dialog.setMessage("Fetching details...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			jObj = JSONParser.getJSONFromURL(url_jsDetails,
					JobSeekerProfile.this);

			try {
				success = jObj.getInt("success");

				if (success == 1) {
					map = new HashMap<String, String>();
					map.put("js_name", jObj.getString("js_name"));
					map.put("address", jObj.getString("address"));
					map.put("email", jObj.getString("email"));
					map.put("currCompany", jObj.getString("currCompany"));
					map.put("currPosition", jObj.getString("currPosition"));
					map.put("fromDate", jObj.getString("fromDate"));
					map.put("course1", jObj.getString("course1"));
					map.put("passed1", jObj.getString("passed1"));
					map.put("institution1", jObj.getString("institution1"));
					map.put("course2", jObj.getString("course2"));
					map.put("passed2", jObj.getString("passed2"));
					map.put("institution2", jObj.getString("institution2"));

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if (success == 1) {
				String[] fields = { "js_name", "address", "email",
						"currCompany", "currPosition", "fromDate", "course1",
						"passed1", "institution1", "course2", "passed2",
						"institution2" };
				TextView[] tvFields = { js_name, js_location, js_email,
						js_prev_company, js_prev_company_position,
						js_prev_company_from, js_course1, js_grad_passed,
						js_institution1, js_course2, js_post_grad_passed,
						js_institution2 };

				for (int i = 0; i < fields.length; i++) {
					if (fields[i] != null && !fields[i].isEmpty()
							&& !fields[i].equals("") && fields[i].trim().length() >0) {
						tvFields[i].setText(map.get(fields[i]));
					} else {
						if (fields[i].equals(null))tvFields[i].setText("--");
					}
				}

			} else {
				Toast.makeText(JobSeekerProfile.this,
						"No details has been provided yet.", Toast.LENGTH_SHORT)
						.show();
			}

			super.onPostExecute(result);
		}
	}

}
