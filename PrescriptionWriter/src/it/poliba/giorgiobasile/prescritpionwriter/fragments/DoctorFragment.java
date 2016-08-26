package it.poliba.giorgiobasile.prescritpionwriter.fragments;


import it.poliba.giorgiobasile.prescriptionwriter.Doctor;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.activities.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorFragment extends Fragment {
	public static String fragment_tag = "NEW_FRAGMENT";
	Doctor d;
	EditText nameView, surnameView, regionalcodeView, aslcodeView, regionView, provinceView;
	MainActivity activity;
	
	public DoctorFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_doctor, container,
				false);
		
		d = new Doctor(getActivity());
		
		
		nameView = (EditText) rootView.findViewById(R.id.name);
		surnameView = (EditText) rootView.findViewById(R.id.surname);
		regionalcodeView = (EditText) rootView.findViewById(R.id.regionalcode);
		regionView = (EditText) rootView.findViewById(R.id.region);
		provinceView = (EditText) rootView.findViewById(R.id.province);
		aslcodeView = (EditText) rootView.findViewById(R.id.aslcode);

		if(savedInstanceState == null){
			nameView.setText(d.getName());
			surnameView.setText(d.getSurname());
			regionalcodeView.setText(d.getRegionalCode());
			regionView.setText(d.getRegion());
			provinceView.setText(d.getProvince());
			aslcodeView.setText(d.getAslCode());

		}		
		return rootView;
	}
	
	
	public void setFab(){
    	OnClickListener l = new OnClickListener(){

			@Override
			public void onClick(View v) {
				 saveDoctor();
			}};
       
		activity.changeFab(R.drawable.ic_navigation_accept, l);	
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}		
	
	public void onResume() {
        super.onResume();
        activity = (MainActivity) getActivity();
        setFab();
	}
	
	public void saveDoctor(){
		if(nameView.getText().toString().equals("") || 
			surnameView.getText().toString().equals("") || 
			regionalcodeView.getText().toString().equals("") || 
			aslcodeView.getText().toString().equals("") ||
			regionView.getText().toString().equals("") ||
			provinceView.getText().toString().equals("")){
			
			Toast.makeText(getActivity(),getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
		}else{	
			d = new Doctor(nameView.getText().toString(), surnameView.getText().toString(), 
					regionalcodeView.getText().toString(), 
					regionView.getText().toString(),
					provinceView.getText().toString(),
					aslcodeView.getText().toString());

			d.save(getActivity());
			Toast.makeText(getActivity(),getString(R.string.successful_save), Toast.LENGTH_SHORT).show();
		}	
	}
}
