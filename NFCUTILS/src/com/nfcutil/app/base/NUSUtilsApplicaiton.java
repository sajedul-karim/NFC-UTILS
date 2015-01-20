package com.nfcutil.app.base;

import com.nfcutil.app.util.CommonValues;

import android.app.Application;

public class NUSUtilsApplicaiton extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		initializaiton();
	}

	private void initializaiton() {
		CommonValues.Initalization();
	}
}
