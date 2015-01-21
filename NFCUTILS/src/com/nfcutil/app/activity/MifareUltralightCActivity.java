package com.nfcutil.app.activity;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.nfcutils.R;
import com.nfcutil.app.adapters.MifareUltraLightCAdapter;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.util.CommonValues;

public class MifareUltralightCActivity extends NFCUtilsBase implements OnItemClickListener {
	TextView tvUID, tvType, tvMemory, tvPage, tvBlock;
	PinnedHeaderListView lvMifareUltralLightC;
	MifareUltraLightCAdapter adapter;
	
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
}
