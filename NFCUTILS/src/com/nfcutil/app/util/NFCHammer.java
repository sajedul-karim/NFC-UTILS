package com.nfcutil.app.util;

import java.io.IOException;
import com.nfcutil.app.entity.MifareClassic1k;
import com.nfcutil.app.entity.MifareUltraLightC;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

public class NFCHammer {
	
	
	public static boolean readUltraLightValue(Context context,Tag t){
		CommonValues.getInstance().mifareUltraLightList.clear();
		
		Log.d("skm",
				"===========Mifare Ultralight C Read Start==================");
		Tag tag = t;
		MifareUltraLightC mifareUltraLightC;
		String tagId;
		MifareUltralight mifare = MifareUltralight.get(tag);
		Log.d("skm", "Tag Type :" + mifare.getType());
		CommonValues.getInstance().Type = "" + mifare.getType();
		try {
			tagId = CommonTask.getHexString(tag.getId());
			Log.d("skm", "UID :" + tagId.trim());
			CommonValues.getInstance().Name = "Mifare UltraLight";
			CommonValues.getInstance().UID = tagId;
			CommonValues.getInstance().Memory = "64 bytes";
			CommonValues.getInstance().ultraLightCPageSize = ""
					+ mifare.PAGE_SIZE;
			CommonValues.getInstance().ultraLightCPageCount = "16";
			mifare.connect();
			int maxSize = mifare.getMaxTransceiveLength();
			Log.d("skm", "max length:" + maxSize);
			for (int i = 0; i < 4; i++) {
				mifareUltraLightC = new MifareUltraLightC();
				mifareUltraLightC.Header = "Page " + (i * 4) + " to "
						+ (((i + 1) * 4) - 1);

				mifareUltraLightC.pagevalue1 = CommonTask.getHexString(
						mifare.readPages(i * 4)).substring(0, 8);
				mifareUltraLightC.pagevalue2 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 1))).substring(0, 8);
		
				mifareUltraLightC.pagevalue3 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 2))).substring(0, 8);
				mifareUltraLightC.pagevalue4 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 3))).substring(0, 8);
				mifareUltraLightC.block1 = (i * 4);
				mifareUltraLightC.block2 = ((i * 4) + 1);
				mifareUltraLightC.block3 = ((i * 4) + 2);
				mifareUltraLightC.block4 = ((i * 4) + 3);
				CommonValues.getInstance().mifareUltraLightList
						.add(mifareUltraLightC);

			}

		} catch (IOException e) {
			// Log.d("skm", e.getMessage());
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.d("skm", e.getMessage());
				}
			}
		}
		/*if (CommonValues.getInstance().mifareUltraLightList.size() == 4) {
			return true;
		} else {
			
			 * Toast.makeText(context, "Please Hold your card again!",
			 * Toast.LENGTH_LONG).show();
			 
			return false;
		}*/
		return true;
	}

	public static boolean ReadULCValue(Context context, Tag t) {
		CommonValues.getInstance().mifareUltraLightCList.clear();

		Log.d("skm",
				"===========Mifare Ultralight C Read Start==================");
		Tag tag = t;
		String tagId;
		MifareUltraLightC mifareUltraLightC;
		MifareUltralight mifare = MifareUltralight.get(tag);
		Log.d("skm", "Tag Type :" + mifare.getType());
		CommonValues.getInstance().Type = "" + mifare.getType();
		try {
			tagId = CommonTask.getHexString(tag.getId());
			Log.d("skm", "UID :" + tagId.trim());
			CommonValues.getInstance().Name = "Mifare UltraLight C";
			CommonValues.getInstance().UID = tagId;
			CommonValues.getInstance().Memory = "192 bytes";
			CommonValues.getInstance().ultraLightCPageSize = ""
					+ mifare.PAGE_SIZE;
			CommonValues.getInstance().ultraLightCPageCount = "44";
			mifare.connect();
			int maxSize = mifare.getMaxTransceiveLength();
			Log.d("skm", "max length:" + maxSize);
			for (int i = 0; i < 11; i++) {
				mifareUltraLightC = new MifareUltraLightC();
				mifareUltraLightC.Header = "Page " + (i * 4) + " to "
						+ (((i + 1) * 4) - 1);

				mifareUltraLightC.pagevalue1 = CommonTask.getHexString(
						mifare.readPages(i * 4)).substring(0, 8);
				mifareUltraLightC.pagevalue2 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 1))).substring(0, 8);
				if (i == 10 && CommonTask.getHexString(mifare.readPages(41))
								.contains(
										CommonTask.getHexString(
												mifare.readPages(0)).substring(
												0, 16))) {
					mifareUltraLightC.Header = "Page " + (i * 4) + " to 41";
					CommonValues.getInstance().Memory = "168 bytes";
					CommonValues.getInstance().ultraLightCPageCount = "42";
					mifareUltraLightC.pagevalue3 = "";
					mifareUltraLightC.pagevalue4 = "";
					mifareUltraLightC.block1 = (i * 4);
					mifareUltraLightC.block2 = ((i * 4) + 1);
					mifareUltraLightC.block3 = ((i * 4) + 2);
					mifareUltraLightC.block4 = ((i * 4) + 3);
					CommonValues.getInstance().mifareUltraLightCList
							.add(mifareUltraLightC);

					break;
				}

				mifareUltraLightC.pagevalue3 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 2))).substring(0, 8);
				mifareUltraLightC.pagevalue4 = CommonTask.getHexString(
						mifare.readPages(((i * 4) + 3))).substring(0, 8);
				mifareUltraLightC.block1 = (i * 4);
				mifareUltraLightC.block2 = ((i * 4) + 1);
				mifareUltraLightC.block3 = ((i * 4) + 2);
				mifareUltraLightC.block4 = ((i * 4) + 3);
				CommonValues.getInstance().mifareUltraLightCList
						.add(mifareUltraLightC);

			}

		} catch (IOException e) {
			// Log.d("skm", e.getMessage());
		} finally {
			if (mifare != null) {
				try {
					mifare.close();
				} catch (IOException e) {
					Log.d("skm", e.getMessage());
				}
			}
		}
		if (CommonValues.getInstance().mifareUltraLightCList.size() == 11) {
			return true;
		} else {
			/*
			 * Toast.makeText(context, "Please Hold your card again!",
			 * Toast.LENGTH_LONG).show();
			 */
			return false;
		}
	}

	public static boolean ReadClassic1kValue(Context context, MifareClassic _mfc) {
		CommonValues.getInstance().mifareClassic1kList.clear();
		MifareClassic mfc = _mfc;
		MifareClassic1k classic1k;
		try {
			mfc.connect();

			Log.d("skm", "== MifareClassic Info == ");
			CommonValues.getInstance().Name = "Mifare Classic 1K";
			Log.d("skm", "Size: " + mfc.getSize());
			CommonValues.getInstance().Memory = "" + mfc.getSize();
			Log.d("skm", "Type: " + mfc.getType());
			CommonValues.getInstance().Type = "" + mfc.getType();
			Log.d("skm", "BlockCount: " + mfc.getBlockCount());
			CommonValues.getInstance().Block = "" + mfc.getBlockCount();

			Log.d("skm", "MaxTransceiveLength: " + mfc.getMaxTransceiveLength());
			Log.d("skm", "SectorCount: " + mfc.getSectorCount());
			CommonValues.getInstance().Sector = "" + mfc.getSectorCount();
			CommonValues.getInstance().UID = CommonTask.getHexString(mfc
					.getTag().getId());

			Log.d("skm", "Reading sectors...");

			for (int i = 0; i < mfc.getSectorCount(); ++i) {
				classic1k = new MifareClassic1k();
				if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
					Log.d("skm", "Authorized sector " + i + " with MAD key");
					classic1k.Header = i;

				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_DEFAULT)) {
					classic1k.Header = i;
					Log.d("skm", "Authorization granted to sector " + i
							+ " with DEFAULT key");

				} else if (mfc.authenticateSectorWithKeyA(i,
						MifareClassic.KEY_NFC_FORUM)) {
					classic1k.Header = i;
					Log.d("skm", "Authorization granted to sector " + i
							+ " with NFC_FORUM key");

				} else {
					Log.d("skm", "Authorization denied to sector " + i);

					continue;
				}

				for (int k = 0; k < mfc.getBlockCountInSector(i); ++k) {
					int block = mfc.sectorToBlock(i) + k;
					byte[] data = null;

					try {

						data = mfc.readBlock(block);
					} catch (IOException e) {
						Log.d("skm",
								"Block " + block + " data: " + e.getMessage());
						continue;
					}
					String blockData = CommonTask.getHexString(data);

					switch (k) {
					case 0:
						classic1k.Block1Value = blockData;
						classic1k.block1 = (i * 4) + k;
						break;
					case 1:
						classic1k.Block2Value = blockData;
						classic1k.block2 = (i * 4) + k;
						break;
					case 2:
						classic1k.Block3Value = blockData;
						classic1k.block3 = (i * 4) + k;
						break;
					case 3:
						classic1k.Block4Value = blockData;
						classic1k.block4 = (i * 4) + k;
						break;

					}
					Log.d("skm", "Block " + block + " data: " + blockData);
				}
				CommonValues.getInstance().mifareClassic1kList.add(classic1k);
			}

		} catch (Exception exception) {
			exception.printStackTrace();

		}

		if (CommonValues.getInstance().mifareClassic1kList.size() == 16) {
			return true;
		} else {
			/*
			 * Toast.makeText(context, "Please Hold your card again!",
			 * Toast.LENGTH_LONG).show();
			 */
			return false;
		}
	}

	public static boolean writeOnMifareUltralightC(Context _context, Tag tag,
			String pageData, int i) {
		MifareUltralight mifare = null;

		try {
			mifare = MifareUltralight.get(tag);
			mifare.connect();
			mifare.writePage(i, pageData.getBytes("US-ASCII"));

		} catch (Exception ex) {
			ex.printStackTrace();
			Log.d("skm", ex.getMessage());
			// return false;
		} finally {
			try {
				mifare.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return true;
	}

	public static boolean writeOnClassic1K(Context _context, Tag tag,
			String blockdata, String sectorNumber, String blockNumber) {
		MifareClassic mfc = MifareClassic.get(tag);

		try {
			mfc.connect();

			if (mfc.authenticateSectorWithKeyA(Integer.parseInt(sectorNumber),
					MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
				Log.d("skm", "Authorized sector with MAD key");

			} else if (mfc.authenticateSectorWithKeyA(
					Integer.parseInt(sectorNumber), MifareClassic.KEY_DEFAULT)) {
				Log.d("skm",
						"Authorization granted to sector  with DEFAULT key");

			} else if (mfc
					.authenticateSectorWithKeyA(Integer.parseInt(sectorNumber),
							MifareClassic.KEY_NFC_FORUM)) {
				Log.d("skm",
						"Authorization granted to sector with NFC_FORUM key");

			} else if (mfc.authenticateSectorWithKeyB(
					Integer.parseInt(sectorNumber),
					MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
				Log.d("skm", "Authorized sector with MAD key");

			} else if (mfc.authenticateSectorWithKeyB(
					Integer.parseInt(sectorNumber), MifareClassic.KEY_DEFAULT)) {
				Log.d("skm",
						"Authorization granted to sector  with DEFAULT key");

			} else if (mfc
					.authenticateSectorWithKeyB(Integer.parseInt(sectorNumber),
							MifareClassic.KEY_NFC_FORUM)) {
				Log.d("skm",
						"Authorization granted to sector with NFC_FORUM key");

			} else {
				Log.d("skm", "Authorization denied ");

				return false;
			}
			mfc.writeBlock(Integer.parseInt(blockNumber),
					blockdata.getBytes("US-ASCII"));

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		try {
			mfc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

}
