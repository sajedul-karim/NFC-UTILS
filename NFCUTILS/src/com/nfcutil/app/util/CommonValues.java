package com.nfcutil.app.util;

import java.util.ArrayList;

import com.nfcutil.app.entity.MifareClassic1k;

public class CommonValues {
	public static CommonValues commonValues;
	
	public ArrayList<MifareClassic1k> mifareClassic1kList = new ArrayList<MifareClassic1k>();
	
	public static void Initalization(){
		if(commonValues == null)
			commonValues = new CommonValues();
	}
	
	public static CommonValues getInstance(){
		return commonValues;
	}
}
