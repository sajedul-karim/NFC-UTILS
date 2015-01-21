package com.nfcutil.app.activity;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.nfcutils.R;
import com.nfcutil.app.adapters.MifareUltraLightCAdapter;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.entity.MifareUltraLightC;
import com.nfcutil.app.util.CommonValues;

public class MifareUltralightCActivity extends NFCUtilsBase implements OnItemClickListener {
	TextView tvUID, tvType, tvMemory, tvPage, tvBlock;
	PinnedHeaderListView lvMifareUltralLightC;
	MifareUltraLightCAdapter adapter;
	MifareUltraLightC mifareUltraLightC;
	
	@Override
	protected void onCreate(Bundle saveInstance) {
		super.onCreate(saveInstance);
		setContentView(R.layout.activity_mifare_ultralight_c);
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
	}

	private void Initalization() {
		tvUID = (TextView) findViewById(R.id.tvUID);
		tvType = (TextView) findViewById(R.id.tvType);
		tvMemory = (TextView) findViewById(R.id.tvMemory);
		tvPage = (TextView) findViewById(R.id.tvPage);
		tvBlock = (TextView) findViewById(R.id.tvBlock);
		lvMifareUltralLightC = (PinnedHeaderListView) findViewById(R.id.lvMifareUltralLightC);
		
		lvMifareUltralLightC.setOnItemClickListener(this);
		setValue();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		try{
			if((position%2) != 0){
				mifareUltraLightC = (MifareUltraLightC) view.getTag();
				showValue(mifareUltraLightC);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void setValue(){
		tvUID.setText(CommonValues.getInstance().UID);
		tvType.setText(CommonValues.getInstance().Type);
		tvMemory.setText(CommonValues.getInstance().Memory);
		tvPage.setText(CommonValues.getInstance().ultraLightCPageCount);
		tvBlock.setText(CommonValues.getInstance().ultraLightCPageSize);
	}
	
	private void setListValue(){
		adapter = new MifareUltraLightCAdapter(this, R.layout.classic_1k_individual_item, CommonValues.getInstance().mifareUltraLightCList);
		lvMifareUltralLightC.setAdapter(adapter);
	}
	
	private void showValue(MifareUltraLightC _mifareUltraLightC){
		Dialog dialog = new Dialog(this, AlertDialog.THEME_HOLO_LIGHT);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.nfc_write_dialog);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;	
		
		final Spinner spBlockNumber = (Spinner) dialog.findViewById(R.id.spBlockNumber);
		TextView value1 = (TextView) dialog.findViewById(R.id.value1);
		TextView value2 = (TextView) dialog.findViewById(R.id.value2);
		TextView value3 = (TextView) dialog.findViewById(R.id.value3);
		TextView value4 = (TextView) dialog.findViewById(R.id.value4);
		
		String[] value = {""+_mifareUltraLightC.block1, ""+_mifareUltraLightC.block2,""+_mifareUltraLightC.block3,""+_mifareUltraLightC.block4};
		spBlockNumber.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line	, value));
		
		value1.setText(_mifareUltraLightC.pagevalue1);
		value2.setText(_mifareUltraLightC.pagevalue2);
		value3.setText(_mifareUltraLightC.pagevalue3);
		value4.setText(_mifareUltraLightC.pagevalue4);
		
		spBlockNumber.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				String blockNumber = (String) spBlockNumber.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(), blockNumber, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				
			}
		});
		
		
		dialog.show();
		
	}
}
