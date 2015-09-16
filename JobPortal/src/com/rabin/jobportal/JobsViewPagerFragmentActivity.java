package com.rabin.jobportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class JobsViewPagerFragmentActivity extends DrawerActivity {

	public static ViewPager mPager;
	public TabsPagerAdapter mAdapter;
	private PageIndicator mIndicator;
	public static JobSeekerSessionManager sm;
	
	int pos = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_js_logged_in);
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		sm = new JobSeekerSessionManager(getApplicationContext());

		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		mPager.setOffscreenPageLimit(10);
		mPager.setAdapter(mAdapter);
		
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		Intent i = getIntent();
		if(i.getExtras() != null){
		pos = i.getIntExtra("position", 0);
		}
		mPager.setCurrentItem(pos);
		
		

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mIndicator.onPageSelected(position);
				getActionBar().setTitle(mAdapter.getPageTitle(position));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
	}
}

class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new RecentJobsFragment()
					.newInstance((String) getPageTitle(position));
			break;

		case 1:
			fragment = new JobCategoryFragment();
			break;

		case 2:
			fragment = new CompaniesFragment();
			break;

		case 3:
			fragment = new SearchFragment();
			//fragment = new NavigationDrawerFragment();
			break;
		}

		return fragment;
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		String title = "";
		switch (position) {
		case 0:
			title = "Recent Jobs";
			break;

		case 1:
			title = "Jobs by Category";
			break;
		case 2:
			title = "Jobs by Company";
			break;
		case 3:
			title = "Search Jobs";
			break;
		}
		return title;
	}

}
