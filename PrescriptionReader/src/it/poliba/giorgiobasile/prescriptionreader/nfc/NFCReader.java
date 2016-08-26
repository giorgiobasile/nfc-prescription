package it.poliba.giorgiobasile.prescriptionreader.nfc;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

public class NFCReader {
	public NFCReader(){}
	
	public String readNdefRecord(NdefRecord record)  throws UnsupportedEncodingException {

        byte[] payload = record.getPayload();
 
        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
 
        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;
         
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"
         
        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        
	}
	
	public String read(Tag tag){
        
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag. 
            return null;
        }
 
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
 
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    return readNdefRecord(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e("nfc", "Unsupported Encoding", e);
                }
            }
        }
        return "";
	}
	
}
