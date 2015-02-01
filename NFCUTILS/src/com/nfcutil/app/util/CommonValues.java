package com.nfcutil.app.util;

import java.util.ArrayList;

import android.nfc.tech.MifareUltralight;

import com.nfcutil.app.entity.MifareClassic1k;
import com.nfcutil.app.entity.MifareUltraLightC;

public class CommonValues {
	public static CommonValues commonValues;
	
	public ArrayList<MifareClassic1k> mifareClassic1kList = new ArrayList<MifareClassic1k>();
	public ArrayList<MifareUltraLightC> mifareUltraLightCList = new ArrayList<MifareUltraLightC>();
	public ArrayList<MifareUltraLightC> mifareUltraLightList = new ArrayList<MifareUltraLightC>();
	
	public String Name;
	public String UID;
	public String Type;
	public String Memory;
	public String Sector;
	public String Block;
	public String ultraLightCPageSize;
	public String ultraLightCPageCount;
	
	public static void Initalization(){
		if(commonValues == null)
			commonValues = new CommonValues();
	}
	
	public static CommonValues getInstance(){
		return commonValues;
	}
}
