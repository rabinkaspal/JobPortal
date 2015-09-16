package com.rabin.jobportal;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResults extends BaseActivity {
	String category_name;
	String cat_id;
	TextView txt_category_name;
	ListView lv_jobs;
	RecentJobsAdapter mAdapter;
	
	ArrayList<HashMap<String, String>> data, moreData;
	Context mContext;
	LinearLayout loadingView;
	final String url_searchjobs =  "";
	int rowCount;
	//page=1&cat_id=15
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdapter.notifyDataSetChanged();
		//Toast.makeText(mContext, "resumed", Toast.LENGTH_SHORT).show();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_job_by_category);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		
		data = new ArrayList<HashMap<String,String>>();
		
		Intent intent = getIntent();
		
		data = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("searched_jobs");
		
		rowCount = intent.getIntExtra("rowCount", 0);
		
		txt_category_name = (TextView) findViewById(R.id.category_name);
		txt_category_name.setText(rowCount + " Jobs found");

		
		mContext = getApplicationContext();

		
		loadingView = (LinearLayout) findViewById(R.id.loadingView);
		loadingView.setVisibility(View.GONE);

		
		lv_jobs = (ListView) findViewById(R.id.lv_jobs);
		mAdapter = new RecentJobsAdapter(mContext, data, R.layout.appied_saved_jobs_list_item, getClass().getSimpleName());
		
		lv_jobs.setAdapter(mAdapter);
		
//		lv_jobs.setOnScrollListener(new EndlessScrollListener(loadingView) {
//
//			@Override
//			public void onLoadMore(int page, int totalItemsCount) {
//				//new FetchJobs().execute(url_data, Integer.toString(page), cat_id);
//			}
//		});
		
	}

	
}
