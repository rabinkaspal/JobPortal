package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterJSDetails extends DrawerActivity {

	EditText etFirstName, etLastName, etEmail, etPhone, etLocation, etKeyskill;
	String firstName, lastName, email, phone, location, keySkill;
	Button btnNext;

	JobSeeker js, jobseeker;
	JobSeekerSessionManager sm;
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_js_details);
		super.onCreate(savedInstanceState);

		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etLocation = (EditText) findViewById(R.id.etLocation);
		etKeyskill = (EditText) findViewById(R.id.etKeyskill);

		btnNext = (Button) findViewById(R.id.btnNext);

		sm = new JobSeekerSessionManager(getApplicationContext());
		session = new SessionManager(getApplicationContext());

		js = new JobSeeker();
		js = (JobSeeker) getIntent().getSerializableExtra("jobseeker");

		Toast.makeText(getApplicationContext(),
				js.username + js.password + js.js_likes, Toast.LENGTH_SHORT)
				.show();

		etFirstName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				ValidateFields.validateNames(etFirstName);
			}
		});
		etLastName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ValidateFields.validateNames(etLastName);
			}
		});

		etEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ValidateFields.isValidEmail(etEmail);
			}
		});

		etPhone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ValidateFields.validatePhone(etPhone);
			}
		});

		etLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ValidateFields.validateLocation(etLocation);
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				firstName = etFirstName.getText().toString().trim();
				lastName = etLastName.getText().toString().trim();
				email = etEmail.getText().toString().trim();
				phone = etPhone.getText().toString().trim();
				location = etLocation.getText().toString().trim();
				keySkill = etKeyskill.getText().toString().trim();

				if (!firstName.equals("") && !lastName.equals("")
						&& !email.equals("") && !phone.equals("")
						&& !location.equals("")) {
					if (firstName.length() < 3) {
						etFirstName
								.setError("must be at least 3 characters long.");
						etFirstName.setBackgroundColor(Color
								.parseColor("#FA8072"));
					} else if (lastName.length() < 3) {
						etLastName
								.setError("must be at least 3 characters long.");
						etLastName.setBackgroundColor(Color
								.parseColor("#FA8072"));
					} else if (!ValidateFields.isEmailValid(email)) {
						etEmail.setError("Invalid Email");
						etEmail.setBackgroundColor(Color.parseColor("#FA8072"));
					} else if (phone.length() < 9 || phone.length() > 10) {
						etPhone.setError("Invalid phone");
						etPhone.setBackgroundColor(Color.parseColor("#FA8072"));
					} else if (location.length() < 4) {
						etLocation
								.setError("must be at least 4 characters long");
						etLocation.setBackgroundColor(Color
								.parseColor("#FA8072"));
					} else {
						// Toast.makeText(getApplicationContext(), "all ok",
						// Toast.LENGTH_SHORT).show();
						new RegisterJobSeeker().execute();

					}
				} else {
					Toast.makeText(getApplicationContext(), "Fill all Fields",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				RegisterJSDetails.this);
		alertDialog.setTitle("Caution");
		alertDialog.setCancelable(false);
		alertDialog.setMessage("\nYour data will not be saved. Go Back?\n");
		alertDialog.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				}).setNegativeButton("Cancel",null);
		alertDialog.show();
	}

		class RegisterJobSeeker extends AsyncTask<Void, Void, Void> {

			ProgressDialog dialog;

			String url_registerJS = AppConstants.HOST_ADDRESS
					+ "job_portal/register_js.php";

			ArrayList<NameValuePair> query;

			JSONObject jObj;
			int js_id;
			int success = 0;
			String message = "";

			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(RegisterJSDetails.this);
				dialog.setMessage("Registering User...");
				dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				
//				String education = "{\"course1\":\"\",\"year1\":\"\",\"month1\":\"\",\"institution1\":\"\",\"course2\":\"\",\"year2\":\"\",\"month2\":\"\",\"institution2\":\"\"}";
//				String employment ="{\"company\":\"\",\"position\":\"\",\"fromYear\":\"\",\"fromMonth\":\"\",\"toYear\":\"present\",\"toMonth\":\"present\"}";
				
				
				query = new ArrayList<NameValuePair>();
				query.add(new BasicNameValuePair("username", js.username));
				query.add(new BasicNameValuePair("password", js.password));
				query.add(new BasicNameValuePair("cat_likes", js.js_likes));
				query.add(new BasicNameValuePair("firstname", firstName));
				query.add(new BasicNameValuePair("lastname", lastName));
				query.add(new BasicNameValuePair("email", email));
				query.add(new BasicNameValuePair("phone", phone));
				query.add(new BasicNameValuePair("location", location));
				query.add(new BasicNameValuePair("keyskills", keySkill));
//				query.add(new BasicNameValuePair("educational_degrees", education));
//				query.add(new BasicNameValuePair("previous_jobs", employment));
				
				
				jObj = JSONParser.makeHttpRequest(url_registerJS, "GET", query);

				Log.e("jObj", jObj.toString());
				
				try {
					success = jObj.getInt("success");

					if (success == 1) {
						js_id = jObj.getInt("js_id");
						message = jObj.getString("message");
					} else {
						message = jObj.getString("message");
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
					sm.createLoginSession(firstName + " " + lastName, email,
							Integer.toString(js_id));
					session.loginJobSeeker();

					Intent intent = new Intent(RegisterJSDetails.this,
							JobsViewPagerFragmentActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();

				} else {
					Toast.makeText(RegisterJSDetails.this, message,
							Toast.LENGTH_SHORT).show();
				}

				super.onPostExecute(result);
			}
		}

	}
