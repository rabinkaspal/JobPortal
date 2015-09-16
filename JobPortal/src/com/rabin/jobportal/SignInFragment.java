package com.rabin.jobportal;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInFragment extends Fragment {

	EditText etUsername, etPassword;
	String jsUsername, jsPassword;
	Button btnSignIn;
	CheckBox togglePwd;
	int job_id;

	JobSeekerSessionManager sm;
	SessionManager session;
	
	ArrayList<NameValuePair> params;
	String retJsId, retJsName, retJsEmail;
	String applied_jobs, saved_jobs;

	String urlJsLogin = AppConstants.HOST_ADDRESS
			+ "job_portal/jobseeker_login.php";

	public SignInFragment newInstance(int job_id) {
		SignInFragment fg = new SignInFragment();
		fg.job_id = job_id;
		return fg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sign_in, container,
				false);

		sm = new JobSeekerSessionManager(getActivity());
		session = new SessionManager(getActivity());
		// Toast.makeText(getActivity(),
		// getActivity().getClass().getSimpleName(),Toast.LENGTH_SHORT).show();

		TextView txtSkip = (TextView) rootView.findViewById(R.id.txtSkip);

		// if(getActivity().getClass().getSimpleName().equalsIgnoreCase("EmployerSignInRegisterFragmentActivity")){
		// txtSkip.setVisibility(View.GONE);
		// isEmployer.setVisibility(View.GONE);
		// }
		btnSignIn = (Button) rootView.findViewById(R.id.btnSignIn);
		togglePwd = (CheckBox) rootView.findViewById(R.id.togglePwd);

		togglePwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (togglePwd.isChecked()) {
					etPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					togglePwd.setText("Hide Password");
				} else if (!togglePwd.isChecked()) {
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					togglePwd.setText("Show Password");
				}
			}
		});

		etUsername = (EditText) rootView.findViewById(R.id.jsUsername);
		etPassword = (EditText) rootView.findViewById(R.id.jsPassword);

		etUsername.requestFocus();

		etUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				ValidateFields.validateText(etUsername);
			}
		});

		etPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				ValidateFields.isValidPassword(etPassword);
			}
		});

		btnSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jsUsername = etUsername.getText().toString();
				jsPassword = etPassword.getText().toString();

				new LoginJobSeeker().execute(jsUsername, jsPassword);

			}
		});

		txtSkip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						JobsViewPagerFragmentActivity.class);
				startActivity(intent);
			}
		});

		return rootView;
	}

	private class LoginJobSeeker extends AsyncTask<String, Void, Void> {

		ProgressDialog dialog;
		JSONObject jsonObject;
		JSONArray jsonArray;
		int success = 0;
		String message;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(getActivity());
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.setMessage("Logging in...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(String... args) {
			// retrieve JSON Objects from URL address
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", args[0]));
			params.add(new BasicNameValuePair("password", args[1]));
			Log.d("params login user", params.toString());

			jsonObject = JSONParser.makeHttpRequest(urlJsLogin, "GET", params);
			Log.e("JSON login user", jsonObject.toString());
			try {
				success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("jobseeker");

					for (int i = 0; i < jsonArray.length(); i++) {

						jsonObject = jsonArray.getJSONObject(i);
						// retrieve json objects
						retJsId = jsonObject.getString("jsId");
						retJsName = jsonObject.getString("jsName");
						retJsEmail = jsonObject.getString("jsEmail");

						applied_jobs = jsonObject.getString("applied_jobs");
						saved_jobs = jsonObject.getString("saved_jobs");

						Log.e("returned data Login", retJsId + " : "
								+ retJsName);
					}
				} else if (success == 0) {
					message = jsonObject.getString("message");
				}

			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if (success == 1) {
				// Toast.makeText(getActivity(),
				// retJsId+'\n'+retJsName+'\n'+retJsEmail,
				// Toast.LENGTH_SHORT).show();

				sm.createLoginSession(retJsName, retJsEmail, retJsId);
				session.loginJobSeeker();
				
				if (applied_jobs != null && !applied_jobs.isEmpty()) {
					sm.addAppliedJob(applied_jobs);
				}
				if (saved_jobs != null && !saved_jobs.isEmpty()) {
					sm.addSavedJob(saved_jobs);
				}
				Intent intent = new Intent(getActivity(),
						JobsViewPagerFragmentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if (job_id>0 && !sm.idInPref(Integer.toString(job_id), 0)) {
					sm.addAppliedJob(Integer.toString(job_id));
					new AppliedJobsSubmitTask(getActivity()).execute("0",
							sm.getAppliedJobs());
				}
				CookieManager.getInstance().removeSessionCookie();
				startActivity(intent);
				getActivity().finish();
			} else {
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
						.show();
				etPassword.setText("");
			}
		}
	}

}
