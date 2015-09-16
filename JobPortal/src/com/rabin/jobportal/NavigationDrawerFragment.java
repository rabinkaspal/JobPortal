package com.rabin.jobportal;

import java.util.ArrayList;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NavigationDrawerFragment extends Fragment {

	public String[] navMenuTitles;
	public TypedArray navMenuIcons;

	public ListView mDrawerList;
	public ArrayList<NavDrawerItem> navDrawerItems;
	public NavDrawerListAdapter adapter;

	public JobSeekerSessionManager sm;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_nav_drawer,
				container, true);

		sm = new JobSeekerSessionManager(getActivity());

		mDrawerList = (ListView) rootView.findViewById(R.id.list_slidermenu);

	
			navMenuTitles = getResources().getStringArray(
					R.array.jp_nav_drawer_items);
		
		
			navMenuIcons = getResources().obtainTypedArray(
					R.array.jp_nav_drawer_icons);
		

		mDrawerList.setBackgroundColor(getResources().getColor(
				R.color.list_background));

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		navMenuIcons.recycle();

		adapter = new NavDrawerListAdapter(getActivity(), navDrawerItems);
		mDrawerList.setAdapter(adapter);
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		
		return rootView;
			
	
	}
	
	private void displayView(int position) {
		// Fragment fragment = null;

		String classname = getClass().getSimpleName();

		if (!sm.isLoggedIn()) {

			switch (position) {
			case 0:
//				// fragment = new JobsViewPagerFragmentActivity();
//				if (!classname.equals("JobsViewPagerFragmentActivity"))
//					startActivity(new Intent(getApplicationContext(),
//							JobsViewPagerFragmentActivity.class));
//				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 1:
				// fragment = new RegisterFragment();
				break;
			case 2:
				// fragment = new RegisterFragment();
				break;
			case 3:
				// fragment = new RegisterFragment();
				break;
			case 4:
				// fragment = new RegisterFragment();
				break;
			case 5:
				// fragment = new RegisterFragment();
				break;

			default:
				break;
			}
		} else {
			switch (position) {
			case 0:
				// fragment = new JobsViewPagerFragmentActivity();
//				if (!classname.equals("JobsViewPagerFragmentActivity"))
//					startActivity(new Intent(getApplicationContext(),
//							JobsViewPagerFragmentActivity.class));
//				mDrawerLayout.closeDrawer(mDrawerList);
				break;
			case 1:
				// fragment = new RegisterFragment();
				break;
			case 2:
				// fragment = new RegisterFragment();
				break;
			case 3:
				// fragment = new RegisterFragment();
				break;
			case 4:
				// fragment = new RegisterFragment();
				break;
			case 5:
				// fragment = new RegisterFragment();
//				sm.logoutUser();
//				startActivity(new Intent(getApplicationContext(),
//						JobsViewPagerFragmentActivity.class));
//				mDrawerLayout.closeDrawer(mDrawerList);
				break;

			default:
				break;
			}

		}
	
}

	
	private class SlideMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayView(position);
		}
	}
	
}