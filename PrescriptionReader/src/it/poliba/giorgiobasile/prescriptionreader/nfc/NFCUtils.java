package it.poliba.giorgiobasile.prescriptionreader.nfc;

//import it.poliba.giorgiobasile.prescriptionreader.encrypt.DESEncryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class NFCUtils {
	
	public static String readNfcMessage(Intent intent){
    	NdefMessage[] msgs = null;
    	Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        }
        NdefRecord rec = msgs[0].getRecords()[0];
        byte[] payload = rec.getPayload();
        String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = payload[0] & 0077;

        String s = "";
		try {
			s = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
			     textEncoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /*DESEncryption myEncryptor = null;
		try {
			myEncryptor = new DESEncryption();
		}catch(Exception e){
			e.printStackTrace();
		}	
		return myEncryptor.decrypt(s);*/
		return s;
    }
	
	
	public static void registerNfcMessage(Activity activity, String message){
    	//DESEncryption myEncryptor;
		try {
			//myEncryptor = new DESEncryption();
            //String encrypted = myEncryptor.encrypt(message);
        	//NdefRecord rec = createTextRecord(encrypted, Locale.ITALIAN, true);
			NdefRecord rec = createTextRecord(message, Locale.ITALIAN, true);
        	NdefMessage msg = createNdefMessage(rec);
        	NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        	nfcAdapter.setNdefPushMessage(msg, activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static NdefMessage createNdefMessage(NdefRecord rec){
		NdefMessage msg = new NdefMessage(
    	        new NdefRecord[] {
    	            rec,
    	            NdefRecord.createApplicationRecord("it.poliba.giorgiobasile.prescriptionreader")});
		return msg;
	}
	
	public static NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
        NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }
}
