package com.rabin.jobportal;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

public class JSONParser {

	Context mContext;
	
	public JSONParser() {
	}

	public static JSONObject getJSONFromURL(String url, final Context context) {
		InputStream is = null;
		String json = "";
		JSONObject jObj = null;

		// Download JSON data from URl
		try {
			HttpParams httpParams = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 10000;

			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

			HttpPost httpPost = new HttpPost(url);
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (NoHttpResponseException e) {
			Log.e("log_tag", "No response " + e.getMessage());
			
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setMessage("Connection Issue.");
			alert.setPositiveButton("Retry", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent i = ((Activity)context).getIntent();
					((Activity)context).startActivity(i);
				}
			}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Activity)context).onBackPressed();
				}
			}).show();
			

//			Intent i = new Intent(context, NoHttpConnection.class);
//			context.startActivity(i);
		} catch (Exception e) {
			Log.e("log_tag", "Error in http Connection" + e.getMessage());
			e.printStackTrace();
		}

		// convert response to string
		try {
			if (is != null) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} else {
				Log.i("1", "null stream show alert here.");
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setMessage("Connection Issue.");
				alert.setPositiveButton("Retry", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = ((Activity)context).getIntent();
						((Activity)context).startActivity(i);
					}
				}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity)context).onBackPressed();
					}
				}).show();
				
				
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result ");
			e.printStackTrace();
		}

		try {
			if (json.trim().length() > 0) {
				jObj = new JSONObject(json);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jObj;
	}

	public static JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		try {

			HttpParams httpParams = new BasicHttpParams();
			int timeoutConnection = 3000;
			int timeoutSocket = 10000;

			HttpConnectionParams.setConnectionTimeout(httpParams,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);

			if (method == "POST") {
				HttpClient httpClient = new DefaultHttpClient(httpParams);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse respose = httpClient.execute(httpPost);
				HttpEntity httpEntity = respose.getEntity();
				is = httpEntity.getContent();

			} else if (method == "GET") {
				HttpClient httpClient = new DefaultHttpClient(httpParams);
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				
				Log.d("search param", url);
				
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				is = httpEntity.getContent();

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = "";

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jObj;
	}

}
