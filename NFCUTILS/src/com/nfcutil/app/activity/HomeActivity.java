package com.nfcutil.app.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import com.example.nfcutils.R;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.util.CommonTask;
import com.skarim.app.utils.CommonTasks;

public class HomeActivity extends NFCUtilsBase {
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_home);
		Initialization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				CommonTask.showWirelessSettingsDialog(this);
			}
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}

	private void Initialization() {
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			CommonTask.showMessage(this, R.string.error, R.string.no_nfc);
			// finish();
			return;
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		resolveIntent(intent);
	}

	private void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			getTagInfo(intent);
		}
	}

	private void getTagInfo(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		String[] techList = tag.getTechList();
		for (int i = 0; i < techList.length; i++) {
			if (techList[i].equals(MifareClassic.class.getName())) {
				MifareClassic mifareClassicTag = MifareClassic.get(tag);
				switch (mifareClassicTag.getType()) {
				case MifareClassic.TYPE_CLASSIC:
					MifareClassic mfc = MifareClassic.get(tag);
					// resolveIntentClassic(mfc);
					break;
				case MifareClassic.TYPE_PLUS:
					break;
				case MifareClassic.TYPE_PRO:
					break;
				}
			} else if (techList[i].equals(MifareUltralight.class.getName())) {
				MifareUltralight mifareUlTag = MifareUltralight.get(tag);
				switch (mifareUlTag.getType()) {
				case MifareUltralight.TYPE_ULTRALIGHT:
					break;
				case MifareUltralight.TYPE_ULTRALIGHT_C:
					dialog = ProgressDialog.show(this, "",
							"Reading the Tag , Please wait...", true);
					
					dialog.show();
				
					readMifareUltraLightC(tag);
					break;
				}
			} else if (techList[i].equals(IsoDep.class.getName())) {
				// info[1] = "IsoDep";
				@SuppressWarnings("unused")
				IsoDep isoDepTag = IsoDep.get(tag);
				// info[0] += "IsoDep \n";
			} else if (techList[i].equals(Ndef.class.getName())) {
				Ndef.get(tag);
			} else if (techList[i].equals(NdefFormatable.class.getName())) {
				@SuppressWarnings("unused")
				NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
			}
		}
	}

	private void readMifareUltraLightC(Tag t) {
		ArrayList<String> dataList = new ArrayList<String>();
		Log.d("skm",
				"===========Mifare Ultralight C Read Start==================");
		Tag tag = t;
		String tagId;

		MifareUltralight mifare = MifareUltralight.get(tag);
		Log.d("skm", "Tag Type :" + mifare.getType());
		try {
			tagId = CommonTasks.getHexString(tag.getId());
			Log.d("skm", "UID :" + tagId.trim());
			mifare.connect();
			for (int i = 0; i < 45; i++) {

				byte[] payload = mifare.readPages(i);
				dataList.add(i, CommonTasks.getHexString(payload));
			}

		} catch (IOException e) {
			Log.d("skm", e.getMessage());
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
					dialog.dismiss();
				} catch (IOException e) {
					Log.d("skm", e.getMessage());
				}
			}
		}
		for(int j=0;j<dataList.size();j++)
		Log.d("skm", dataList.get(j));
		
		dialog.dismiss();
		if(dataList.size()>0){
			
			  Intent intent=new  Intent(this,MifareUltralightCActivity.class);
			  intent.putExtra("TAG_DATA", dataList); 
			  startActivity(intent);
			 
		}
	}
}
