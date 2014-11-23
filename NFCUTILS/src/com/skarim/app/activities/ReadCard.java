package com.skarim.app.activities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import com.example.nfcutils.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import java.text.DateFormat;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.provider.Settings;

public class ReadCard extends Activity {

	TextView tvCardData;
	private static final DateFormat TIME_FORMAT = SimpleDateFormat
			.getDateTimeInstance();

	/*********** Card Related variable **********************/
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;

	private AlertDialog mDialog;

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
					
			String[] info=getTagInfo(intent);
			
			tvCardData.append("\n");
			for(int i=0; i<info.length;i++) {
			    String someString = (String)info[i];
			    tvCardData.append(someString);
			}
		
		}
	}

	public void readTag(Tag t) {
		Tag tag = t;

		MifareUltralight mifare = MifareUltralight.get(tag);
		try {
			mifare.connect();
			byte[] payload = mifare.readPages(4);
			String pay = new String(payload, Charset.forName("US-ASCII"));
			Log.d("hwt", pay);
			tvCardData.append("\n\n*****" + TIME_FORMAT.format(new Date())
					+ "**********");
			tvCardData.append("\npayLoad : " + pay);
			
		} catch (IOException e) {
			Log.e("hwt",
					"IOException while writing MifareUltralight message...", e);
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.d("hwt", "Error closing tag...", e);
				}
			}
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
	    String prefix = "android.nfc.tech.";
	    String[] info = new String[2];

	    // UID
	    byte[] uid = tag.getId();
	    info[0] = "UID In Hex: " + getHex(uid) + "\n";

	    // Tech List
	    String[] techList = tag.getTechList();
	    String techListConcat = "Technologies: ";
	    for(int i = 0; i < techList.length; i++) {
	        techListConcat += techList[i].substring(prefix.length()) + ",";
	    }
	    info[0] += techListConcat.substring(0, techListConcat.length() - 1) + "\n\n";

	    // Mifare Classic/UltraLight Info
	    info[0] += "Card Type: ";
	    String type = "Unknown";
	    for(int i = 0; i < techList.length; i++) {
	        if(techList[i].equals(MifareClassic.class.getName())) {
	            info[1] = "Mifare Classic";
	            MifareClassic mifareClassicTag = MifareClassic.get(tag);

	            // Type Info
	            switch (mifareClassicTag.getType()) {
	            case MifareClassic.TYPE_CLASSIC:
	                type = "Classic";
	                break;
	            case MifareClassic.TYPE_PLUS:
	                type = "Plus";
	                break;
	            case MifareClassic.TYPE_PRO:
	                type = "Pro";
	                break;
	            }
	            info[0] += "Mifare " + type + "\n";

	            // Size Info
	            info[0] += "Size: " + mifareClassicTag.getSize() + " bytes \n" +
	                    "Sector Count: " + mifareClassicTag.getSectorCount() + "\n" +
	                    "Block Count: " + mifareClassicTag.getBlockCount() + "\n";
	        } else if(techList[i].equals(MifareUltralight.class.getName())) {
	            info[1] = "Mifare UltraLight";
	            MifareUltralight mifareUlTag = MifareUltralight.get(tag);

	            // Type Info
	            switch (mifareUlTag.getType()) {
	            case MifareUltralight.TYPE_ULTRALIGHT:
	                type = "Ultralight";
	                break;
	            case MifareUltralight.TYPE_ULTRALIGHT_C:
	                type = "Ultralight C";
	                readTag(tag);
	                break;
	            }
	            info[0] += "Mifare " + type + "\n";
	        } else if(techList[i].equals(IsoDep.class.getName())) {
	            info[1] = "IsoDep";
	            IsoDep isoDepTag = IsoDep.get(tag);
	            info[0] += "IsoDep \n";
	        } else if(techList[i].equals(Ndef.class.getName())) {
	            Ndef ndefTag = Ndef.get(tag);
	            info[0] += "Is Writable: " + ndefTag.isWritable() + "\n" +
	                    "Can Make ReadOnly: " + ndefTag.canMakeReadOnly() + "\n";
	        } else if(techList[i].equals(NdefFormatable.class.getName())) {
	            NdefFormatable ndefFormatableTag = NdefFormatable.get(tag);
	            }
	    } 

	    return info;
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

}
