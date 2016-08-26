package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class MyPatients {
	private static ArrayList<Patient> patients = null;
	public static String FILE_PATH = "patients.ser";
	
	public MyPatients(Context context){
		if(patients == null){
			boolean success = read(context);
			if(!success)
				patients = new ArrayList<Patient>();
		}	
	}
	
	public boolean read(Context context){
		FileInputStream fin;
		try {
			fin = context.openFileInput(FILE_PATH);
		
			ObjectInputStream ois;
			ois = new ObjectInputStream(fin);
			
			try {
				MyPatients.patients = (ArrayList<Patient>) ois.readObject();
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
	
	public void addPatient(Patient p){
		patients.add(0, p);
	}
	
	public void removePatient(int pos){
		patients.remove(pos);
	}
	
	/*public ArrayList<String[]> listPrescriptions(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		ArrayList<Prescription> pList = getPrescriptions();
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    	for(Prescription p : pList)
    		list.add(new String[]{p.getUser().getName() + " " + p.getUser().getSurname(), dateFormat.format(p.getDate())});
    	return list;
	}*/

	public ArrayList<Patient> getPatients() {
		return patients;
	}
	
	public void save(Context context) {

		try {
			FileOutputStream fout = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fout);
			oos.writeObject(patients);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
