package com.nfcutil.app.activity;

import com.nfcutils.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				SplashScreenActivity.this.finish();
				Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}, 3000);
	}
}
