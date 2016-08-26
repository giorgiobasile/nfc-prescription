package it.poliba.giorgiobasile.prescriptionwriter.dialogs;

import it.poliba.giorgiobasile.prescriptionwriter.MyPatients;
import it.poliba.giorgiobasile.prescriptionwriter.Patient;
import it.poliba.giorgiobasile.prescriptionwriter.R;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;


public class PatientSelectFragment extends DialogFragment {
	
	public static String fragment_tag = "ACTIVE_INGREDIENTS_FRAGMENT";
	OnSelectedListener mCallback;
	private Dialog dialog;
	EditText quantityView, assumptionView, filterView;
	ArrayList<Patient> filtered = new ArrayList<Patient>();
	PatientAdapter adapter;
	public PatientSelectFragment(){}
	
	public static PatientSelectFragment newInstance() {
		PatientSelectFragment frag = new PatientSelectFragment();
        return frag;
    }

    // Container Activity must implement this interface
    public interface OnSelectedListener {
        public void onPatientSelected(Patient p);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		RelativeLayout dialogTitle = (RelativeLayout)inflater.inflate(R.layout.custom_title, null);
		ImageView icon = (ImageView)dialogTitle.findViewById(R.id.icon);
		icon.setImageResource(R.drawable.thermo_icon);
		TextView title = (TextView)dialogTitle.findViewById(R.id.label);
		title.setText(R.string.patient_choice);
		b.setCustomTitle(dialogTitle);
		b.setCancelable(false);
		LinearLayout dialoglayout = (LinearLayout)inflater.inflate(R.layout.patients_custom_dialog, null);
        ListView list = (ListView)dialoglayout.findViewById(R.id.listView1);
        MyPatients pats = new MyPatients(getActivity());
        filtered.addAll(pats.getPatients());
        if(filtered.size() > 0){
        	adapter = new PatientAdapter(getActivity(), filtered);
        	list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					
						mCallback.onPatientSelected(filtered.get(pos));
						dialog.dismiss();
						
				}});
        	list.setAdapter(adapter);
        }
        quantityView = (EditText)dialoglayout.findViewById(R.id.quantity);
        assumptionView = (EditText)dialoglayout.findViewById(R.id.assumption);
        
        filterView = (EditText)dialoglayout.findViewById(R.id.filter);
        filterView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				filtered.clear();
				MyPatients pats = new MyPatients(getActivity());
		        ArrayList<Patient> listPatient =  pats.getPatients();
		        for(Patient p : listPatient){
		        	if(p.getName().toLowerCase().contains(s.toString().toLowerCase()) || p.getSurname().toLowerCase().contains(s.toString().toLowerCase()) ||  p.getTaxcode().toUpperCase().contains(s.toString().toUpperCase())){
		        		filtered.add(p);
		        	}
		        }
		        adapter.refresh(filtered);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}});
        b.setView(dialoglayout);
        dialog = b.create();
	    return dialog;
    }
	
	public static class PatientAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<Patient> objects;

		public PatientAdapter(Context context, ArrayList<Patient> objects) {
			this.context = context;
			this.objects = objects;
		}

		
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			Patient p = objects.get(position);

			if (p != null) {

				TextView patientName = (TextView) v.findViewById(android.R.id.text1);

				// check to see if each individual textview is null.
				// if not, assign some text!
				if (patientName != null){
					patientName.setText(p.getName() + " " + p.getSurname());
				}
				
			}

			// the view must be returned to our activity
			return v;

		}

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        
        public void refresh(ArrayList<Patient> drugsUpdate)
        {
            this.objects = drugsUpdate;
            notifyDataSetChanged();
        }
        
	}
	
	
}