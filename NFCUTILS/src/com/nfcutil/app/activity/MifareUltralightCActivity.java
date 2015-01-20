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
		tvDataField = (TextView) findViewById(R.id.tvDataField);
		//parseIntent(getIntent().getExtras());
	}

	/*private void parseIntent(Bundle _bundle) {
		dataList = _bundle.getStringArrayList("TAG_DATA");
		MifareUltraLightC c;
		for (int i = 0; i < dataList.size(); i++) {
			c = new MifareUltraLightC();
			c.Header = i;
			c.pagevalue = dataList.get(i);
			objList.add(c);
		}
		dataList.clear();

	}*/
}
