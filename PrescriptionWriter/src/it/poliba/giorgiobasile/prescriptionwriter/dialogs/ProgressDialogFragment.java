package it.poliba.giorgiobasile.prescriptionwriter.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class ProgressDialogFragment extends DialogFragment {
	
	public static String fragment_tag = "PROGRESS_DIALOG_FRAGMENT";
	
	
	public static ProgressDialogFragment newInstance( int title, int message) {
		//activity = newActivity;
		ProgressDialogFragment frag = new ProgressDialogFragment();
		Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        args.putInt("message", message);
        frag.setArguments(args);
        return frag;
    }
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    // Retain this fragment across configuration changes.
	    setRetainInstance(true);
	  }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");
		ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(title);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(message));
        mProgressDialog.setCanceledOnTouchOutside(false);
        
        setCancelable(false);
        
        return mProgressDialog;
    }
	
	
}