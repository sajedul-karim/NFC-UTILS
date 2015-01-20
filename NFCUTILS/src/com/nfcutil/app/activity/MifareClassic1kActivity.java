package com.nfcutil.app.activity;

import android.os.Bundle;

import com.example.nfcutils.R;
import com.nfcutil.app.base.NFCUtilsBase;

public class MifareClassic1kActivity extends NFCUtilsBase{
	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_mifare_classic_1k);
	}
}
