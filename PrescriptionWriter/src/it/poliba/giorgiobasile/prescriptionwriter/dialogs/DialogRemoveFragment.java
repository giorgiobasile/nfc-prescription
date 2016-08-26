package it.poliba.giorgiobasile.prescriptionwriter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class DialogRemoveFragment extends DialogFragment {
	
	public static String fragment_tag = "DIALOG_REMOVE_FRAGMENT";
	
	int position;
	OnSelectedListener mCallback;
	String message;
	
	public DialogRemoveFragment(){
		
	}
	
	
	public DialogRemoveFragment(String message, int position) {
		this.position = position;
		this.message = message;
	}

	public interface OnSelectedListener {
        public void onSelected(int position);
    }

	public static DialogRemoveFragment newInstance(String message, int position) {
		DialogRemoveFragment frag = new DialogRemoveFragment(message, position);
        return frag;
    }
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSelectedListener");
        }
    }
    @Override
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	if(savedInstanceState != null)
    		message = savedInstanceState.getString("message");
    }
    
	@Override
    public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putString("message", message);
    }
    
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new Builder(getActivity());
		b.setTitle(android.R.string.dialog_alert_title);
        b.setMessage(message);
        b.setCancelable(false);
        b.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	mCallback.onSelected(position);

                dialog.dismiss();
            }
        });
        b.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
	    return b.create();
    }
	
	
}