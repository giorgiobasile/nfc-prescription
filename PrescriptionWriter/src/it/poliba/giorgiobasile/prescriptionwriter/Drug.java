package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Drug implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2435180514490959103L;
	public static final int ACTIVE_INGREDIENT = 0;
	public static final int EQUIVALENCY_GROUP_DESCRIPTION = 1;
	public static final int NAMING_AND_PACKAGING = 2;
	public static final int PUBLIC_PRICE = 3;
	public static final int COMPANY = 4;
	public static final int AIC_CODE = 5;
	public static final int EQUIVALENCY_GROUP_CODE = 6;
	public static final int TRANSPARENCY_LIST = 7;
	public static final int REGION_LIST = 8;
	public static final int OXYGEN = 9;
	
	private String ActiveIngredient;
	private String EquivalencyGroupDescription;
	private String NamingAndPackaging;
	private String PublicPrice;
	private String Company;
	private String AICcode;
	private String EquivalencyGroupCode;
	private String TransparencyList;
	private String RegionList;
	private String Oxygen;
	
	public Drug(){}
	
	Drug(String ActiveIngredient, String EquivalencyGroupDescription, String NamingAndPackaging, String PublicPrice, 
			String Company, String AICcode, String EquivalencyGroupCode, String TransparencyList, String RegionList, String Oxygen){
		
		this.setActiveIngredient(ActiveIngredient);
		this.setEquivalencyGroupDescription(EquivalencyGroupDescription);
		this.setNamingAndPackaging(NamingAndPackaging);
		this.setPublicPrice(PublicPrice);
		this.setCompany(Company);
		this.setAICcode(AICcode);
		this.setEquivalencyGroupCode(EquivalencyGroupCode);
		this.setTransparencyList(TransparencyList);
		this.setRegionList(RegionList);
		this.setOxygen(Oxygen);
	}
	
	public void buildDrug(String[] drugRow){
		this.setActiveIngredient(drugRow[ACTIVE_INGREDIENT]);
		this.setEquivalencyGroupDescription(drugRow[EQUIVALENCY_GROUP_DESCRIPTION]);
		this.setNamingAndPackaging(drugRow[NAMING_AND_PACKAGING]);
		this.setPublicPrice(drugRow[PUBLIC_PRICE]);
		this.setCompany(drugRow[COMPANY]);
		this.setAICcode(drugRow[AIC_CODE]);
		this.setEquivalencyGroupCode(drugRow[EQUIVALENCY_GROUP_CODE]);
		this.setTransparencyList(drugRow[TRANSPARENCY_LIST]);
		this.setRegionList(drugRow[REGION_LIST]);
		this.setOxygen(drugRow[OXYGEN]);
	}

	public String getOxygen() {
		return Oxygen;
	}

	public void setOxygen(String oxygen) {
		Oxygen = oxygen;
	}

	public String getTransparencyList() {
		return TransparencyList;
	}

	public void setTransparencyList(String transparencyList) {
		TransparencyList = transparencyList;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getPublicPrice() {
		return PublicPrice;
	}

	public void setPublicPrice(String publicPrice) {
		PublicPrice = publicPrice;
	}

	public String getNamingAndPackaging() {
		return NamingAndPackaging;
	}

	public void setNamingAndPackaging(String namingAndPackaging) {
		NamingAndPackaging = namingAndPackaging;
	}

	public String getAICcode() {
		return AICcode;
	}

	public void setAICcode(String aICcode) {
		AICcode = aICcode;
	}

	public String getEquivalencyGroupCode() {
		return EquivalencyGroupCode;
	}

	public void setEquivalencyGroupCode(String equivalencyGroupCode) {
		EquivalencyGroupCode = equivalencyGroupCode;
	}

	public String getRegionList() {
		return RegionList;
	}

	public void setRegionList(String regionList) {
		RegionList = regionList;
	}

	public String getEquivalencyGroupDescription() {
		return EquivalencyGroupDescription;
	}

	public void setEquivalencyGroupDescription(
			String equivalencyGroupDescription) {
		EquivalencyGroupDescription = equivalencyGroupDescription;
	}

	public String getActiveIngredient() {
		return ActiveIngredient;
	}

	public void setActiveIngredient(String activeIngredient) {
		ActiveIngredient = activeIngredient;
	}
	
	public ActiveIngredient createActive(){
		return new ActiveIngredient(ActiveIngredient, EquivalencyGroupDescription, EquivalencyGroupCode);
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
