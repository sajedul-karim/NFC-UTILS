package com.nfcutil.app.base;

import com.nfcutils.app.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class NFCUtilsBase extends FragmentActivity{
	ActionBar actionBar;
	@Override
	protected void onCreate(Bundle saveInstance) {
		setTheme(R.style.nfc_utils);
		super.onCreate(saveInstance);
		createActionBarStyle();
	}

	private void createActionBarStyle() {
		actionBar = getActionBar();
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("NFC Utils");
	}
}
