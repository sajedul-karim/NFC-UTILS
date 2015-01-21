package com.nfcutil.app.activity;

import java.util.ArrayList;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nfcutils.R;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.entity.MifareUltraLightC;

public class MifareUltralightCActivity extends NFCUtilsBase {
	MifareUltralight ultralight;
	Bundle bundle;
	Tag tag;
	ArrayList<String> dataList = new ArrayList<String>();
	ArrayList<MifareUltraLightC> objList = new ArrayList<MifareUltraLightC>();
	TextView tvDataField;

	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_mifare_ultralight_c);
	}
}
