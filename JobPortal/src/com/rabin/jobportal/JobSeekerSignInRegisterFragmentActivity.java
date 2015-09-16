package com.rabin.jobportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class JobSeekerSignInRegisterFragmentActivity extends DrawerActivity{

	ViewPager jsPager;
	JSPagerAdapter mAdapter;
	PageIndicator mIndicator;

	static int job_id;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.fragment_activity_jobseeker);
		super.onCreate(arg0);
		
		
		Intent i = getIntent();
		
		if(i.getExtras() != null){
		job_id = Integer.parseInt(i.getStringExtra("job_id"));
		}
		//Toast.makeText(getApplicationContext(), Integer.toString(job_id), Toast.LENGTH_SHORT).show();
		
		jsPager = (ViewPager) findViewById(R.id.js_pager);
		mAdapter = new JSPagerAdapter(getSupportFragmentManager());

		jsPager.setAdapter(mAdapter);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(jsPager);

	}
	
}

class JSPagerAdapter extends FragmentPagerAdapter {

	public JSPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new SignInFragment().newInstance(JobSeekerSignInRegisterFragmentActivity.job_id);
			break;

		case 1:
			fragment = new RegisterFragment();
			break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		String title = "";
		switch (position) {
		case 0:
			title = "Sign In";
			break;

		case 1:
			title = "Register";
			break;
		}
		return title;
	}

}
