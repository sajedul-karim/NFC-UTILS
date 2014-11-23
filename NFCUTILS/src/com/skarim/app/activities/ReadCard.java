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
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.provider.Settings;

public class ReadCard extends Activity{
	
	TextView tvCardData;
	private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
	
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
		 mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

	        mAdapter = NfcAdapter.getDefaultAdapter(this);
	        if (mAdapter == null) {
	            showMessage(R.string.error, R.string.no_nfc);
	            finish();
	            return;
	        }

	        mPendingIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
	}

	private void setContentViews() {
		tvCardData=(TextView) findViewById(R.id.tvCardData);
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
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                readTag(tag);
               /* byte[] payload = readTag(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };*/
            }
          
        }
    }
	
	public void readTag(Parcelable p) {
		Tag tag = (Tag) p;
		byte[] id = tag.getId();
		tvCardData.append("\n\nUID : "+getHex(id));
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            String pay=new String(payload, Charset.forName("US-ASCII"));
            Log.d("hwt", pay);
            tvCardData.append("\npayLoad : "+pay);
            tvCardData.append("\n*****"+TIME_FORMAT.format(new Date())+"******");
        } catch (IOException e) {
            Log.e("hwt", "IOException while writing MifareUltralight message...", e);
        } finally {
            if (mifare != null) {
               try {
                   mifare.close();
               }
               catch (IOException e) {
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
	
	 private void showMessage(int title, int message) {
	        mDialog.setTitle(title);
	        mDialog.setMessage(getText(message));
	        mDialog.show();
	    }
	 private void showWirelessSettingsDialog() {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(R.string.nfc_disabled);
	        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogInterface, int i) {
	                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	                startActivity(intent);
	            }
	        });
	        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogInterface, int i) {
	                finish();
	            }
	        });
	        builder.create().show();
	        return;
	    }

}
