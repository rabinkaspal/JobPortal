package com.rabin.jobportal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public  class AlertDialogManager {

	
	
	public static void showAlertDialogGoBack(final Context context, String title, String message){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity)context);
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).onBackPressed();
				
			}
		});
		alertDialog.show();
}
	
}
