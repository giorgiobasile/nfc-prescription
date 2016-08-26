package it.poliba.giorgiobasile.prescriptionreader.settings;

import it.poliba.giorgiobasile.prescriptionreader.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment{
	
	SharedPreferences prefs;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        /*Preference credits = (Preference)findPreference(getResources().getString(R.string.credits_key));
        credits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) { 
	            startActivity(new Intent(arg0.getContext(), AboutActivity.class));   
	            return true;
	        }
        });*/
        Preference feedback = (Preference)findPreference(getResources().getString(R.string.sendfeedback_key));
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	        @Override
	        public boolean onPreferenceClick(Preference arg0) { 
	            sendEmail(getResources().getString(R.string.app_name));  
	            return true;
	        }
        });
        
    }
	

	public void sendEmail(String subject){
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
	   

		/* Send it off to the Activity-Chooser */
		startActivity(emailIntent);
	}
}
