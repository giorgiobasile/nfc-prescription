package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Patient implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -399139023788831624L;
	private String name;
	private String surname;
	private String taxcode;
	
	Patient(){
		this.name = "";
		this.surname = "";
		this.taxcode = "";
	}
	
	public Patient(String name, String surname, String taxcode){
		this.name = name;
		this.surname = surname;
		this.taxcode = taxcode;
	}
	
	public String getTaxcode() {
		return taxcode;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
