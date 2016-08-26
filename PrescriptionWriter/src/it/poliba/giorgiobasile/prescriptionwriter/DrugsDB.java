package it.poliba.giorgiobasile.prescriptionwriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class DrugsDB { 
	
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
	
	
	private static ArrayList<Drug> listDrugs = null;
	private static ArrayList<ActiveIngredient> listActives = null;
	
	public DrugsDB(){
		if(listDrugs == null)
			listDrugs = new ArrayList<Drug>();
		if(listActives == null)
			listActives = new ArrayList<ActiveIngredient>();
	};
	
	public void readCsvFile(InputStreamReader file){
    	CSVReader reader = null;
		try {
			reader = new CSVReader(new BufferedReader(file), ';');
		    List<String[]> list = reader.readAll();
		    for(String[] x : list){
		    	Drug d = new Drug();
		    	d.buildDrug(x);
		    	listDrugs.add(d);
		    	ActiveIngredient ai = d.createActive();
		    	if(!listActives.contains(ai))
		    		listActives.add(ai);
		    }
		    reader.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public ArrayList<Drug> getDrugs(){
		return listDrugs;
	}
	
	public ArrayList<ActiveIngredient> getActives(){
		return listActives;
	}
}
