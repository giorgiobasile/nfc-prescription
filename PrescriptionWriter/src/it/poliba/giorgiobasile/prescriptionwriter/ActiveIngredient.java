package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ActiveIngredient implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2648243359458465154L;
	public static final int ACTIVE_INGREDIENT = 0;
	public static final int EQUIVALENCY_GROUP_DESCRIPTION = 1;
	public static final int EQUIVALENCY_GROUP_CODE = 6;
	
	private String activeIngredient;
	private String equivGroupDescr;
	private String equivGroupCode;
	
	public ActiveIngredient(String activeIngredient, String equivGroupDescr, String equivGroupCode){
		this.activeIngredient = activeIngredient;
		this.equivGroupCode = equivGroupCode;
		this.equivGroupDescr = equivGroupDescr;
	}

	public String getActiveIngredient() {
		return activeIngredient;
	}

	public void setActiveIngredient(String activeIngredient) {
		this.activeIngredient = activeIngredient;
	}

	public String getEquivGroupCode() {
		return equivGroupCode;
	}

	public void setEquivGroupCode(String equivGroupCode) {
		this.equivGroupCode = equivGroupCode;
	}

	public String getEquivGroupDescr() {
		return equivGroupDescr;
	}

	public void setEquivGroupDescr(String equivGroupDescr) {
		this.equivGroupDescr = equivGroupDescr;
	}
	
	@Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof ActiveIngredient){
        	ActiveIngredient a = (ActiveIngredient) object;
            if(a.getEquivGroupCode().equals(this.equivGroupCode))
            	sameSame = true;
        }

        return sameSame;
    }
	
	private void readObject(
		     ObjectInputStream aInputStream
		   ) throws ClassNotFoundException, IOException {
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
