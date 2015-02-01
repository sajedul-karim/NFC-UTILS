package com.nfcutil.app.adapters;

import java.util.ArrayList;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

import com.nfcutils.app.R;
import com.nfcutil.app.entity.MifareClassic1k;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Classic1KAdapter extends SectionedBaseAdapter {

	ArrayList<MifareClassic1k> list = null;
	MifareClassic1k classic1k;
	Context context;
	
	
	
	/////kewfhwwkfrhwqkjrwqkjrgwqjgrjwgrjg

	public Classic1KAdapter(Context _context, 
			int textViewResourceId, List<MifareClassic1k> objects) {
		context = _context;
		list = (ArrayList<MifareClassic1k>) objects;
	}

	@Override
	public MifareClassic1k getItem(int section, int position) {
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		return 0;
	}

	@Override
	public int getSectionCount() {
		return list.size();
	}

	@Override
	public int getCountForSection(int section) {
		return 1;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		View mifareClassic1kView = convertView;

		try {
			classic1k = list.get(section);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mifareClassic1kView = inflater.inflate(
						R.layout.classic_1k_individual_item, null);
			}
			TextView tvPage1 = (TextView) mifareClassic1kView.findViewById(R.id.tvPage1);
			TextView tvPage2 = (TextView) mifareClassic1kView.findViewById(R.id.tvPage2);
			TextView tvPage3 = (TextView) mifareClassic1kView.findViewById(R.id.tvPage3);
			TextView tvPage4 = (TextView) mifareClassic1kView.findViewById(R.id.tvPage4);
			
			if(classic1k.Header==0){
				tvPage1.setTextColor(Color.RED);
				tvPage1.setTypeface(null, Typeface.BOLD);
			}else{
				tvPage1.setTextColor(Color.BLACK);
				tvPage1.setTypeface(null, Typeface.NORMAL);
			}
			
			tvPage1.setText(classic1k.Block1Value);
			tvPage2.setText(classic1k.Block2Value);
			tvPage3.setText(classic1k.Block3Value);
			tvPage4.setText(classic1k.Block4Value);
			
			mifareClassic1kView.setTag(classic1k);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return mifareClassic1kView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		View mifareClassic1kView = convertView;

		try {
			classic1k = list.get(section);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mifareClassic1kView = inflater.inflate(
						R.layout.header_layout, null);
			}
			TextView tvPage1 = (TextView) mifareClassic1kView.findViewById(R.id.textItem);
			
			tvPage1.setText("Sector : "+classic1k.Header);
			mifareClassic1kView.setTag(classic1k);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return mifareClassic1kView;
	}
}
