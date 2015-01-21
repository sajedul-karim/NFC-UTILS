package com.nfcutil.app.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.nfcutils.R;
import com.nfcutil.app.entity.MifareClassic1k;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Classic1KAdapter extends ArrayAdapter<MifareClassic1k> {

	ArrayList<MifareClassic1k> list = null;
	MifareClassic1k classic1k;
	Context context;

	public Classic1KAdapter(Context _context, int resource,
			int textViewResourceId, List<MifareClassic1k> objects) {
		super(_context, resource, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<MifareClassic1k>) objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MifareClassic1k getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View classic1KView = convertView;

		try {
			classic1k = list.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				classic1KView = inflater.inflate(R.layout.classic_1k_individual_item, null);
			}
			TextView tvPage1=(TextView) classic1KView.findViewById(R.id.tvPage1);
			TextView tvPage2=(TextView) classic1KView.findViewById(R.id.tvPage2);
			TextView tvPage3=(TextView) classic1KView.findViewById(R.id.tvPage3);
			TextView tvPage4=(TextView) classic1KView.findViewById(R.id.tvPage4);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return classic1KView;
	}
}
