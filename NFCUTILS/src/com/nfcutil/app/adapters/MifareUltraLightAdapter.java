package com.nfcutil.app.adapters;

import java.util.ArrayList;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfcutils.app.R;
import com.nfcutil.app.entity.MifareUltraLightC;

public class MifareUltraLightAdapter extends SectionedBaseAdapter {

	ArrayList<MifareUltraLightC> ultraLightCList = null;
	Context context;
	MifareUltraLightC ultraLightC;

	public MifareUltraLightAdapter(Context _context, int textViewResourceId,
			List<MifareUltraLightC> _ultraLightCList) {
		ultraLightCList = (ArrayList<MifareUltraLightC>) _ultraLightCList;
		context = _context;
	}

	@Override
	public MifareUltraLightC getItem(int section, int position) {
		//return ultraLightCList.get(position);
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		//return ultraLightCList.get(position).hashCode();
		return 0;
	}

	@Override
	public int getSectionCount() {
		return 4;
	}

	@Override
	public int getCountForSection(int section) {
		return 1;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		View ultralightCView = convertView;

		try {
			ultraLightC = ultraLightCList.get(section);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ultralightCView = inflater.inflate(
						R.layout.ultralight_c_individual_item, null);
			}
			TextView tvPage1 = (TextView) ultralightCView.findViewById(R.id.tvPage1);
			TextView tvPage2 = (TextView) ultralightCView.findViewById(R.id.tvPage2);
			TextView tvPage3 = (TextView) ultralightCView.findViewById(R.id.tvPage3);
			TextView tvPage4 = (TextView) ultralightCView.findViewById(R.id.tvPage4);
			if(ultraLightC.Header.equals("Page 0 to 3") ){
				tvPage1.setTextColor(Color.RED);
				tvPage2.setTextColor(Color.RED);
				tvPage3.setTextColor(Color.RED);
				tvPage4.setTextColor(Color.RED);
			}else{
				tvPage1.setTextColor(Color.BLACK);
				tvPage2.setTextColor(Color.BLACK);
				tvPage3.setTextColor(Color.BLACK);
				tvPage4.setTextColor(Color.BLACK);
			}
			tvPage1.setText(ultraLightC.pagevalue1);
			tvPage2.setText(ultraLightC.pagevalue2);
			tvPage3.setText(ultraLightC.pagevalue3);
			tvPage4.setText(ultraLightC.pagevalue4);
			
			ultralightCView.setTag(ultraLightC);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return ultralightCView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		View ultralightCView = convertView;

		try {
			ultraLightC = ultraLightCList.get(section);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ultralightCView = inflater.inflate(
						R.layout.header_layout, null);
			}
			TextView tvPage1 = (TextView) ultralightCView.findViewById(R.id.textItem);
			
			tvPage1.setText(ultraLightC.Header);
			
			ultralightCView.setTag(ultraLightC);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return ultralightCView;
	}

}
