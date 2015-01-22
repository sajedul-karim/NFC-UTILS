package com.nfcutil.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	public static void showMessage(final Context _context, int title,
			int message) {
		mDialog = new AlertDialog.Builder(_context).setNeutralButton("Ok",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((HomeActivity) _context).finish();
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

	public static String hexToAscii(String hexValue) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hexValue.length(); i += 2) {
			String str = hexValue.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}
	
	public static void createToast(String message, Context _context, int color){
		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View tostLayout = inflater.inflate(R.layout.toast_design, null, false);
		
		//ImageView ivToastImage =(ImageView) tostLayout.findViewById(R.id.ivToastImage);
		TextView tvToastMessage = (TextView) tostLayout.findViewById(R.id.tvToastMessage);
		RelativeLayout rlToastBackGround = (RelativeLayout) tostLayout.findViewById(R.id.rlToastBackGround);
		
		//ivToastImage.setImageResource(resId);
		rlToastBackGround.setBackgroundColor(color);
		tvToastMessage.setText(message);
		
		int Y = _context.getResources().getDimensionPixelSize(R.dimen.action_bar_default_height);
		
		Toast toast = new Toast(_context);
		toast.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0, Y);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(tostLayout);
		toast.show();
	}
}
