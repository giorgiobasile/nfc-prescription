package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;

public class MyPrescriptions {
	private static ArrayList<Prescription> prescriptions = null;
	public static String FILE_PATH = "prescriptions.ser";
	//private final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	public MyPrescriptions(Context context){
		if(prescriptions == null){
			boolean success = read(context);
			if(!success)
				prescriptions = new ArrayList<Prescription>();
		}	
	}
	
	public boolean read(Context context){
		FileInputStream fin;
		try {
			fin = context.openFileInput(FILE_PATH);
		
			ObjectInputStream ois;
			ois = new ObjectInputStream(fin);
			
			try {
				MyPrescriptions.prescriptions = (ArrayList<Prescription>) ois.readObject();
				return true;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void addPrescription(Prescription p){
		prescriptions.add(0, p);
	}
	
	public void removePrescription(int pos){
		prescriptions.remove(pos);
	}
	
	/*public ArrayList<String[]> listPrescriptions(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<Prescription> pList = getPrescriptions();
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    	for(Prescription p : pList)
    		list.add(new String[]{p.getUser().getName() + " " + p.getUser().getSurname(), dateFormat.format(p.getDate())});
    	return list;
	}*/

	public ArrayList<Prescription> getPrescriptions() {
		return prescriptions;
	}
	
	public void save(Context context) {

		try {
			FileOutputStream fout = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fout);
			oos.writeObject(prescriptions);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
