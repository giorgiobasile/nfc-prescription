package it.poliba.giorgiobasile.prescriptionwriter;


import it.poliba.giorgiobasile.prescriptionwriter.encrypt.DESEncryption;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Prescription implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5565697380515045946L;
	private List<DrugPrescr> drugsPrescr;
	private Patient patient;
	private Date date;
	
	public Prescription(){
		this.setDrugsPrescr(new ArrayList<DrugPrescr>());
		this.patient = new Patient();
	}
	
	public Prescription(Patient patient, List<DrugPrescr> drugsPrescr, Date date){
		this.setDrugsPrescr(drugsPrescr);
		this.setPatient(patient);
		this.setDate(date);
	}

	public List<DrugPrescr> getDrugsPrescr() {
		return drugsPrescr;
	}

	public void setDrugsPrescr(List<DrugPrescr> drugsPrescr) {
		this.drugsPrescr = drugsPrescr;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	

	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
	     //always perform the default de-serialization first
	     aInputStream.defaultReadObject();
	  }

   /**
   * This is the default implementation of writeObject.
   * Customise if necessary.
   */
   private void writeObject(
     ObjectOutputStream aOutputStream
   ) throws IOException {
     //perform the default serialization for all non-transient, non-static fields
     aOutputStream.defaultWriteObject();
   }

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String composeMessage(Context context){
		String message = "";
		String separator = "!";
		Doctor d = new Doctor(context);
		message = message + d.getName();
		message = message + separator + d.getSurname();
		message = message + separator + d.getRegionalCode();
		message = message + separator + d.getAslCode();
		message = message + separator + this.getPatient().getName();
		message = message + separator + this.getPatient().getSurname();
		message = message + separator + this.getPatient().getTaxcode();
		for(DrugPrescr dp : this.getDrugsPrescr()){
			message = message + separator + dp.getActive().getEquivGroupCode();
			message = message + separator + dp.getQuantity();
			message = message + separator + dp.getAssumption();
		}
		message = message + separator + "**";
		message = encryptMessage(message, context);
		
		return message;
	}
	
	public String encryptMessage(String message, Context context){
		DESEncryption myEncryptor;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String alg = sp.getString(context.getString(R.string.security_key), context.getString(R.string.security_algorithm_default));
		if(alg.equals(context.getResources().getStringArray(R.array.security_algorithms_values)[0])){
			//DES
			try {
				myEncryptor = new DESEncryption(DESEncryption.FIRST_KEY);
				message = myEncryptor.encrypt(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//TRIPLE DES
			try {
				myEncryptor = new DESEncryption(DESEncryption.FIRST_KEY);
				message = myEncryptor.encrypt(message);
				myEncryptor = new DESEncryption(DESEncryption.SECOND_KEY);
				message = myEncryptor.encrypt(message);
				myEncryptor = new DESEncryption(DESEncryption.THIRD_KEY);
				message = myEncryptor.encrypt(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message;
	}
	public String decryptMessage(String message, Context context){
		DESEncryption myEncryptor;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String alg = sp.getString(context.getString(R.string.security_key), context.getString(R.string.security_algorithm_default));
		if(alg.equals(context.getResources().getStringArray(R.array.security_algorithms_values)[0])){
			//DES
			try {
				myEncryptor = new DESEncryption(DESEncryption.FIRST_KEY);
				message = myEncryptor.decrypt(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//TRIPLE DES
			try {
				myEncryptor = new DESEncryption(DESEncryption.THIRD_KEY);
				message = myEncryptor.decrypt(message);
				myEncryptor = new DESEncryption(DESEncryption.SECOND_KEY);
				message = myEncryptor.decrypt(message);
				myEncryptor = new DESEncryption(DESEncryption.FIRST_KEY);
				message = myEncryptor.decrypt(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message;
	}
	
	
}
