package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class Doctor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1307477016462769902L;
	
	public static String FILE_PATH = "doctor.ser";
	private String name;
	private String surname;
	private String regionalCode;
	private String region;
	private String province;
	private String aslCode;
	
	
	
	public Doctor(Context context){
		if(name == null){
			boolean success = read(context);
			if(!success){
				name = "";
				name = "";
				surname = "";
				regionalCode = "";
				region = "";
				province = "";
				aslCode = "";
			}
		}	
	}
	
	public Doctor(String name, String surname, String regionalCode,
			String region, String province, String aslCode) {
		this.setName(name);
		this.setSurname(surname);
		this.setRegionalCode(regionalCode);
		this.setRegion(region);
		this.setProvince(province);
		this.setAslCode(aslCode);
	}

	public boolean read(Context context){
		FileInputStream fin;
		try {
			fin = context.openFileInput(FILE_PATH);
		
			ObjectInputStream ois;
			ois = new ObjectInputStream(fin);
			
			try {
				Doctor temp = (Doctor) ois.readObject();
				this.setName(temp.getName());
				this.setSurname(temp.getSurname());
				this.setRegionalCode(temp.getRegionalCode());
				this.setRegion(temp.getRegion());
				this.setProvince(temp.getProvince());
				this.setAslCode(temp.getAslCode());
				
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
	
	public void save(Context context) {

		try {
			FileOutputStream fout = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getAslCode() {
		return aslCode;
	}
	public void setAslCode(String aslCode) {
		this.aslCode = aslCode;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getRegionalCode() {
		return regionalCode;
	}
	public void setRegionalCode(String regionalCode) {
		this.regionalCode = regionalCode;
	}
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
	     //always perform the default de-serialization first
	     aInputStream.defaultReadObject();
	  }

	  /**
	  * This is the default implementation of writeObject.
	  * Customise if necessary.
	  */
	  private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
	    //perform the default serialization for all non-transient, non-static fields
	    aOutputStream.defaultWriteObject();
	  }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
