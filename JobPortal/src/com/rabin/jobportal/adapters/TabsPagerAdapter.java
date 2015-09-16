package com.rabin.jobportal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		Fragment fragment = null;
		switch (position) {
		case 0:
			//fragment =  new JobseekerFragmentActivity();
			break;

		case 1:
			//fragment =  new EmployerFragmentActivity();
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
			title =  "JOBSEEKER";
			break;
			
		case 1: 
			title =  "EMPLOYER";
			break;
		}
		return title;
	}
	

}
