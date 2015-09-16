package com.rabin.jobportal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class EmployerSignInRegisterFragmentActivity extends BaseActivity {

	ViewPager jsPager;
	EmpPagerAdapter mAdapter;
	PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.fragment_activity_employer);


		jsPager = (ViewPager) findViewById(R.id.emp_pager);
		mAdapter = new EmpPagerAdapter(getSupportFragmentManager());

		jsPager.setAdapter(mAdapter);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(jsPager);
		
		
	}

}

class EmpPagerAdapter extends FragmentPagerAdapter {

	public EmpPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new SignInFragment();
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
