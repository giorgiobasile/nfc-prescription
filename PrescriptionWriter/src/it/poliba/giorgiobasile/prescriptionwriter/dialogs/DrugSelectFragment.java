package it.poliba.giorgiobasile.prescriptionwriter.dialogs;

import it.poliba.giorgiobasile.prescriptionwriter.ActiveIngredient;
import it.poliba.giorgiobasile.prescriptionwriter.DrugsDB;
import it.poliba.giorgiobasile.prescriptionwriter.R;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;


public class DrugSelectFragment extends DialogFragment {
	
	public static String fragment_tag = "ACTIVE_INGREDIENTS_FRAGMENT";
	OnSelectedListener mCallback;
	private int position;
	private AlertDialog dialog;
	EditText quantityView, assumptionView, filterView;
	private boolean sub;
	ArrayList<ActiveIngredient> filtered = new ArrayList<ActiveIngredient>();
	ActivesAdapter adapter;
	public DrugSelectFragment(){}
	
	public DrugSelectFragment(int position, boolean sub){
		this.position = position;
		this.sub = sub;
	}
	
	public static DrugSelectFragment newInstance(int position, boolean sub) {
		DrugSelectFragment frag = new DrugSelectFragment(position, sub);
        return frag;
    }

    // Container Activity must implement this interface
    public interface OnSelectedListener {
        public void onDrugSelected(ActiveIngredient d, int position2, boolean sub, int quantity, String assumption);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSelectedListener");
        }
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		RelativeLayout dialogTitle = (RelativeLayout)inflater.inflate(R.layout.drug_dialog_title, null);
		/*ImageView icon = (ImageView)dialogTitle.findViewById(R.id.icon);
		icon.setImageResource(R.drawable.aifa_logo);*/
		TextView title = (TextView)dialogTitle.findViewById(R.id.label);
		title.setText(R.string.drug_choice);
		b.setCustomTitle(dialogTitle);
		b.setCancelable(false);
		LinearLayout dialoglayout = (LinearLayout)inflater.inflate(R.layout.prescription_custom_dialog, null);
		
        ListView list = (ListView)dialoglayout.findViewById(R.id.listView1);
        DrugsDB ai = new DrugsDB();
        filtered.addAll(ai.getActives());
        if(filtered.size() > 0){
        	adapter = new ActivesAdapter(getActivity(), filtered);
        	list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					String quantity = quantityView.getText().toString();
					if(quantity.equals("")){
						Toast.makeText(getActivity(), getString(R.string.empty_quantity), Toast.LENGTH_SHORT).show();
					}else if(Integer.parseInt(quantity) == 0){
						Toast.makeText(getActivity(), getString(R.string.low_quantity), Toast.LENGTH_SHORT).show();						
					}else{
						mCallback.onDrugSelected(filtered.get(pos), position, sub, Integer.parseInt(quantityView.getText().toString()), assumptionView.getText().toString() );
						dialog.dismiss();
					}	
				}});
        	list.setAdapter(adapter);
        	
        }
        quantityView = (EditText)dialoglayout.findViewById(R.id.quantity);
        assumptionView = (EditText)dialoglayout.findViewById(R.id.assumption);
        
        filterView = (EditText)dialoglayout.findViewById(R.id.filter);
        filterView.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				filtered.clear();
				DrugsDB ai = new DrugsDB();
		        ArrayList<ActiveIngredient> listActives =  ai.getActives();
		        for(ActiveIngredient d : listActives){
		        	if(d.getEquivGroupDescr().toLowerCase().contains(s.toString().toLowerCase()) || d.getActiveIngredient().toLowerCase().contains(s.toString().toLowerCase())|| d.getEquivGroupCode().toLowerCase().contains(s.toString().toLowerCase())){
		        		filtered.add(d);
		        	}
		        }
		        adapter.refresh(filtered);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}});
        b.setView(dialoglayout);
        dialog = b.create();
	    return dialog;
    }
	
	public static class ActivesAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<ActiveIngredient> objects;

		public ActivesAdapter(Context context, ArrayList<ActiveIngredient> objects) {
			this.context = context;
			this.objects = objects;
		}

		
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			ActiveIngredient d = objects.get(position);

			if (d != null) {

				TextView drugName = (TextView) v.findViewById(android.R.id.text1);

				// check to see if each individual textview is null.
				// if not, assign some text!
				if (drugName != null){
					drugName.setText(d.getEquivGroupDescr());
				}
				
			}

			// the view must be returned to our activity
			return v;

		}

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        
        public void refresh(ArrayList<ActiveIngredient> drugsUpdate)
        {
            this.objects = drugsUpdate;
            notifyDataSetChanged();
        }
        
	}
	
	
}