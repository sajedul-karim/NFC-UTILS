package com.skarim.app.activities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.example.nfcutils.R;
import com.skarim.app.utils.CommonTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.Settings;
import android.widget.LinearLayout.LayoutParams;

public class ReadCard extends Activity {

	TextView tvCardData;
	/*********** Card Related variable **********************/
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;

	private AlertDialog mDialog;
	
	LinearLayout llParentLinearlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_readcard);
		setContentViews();

		setNFCBackGround();
	}

	private void setNFCBackGround() {
		mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null)
				.create();

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			showMessage(R.string.error, R.string.no_nfc);
			finish();
			return;
		}

		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

	}

	private void setContentViews() {
		tvCardData = (TextView) findViewById(R.id.tvCardData);
		tvCardData.setMovementMethod(new ScrollingMovementMethod());
		llParentLinearlayout=(LinearLayout) findViewById(R.id.llParentLinearlayout);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				showWirelessSettingsDialog();
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

	private String getHex(byte[] bytes) {
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

	private String[] getTagInfo(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		// Tech List
		String[] techList = tag.getTechList();

		for (int i = 0; i < techList.length; i++) {
			if (techList[i].equals(MifareClassic.class.getName())) {
				// info[1] = "Mifare Classic";
				MifareClassic mifareClassicTag = MifareClassic.get(tag);

				// Type Info
				switch (mifareClassicTag.getType()) {
				case MifareClassic.TYPE_CLASSIC:
					MifareClassic mfc = MifareClassic.get(tag);
					// readMifareClassic(mfc);
					resolveIntentClassic(mfc);
					break;
				case MifareClassic.TYPE_PLUS:
					break;
				case MifareClassic.TYPE_PRO:
					break;
				}
			} else if (techList[i].equals(MifareUltralight.class.getName())) {
				// info[1] = "Mifare UltraLight";
				MifareUltralight mifareUlTag = MifareUltralight.get(tag);

				// Type Info
				switch (mifareUlTag.getType()) {
				case MifareUltralight.TYPE_ULTRALIGHT:
					break;
				case MifareUltralight.TYPE_ULTRALIGHT_C:
					readMifareUltraLightC(tag);
					break;
				}
				// info[0] += "Mifare " + type + "\n";
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

		return null;
	}

	private void showMessage(int title, int message) {
		mDialog.setTitle(title);
		mDialog.setMessage(getText(message));
		mDialog.show();
	}

	private void showWirelessSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.nfc_disabled);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						startActivity(intent);
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				});
		builder.create().show();
		return;
	}

	public void readMifareUltraLightC(Tag t) {
		llParentLinearlayout.removeAllViews();
		llParentLinearlayout.addView(tvCardData);
		tvCardData.setVisibility(View.VISIBLE);
		Log.d("skm",
				"===========Mifare Ultralight C Read Start==================");
		tvCardData.setText("\nReading Mifare Ultralight C ");
		Tag tag = t;
		String tagId = getHex(tag.getId());
		tvCardData.append("\nTag ID :" + tagId);
		Log.d("skm", "UID :" + tagId.trim());
		Log.d("skm", "UID length:" + tagId.length());

		MifareUltralight mifare = MifareUltralight.get(tag);
		Log.d("skm", "Tag Type :" + mifare.getType());
		tvCardData.append("\nTag Type :" + mifare.getType());
		try {
			mifare.connect();
			int count = 0;
			tvCardData.append("\n************* Page 0 to 3**************\n");
			for (int i = 0; i < 45; i++) {
				if (count == 4 & i != 44) {
					tvCardData.append("\n\n*************** Page " + (i) + " to "
							+ (i + 4 - 1) + " **************");
					count = 0;
				}
				byte[] payload = mifare.readPages(i);
				tvCardData.append("\n" + CommonTasks.getHexString(payload));
				new String(payload, Charset.forName("US-ASCII"));
				Log.d("skm",
						"Data on page : " + i + " is : "
								+ CommonTasks.getHexString(payload));
				count++;
			}

		} catch (IOException e) {
			Log.d("skm", e.getMessage());
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.d("skm", e.getMessage());
				}
			}
		}
	}

	private void resolveIntentClassic(MifareClassic _mfc) {

		List<TextView> ltvTotal=new ArrayList<TextView>();
		
		llParentLinearlayout.removeAllViews();
		MifareClassic mfc = _mfc;

		try {
			mfc.connect();
			
			TextView tv1 = new TextView(this);
			tv1.setTextSize(16);
			tv1.setTextColor(Color.RED);
			tv1.setTypeface(null,Typeface.ITALIC);
			tv1.setPadding(10, 10, 5, 0);
			tv1.setGravity(Gravity.LEFT);

			Log.d("skm", "");
			Log.d("skm", "== MifareClassic Info == ");
			tv1.setText("\n***********MifareClassic Info*********");
			Log.d("skm", "Size: " + mfc.getSize());
			tv1.append("\nCard Type :" + mfc.getType());
			tv1.append("\nMemory Size :" + mfc.getSize());
			Log.d("skm", "Type: " + mfc.getType());
			tv1.append("\nNumber of Sector :" + mfc.getSectorCount());
			Log.d("skm", "BlockCount: " + mfc.getBlockCount());
			tv1.append("\nNumber of Block in each sector :"
					+ mfc.getBlockCount());
			Log.d("skm", "MaxTransceiveLength: " + mfc.getMaxTransceiveLength());
			Log.d("skm", "SectorCount: " + mfc.getSectorCount());

			Log.d("skm", "Reading sectors...");
			
			ltvTotal.add(tv1);
			
			
			for (int i = 0; i < mfc.getSectorCount(); ++i) {
				
				/*LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 3, 0, 0);*/
				TextView tv2 = new TextView(this);
				tv2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				tv2.setTextSize(14);
				tv2.setTextColor(Color.BLACK);
				tv2.setPadding(5, 2, 5, 0);
				tv2.setTypeface(null, Typeface.BOLD);
				tv2.setGravity(Gravity.LEFT);
				

				if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
					Log.d("skm", "Authorized sector " + i + " with MAD key");
					tv2.setText("\nAuthorized sector " + i
							+ " with MAD key");
				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_DEFAULT)) {
					Log.d("skm", "Authorization granted to sector " + i
							+ " with DEFAULT key");
					tv2.append("\nAuthorized sector " + i
							+ " with DEFAULT key");
				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_NFC_FORUM)) {
					Log.d("skm", "Authorization granted to sector " + i
							+ " with NFC_FORUM key");
					tv2.append("\nAuthorized sector " + i
							+ " with NFC_FORUM key");
				} else {
					Log.d("skm", "Authorization denied to sector " + i);
					tv2.append("\nAuthorization denied to sector " + i
							+ "");
					continue;
				}
				
				ltvTotal.add(tv2);

				for (int k = 0; k < mfc.getBlockCountInSector(i); ++k) {
					
					TextView tv3 = new TextView(this);
					tv3.setTextSize(12);
					tv3.setTextColor(Color.BLUE);
					tv3.setPadding(10, 0, 5, 0);
					tv3.setGravity(Gravity.LEFT);
					
					int block = mfc.sectorToBlock(i) + k;
					byte[] data = null;

					try {

						data = mfc.readBlock(block);
					} catch (IOException e) {
						Log.d("skm",
								"Block " + block + " data: " + e.getMessage());
						tv3.append("\nBlock " + block + " data: "
								+ e.getMessage());
						continue;
					}

					String blockData = CommonTasks.getHexString(data);
					Log.d("skm", "Block " + block + " data: " + blockData);
					tv3.append("\n"+block+"->  " + blockData);
					ltvTotal.add(tv3);
				}
			}
			mfc.close();

		} catch (IOException e) {
			Log.d("skm", e.getMessage());
		} finally {
			try {
				mfc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<ltvTotal.size();i++){
			llParentLinearlayout.addView(ltvTotal.get(i));
		}
		

	}

}
