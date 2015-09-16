package com.rabin.jobportal;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class EmployerSection extends Activity {

	android.webkit.WebView wv;
	EmployerSessionManager emp_session;
	SessionManager session;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.webview);

		emp_session = new EmployerSessionManager(getApplicationContext());
		session = new SessionManager(getApplicationContext());
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);

		wv = (android.webkit.WebView) findViewById(R.id.webView);

		wv.addJavascriptInterface(new JavaScriptInterface(this), "Android");

		wv.setWebViewClient(new MyWebViewClient());
		wv.getSettings().setJavaScriptEnabled(true);

		wv.getSettings().setDomStorageEnabled(true);

		wv.loadUrl(AppConstants.HOST_ADDRESS + "job_portal/admin/dashboard.php");

	}

	public class JavaScriptInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void showToast(String toast) {
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void setSession() {
			emp_session.createLoginSession();
			session.loginEmployer();
			wv.clearHistory();
			wv.clearFormData();
			wv.clearCache(true);
			
			Intent intent = new Intent(getApplicationContext(), EmployerSection.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			
		}
		
		@JavascriptInterface
		public void logOut() {
			Toast.makeText(mContext, "Logge Out Now", Toast.LENGTH_SHORT).show();
			emp_session.logoutUser();
			session.logoutUser();
			wv.clearHistory();
			wv.clearFormData();
			wv.clearCache(true);
		}

		@JavascriptInterface
		public void GoBack(final int loggedIn) {

			Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setMessage("\nAre you sure you want to exit?\n");
			dialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							wv.clearHistory();
							wv.clearFormData();
							wv.clearCache(true);
							wv.destroy();

							if (loggedIn == 0) {
								CookieManager.getInstance()
										.removeSessionCookie();
								emp_session.logoutUser();
								session.logoutUser();
								Intent intent = new Intent(mContext,
										JobsViewPagerFragmentActivity.class);
								// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(new Intent(intent));
								finish();
							} else if (loggedIn == 1) {
								finishAffinity();
								// Intent intent = new
								// Intent(Intent.ACTION_MAIN);
								// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								// intent.addCategory(Intent.CATEGORY_HOME);
								// startActivity(intent);
							}
						}
					});

			dialog.setNegativeButton("Cancel", null);
			dialog.show();

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			finish();
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
						"com.rabin.jobportal.EmployerSection")) {

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

		} else
			super.onBackPressed();
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(android.webkit.WebView view,
				String url) {
			view.loadUrl(url);
			return true;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		}
		// If it wasn't the Back key or there's no web page history, bubble up
		// to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

}
