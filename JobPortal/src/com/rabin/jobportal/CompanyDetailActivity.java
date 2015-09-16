package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompanyDetailActivity extends DrawerActivity {

	TextView company_name, address, industry, ownership, numEmployees, url,
			phone, email, company_info, instructions;
	Button btnViewJobsByCompany;
	String emp_id;
	final String url_data = AppConstants.HOST_ADDRESS
			+ "job_portal/get_company_details.php?";
	ArrayList<HashMap<String, String>> data;

	@Override
	protected void onCreate(Bundle arg0) {
		

		data = new ArrayList<HashMap<String, String>>();
		
		Intent intent = getIntent();
		emp_id = intent.getStringExtra("emp_id");
		
		new FetchEmployerDetails().execute(url_data, emp_id);

		setContentView(R.layout.company_detail);
		super.onCreate(arg0);
		

		btnViewJobsByCompany = (Button) findViewById(R.id.btnViewJobsByCompany);

		company_name = (TextView) findViewById(R.id.company_name);
		address = (TextView) findViewById(R.id.address);
		industry = (TextView) findViewById(R.id.industry);
		ownership = (TextView) findViewById(R.id.ownership);
		numEmployees = (TextView) findViewById(R.id.numEmployees);
		url = (TextView) findViewById(R.id.url);
		phone = (TextView) findViewById(R.id.phone);
		email = (TextView) findViewById(R.id.email);
		company_info = (TextView) findViewById(R.id.company_info);
		instructions = (TextView) findViewById(R.id.instructions);

		btnViewJobsByCompany.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent jobCategoryIntent = new Intent(getApplicationContext(),
						JobByCompany.class);
				jobCategoryIntent.putExtra("emp_id", emp_id);
				jobCategoryIntent.putExtra("company_name", "company_name");
				startActivity(jobCategoryIntent);

			}
		});

	}

	private class FetchEmployerDetails extends AsyncTask<String, Void, Void> {

		ProgressDialog dialog;
		JSONObject jsonObject;
		JSONArray jsonArray;
		int success = 0;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getApplicationContext());
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Loading...");
		}

		@Override
		protected Void doInBackground(String... params) {

			jsonObject = JSONParser.getJSONFromURL(params[0] + "emp_id="
					+ params[1], getApplicationContext());
			Log.e("url calling", params[0] + "emp_id=" + params[1]);
			// jsonObject.toString());

			try {
				success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("company_details");

					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();

						jsonObject = jsonArray.getJSONObject(i);

						map.put("company_name",
								jsonObject.getString("company_name"));
						map.put("address", jsonObject.getString("address"));
						map.put("industry", jsonObject.getString("industry"));
						map.put("ownership", jsonObject.getString("ownership"));
						map.put("numEmployees",
								jsonObject.getString("numEmployees"));
						map.put("url", jsonObject.getString("url"));
						map.put("phone", jsonObject.getString("phone"));
						map.put("email", jsonObject.getString("email"));
						map.put("company_info",
								jsonObject.getString("company_info"));
						map.put("instructions",
								jsonObject.getString("instructions"));
						map.put("logo", jsonObject.getString("logo"));

						data.add(map);

						Log.e("data", data.toString());

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

			company_name.setText(data.get(0).get("company_name"));
			address.setText(data.get(0).get("address"));
			industry.setText(data.get(0).get("industry"));
			ownership.setText(data.get(0).get("ownership"));
			numEmployees.setText(data.get(0).get("numEmployees"));
			url.setText(data.get(0).get("url"));
			phone.setText(data.get(0).get("phone"));
			email.setText(data.get(0).get("email"));
			company_info.setText(data.get(0).get("company_info"));
			instructions.setText(data.get(0).get("instructions"));

		}

	}

}
