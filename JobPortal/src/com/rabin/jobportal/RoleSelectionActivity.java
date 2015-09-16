package com.rabin.jobportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoleSelectionActivity extends Activity {

	Button btnJobseeker, btnEmployer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role);
		
		
		btnJobseeker = (Button) findViewById(R.id.btnJobseeker);
		btnEmployer = (Button) findViewById(R.id.btnEmployer);
		
		
		btnJobseeker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), JobSeekerSignInRegisterFragmentActivity.class));
			}
		});
		
		
		btnEmployer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), EmployerSignInRegisterFragmentActivity.class));
			}
		});
		
		
	}
	
	
}
