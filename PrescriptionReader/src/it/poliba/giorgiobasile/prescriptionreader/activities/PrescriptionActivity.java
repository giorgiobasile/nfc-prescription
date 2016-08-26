package it.poliba.giorgiobasile.prescriptionreader.activities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import it.poliba.giorgiobasile.prescriptionreader.DrugsDB;
import it.poliba.giorgiobasile.prescriptionreader.DrugPrescr;
import it.poliba.giorgiobasile.prescriptionreader.DrugRow;
import it.poliba.giorgiobasile.prescriptionreader.Prescription;
import it.poliba.giorgiobasile.prescriptionreader.R;
import it.poliba.giorgiobasile.prescriptionreader.dialogs.ProgressDialogFragment;
import it.poliba.giorgiobasile.prescriptionreader.nfc.NFCUtils;
import it.poliba.giorgiobasile.prescriptionreader.settings.SettingsActivity;
import it.poliba.giorgiobasile.prescriptionreader.dialogs.NFCDialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrescriptionActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_prescription);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		/*getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, new PlaceholderFragment()).commit();*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
			case android.R.id.home:
				finish();
				break;
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		Prescription p = new Prescription();
		
		TextView doctorView, nameView, surnameView, taxcodeView;
		LinearLayout lin;
		public PlaceholderFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_prescription,
					container, false);
				doctorView = (TextView) rootView.findViewById(R.id.doctor);
				nameView = (TextView) rootView.findViewById(R.id.name);
				surnameView = (TextView) rootView.findViewById(R.id.surname);
				taxcodeView = (TextView) rootView.findViewById(R.id.taxcode);
				lin = (LinearLayout) rootView.findViewById(R.id.presc);
			if(savedInstanceState != null){
				p = (Prescription) savedInstanceState.getSerializable("prescription");
			}
			/*nameView.setText(p.getUser().getName());
			surnameView.setText(p.getUser().getSurname());
			taxcodeView.setText(p.getUser().getTaxcode());
			
			int i = 0;
			for(DrugPrescr dp : p.getDrugsPrescr()){
				addDrugView(rootView, dp, i);
				i++;
			}	*/
			Intent intent = getActivity().getIntent();
	        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
	        	loadCsvandPrescr(intent, rootView);
	        }
			return rootView;
		}
		
		public void loadCsvandPrescr(final Intent intent, final View rootView){
        	new AsyncTask<Void, String, Void>() {
        		
        		ProgressDialogFragment prog;
        		
        		@Override
    			protected void onPreExecute(){
        			prog = ProgressDialogFragment.newInstance( R.string.loading, R.string.loading_database);
                    prog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), ProgressDialogFragment.fragment_tag);
        		}
    			@Override
    			protected Void doInBackground(Void... arg0) {
    				DrugsDB ai = new DrugsDB();
    				if(ai.getDrugs().size() == 0){
	    				try {
							InputStreamReader csv = new InputStreamReader(getActivity().getAssets().open("principi_attivi.csv"), "UTF-8");
							ai.readCsvFile(csv);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}    		
    				}
    				return null;
    			}
    			@Override 
    			protected void onPostExecute(Void arg0){
    				try{
	    				prog.dismissAllowingStateLoss();
	    				String s = NFCUtils.readNfcMessage(intent);    				
	    				p = new Prescription(s, getActivity());
	    				NFCUtils.registerNfcMessage(getActivity(), p.composeMessage(getActivity()));
						setPrescriptionViews();
    				}catch(Exception e){
    					e.printStackTrace();
    					try{
    						String message = "";
    						if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
    							message = getActivity().getString(R.string.error_reading_ndef);
    						else if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
    							message = getActivity().getString(R.string.error_reading_tag);
    						
    						NFCDialogFragment.newInstance(false, message).show(((FragmentActivity) getActivity()).getSupportFragmentManager(), NFCDialogFragment.fragment_tag);

    					}catch(Exception e1){
    						e.printStackTrace();
    					}
    				}   
    			}
        		
        	}.execute();
        }
		
		public void setPrescriptionViews(){
			doctorView.setText(p.getDoctor().getName() + " " + p.getDoctor().getSurname());
			nameView.setText(p.getPatient().getName());
			surnameView.setText(p.getPatient().getSurname());
			taxcodeView.setText(p.getPatient().getTaxcode());
			int i = 0;
			for(DrugPrescr dp : p.getDrugsPrescr()){
				addDrugView(dp, i);
				i++;
			}
			doctorView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(), DoctorActivity.class);
					intent.putExtra("doctor", (Serializable)p.getDoctor());
					startActivity(intent);
				}
				
			});
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState){
			super.onSaveInstanceState(outState);
			outState.putSerializable("prescription", (Serializable) p);
		}
		
		public void addDrugView(DrugPrescr drugprescr, int position){
			
			DrugRow drugLin;
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            drugLin = (DrugRow) inflater.inflate(R.layout.drug_row, lin, false);
            drugLin.setPosition(position);
            TextView drugName = (TextView)(drugLin.findViewById(R.id.drug_name));
            drugName.setText(drugprescr.getActive().getEquivGroupDescr());
            /*EditText drugquantity = (EditText)(drugLin.findViewById(R.id.drug_quantity));
            drugquantity.setText(String.valueOf(drugprescr.getquantity()));*/
            drugLin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					DrugRow dr = (DrugRow)v;
					dr.getPosition();
					DrugPrescr d = p.getDrugsPrescr().get(dr.getPosition());
					String message = getString(R.string.quantity) + ": " + d.getQuantity() + " " + getString(R.string.assumption) + ": " + d.getAssumption(); 
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}});
            
            
            lin.addView(drugLin);
		}
	}
}
