package com.ihangjing.common;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

import com.ihangjing.ZJYXForAndroid.R;

public class AlertDialogUtils {
	public static AlertDialog createAlertDialog(Context paramContext,
			String paramString1, String paramString2) {
		AlertDialog localAlertDialog = new AlertDialog.Builder(paramContext)
				.create();
		localAlertDialog.show();

		localAlertDialog.setContentView(R.layout.alertdialog);
		TextView localTextView1 = (TextView) localAlertDialog
				.findViewById(R.id.alertmsg);
		TextView localTextView2 = (TextView) localAlertDialog
				.findViewById(R.id.alerttext);
		
		localTextView1.setText(paramString1);
		localTextView2.setText(paramString2);
		
		localAlertDialog.setCanceledOnTouchOutside(true);
		return localAlertDialog;
	}
}
