package com.nfcutil.app.activity;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfcutil.app.adapters.Classic1KAdapter;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.entity.MifareClassic1k;
import com.nfcutil.app.util.CommonTask;
import com.nfcutil.app.util.CommonValues;
import com.nfcutil.app.util.NFCHammer;
import com.nfcutils.app.R;

public class MifareClassic1kActivity extends NFCUtilsBase implements
		OnItemClickListener, OnClickListener {
	TextView tvUID, tvType, tvMemory, tvPage, tvBlock, value1, value2, value3,
			value4;
	PinnedHeaderListView lvMifareClassic1k;
	Classic1KAdapter adapter;
	MifareClassic1k mifareClassic1k;

	LinearLayout llViewOfValue, llEditOfValue;
	EditText etNFCValue;
	RelativeLayout rlCancel, rlOK, rlshowvalue;
	Spinner spBlockNumber, spShowBlockValue;

	String selectedBlockNumber;
	int HeaderNmber;
	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	Tag tag;
	AlertDialog writeAlertDialog;
	ImageView iv_info;
	boolean isWriteDone = false;

	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_mifare_classic_1k);
		Initalization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListValue();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!isWriteDone){
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(MifareClassic1kActivity.this);
		}
		}
	}

	private void Initalization() {
		tvUID = (TextView) findViewById(R.id.tvUID);
		tvType = (TextView) findViewById(R.id.tvType);
		tvMemory = (TextView) findViewById(R.id.tvMemory);
		tvPage = (TextView) findViewById(R.id.tvPage);
		tvBlock = (TextView) findViewById(R.id.tvBlock);
		lvMifareClassic1k = (PinnedHeaderListView) findViewById(R.id.lvMifareClassic1k);
		iv_info = (ImageView) findViewById(R.id.iv_info);

		lvMifareClassic1k.setOnItemClickListener(this);
		iv_info.setOnClickListener(this);
		setValue();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		try {
			if ((position % 2) != 0) {
				mifareClassic1k = (MifareClassic1k) view.getTag();
				showValue(mifareClassic1k);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void showValue(final MifareClassic1k _mifareClassic1k) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("NFC Write Sector #" + _mifareClassic1k.Header);
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.write_1k_card, null);
		builder.setView(dialogView);
		final AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		spBlockNumber = (Spinner) dialogView.findViewById(R.id.spBlockNumber);
		spShowBlockValue = (Spinner) dialogView
				.findViewById(R.id.spShowBlockValue);
		value1 = (TextView) dialogView.findViewById(R.id.value1);
		value2 = (TextView) dialogView.findViewById(R.id.value2);
		value3 = (TextView) dialogView.findViewById(R.id.value3);
		value4 = (TextView) dialogView.findViewById(R.id.value4);
		llViewOfValue = (LinearLayout) dialogView
				.findViewById(R.id.llViewOfValue);
		llEditOfValue = (LinearLayout) dialogView
				.findViewById(R.id.llEditOfValue);
		etNFCValue = (EditText) dialogView.findViewById(R.id.etNFCValue);
		rlCancel = (RelativeLayout) dialogView.findViewById(R.id.rlCancel);
		rlOK = (RelativeLayout) dialogView.findViewById(R.id.rlOK);
		rlshowvalue = (RelativeLayout) dialogView
				.findViewById(R.id.rlshowvalue);

		if (_mifareClassic1k.Header == 0) {
			String[] valueOfULC = { "Select One",
					String.valueOf(_mifareClassic1k.block2),
					String.valueOf(_mifareClassic1k.block3) };
			spBlockNumber.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, valueOfULC));
		} else {
			String[] valueOfULC = { "Select One",
					String.valueOf(_mifareClassic1k.block1),
					String.valueOf(_mifareClassic1k.block2),
					String.valueOf(_mifareClassic1k.block3) };
			spBlockNumber.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, valueOfULC));
		}

		String[] convertOfULCValue = { "Hex Value", "Asci Value" };
		spShowBlockValue.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, convertOfULCValue));

		llViewOfValue.setVisibility(View.VISIBLE);
		llEditOfValue.setVisibility(View.GONE);

		if (_mifareClassic1k.Header != 0) {
			value1.setText(_mifareClassic1k.Block1Value);
			value2.setText(_mifareClassic1k.Block2Value);
			value3.setText(_mifareClassic1k.Block3Value);
		} else {
			value2.setText(_mifareClassic1k.Block2Value);
			value3.setText(_mifareClassic1k.Block3Value);
		}

		spShowBlockValue
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String value = (String) spShowBlockValue
								.getItemAtPosition(position);
						if (value.equals("Hex Value")) {
							if (_mifareClassic1k.Header != 0) {
								value1.setText(_mifareClassic1k.Block1Value);
								value2.setText(_mifareClassic1k.Block2Value);
								value3.setText(_mifareClassic1k.Block3Value);
							} else {
								value2.setText(_mifareClassic1k.Block2Value);
								value3.setText(_mifareClassic1k.Block3Value);
							}

						} else {
							if (_mifareClassic1k.Header != 0) {
								value1.setText(CommonTask
										.hexToAscii((_mifareClassic1k.Block1Value)));
								value2.setText(CommonTask
										.hexToAscii((_mifareClassic1k.Block2Value)));
								value3.setText(CommonTask
										.hexToAscii((_mifareClassic1k.Block3Value)));
							} else {
								value2.setText(CommonTask
										.hexToAscii((_mifareClassic1k.Block2Value)));
								value3.setText(CommonTask
										.hexToAscii((_mifareClassic1k.Block3Value)));
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spBlockNumber.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectedBlockNumber = (String) spBlockNumber
						.getItemAtPosition(position);
				if (!selectedBlockNumber.equals("Select One") && position != 0) {
					llViewOfValue.setVisibility(View.GONE);
					llEditOfValue.setVisibility(View.VISIBLE);
					rlshowvalue.setVisibility(View.GONE);
					if (_mifareClassic1k.Header == 0) {
						if ((position + 1) == 2) {
							etNFCValue.setText(_mifareClassic1k.Block2Value
									.equals("00000000000000000000000000000000") ? ""
									: CommonTask
											.hexToAscii(_mifareClassic1k.Block2Value));
						} else if ((position + 1) == 3) {
							etNFCValue.setText(_mifareClassic1k.Block3Value
									.equals("00000000000000000000000000000000") ? ""
									: CommonTask
											.hexToAscii(_mifareClassic1k.Block3Value));
						}
					} else {
						if (position == 1) {
							etNFCValue.setText(_mifareClassic1k.Block1Value
									.equals("00000000000000000000000000000000") ? ""
									: CommonTask
											.hexToAscii(_mifareClassic1k.Block1Value));
						} else if (position == 2) {
							etNFCValue.setText(_mifareClassic1k.Block2Value
									.equals("00000000000000000000000000000000") ? ""
									: CommonTask
											.hexToAscii(_mifareClassic1k.Block2Value));
						} else if (position == 3) {
							etNFCValue.setText(_mifareClassic1k.Block3Value
									.equals("00000000000000000000000000000000") ? ""
									: CommonTask
											.hexToAscii(_mifareClassic1k.Block3Value));
						}
					}

				} else {
					llViewOfValue.setVisibility(View.VISIBLE);
					llEditOfValue.setVisibility(View.GONE);
					rlshowvalue.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		rlCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.dismiss();
			}
		});

		rlOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selectedBlockNumber.equals("Select One")) {
					if (etNFCValue.getText().toString().length() != 16) {
						etNFCValue.setError("Please be add 16 Charecter!!!");
						return;
					}
					alertDialog.dismiss();
					writeMC1kValue(etNFCValue.getText().toString(),
							_mifareClassic1k.Header,
							Integer.parseInt(selectedBlockNumber));
				}
			}
		});
		alertDialog.show();

	}

	private void setValue() {
		tvUID.setText(CommonValues.getInstance().UID);
		tvType.setText(CommonValues.getInstance().Type);
		tvMemory.setText(CommonValues.getInstance().Memory + " bytes");
		tvPage.setText(CommonValues.getInstance().Block);
		tvBlock.setText(CommonValues.getInstance().Sector);
	}

	private void setListValue() {
		adapter = new Classic1KAdapter(this,
				R.layout.classic_1k_individual_item,
				CommonValues.getInstance().mifareClassic1kList);
		lvMifareClassic1k.setAdapter(adapter);
	}

	private void writeMC1kValue(String value, int header, int blocknumber) {
		try {
			AlertDialog.Builder writeBuilder = new AlertDialog.Builder(this,
					AlertDialog.THEME_HOLO_LIGHT);
			writeBuilder.setTitle("NFC Write");
			writeBuilder.setMessage("Please Tap the card!!!");
			writeBuilder.setCancelable(false);
			writeBuilder.setNegativeButton("Dismis",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mAdapter != null) {
								mAdapter.disableForegroundDispatch(MifareClassic1kActivity.this);
							}
							isWriteDone = true;
							dialog.dismiss();
						}
					});
			writeAlertDialog = writeBuilder.create();
			writeAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			HeaderNmber = header;
			mAdapter = NfcAdapter.getDefaultAdapter(this);
			if (mAdapter == null) {
				CommonTask.showMessage(this, R.string.error, R.string.no_nfc);
				// finish();
				return;
			}
			mPendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, getClass())
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			if (mAdapter != null) {
				if (!mAdapter.isEnabled()) {
					CommonTask.showWirelessSettingsDialog(this);
				}
				mAdapter.enableForegroundDispatch(this, mPendingIntent, null,
						null);
				isWriteDone=true;
			}
			writeAlertDialog.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		/*NFCDispatchDisable disable = new NFCDispatchDisable();
		disable.execute();*/
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
		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		String[] techList = tag.getTechList();
		for (int i = 0; i < techList.length; i++) {
			if (techList[i].equals(MifareClassic.class.getName())) {
				MifareClassic mifareClassicTag = MifareClassic.get(tag);
				switch (mifareClassicTag.getType()) {
				case MifareClassic.TYPE_CLASSIC:
					MifareClassic mfc = MifareClassic.get(tag);
					boolean result = NFCHammer.writeOnClassic1K(this, tag,
							etNFCValue.getText().toString(), "" + HeaderNmber,
							"" + selectedBlockNumber);
					if (result) {
						result = NFCHammer.ReadClassic1kValue(this, mfc);
						if (result) {
							adapter = new Classic1KAdapter(
									this,
									R.layout.classic_1k_individual_item,
									CommonValues.getInstance().mifareClassic1kList);
							isWriteDone=false;
							writeAlertDialog.dismiss();
							adapter.notifyDataSetChanged();
							isWriteDone = true;
						}
					}
					break;
				case MifareClassic.TYPE_PLUS:

					break;
				case MifareClassic.TYPE_PRO:

					break;
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.iv_info) {
			LayoutInflater inflater = getLayoutInflater();
			View dialoglayout = inflater.inflate(
					R.layout.info_dialog_layout_1k, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(dialoglayout);
			builder.show();
		}

	}
	
	public class NFCDispatchDisable extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			while(true){
				if(isWriteDone){
					if (mAdapter != null) {
						mAdapter.disableForegroundDispatch(MifareClassic1kActivity.this);
					}
					isWriteDone = false;
					break;
				}
			}
			
			return null;
		}
		
	}
}
