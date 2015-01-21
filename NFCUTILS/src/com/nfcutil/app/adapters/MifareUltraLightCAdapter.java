package com.nfcutil.app.adapters;

import java.util.ArrayList;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nfcutils.R;
import com.nfcutil.app.entity.MifareUltraLightC;

public class MifareUltraLightCAdapter extends SectionedBaseAdapter {

	ArrayList<MifareUltraLightC> ultraLightCList = null;
	Context context;
	MifareUltraLightC ultraLightC;

	public MifareUltraLightCAdapter(Context _context, int textViewResourceId,
			List<MifareUltraLightC> _ultraLightCList) {
		ultraLightCList = (ArrayList<MifareUltraLightC>) _ultraLightCList;
		context = _context;
	}

	@Override
	public MifareUltraLightC getItem(int section, int position) {
		return ultraLightCList.get(position);
	}

	@Override
	public long getItemId(int section, int position) {
		return ultraLightCList.get(position).hashCode();
	}

	@Override
	public int getSectionCount() {
		return ultraLightCList.size();
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
			ultraLightC = ultraLightCList.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ultralightCView = inflater.inflate(
						R.layout.classic_1k_individual_item, null);
			}
			TextView tvPage1 = (TextView) ultralightCView.findViewById(R.id.tvPage1);
			TextView tvPage2 = (TextView) ultralightCView.findViewById(R.id.tvPage2);
			TextView tvPage3 = (TextView) ultralightCView.findViewById(R.id.tvPage3);
			TextView tvPage4 = (TextView) ultralightCView.findViewById(R.id.tvPage4);
			
			tvPage1.setText(ultraLightC.pagevalue1);
			tvPage2.setText(ultraLightC.pagevalue2);
			tvPage3.setText(ultraLightC.pagevalue3);
			tvPage4.setText(ultraLightC.pagevalue4);
			
			ultralightCView.setTag(ultralightCView);
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
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return ultralightCView;
	}

}
