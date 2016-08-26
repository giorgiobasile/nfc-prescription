package it.poliba.giorgiobasile.prescriptionreader.dialogs;

import it.poliba.giorgiobasile.prescriptionreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class NFCDialogFragment extends DialogFragment {
	
	public static String fragment_tag = "NFC_DIALOG_FRAGMENT";
	
	boolean success;
	String phrase;
	
	public NFCDialogFragment(){
		
	}
	
	public NFCDialogFragment(boolean success, String phrase) {
		this.success = success;
		this.phrase = phrase;
	}

	

	public static NFCDialogFragment newInstance(boolean success, String phrase) {
		NFCDialogFragment frag = new NFCDialogFragment(success, phrase);
        return frag;
    }
    
	
    @Override
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	if(savedInstanceState != null){
    		success = savedInstanceState.getBoolean("success");
    		phrase = savedInstanceState.getString("phrase");
    	}	
    }
    
	@Override
    public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putBoolean("success", success);
		outState.putString("phrase", phrase);
    }
    
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		RelativeLayout dialoglayout = (RelativeLayout)inflater.inflate(R.layout.dialog_success, null);
		ImageView im = (ImageView)dialoglayout.findViewById(R.id.outcome_image);
		TextView text = (TextView)dialoglayout.findViewById(R.id.outcome_text);
		text.setText(phrase);
		if(success){
			im.setImageResource(R.drawable.ok_icon);
		}else{
			im.setImageResource(R.drawable.error_icon);
		}
		
		b.setView(dialoglayout);
        b.setCancelable(false);
        b.setNeutralButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        
	    return b.create();
    }
	
	
}