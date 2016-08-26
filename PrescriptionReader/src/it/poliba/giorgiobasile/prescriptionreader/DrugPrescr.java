package it.poliba.giorgiobasile.prescriptionreader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DrugPrescr implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4116748291123427926L;
	private ActiveIngredient drug;
	private int quantity;
	private String assumption;
	
	DrugPrescr(){}
	
	public DrugPrescr(ActiveIngredient drug, int quantity, String assumption){
		this.drug = drug;
		this.quantity = quantity;
		this.assumption = assumption;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getAssumption() {
		return assumption;
	}
	public void setAssumption(String assumption) {
		this.assumption = assumption;
	}
	public ActiveIngredient getActive() {
		return drug;
	}
	public void setActive(ActiveIngredient drug) {
		this.drug = drug;
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
}
