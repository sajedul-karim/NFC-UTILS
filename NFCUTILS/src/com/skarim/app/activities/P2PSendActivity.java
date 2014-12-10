package com.skarim.app.activities;

import com.example.nfcutils.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.EditText;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;
import java.nio.charset.Charset;


public class P2PSendActivity extends Activity implements CreateNdefMessageCallback {
    NfcAdapter mNfcAdapter;
    TextView textView;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_beam);
        editText = (EditText) findViewById(R.id.etMessage);
        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = (editText.getText().toString()+"\n\n" + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/skarim.nfc.beam", text.getBytes())
        });
        return msg;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
    	
        textView = (TextView) findViewById(R.id.tvMessage);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }
    
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

	@SuppressLint("NewApi")
	private static NdefRecord createMime(String mimeType, byte[] mimeData) {
		if (mimeType == null)
			throw new NullPointerException("mimeType is null");

		// We only do basic MIME type validation: trying to follow the
		// RFCs strictly only ends in tears, since there are lots of MIME
		// types in common use that are not strictly valid as per RFC rules
		mimeType = Intent.normalizeMimeType(mimeType);
		if (mimeType.length() == 0)
			throw new IllegalArgumentException("mimeType is empty");
		int slashIndex = mimeType.indexOf('/');
		if (slashIndex == 0)
			throw new IllegalArgumentException("mimeType must have major type");
		if (slashIndex == mimeType.length() - 1) {
			throw new IllegalArgumentException("mimeType must have minor type");
		}
		// missing '/' is allowed

		// MIME RFCs suggest ASCII encoding for content-type
		byte[] typeBytes = mimeType.getBytes(US_ASCII);
		return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, typeBytes, null,
				mimeData);
	}
    
}
