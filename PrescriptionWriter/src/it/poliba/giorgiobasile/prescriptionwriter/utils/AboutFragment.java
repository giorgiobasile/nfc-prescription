package it.poliba.giorgiobasile.prescriptionwriter.utils;

import it.poliba.giorgiobasile.prescriptionwriter.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class AboutFragment extends DialogFragment {
	
	public static String fragment_tag = "PROGRESS_DIALOG_FRAGMENT";
	
	public static AboutFragment newInstance() {
		AboutFragment frag = new AboutFragment();
        return frag;
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new Builder(getActivity());
        b.setTitle(R.string.app_name);
        b.setMessage(R.string.app_description);
        return b.create();
    }
	
	
}