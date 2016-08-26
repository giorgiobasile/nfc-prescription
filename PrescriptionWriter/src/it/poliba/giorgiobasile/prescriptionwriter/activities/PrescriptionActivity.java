package it.poliba.giorgiobasile.prescriptionwriter.activities;

import java.io.Serializable;

import it.poliba.giorgiobasile.prescriptionwriter.DrugPrescr;
import it.poliba.giorgiobasile.prescriptionwriter.DrugRow;
import it.poliba.giorgiobasile.prescriptionwriter.MyPrescriptions;
import it.poliba.giorgiobasile.prescriptionwriter.Prescription;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrescriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_prescription);
		
		if (savedInstanceState == null) {
			int pos = getIntent().getExtras().getInt("prescription");
			MyPrescriptions prescr = new MyPrescriptions(this);
			Prescription p = prescr.getPrescriptions().get(pos);
			
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(p)).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.prescription, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		Prescription p;
		TextView nameView, surnameView, taxcodeView;
		
		public PlaceholderFragment() {
		}
		
		public PlaceholderFragment(Prescription p) {
			this.p = p;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_prescription,
					container, false);
			
				nameView = (TextView) rootView.findViewById(R.id.name);
				surnameView = (TextView) rootView.findViewById(R.id.surname);
				taxcodeView = (TextView) rootView.findViewById(R.id.taxcode);

			if(savedInstanceState != null){
				p = (Prescription) savedInstanceState.getSerializable("prescription");
			}

			nameView.setText(p.getPatient().getName());
			surnameView.setText(p.getPatient().getSurname());
			taxcodeView.setText(p.getPatient().getTaxcode());
			
			int i = 0;
			for(DrugPrescr dp : p.getDrugsPrescr()){
				addDrugView(rootView, dp, i);
				i++;
			}	
			return rootView;
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState){
			super.onSaveInstanceState(outState);
			outState.putSerializable("prescription", (Serializable) p);
		}
		
		public void addDrugView(final View root, DrugPrescr drugprescr, int position){
			LinearLayout lin = (LinearLayout) root.findViewById(R.id.presc);
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
