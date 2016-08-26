package it.poliba.giorgiobasile.prescriptionreader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Doctor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1307477016462769902L;
	private String name;
	private String surname;
	private String regionalCode;
	private String aslCode;
	public String getName() {
		return name;
	}
	
	public Doctor(){
		name = "";
		surname = "";
		regionalCode = "";
		aslCode = "";
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
}
