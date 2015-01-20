package com.nfcutil.app.activity;

import android.os.Bundle;

import com.example.nfcutils.R;
import com.nfcutil.app.base.NFCUtilsBase;

public class MifareUltralightCActivity extends NFCUtilsBase{
	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_mifare_ultralight_c);
	}
}
