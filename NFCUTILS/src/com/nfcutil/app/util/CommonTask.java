package com.nfcutil.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;

import com.example.nfcutils.R;
import com.nfcutil.app.activity.HomeActivity;

public class CommonTask {
	static AlertDialog mDialog;

	public static void showWirelessSettingsDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.nfc_disabled);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);
					}
				});
		builder.create().show();
	}
	
	public static void showMessage(final Context _context,int title, int message) {
		mDialog = new AlertDialog.Builder(_context).setNeutralButton("Ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((HomeActivity)_context).finish();
			}
		}).create();
		mDialog.setTitle(title);
		mDialog.setMessage(_context.getText(message));
		mDialog.show();
	}
	
	public static String getHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
			if (i > 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
