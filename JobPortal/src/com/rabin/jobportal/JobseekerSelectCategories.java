package com.rabin.jobportal;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class JobseekerSelectCategories extends DrawerActivity {

	ListView lv_joblistings;
	static ArrayList<Categories> categories;
	CategorySelectListViewAdapter mAdapter;

	Button btnNext;

	JSONObject jsonObject;
	JSONArray jsonArray;

	final String url_categories = AppConstants.HOST_ADDRESS
			+ "job_portal/get_all_categories.php";

	String cat_likes = "";
	JobSeeker js, jobseeker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.categories_likes_listview);
		super.onCreate(savedInstanceState);

		js = new JobSeeker();
		js = (JobSeeker) getIntent().getSerializableExtra("jobseeker");

		categories = new ArrayList<Categories>();

		btnNext = (Button) findViewById(R.id.btnNext);

		new DownloadCategories().execute();
		lv_joblistings = (ListView) findViewById(R.id.lv_joblisting);
		mAdapter = new CategorySelectListViewAdapter(getApplicationContext()
				.getApplicationContext(), categories);

		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (Categories p : mAdapter.getBox()) {
					if (p.box) {
						if (cat_likes.equals("")) {
							cat_likes = Integer.toString(p.cat_id);
						} else {
							cat_likes += "," + Integer.toString(p.cat_id);
						}
					}
				}

				if (cat_likes.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Select at least one category.", Toast.LENGTH_SHORT)
							.show();
				} else {
					jobseeker = new JobSeeker(js.username, js.password,
							cat_likes);
					Intent intent = new Intent(getApplicationContext(),
							RegisterJSDetails.class);
					intent.putExtra("jobseeker", jobseeker);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				JobseekerSelectCategories.this);
		alertDialog.setTitle("Caution");
		alertDialog.setCancelable(false);
		alertDialog.setMessage("\nYour data will not be saved. Go Back?\n");
		alertDialog.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				}).setNegativeButton("Cancel",null);
		alertDialog.show();
	}

	private class DownloadCategories extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			jsonObject = JSONParser.getJSONFromURL(url_categories,
					getApplicationContext());
			Log.e("asdf", jsonObject.toString());

			try {
				int success = jsonObject.getInt("success");
				if (success == 1) {
					jsonArray = jsonObject.getJSONArray("categories");

					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject = jsonArray.getJSONObject(i);

						Categories category = new Categories(
								Integer.parseInt(jsonObject.getString("cat_id")),
								jsonObject.getString("cat_name"), false);
						categories.add(category);
					}
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			lv_joblistings.setAdapter(mAdapter);
		}

	}

}

class CategorySelectListViewAdapter extends BaseAdapter {

	ArrayList<Categories> data;
	Context mContext;
	LayoutInflater inflater;

	public CategorySelectListViewAdapter(Context context,
			ArrayList<Categories> data) {
		this.mContext = context;
		this.data = data;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.select_category_likes_list_item, null);

			holder.category = (TextView) convertView
					.findViewById(R.id.categoryName);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.category.setText(data.get(position).cat_name);
		holder.checkBox.setChecked(data.get(position).box);
		holder.checkBox.setOnCheckedChangeListener(myCheckChangList);
		holder.checkBox.setTag(position);

		return convertView;
	}

	Categories getProduct(int position) {
		return ((Categories) getItem(position));
	}

	ArrayList<Categories> getBox() {
		ArrayList<Categories> box = new ArrayList<Categories>();
		for (Categories p : data) {
			if (p.box)
				box.add(p);
		}
		return box;
	}

	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getProduct((Integer) buttonView.getTag()).box = isChecked;
		}
	};

	static class ViewHolder {
		TextView category;
		CheckBox checkBox;
	}

}
