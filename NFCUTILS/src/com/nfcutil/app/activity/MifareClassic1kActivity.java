package com.nfcutil.app.activity;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcutils.R;
import com.nfcutil.app.adapters.Classic1KAdapter;
import com.nfcutil.app.base.NFCUtilsBase;
import com.nfcutil.app.entity.MifareClassic1k;
import com.nfcutil.app.entity.MifareUltraLightC;
import com.nfcutil.app.util.CommonValues;

public class MifareClassic1kActivity extends NFCUtilsBase implements OnItemClickListener{
	TextView tvUID, tvType, tvMemory, tvPage, tvBlock;
	PinnedHeaderListView lvMifareClassic1k;
	Classic1KAdapter adapter;
	MifareClassic1k mifareClassic1k;
	
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
	}
	
	private void Initalization() {
		tvUID = (TextView) findViewById(R.id.tvUID);
		tvType = (TextView) findViewById(R.id.tvType);
		tvMemory = (TextView) findViewById(R.id.tvMemory);
		tvPage = (TextView) findViewById(R.id.tvPage);
		tvBlock = (TextView) findViewById(R.id.tvBlock);
		lvMifareClassic1k = (PinnedHeaderListView) findViewById(R.id.lvMifareClassic1k);
		
		lvMifareClassic1k.setOnItemClickListener(this);
		setValue();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		try{
			if((position%2) != 0){
				mifareClassic1k = (MifareClassic1k) view.getTag();
				Toast.makeText(this, mifareClassic1k.toString(), Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void setValue(){
		tvUID.setText(CommonValues.getInstance().UID);
		tvType.setText(CommonValues.getInstance().Type);
		tvMemory.setText(CommonValues.getInstance().Memory);
		tvPage.setText(CommonValues.getInstance().Block);
		tvBlock.setText(CommonValues.getInstance().Sector);
	}
	
	private void setListValue(){
		adapter = new Classic1KAdapter(this, R.layout.classic_1k_individual_item, CommonValues.getInstance().mifareClassic1kList);
		lvMifareClassic1k.setAdapter(adapter);
	}
}
