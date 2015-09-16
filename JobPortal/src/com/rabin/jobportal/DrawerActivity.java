package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DrawerActivity extends FragmentActivity {

	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;

	public CharSequence mDrawerTitle;

	public CharSequence mTitle;

	public String[] navMenuTitles;
	public TypedArray navMenuIcons;

	public ArrayList<NavDrawerItem> navDrawerItems;
	public NavDrawerListAdapter adapter;

	public JobSeekerSessionManager sm;

	LinearLayout profile_layout_li, profile_layout_lo, tapToSignUpLogin;
	TextView jobseeker_name, jobseeker_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.drawer_layout);

		sm = new JobSeekerSessionManager(getApplicationContext());
		mTitle = mDrawerTitle = getTitle();

		if (!sm.isLoggedIn()) {
			navMenuTitles = getResources().getStringArray(
					R.array.jp_nav_drawer_items);
		} else {
			navMenuTitles = getResources().getStringArray(
					R.array.jp_nav_drawer_items_logged_in);
		}

		if (!sm.isLoggedIn()) {
			navMenuIcons = getResources().obtainTypedArray(
					R.array.jp_nav_drawer_icons);
		} else {
			navMenuIcons = getResources().obtainTypedArray(
					R.array.jp_nav_drawer_icons_logged_in);
		}

		jobseeker_name = (TextView) findViewById(R.id.jobseeker_name);
		jobseeker_email = (TextView) findViewById(R.id.jobseeker_email);

		if (sm.isLoggedIn()) {
			jobseeker_name.setText(sm.getJsName());
			jobseeker_email.setText(sm.getJsEmail());
		}

		tapToSignUpLogin = (LinearLayout) findViewById(R.id.tapToSignUpLogin);

		tapToSignUpLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!sm.isLoggedIn()) {
					startActivity(new Intent(getApplicationContext(),
							JobSeekerSignInRegisterFragmentActivity.class));
					
				} else {
					Intent profileIntent = new Intent(getApplicationContext(),
							JobSeekerProfile.class);
					startActivity(profileIntent);
				}
				mDrawerLayout.closeDrawer(Gravity.START);
			}
		});

		profile_layout_li = (LinearLayout) findViewById(R.id.profile_layout_logged_in);
		profile_layout_lo = (LinearLayout) findViewById(R.id.profile_layout_logged_out);

		profile_layout_li.setVisibility(View.GONE);
		profile_layout_lo.setVisibility(View.GONE);

		if (sm.isLoggedIn()) {
			profile_layout_li.setVisibility(View.VISIBLE);
			profile_layout_lo.setVisibility(View.GONE);
		} else {
			profile_layout_li.setVisibility(View.GONE);
			profile_layout_lo.setVisibility(View.VISIBLE);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) mDrawerList
				.getLayoutParams();
		// params.width = getResources().getDisplayMetrics().widthPixels * 2/8 ;
		params.width = LayoutParams.MATCH_PARENT;
		mDrawerList.setLayoutParams(params);

		mDrawerList.setBackgroundColor(getResources().getColor(
				R.color.list_background));

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		navMenuIcons.recycle();

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View drawerView) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			};

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			};
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// displayView(0);
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	}

	private void displayView(int position) {
		// Fragment fragment = null;

		String classname = getClass().getSimpleName();

		if (!sm.isLoggedIn()) {

			switch (position) {
			case 0:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 0);
					startActivity(i);
					finish();
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(0);
				break;
			case 1:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 1);
					startActivity(i);
					finish();
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(1);
				break;
			case 2:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 2);
					startActivity(i);
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(2);
				break;
			case 3:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 3);
					startActivity(i);
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(3);
				break;
			case 4:
				if (!classname.equals("EmployerSection"))
					startActivity(new Intent(getApplicationContext(),
							EmployerSection.class));
				break;
			case 5:
				// fragment = new RegisterFragment();
				break;

			default:
				break;
			}
			// mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerLayout.closeDrawer(Gravity.START);
		} else {
			switch (position) {
			case 0:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 0);
					startActivity(i);
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(3);
				break;
			case 1:
				if (!classname.equals("JobSeekerProfile")) {
					Intent i = new Intent(getApplicationContext(),
							JobSeekerProfile.class);
					startActivity(i);
				}
				break;
			case 2:
				if (!classname.equals("Response")) {
					Intent i = new Intent(getApplicationContext(),
							Response.class);
					startActivity(i);
				}
				break;
			case 3:
				if (!classname.equals("JobsViewPagerFragmentActivity")) {
					Intent i = new Intent(getApplicationContext(),
							JobsViewPagerFragmentActivity.class);
					i.putExtra("position", 0);
					startActivity(i);
				} else
					JobsViewPagerFragmentActivity.mPager.setCurrentItem(0);
				break;
			case 4:
				if (!classname.equals("AppliedJobsActivity")) {
					Intent i = new Intent(getApplicationContext(),
							AppliedJobsActivity.class);
					startActivity(i);
				}
				break;

			case 5:
				if (!classname.equals("SavedJobsActivity")) {
					Intent i = new Intent(getApplicationContext(),
							SavedJobsActivity.class);
					startActivity(i);
				}
				break;

			case 6:
				if (!classname.equals("SetJobAlert")) {
					Intent i = new Intent(getApplicationContext(),
							SetJobAlert.class);
					startActivity(i);
				}
				break;

			case 7:
				// fragment = new RegisterFragment();
				sm.logoutUser();
				startActivity(new Intent(getApplicationContext(),
						JobsViewPagerFragmentActivity.class));
				break;

			default:
				break;
			}
			mDrawerLayout.closeDrawer(Gravity.START);

		}

		// if (fragment != null) {
		// FragmentManager fragmentManager = getFragmentManager();
		// fragmentManager.beginTransaction()
		// .replace(R.id.frame_container, fragment).commit();
		//
		// mDrawerList.setItemChecked(position, true);
		// mDrawerList.setSelection(position);
		// setTitle(navMenuTitles[position]);
		// mDrawerLayout.closeDrawer(mDrawerList);
		//
		// }else{
		// Log.e("MainActivity", "Error in creating fragment");
		// }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {

		case R.id.action_back:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	int backClickCount = 0;

	@Override
	public void onBackPressed() {
		backClickCount++;

		ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskList = mngr
				.getRunningTasks(10);

		if (taskList.get(0).numActivities == 1
				&& taskList.get(0).topActivity.getClassName().equals(
						"com.rabin.jobportal.JobsViewPagerFragmentActivity")) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					backClickCount = 0;
				}
			}, 2000);

			if (backClickCount == 1) {
				Toast.makeText(getApplicationContext(),
						"Press back again to exit.", Toast.LENGTH_SHORT).show();
			} else {
				backClickCount = 0;
				finish();
			}

		}else
		 super.onBackPressed();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.START);
		menu.findItem(R.id.action_back).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class SlideMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayView(position);
		}
	}

}
