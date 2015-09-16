package com.rabin.jobportal;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JobDetailActivity extends DrawerActivity {

	TextView job_title, company, job_id, date_posted, date_ending, experience,
			location, salary, key_skills, qualifications, job_description;
	TextView company_name, company_category, company_address, telephone,
			company_description;
	TextView txtCompanyDetail, txtApply, txtSaveJob;

	String employer_id;

	HashMap<String, String> job;

	JobSeekerSessionManager sm;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.job_detail);
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

		sm = new JobSeekerSessionManager(getApplicationContext());

		Intent intent = getIntent();
		job = (HashMap<String, String>) intent.getSerializableExtra("job");

		job_title = (TextView) findViewById(R.id.job_title);
		company = (TextView) findViewById(R.id.company);
		job_id = (TextView) findViewById(R.id.job_id);
		date_posted = (TextView) findViewById(R.id.date_posted);
		date_ending = (TextView) findViewById(R.id.date_ending);
		experience = (TextView) findViewById(R.id.experience);
		location = (TextView) findViewById(R.id.location);
		salary = (TextView) findViewById(R.id.salary);
		key_skills = (TextView) findViewById(R.id.skills);
		qualifications = (TextView) findViewById(R.id.qualifications);
		job_description = (TextView) findViewById(R.id.job_description);

		company_name = (TextView) findViewById(R.id.company_name);
		company_category = (TextView) findViewById(R.id.category);
		company_address = (TextView) findViewById(R.id.company_address);
		telephone = (TextView) findViewById(R.id.telephone);
		company_description = (TextView) findViewById(R.id.company_description);

		txtCompanyDetail = (TextView) findViewById(R.id.txtCompanyDetail);
		employer_id = job.get("employer_id");

		txtApply = (TextView) findViewById(R.id.txtApply);
		txtSaveJob = (TextView) findViewById(R.id.txtSaveJob);

		String ending_date = "";
		switch (Integer.parseInt(job.get("date_ending"))) {
		case 0:
			ending_date = "1 week";
			break;
		case 1:
			ending_date = "2 weeks";
			break;
		case 2:
			ending_date = "3 weeks";
			break;
		case 3:
			ending_date = "1 month";
			break;
		case 4:
			ending_date = "2 months";
			break;
		case 5:
			ending_date = "1 year";
			break;
		case 6:
			ending_date = "2 years";
			break;
		}

		job_title.setText(job.get("title"));
		company.setText(job.get("company_name"));
		job_id.setText(job.get("job_id"));
		date_posted.setText(job.get("date_posted"));
		date_ending.setText(ending_date + " from posted date");
		experience.setText(job.get("experience") + "+ years");
		location.setText(job.get("location"));
		salary.setText("Rs." + job.get("salary") + " (per month)");
		key_skills.setText(job.get("key_skills"));
		qualifications.setText(job.get("qualifications"));
		job_description.setText(Html.fromHtml(job.get("body")));

		company_name.setText(job.get("company_name"));
		company_category.setText(job.get("company_category"));
		company_address.setText(job.get("company_address"));
		telephone.setText(job.get("telephone"));
		company_description.setText((job.get("company_description")));

		txtCompanyDetail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CompanyDetailActivity.class);
				intent.putExtra("emp_id", employer_id);
				startActivity(intent);

			}
		});

		if (sm.isLoggedIn() && sm.idInPref(job.get("job_id"), 0)) {
			txtApply.setText("Applied");
			txtApply.setBackground(getResources().getDrawable(
					R.drawable.btn_normal));
			txtApply.setEnabled(false);
			txtSaveJob.setEnabled(false);
			txtSaveJob.setBackground(getResources().getDrawable(
					R.drawable.btn_normal));

		}

		if (sm.isLoggedIn() && sm.idInPref(job.get("job_id"), 1)) {
			txtSaveJob.setText("Saved");
			txtSaveJob.setBackground(getResources().getDrawable(
					R.drawable.btn_normal));
			txtSaveJob.setEnabled(false);
		}

		txtApply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// sm.checkLogin();
				if (sm.isLoggedIn()) {

					if (!RecentJobsFragment.rejected_jobs.contains(job
							.get("job_id"))) {

						if (sm.getAppliedJobCount() < 10) {

							if (sm.idInPref(job.get("job_id"), 1)) {
								sm.removeSavedJob(job.get("job_id"));
								txtSaveJob.setText("Save Job");
								txtSaveJob.setEnabled(false);
								txtSaveJob.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
								new AppliedJobs().execute("1",
										sm.getSavedJobs());

								sm.addAppliedJob(job.get("job_id"));
								txtApply.setText("Applied");
								txtApply.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
								txtApply.setEnabled(false);
								txtSaveJob.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
								txtSaveJob.setEnabled(false);
								new AppliedJobs().execute("0",
										sm.getAppliedJobs());
							} else {
								sm.addAppliedJob(job.get("job_id"));
								txtApply.setText("Applied");
								txtApply.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
								txtApply.setEnabled(false);
								txtSaveJob.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
								txtSaveJob.setEnabled(false);
								new AppliedJobs().execute("0",
										sm.getAppliedJobs());
								txtSaveJob.setEnabled(false);
								txtSaveJob.setBackground(getResources()
										.getDrawable(R.drawable.btn_normal));
							}
						} else {
							Toast.makeText(JobDetailActivity.this,
									"10 jobs reached.", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(JobDetailActivity.this,
								"Cannot Apply. This job has already been declined for you.", Toast.LENGTH_SHORT).show();
					}
				} else {

					Intent i = new Intent(getApplicationContext(),
							JobSeekerSignInRegisterFragmentActivity.class);
					i.putExtra("job_id", job.get("job_id"));
					startActivity(i);

				}
			}
		});

		txtSaveJob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (sm.isLoggedIn()) {

					if (!RecentJobsFragment.rejected_jobs.contains(job
							.get("job_id"))) {

					if (sm.getSavedJobCount() < 20) {

						sm.addSavedJob(job.get("job_id"));
						txtSaveJob.setText("Saved");
						txtSaveJob.setBackground(getResources().getDrawable(
								R.drawable.btn_normal));
						;
						txtSaveJob.setEnabled(false);
						new AppliedJobs().execute("1", sm.getSavedJobs());

					} else {
						Toast.makeText(JobDetailActivity.this,
								"20 saved jobs reached.", Toast.LENGTH_SHORT)
								.show();
					}
					} else {
						Toast.makeText(JobDetailActivity.this,
								"Cannot Save Job. This job has already been declined for you.", Toast.LENGTH_SHORT).show();
					}
				} else {

					Intent i = new Intent(getApplicationContext(),
							JobSeekerSignInRegisterFragmentActivity.class);
					i.putExtra("job_id", job.get("job_id"));
					startActivity(i);

				}
			}

		});

	}

	private class AppliedJobs extends AsyncTask<String, Void, Void> {

		JSONObject jsonObject;
		int success;
		String url_saveAppliedJobs = AppConstants.HOST_ADDRESS
				+ "job_portal/saveAppliedJobs.php?js_id=" + sm.getJsId();
		String url_saveSavedJobs = AppConstants.HOST_ADDRESS
				+ "job_portal/saveSavedJobs.php?js_id=" + sm.getJsId();

		@Override
		protected Void doInBackground(String... param) {
			if (Integer.parseInt(param[0]) == 0) {
				jsonObject = JSONParser.getJSONFromURL(url_saveAppliedJobs
						+ "&jobs=" + param[1], getApplicationContext());
			} else if (Integer.parseInt(param[0]) == 1) {
				jsonObject = JSONParser.getJSONFromURL(url_saveSavedJobs
						+ "&jobs=" + param[1], getApplicationContext());
			}

			try {
				success = jsonObject.getInt("success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (success == 1) {
				// Toast.makeText(getApplicationContext(), "applied",
				// Toast.LENGTH_SHORT).show();
			}
		}

	}

}
