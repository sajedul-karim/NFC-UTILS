package com.nfcutil.app.activity;

import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.Toast;

import com.example.nfcutils.R;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.entity.MifareClassic1k;
import com.nfcutil.app.entity.MifareUltraLightC;
import com.nfcutil.app.util.CommonTask;
import com.nfcutil.app.util.CommonValues;
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
					getMifareClassic1KData(mfc);
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
		// ArrayList<String> dataList = new ArrayList<String>();
		CommonValues.getInstance().mifareUltraLightCList.clear();
		Log.d("skm",
				"===========Mifare Ultralight C Read Start==================");
		Tag tag = t;
		String tagId;
		MifareUltraLightC mifareUltraLightC;
		MifareUltralight mifare = MifareUltralight.get(tag);
		Log.d("skm", "Tag Type :" + mifare.getType());
		CommonValues.getInstance().Type=""+mifare.getType();
		try {
			tagId = CommonTasks.getHexString(tag.getId());
			Log.d("skm", "UID :" + tagId.trim());
			CommonValues.getInstance().Name="Mifare UltraLight C";
			CommonValues.getInstance().UID=tagId;
			CommonValues.getInstance().Memory="192 bytes";
			CommonValues.getInstance().ultraLightCPageSize=""+mifare.PAGE_SIZE;
			CommonValues.getInstance().ultraLightCPageCount="44";
			mifare.connect();
			for (int i = 0; i < 11; i++) {
				mifareUltraLightC = new MifareUltraLightC();
				mifareUltraLightC.Header = "Page " + (i * 4) + " to "
						+ (((i + 1) * 4) - 1);
				mifareUltraLightC.pagevalue1 = CommonTasks.getHexString(mifare
						.readPages(i * 4));
				mifareUltraLightC.pagevalue2 = CommonTasks.getHexString(mifare
						.readPages(((i * 4) + 1)));
				mifareUltraLightC.pagevalue3 = CommonTasks.getHexString(mifare
						.readPages(((i * 4) + 2)));
				mifareUltraLightC.pagevalue4 = CommonTasks.getHexString(mifare
						.readPages(((i * 4) + 3)));
				CommonValues.getInstance().mifareUltraLightCList
						.add(mifareUltraLightC);
			
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

		dialog.dismiss();
		if (CommonValues.getInstance().mifareUltraLightCList.size() == 11) {

			Intent intent = new Intent(this, MifareUltralightCActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		}else{
			Toast.makeText(this, "Please Hold your card again! Until Progress bar will be finished!", Toast.LENGTH_LONG).show();
		}
	}

	public void getMifareClassic1KData(MifareClassic _mfc) {

		MifareClassic mfc = _mfc;
		MifareClassic1k classic1k;
		try {
			mfc.connect();

			Log.d("skm", "== MifareClassic Info == ");
			CommonValues.getInstance().Name="Mifare Classic 1K";
			Log.d("skm", "Size: " + mfc.getSize());
			CommonValues.getInstance().Memory=""+mfc.getSize();
			Log.d("skm", "Type: " + mfc.getType());
			CommonValues.getInstance().Type=""+mfc.getType();
			Log.d("skm", "BlockCount: " + mfc.getBlockCount());
			CommonValues.getInstance().Block=""+mfc.getBlockCount();

			Log.d("skm", "MaxTransceiveLength: " + mfc.getMaxTransceiveLength());
			Log.d("skm", "SectorCount: " + mfc.getSectorCount());
			CommonValues.getInstance().Sector=""+mfc.getSectorCount();
			CommonValues.getInstance().UID=CommonTasks.getHexString(mfc.getTag().getId());

			Log.d("skm", "Reading sectors...");

			for (int i = 0; i < mfc.getSectorCount(); ++i) {
				classic1k = new MifareClassic1k();
				if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
					Log.d("skm", "Authorized sector " + i + " with MAD key");
					classic1k.Header = i;

				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_DEFAULT)) {
					classic1k.Header = i;
					Log.d("skm", "Authorization granted to sector " + i
							+ " with DEFAULT key");

				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_NFC_FORUM)) {
					classic1k.Header = i;
					Log.d("skm", "Authorization granted to sector " + i
							+ " with NFC_FORUM key");

				} else {
					Log.d("skm", "Authorization denied to sector " + i);

					continue;
				}


				for (int k = 0; k < mfc.getBlockCountInSector(i); ++k) {
					int block = mfc.sectorToBlock(i) + k;
					byte[] data = null;

					try {

						data = mfc.readBlock(block);
					} catch (IOException e) {
						Log.d("skm",
								"Block " + block + " data: " + e.getMessage());
						continue;
					}
					String blockData = CommonTasks.getHexString(data);

					switch (k) {
					case 0:
						classic1k.Block1Value = blockData;
						break;
					case 1:
						classic1k.Block2Value = blockData;
						break;
					case 2:
						classic1k.Block3Value = blockData;
						break;
					case 3:
						classic1k.Block4Value = blockData;
						break;

					}
					Log.d("skm", "Block " + block + " data: " + blockData);
				}
				CommonValues.getInstance().mifareClassic1kList.add(classic1k);
			}

		} catch (Exception exception) {
			exception.printStackTrace();

		}
		
		if(CommonValues.getInstance().mifareClassic1kList.size()>0){
			Intent intent = new Intent(this, MifareClassic1k.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

	}
}
