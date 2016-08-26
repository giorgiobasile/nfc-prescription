package it.poliba.giorgiobasile.prescriptionreader;

import it.poliba.giorgiobasile.prescriptionreader.DrugPrescr;
import it.poliba.giorgiobasile.prescriptionreader.encrypt.DESEncryption;

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
	private Doctor doctor;
	
	public Prescription(){
		this.setDrugsPrescr(new ArrayList<DrugPrescr>());
		this.patient = new Patient();
		this.doctor = new Doctor();
	}
	
	public Prescription(Patient user, List<DrugPrescr> drugsPrescr, Date date, Doctor doctor){
		this.setDrugsPrescr(drugsPrescr);
		this.setPatient(user);
		this.setDate(date);
		this.setDoctor(doctor);
	}
	
	public Prescription(String message, Context context){
		try {
			message = decryptMessage(message, context);
			System.out.println(message);
			String separator = "!";
			String field = "";
			int index = 0;
			
			this.patient = new Patient();
			this.doctor = new Doctor();
			//doctorname
			index = message.indexOf(separator);
			this.getDoctor().setName(message.substring(0, index));
			message = message.substring(index + 1);
			System.out.println(field);
			//doctorsurname
			index = message.indexOf(separator);
			this.getDoctor().setSurname(message.substring(0, index));
			message = message.substring(index + 1);
			//regionalcode
			index = message.indexOf(separator);
			this.getDoctor().setRegionalCode(message.substring(0, index));
			message = message.substring(index + 1);
			//aslcode
			index = message.indexOf(separator);
			this.getDoctor().setAslCode(message.substring(0, index));
			message = message.substring(index + 1);
			
			
			//name
			index = message.indexOf(separator);
			this.getPatient().setName(message.substring(0, index));
			message = message.substring(index + 1);
			//surname
			index = message.indexOf(separator);
			this.getPatient().setSurname(message.substring(0, index));
			message = message.substring(index + 1);
			//taxcode
			index = message.indexOf(separator);
			this.getPatient().setTaxcode(message.substring(0, index));
			message = message.substring(index + 1);
			
			DrugsDB ai = new DrugsDB();
			setDrugsPrescr(new ArrayList<DrugPrescr>());
			
			while(!message.startsWith("**")){
				
				DrugPrescr dp = new DrugPrescr();
				ActiveIngredient d = new ActiveIngredient();
				
				int i = 0;
				index = message.indexOf(separator);
				field = message.substring(0, index);
				
				for(i = 0; i < ai.getActives().size(); i++){
					if(ai.getActives().get(i).getEquivGroupCode().equals(field)){
						d = ai.getActives().get(i);
						dp.setActive(d);
						break;
					}//else{System.out.println("Primo: " +ai.getActives().get(i).getEquivGroupCode() + " Secondo: "+field);}
				}
				
				message = message.substring(index + 1);
				
				index = message.indexOf(separator);
				dp.setQuantity(Integer.parseInt(message.substring(0, index)));
				message = message.substring(index + 1);

				index = message.indexOf(separator);
				dp.setAssumption(message.substring(0, index));
				message = message.substring(index + 1);
				getDrugsPrescr().add(dp);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		message = message + this.getDoctor().getName();
		message = message + separator + this.getDoctor().getSurname();
		message = message + separator + this.getDoctor().getRegionalCode();
		message = message + separator + this.getDoctor().getAslCode();
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

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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
