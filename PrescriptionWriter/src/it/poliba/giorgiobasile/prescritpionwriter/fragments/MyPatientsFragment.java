package it.poliba.giorgiobasile.prescritpionwriter.fragments;

import it.poliba.giorgiobasile.prescriptionwriter.MyPatients;
import it.poliba.giorgiobasile.prescriptionwriter.Patient;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.activities.MainActivity;
import it.poliba.giorgiobasile.prescriptionwriter.activities.NewPatientActivity;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.DialogRemoveFragment;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MyPatientsFragment extends Fragment{
	ListView listPatients = null;
	ArrayList<Patient> patients = new ArrayList<Patient>();
	PatientsAdapter adapter = null;
	MainActivity activity;
	
	
	public MyPatientsFragment() {
	}


    public void onResume() {
        super.onResume();
        activity = (MainActivity) getActivity();
        setFab();
        MyPatients pat = new MyPatients(getActivity());
        patients = pat.getPatients();
        if(adapter != null){    
        	adapter.refresh(patients);
        }	
    }
    
    public void setFab(){
    	OnClickListener l = new OnClickListener(){

			@Override
			public void onClick(View v) {
				 startActivity(new Intent(activity, NewPatientActivity.class));
			}};
       
		activity.changeFab(R.drawable.ic_content_new, l, listPatients);	
		
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patients, container, false);
        //ArrayList<String[]> list = new ArrayList<String[]>();
         /*if(result && presc.getPrescriptions().size() > 0){
        	list = presc.listPrescriptions();
        }else
        	list.add(new String[]{getString(R.string.no_recents), ""});*/

        listPatients = (ListView) rootView.findViewById(R.id.list_patients);
        adapter = new PatientsAdapter(getActivity(), patients);
        listPatients.setAdapter(adapter);
        listPatients.setEmptyView(rootView.findViewById(R.id.empty_view));
        /*listPatients.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Intent intent = new Intent(getActivity(),PrescriptionActivity.class);
				//intent.putExtra("prescription", position);
				//startActivity(intent);
				
			}});*/
        return rootView;
    }

    public void removeItem(int position){
    	adapter.onRemove(position);
    }
    
    static class PatientsAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<Patient> patients;

        public PatientsAdapter(Context context, ArrayList<Patient> arrayList) {
            this.context = context;
            this.patients = arrayList;
        }

        @Override
        public int getCount() {
            return patients.size();
        }

        @Override
        public Object getItem(int position) {
            return patients.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(
                        R.layout.patient_row, parent, false);
            } 
            
            Patient p = patients.get(position);
            //DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            ((TextView)convertView.findViewById(R.id.text1)).setText(p.getName() + " " + p.getSurname());
            ((TextView)convertView.findViewById(R.id.text2)).setText(p.getTaxcode());

            ImageView menuButton = (ImageView) convertView.findViewById(R.id.menu_button);
            menuButton.setOnClickListener(new OnClickListener() {  
                
            	@Override  
            	public void onClick(View v) {  
                 //Creating the instance of PopupMenu  
                	PopupMenu popup = new PopupMenu(context, v);  
                 //Inflating the Popup using xml file  
                	popup.getMenuInflater().inflate(R.menu.patient_menu, popup.getMenu());  
                
                 //registering popup with OnMenuItemClickListener  
                	popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	                	public boolean onMenuItemClick(MenuItem item) {  
	                		switch(item.getItemId()){
	                			case R.id.action_delete:
	                				DialogRemoveFragment aif = DialogRemoveFragment.newInstance(context.getString(R.string.patient_delete_confirm),position);
	                                aif.show(((FragmentActivity) context).getSupportFragmentManager(), DialogRemoveFragment.fragment_tag);
	    	                        break;
	                		}
		                	return true;  
	                	}
	                });  
       
                	popup.show();//showing popup menu  
            	}  
            });//closing the setOnClickListener method  
            
            
            return convertView;
        }
        
        public void refresh(ArrayList<Patient> recentsUpdate)
        {
            this.patients = recentsUpdate;
            notifyDataSetChanged();
        }

		public void onRemove(int position) {
			MyPatients pats = new MyPatients(context);
			pats.removePatient(position);
			pats.save(context);
	        patients = pats.getPatients();
	        notifyDataSetChanged();
			Toast.makeText(context, context.getString(R.string.patient_deleted), Toast.LENGTH_SHORT).show();
		}
    }	
}
