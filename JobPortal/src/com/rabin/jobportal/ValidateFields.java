package com.rabin.jobportal;

import android.graphics.Color;
import android.widget.EditText;

public class ValidateFields {

	public static boolean validatePersonName(EditText edt)
			throws NumberFormatException {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty.");
			result = false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Should be Alphabets Only.");
			result = false;
		} else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}

	public static boolean validatePhone(EditText edt){
		boolean result;
		if (edt.getText().toString().trim().length() <= 0 || edt.getText().toString().length() < 9 || edt.getText().toString().length() > 10) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Should be 10 digits.");
			result = false;
		}else{
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}
	
	public static boolean validateText(EditText edt)
			throws NumberFormatException {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty.");
			result = false;
		} else if(edt.getText().toString().trim().length() <= 6){
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Username must be at least 6 characters long.");
			result = false;
		}else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}
	
	public static boolean validateNames(EditText edt)
			throws NumberFormatException {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty.");
			result = false;
		} else if(edt.getText().toString().trim().length() <= 3){
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Username must be at least 6 characters long.");
			result = false;
		}else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}
	
	public static boolean validateLocation(EditText edt)
			throws NumberFormatException {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty.");
			result = false;
		}else if(edt.getText().toString().trim().length() <= 4){
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Username must be at least 6 characters long.");
			result = false;
		}else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}
	
	public static boolean validateToken(EditText edt)
			throws NumberFormatException {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty.");
			result = false;
		}else if (edt.getText().toString().length() < 8){
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be less than 8 characters.");
			result = false;
		} else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}
	
	
	public static boolean isValidEmail(EditText edt) {
		Boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Cannot be empty");
			result = false;
		} else if (isEmailValid(edt.getText().toString()) == false) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Invalid Email Address");
			result = false;
		} else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result = true;
		}
		return result;
	}

	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	} // end of email matcher

	public static boolean isValidPassword(EditText edt) {
		boolean result=false;
		if (edt.getText().toString().trim().length() <= 0) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Enter Password!");
			result = false;
		} else if (edt.getText().toString().trim().length() < 6) {
			edt.setBackgroundColor(Color.parseColor("#FA8072"));
			edt.setError("Password must be 6 characters long!");
			result = false;
		} else {
			edt.setBackgroundColor(Color.parseColor("#98FB98"));
			result=true;
		}
		return result;
	}

	public static boolean isValidConfirmPwd(EditText pwd, EditText c_pwd) {
		boolean result=false;
		if (!(pwd.getText().toString().trim().length() == 0)
				&& !(c_pwd.getText().toString().trim().length() == 0)) {
			if (!pwd.getText().toString().equals(c_pwd.getText().toString())) {
				c_pwd.setBackgroundColor(Color.parseColor("#FA8072"));
				c_pwd.setError("Passwords must match!");
				result =  false;
			}else{
				c_pwd.setBackgroundColor(Color.parseColor("#98FB98"));
				result = true;
			}

		} else {
			if (pwd.getText().toString().trim().length() <= 0) {
				pwd.setBackgroundColor(Color.parseColor("#FA8072"));
				pwd.setError("Enter Password!");
				result=false;
			} else if (c_pwd.getText().toString().trim().length() <= 0) {
				c_pwd.setBackgroundColor(Color.parseColor("#FA8072"));
				c_pwd.setError("Enter Password Again!");
				result=false;
			}
		}
		return result;
	}

	public String Is_Valid_Sign_Number_Validation(int MinLen, int MaxLen,EditText edt) throws NumberFormatException {
		String valid_sign_number;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Sign Number Only");
			valid_sign_number = null;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_sign_number = null;
		} else {
			valid_sign_number = edt.getText().toString();
			// Toast.makeText(getApplicationContext(),
			// ""+edt.getText().toString(), Toast.LENGTH_LONG).show();
		}
		return valid_sign_number;

	} // END OF Edittext validation

	// phone_number.addTextChangedListener(new
	// PhoneNumberFormattingTextWatcher());

}
