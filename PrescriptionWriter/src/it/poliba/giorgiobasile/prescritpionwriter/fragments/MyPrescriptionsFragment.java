package it.poliba.giorgiobasile.prescritpionwriter.fragments;

import it.poliba.giorgiobasile.prescriptionwriter.DrugsDB;
import it.poliba.giorgiobasile.prescriptionwriter.MyPrescriptions;
import it.poliba.giorgiobasile.prescriptionwriter.Prescription;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.activities.MainActivity;
import it.poliba.giorgiobasile.prescriptionwriter.activities.NewPrescriptionActivity2;
import it.poliba.giorgiobasile.prescriptionwriter.activities.PrescriptionActivity;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.DialogRemoveFragment;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.ProgressDialogFragment;
import it.poliba.giorgiobasile.prescriptionwriter.nfc.NFCWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class MyPrescriptionsFragment extends Fragment {

    	ListView listRecent = null;
    	ArrayList<Prescription> recents = new ArrayList<Prescription>();
    	RecentAdapter adapter = null;
    	MainActivity activity;

    	
    	public MyPrescriptionsFragment() {
    	}

    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    setRetainInstance(true);
    	}

        public void onResume() {
            super.onResume();
            activity = (MainActivity) getActivity();
            setFab();
            loadCsv();
            MyPrescriptions presc = new MyPrescriptions(getActivity());
            recents = presc.getPrescriptions();
            adapter.refresh(recents);
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
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //ArrayList<String[]> list = new ArrayList<String[]>();
             /*if(result && presc.getPrescriptions().size() > 0){
            	list = presc.listPrescriptions();
            }else
            	list.add(new String[]{getString(R.string.no_recents), ""});*/

            listRecent = (ListView) rootView.findViewById(R.id.list_recent);
            adapter = new RecentAdapter(getActivity(), recents);
            listRecent.setAdapter(adapter);
            listRecent.setEmptyView(rootView.findViewById(R.id.empty_view));
            listRecent.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(getActivity(),PrescriptionActivity.class);
					intent.putExtra("prescription", position);
					startActivity(intent);
					
				}});
            
            
            return rootView;
        }
    
        public void setFab(){
        	OnClickListener l = new OnClickListener(){

    			@Override
    			public void onClick(View v) {
    				 startActivity(new Intent(activity, NewPrescriptionActivity2.class));
    			}};
           
    		activity.changeFab(R.drawable.ic_content_new, l, listRecent);	
        }
        
        public void removeItem(int position){
        	adapter.onRemove(position);
        }
    static class RecentAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<Prescription> recents;
    	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

        public RecentAdapter(Context context, ArrayList<Prescription> arrayList) {
            this.context = context;
            this.recents = arrayList;
        }

        @Override
        public int getCount() {
            return recents.size();
        }

        @Override
        public Object getItem(int position) {
            return recents.get(position);
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
                        R.layout.recent_row, parent, false);
            } 
            
            Prescription p = recents.get(position);
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            ((TextView)convertView.findViewById(R.id.text1)).setText(p.getPatient().getName() + " " + p.getPatient().getSurname());
            ((TextView)convertView.findViewById(R.id.text2)).setText(dateFormat.format(p.getDate()));
            
            ImageView menuButton = (ImageView) convertView.findViewById(R.id.menu_button);
            menuButton.setOnClickListener(new OnClickListener() {  
                
            	@Override  
            	public void onClick(View v) {  
                 //Creating the instance of PopupMenu  
                	PopupMenu popup = new PopupMenu(context, v);  
                 //Inflating the Popup using xml file  
                	popup.getMenuInflater().inflate(R.menu.recent_menu, popup.getMenu());  
                
                 //registering popup with OnMenuItemClickListener  
                	popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	                	public boolean onMenuItemClick(MenuItem item) {  
	                		switch(item.getItemId()){
	                			case R.id.action_delete:
	                				DialogRemoveFragment aif = DialogRemoveFragment.newInstance(context.getString(R.string.prescription_delete_confirm), position);
	                                aif.show(((FragmentActivity) context).getSupportFragmentManager(), DialogRemoveFragment.fragment_tag);
	    	                        break;
	                			case R.id.action_nfc:
	                				Tag tag = ((MainActivity)context).getTag();
	                				NFCWriter writer = new NFCWriter();
	                				String message = recents.get(position).composeMessage(context);
	                				boolean success = writer.writeTag(context, tag, message);
	                				if(!success){
	                					((MainActivity)context).setTag(null);
	                					System.out.println(message.length());
	                				}	
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
        
        public void refresh(ArrayList<Prescription> recentsUpdate)
        {
            this.recents = recentsUpdate;
            notifyDataSetChanged();
        }

		public void onRemove(int position) {
			MyPrescriptions presc = new MyPrescriptions(context);
			presc.removePrescription(position);
			presc.save(context);
	        recents = presc.getPrescriptions();
	        notifyDataSetChanged();
			Toast.makeText(context, context.getString(R.string.prescription_deleted), Toast.LENGTH_SHORT).show();
		}

        
    }
}