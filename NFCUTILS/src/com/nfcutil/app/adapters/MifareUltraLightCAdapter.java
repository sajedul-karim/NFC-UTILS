package com.nfcutil.app.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nfcutils.R;
import com.nfcutil.app.entity.MifareUltraLightC;

public class MifareUltraLightCAdapter extends ArrayAdapter<MifareUltraLightC> {

	ArrayList<MifareUltraLightC> ultraLightCList = null;
	Context context;
	MifareUltraLightC ultraLightC;

	public MifareUltraLightCAdapter(Context _context,int textViewResourceId,
			List<MifareUltraLightC> _ultraLightCList) {
		super(_context,textViewResourceId,_ultraLightCList);
		ultraLightCList = (ArrayList<MifareUltraLightC>) _ultraLightCList;
		context = _context;
	}
	
	@Override
	public int getCount() {
		return ultraLightCList.size();
	}
	
	@Override
	public MifareUltraLightC getItem(int position) {
		return ultraLightCList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View ultralightCView=convertView;
		
		try{
			ultraLightC=ultraLightCList.get(position);
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ultralightCView=inflater.inflate(R.layout.classic_1k_individual_item, null);
			}
			TextView tvPage1=(TextView) ultralightCView.findViewById(R.id.tvPage1);
			TextView tvPage2=(TextView) ultralightCView.findViewById(R.id.tvPage2);
			TextView tvPage3=(TextView) ultralightCView.findViewById(R.id.tvPage3);
			TextView tvPage4=(TextView) ultralightCView.findViewById(R.id.tvPage4);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return ultralightCView;
	}

}
