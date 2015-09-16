package com.rabin.jobportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	
	private static int SPLASH_TIME_OUT = 1500;
	SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.activity_splash);
		
		session = new SessionManager(getApplicationContext());
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				//startActivity(new Intent(getApplicationContext(), LoadNews.class));
				//check for who is logged in
				//if jobseeker is logged in go to JobsViewPagerFragmentActivity
				//if employer is logged in go to EmployerSection.java
				int user_type = Integer.parseInt(session.getUserType());
				
				if(user_type==1)
					startActivity(new Intent(getApplicationContext(), EmployerSection.class));
				else if(user_type == 0)
					startActivity(new Intent(getApplicationContext(), JobsViewPagerFragmentActivity.class));
				finish();
			}
			
			
		}, SPLASH_TIME_OUT);
		
	}

	
}
