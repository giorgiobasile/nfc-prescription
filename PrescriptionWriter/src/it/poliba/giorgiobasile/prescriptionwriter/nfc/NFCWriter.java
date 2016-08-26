package it.poliba.giorgiobasile.prescriptionwriter.nfc;

import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.NFCDialogFragment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class NFCWriter {

	public NFCWriter(){}
	
	private NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
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
	
	private NdefMessage createNdefMessage(NdefRecord rec){
		NdefMessage msg = new NdefMessage(
    	        new NdefRecord[] {
    	            rec,
    	            NdefRecord.createApplicationRecord("it.poliba.giorgiobasile.prescriptionreader")});
		return msg;
	}
	
	private void write(String text, Tag tag) throws IOException, FormatException {
		
		NdefRecord rec = createTextRecord(text, Locale.ITALIAN, true);
    	NdefMessage msg = createNdefMessage(rec);
		// Get an instance of Ndef for the tag.
		Ndef ndef = Ndef.get(tag);
		// Enable I/O
		ndef.connect();
		// Write the message
		ndef.writeNdefMessage(msg);
		// Close the connection
		ndef.close();
	}
	
	public boolean writeTag(Context ctx, Tag tag, String payload){
		
		//Con i primi TAG comprati, max 70 bytes
		
		boolean success = false;
		
		try {
			if(tag == null){
				//Toast.makeText(ctx, ctx.getString(R.string.error_detection), Toast.LENGTH_SHORT).show();
				success = false;
				NFCDialogFragment.newInstance(success, ctx.getString(R.string.error_detection)).show(((FragmentActivity) ctx).getSupportFragmentManager(), NFCDialogFragment.fragment_tag);
			}else{
				NFCWriter writer = new NFCWriter();
				writer.write(payload, tag);
				success = true;
				//Toast.makeText(ctx, ctx.getString(R.string.ok_writing), Toast.LENGTH_SHORT).show();
				NFCDialogFragment.newInstance(success, ctx.getString(R.string.ok_writing)).show(((FragmentActivity) ctx).getSupportFragmentManager(), NFCDialogFragment.fragment_tag);
			}
		} catch (IOException e) {
			NFCDialogFragment.newInstance(false, ctx.getString(R.string.error_writing)).show(((FragmentActivity) ctx).getSupportFragmentManager(), NFCDialogFragment.fragment_tag);
			//Toast.makeText(ctx, ctx.getString(R.string.error_writing), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (FormatException e) {
			NFCDialogFragment.newInstance(false, ctx.getString(R.string.error_writing)).show(((FragmentActivity) ctx).getSupportFragmentManager(), NFCDialogFragment.fragment_tag);
			//Toast.makeText(ctx, ctx.getString(R.string.error_writing) , Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return success;
	}
}
