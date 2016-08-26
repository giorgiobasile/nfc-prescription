package it.poliba.giorgiobasile.prescriptionwriter.activities;


import it.poliba.giorgiobasile.prescriptionwriter.MyPatients;

import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.Patient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class NewPatientActivity extends FragmentActivity /*implements DrugSelectFragment.OnSelectedListener*/{
	
	private FloatingActionButton mFab;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		
		mFab = (FloatingActionButton)findViewById(R.id.fabNew);
        mFab.setColor(getResources().getColor(android.R.color.holo_red_light));
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new NewFragment(), NewFragment.fragment_tag).commit();
		}
	}
	
    public void hideFab(View view) {
        mFab.hide(true);
        //getActionBar().hide();
    }

    public void showFab(View view) {
        mFab.hide(false);
        //getActionBar().show();
    }
    
    public void setfabListener(OnClickListener l){
    	mFab.setOnClickListener(l);
    }
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			/*case  R.id.action_settings:
				return true;*/
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*@Override
	public void onDrugSelected(Drug d, int position, boolean sub, int quantity, String assumption) {
		NewFragment f =  (NewFragment) getSupportFragmentManager().findFragmentByTag(NewFragment.fragment_tag);
		if(sub == false)
			f.addDrug(d ,quantity, assumption);
		else
			f.subDrug(d, position, quantity, assumption);
	}*/

	public static class NewFragment extends Fragment {

		//static final int GET_ACTIVE_INGREDIENT = 1;
		public static String fragment_tag = "NEW_FRAGMENT";
		EditText nameView, surnameView, taxcodeView;
		
		public NewFragment() {
		}

		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setHasOptionsMenu(true);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_new_patient, container,
					false);
			
			if(savedInstanceState != null){
				/*name = savedInstanceState.getString("name");
				surname = savedInstanceState.getString("surname");
				taxcode = savedInstanceState.getString("taxcode");*/
				
			}
			nameView = (EditText) rootView.findViewById(R.id.name);
			surnameView = (EditText) rootView.findViewById(R.id.surname);
			taxcodeView = (EditText) rootView.findViewById(R.id.taxcode);
			
			NewPatientActivity activity = (NewPatientActivity) getActivity();
			activity.setfabListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					savePatient();
				}});
			return rootView;
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState){
			super.onSaveInstanceState(outState);
		}		
		
		
		public void savePatient(){
			if(nameView.getText().toString().equals("") || surnameView.getText().toString().equals("") || taxcodeView.getText().toString().equals("")){
				Toast.makeText(getActivity(),getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
			}else if(taxcodeView.getText().toString().length() != 16){
				Toast.makeText(getActivity(),getString(R.string.short_taxcode), Toast.LENGTH_SHORT).show();
			}else if(this.checkCodiceFiscale(taxcodeView.getText().toString()) == false){
				Toast.makeText(getActivity(),getString(R.string.bad_taxcode), Toast.LENGTH_SHORT).show();
			}else{	
				Patient p = new Patient(nameView.getText().toString(), surnameView.getText().toString(), taxcodeView.getText().toString().toUpperCase());
				MyPatients pats = new MyPatients(getActivity());
				pats.read(getActivity());
				pats.addPatient(p);
				pats.save(getActivity());
				Toast.makeText(getActivity(),getString(R.string.successful_save), Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}	
		}
		public boolean checkCodiceFiscale(String codiceFiscale){
			//lo metto maiuscolo
			codiceFiscale = codiceFiscale.toUpperCase();
			//6 lettere 2 numeri 1 lettera 2 numeri 1 lettera 3 numeri 1 lettera
			Pattern pattern = Pattern.compile("[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}");
			Matcher matcher = pattern.matcher(codiceFiscale);
			return matcher.matches();
		}
	}
	
}
