package it.poliba.giorgiobasile.prescriptionwriter.activities;

import it.poliba.giorgiobasile.prescriptionwriter.ActiveIngredient;
import it.poliba.giorgiobasile.prescriptionwriter.Drug;
import it.poliba.giorgiobasile.prescriptionwriter.DrugPrescr;
import it.poliba.giorgiobasile.prescriptionwriter.DrugRow;
import it.poliba.giorgiobasile.prescriptionwriter.MyPrescriptions;
import it.poliba.giorgiobasile.prescriptionwriter.Prescription;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.Patient;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.DrugSelectFragment;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.PatientSelectFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewPrescriptionActivity2 extends FragmentActivity implements DrugSelectFragment.OnSelectedListener, PatientSelectFragment.OnSelectedListener{
	
	private FloatingActionButton mFab;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_prescription);
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
	
	@Override
	public void onDrugSelected(ActiveIngredient d, int position, boolean sub, int quantity, String assumption) {
		NewFragment f =  (NewFragment) getSupportFragmentManager().findFragmentByTag(NewFragment.fragment_tag);
		if(sub == false)
			f.addDrug(d ,quantity, assumption);
		else
			f.subDrug(d, position, quantity, assumption);
	}

	public static class NewFragment extends Fragment {

		static final int GET_ACTIVE_INGREDIENT = 1;
		public static String fragment_tag = "NEW_FRAGMENT";
		public ArrayList<DrugPrescr> drugs = new ArrayList<DrugPrescr>();
		TextView nameView, surnameView, taxcodeView;
		Patient p;
		
		public NewFragment() {
		}

		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_new_prescription2, container,
					false);
			
			Button select_patient = (Button) rootView.findViewById(R.id.select_patient_button);
			select_patient.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					PatientSelectFragment aif = PatientSelectFragment.newInstance();
                    aif.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), PatientSelectFragment.fragment_tag);
				}});
			
			Button new_presc = (Button) rootView.findViewById(R.id.new_presc_button);
			new_presc.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					DrugSelectFragment aif = DrugSelectFragment.newInstance(0, false);
                    aif.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), DrugSelectFragment.fragment_tag);
				}});
			
			
			nameView = (TextView) rootView.findViewById(R.id.name);
			surnameView = (TextView) rootView.findViewById(R.id.surname);
			taxcodeView = (TextView) rootView.findViewById(R.id.taxcode);
			
			if(savedInstanceState != null){
				/*name = savedInstanceState.getString("name");
				surname = savedInstanceState.getString("surname");
				taxcode = savedInstanceState.getString("taxcode");*/
				p = (Patient)savedInstanceState.getSerializable("patient");
				setPatient(p);
				drugs = (ArrayList<DrugPrescr>)savedInstanceState.getSerializable("drugsList");
				int i = 0;
				for(DrugPrescr dg : drugs){
					addDrugView(rootView, dg, i);
					i++;
				}	
			}
			
			NewPrescriptionActivity2 activity = (NewPrescriptionActivity2) getActivity();
			activity.setfabListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					savePrescr();
				}});
			activity.mFab.listenTo((ScrollView)rootView.findViewById(R.id.scroll));
			return rootView;
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState){
			super.onSaveInstanceState(outState);
			outState.putSerializable("drugsList", (Serializable) drugs);
			outState.putSerializable("patient", (Serializable)p);
		}
		
		public void addDrug(ActiveIngredient d, int quantity, String assumption){
			DrugPrescr dp = new DrugPrescr(d, quantity, assumption);
			drugs.add(dp);
			addDrugView(getActivity().findViewById(android.R.id.content), dp, drugs.size() - 1);
			
		}
		
		public void subDrug(ActiveIngredient d, int pos, int quantity, String assumption){
			DrugPrescr dp = new DrugPrescr(d, quantity, assumption);
			drugs.remove(pos);
			drugs.add(pos, dp);
			LinearLayout lin = (LinearLayout) getActivity().findViewById(android.R.id.content).findViewById(R.id.new_presc);
			lin.removeViewAt(pos);
			addDrugView(getActivity().findViewById(android.R.id.content), dp, pos);
		}
		
		public void removeDrug(int pos){
			drugs.remove(pos);
			LinearLayout lin = (LinearLayout) getActivity().findViewById(android.R.id.content).findViewById(R.id.new_presc);
			lin.removeAllViews();
			int i = 0;
			for(DrugPrescr dp : drugs){
				addDrugView(getActivity().findViewById(android.R.id.content), dp, i);
				i++;
			}
		}
		
		public void addDrugView(final View root, DrugPrescr drugprescr, int position){
			LinearLayout lin = (LinearLayout) root.findViewById(R.id.new_presc);
			DrugRow drugLin;
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            drugLin = (DrugRow) inflater.inflate(R.layout.drug_row_new, lin, false);
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
					DrugSelectFragment aif = DrugSelectFragment.newInstance(dr.getPosition(), true);
                    aif.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), DrugSelectFragment.fragment_tag);
				}
			});  
            ImageView but = (ImageView) drugLin.findViewById(R.id.menu_button);
            but.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v){
					DrugRow dr = (DrugRow)(v.getParent().getParent());
					removeDrug(dr.getPosition());
				}	
            });
            lin.addView(drugLin, position);
		}
		
		public void savePrescr(){
			if(p == null){
				Toast.makeText(getActivity(),getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
			}else if(drugs.size() == 0){
				Toast.makeText(getActivity(),getString(R.string.no_drugs_selected), Toast.LENGTH_SHORT).show();
			}else{	
				Prescription prescr = new Prescription(p, drugs, new Date());
				MyPrescriptions p = new MyPrescriptions(getActivity());
				p.read(getActivity());
				p.addPrescription(prescr);
				p.save(getActivity());
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
		
		public void setPatient(Patient p){
			this.p = p;
			if(p != null){
				nameView.setText(p.getName());
				surnameView.setText(p.getSurname());
				taxcodeView.setText(p.getTaxcode());
			}	
		}
	}

	@Override
	public void onPatientSelected(Patient p) {
		NewFragment f =  (NewFragment) getSupportFragmentManager().findFragmentByTag(NewFragment.fragment_tag);
		f.setPatient(p);
	}
	
	
	
}
