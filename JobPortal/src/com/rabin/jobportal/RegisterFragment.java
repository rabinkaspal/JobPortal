package com.rabin.jobportal;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

	EditText etNewUsername, etNewPassword, etNewConfPassword;
	Button btnNext;
	String newUsername, newPassword, newConfPassword;
	int isValid = 0;
	int success = 0;
	JobSeeker jobseeker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);

		etNewUsername = (EditText) rootView.findViewById(R.id.etNewUsername);
		etNewPassword = (EditText) rootView.findViewById(R.id.etNewPassword);
		etNewConfPassword = (EditText) rootView
				.findViewById(R.id.etNewConfirmPassword);

		btnNext = (Button) rootView.findViewById(R.id.btnNext);

		etNewUsername.requestFocus();

		etNewUsername.addTextChangedListener(new TextWatcher() {

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
				ValidateFields.validateText(etNewUsername);
			}
		});

		etNewPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				ValidateFields.isValidPassword(etNewPassword);
			}
		});

		etNewConfPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				ValidateFields.isValidConfirmPwd(etNewPassword,
						etNewConfPassword);
			}
		});

		etNewUsername.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus)
					new CheckUsername(getActivity()).execute(etNewUsername
						.getText().toString().trim().replace(" ", ""));
			}
		});

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(etNewUsername.hasFocus()){
					etNewConfPassword.requestFocus();
				}else{

				newUsername = etNewUsername.getText().toString().trim()
						.replace(" ", "");
				newPassword = etNewPassword.getText().toString().trim()
						.replace(" ", "");
				newConfPassword = etNewConfPassword.getText().toString().trim()
						.replace(" ", "");

				

					if (isValid == 0 || newUsername.length() <= 0
							|| newUsername.length() < 6
							|| newUsername.isEmpty()) {
						Toast.makeText(getActivity(), "Enter valid Username",
								Toast.LENGTH_SHORT).show();
					} else if (newPassword.length() <= 0
							|| newPassword.length() < 6
							|| newPassword.isEmpty()) {
						Toast.makeText(getActivity(), "Enter valid password",
								Toast.LENGTH_SHORT).show();
					} else if (newConfPassword.length() <= 0
							|| newConfPassword.length() < 6
							|| newConfPassword.isEmpty()) {
						Toast.makeText(getActivity(), "Confirm password",
								Toast.LENGTH_SHORT).show();
					} else if (!newPassword.equals(newConfPassword)) {
						Toast.makeText(getActivity(), "Passwords donot match.",
								Toast.LENGTH_SHORT).show();
					} else {
						//all ok
						jobseeker = new JobSeeker(newUsername, newPassword);
						Intent intent = new Intent(getActivity(), JobseekerSelectCategories.class);
						intent.putExtra("jobseeker", jobseeker);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						getActivity().finish();
					}
				}
			}
		});

		return rootView;

	}

	private class CheckUsername extends AsyncTask<String, Void, Void> {

		Context mContext;

		public CheckUsername(Context context) {
			this.mContext = context;
		}

		String url_checkUsername = AppConstants.HOST_ADDRESS
				+ "job_portal/check_username.php?username=";

		String message = "";
		JSONObject jObj;

		@Override
		protected Void doInBackground(String... params) {

			jObj = JSONParser.getJSONFromURL(url_checkUsername + params[0],
					mContext);

			try {
				success = jObj.getInt("success");
				message = jObj.getString("message");
				if (success == 0) {
					isValid = 0;
				} else if (success == 1) {
					isValid = 1;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (isValid == 0) {
				etNewUsername.setBackgroundColor(Color.parseColor("#FA8072"));
				etNewUsername.setError("Username already exists.");
				if (!message.equals(""))
					Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
							.show();
			} else if (isValid == 1) {
				etNewUsername.setBackgroundColor(Color.parseColor("#98FB98"));
				etNewUsername.setError(null);
				if (!message.equals(""))
					Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
							.show();
			}
			super.onPostExecute(result);
		}

	}

}
