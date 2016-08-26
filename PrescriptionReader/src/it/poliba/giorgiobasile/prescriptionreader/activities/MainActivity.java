package it.poliba.giorgiobasile.prescriptionreader.activities;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import it.poliba.giorgiobasile.prescriptionreader.R;
import it.poliba.giorgiobasile.prescriptionreader.DrugsDB;
import it.poliba.giorgiobasile.prescriptionreader.dialogs.ProgressDialogFragment;
import it.poliba.giorgiobasile.prescriptionreader.settings.SettingsActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;



public class MainActivity extends FragmentActivity {

	/*NfcAdapter adapter;
	PendingIntent pendingIntent;
	IntentFilter writeTagFilters[];
	Tag mytag;
	boolean writeMode;*/
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		
		writeTagFilters = new IntentFilter[] { tagDetected };*/
        
        if (savedInstanceState == null) {
        	getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
        if (id == R.id.action_settings) {
        	startActivity(new Intent(this, SettingsActivity.class));
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    /*public Tag getTag(){
    	return mytag;
    }
    
    public void setTag(Tag tag){
    	mytag = tag;
    }
    
    @Override
	public void onPause(){
		super.onPause();
		WriteModeOff();
	}

	@Override
	public void onResume(){
		super.onResume();
		WriteModeOn();
	}

	private void WriteModeOn(){
		writeMode = true;
		adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
	}

	private void WriteModeOff(){
		writeMode = false;
		adapter.disableForegroundDispatch(this);
	}*/
	
	/*@Override
	protected void onNewIntent(Intent intent){
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);    
			Toast.makeText(this, getString(R.string.tag_detected) , Toast.LENGTH_SHORT).show();
		}
	}*/
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        
        public void onResume() {
            super.onResume();
            loadCsv();
            
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            /*Button readButton = (Button)rootView.findViewById(R.id.read_button);
            readButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					MainActivity activity = (MainActivity)getActivity();
					if(activity.mytag == null){
						Toast.makeText(activity, getString(R.string.error_detection), Toast.LENGTH_SHORT).show();
						
					}else{
						NFCReader reader = new NFCReader();
						String s = reader.read(activity.mytag);
						Intent intent = new Intent(getActivity(), PrescriptionActivity.class);
						intent.putExtra("name", s);
						startActivity(intent);
						getActivity().finish();
					}
					
					
				}});*/
            return rootView;
        }
        
        public void loadCsv(){
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
    				prog.dismissAllowingStateLoss();
    			}
        		
        	}.execute();
        }
    }
}
