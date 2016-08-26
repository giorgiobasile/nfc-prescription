package it.poliba.giorgiobasile.prescriptionreader.activities;

import it.poliba.giorgiobasile.prescriptionreader.Doctor;
import it.poliba.giorgiobasile.prescriptionreader.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DoctorActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment((Doctor)getIntent().getSerializableExtra("doctor"))).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.doctor, menu);
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

		private Doctor doctor;
		TextView nameView, surnameView, regionalView, aslView;
		
		public PlaceholderFragment() {
		}
		
		public PlaceholderFragment(Doctor doctor) {
			this.doctor = doctor;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_doctor,
					container, false);
			
			nameView = (TextView)rootView.findViewById(R.id.name);
			surnameView = (TextView)rootView.findViewById(R.id.surname);
			regionalView = (TextView)rootView.findViewById(R.id.regionalcode);
			aslView = (TextView)rootView.findViewById(R.id.aslcode);
			
			nameView.setText(doctor.getName());
			surnameView.setText(doctor.getSurname());
			regionalView.setText(doctor.getRegionalCode());
			aslView.setText(doctor.getAslCode());
			return rootView;
		}
	}
}
