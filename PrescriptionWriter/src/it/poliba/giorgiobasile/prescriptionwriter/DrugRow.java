package it.poliba.giorgiobasile.prescriptionwriter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DrugRow extends LinearLayout{
	
	private int position;

	public DrugRow(Context context, int position) {
		super(context);
		this.setPosition(position);
	}

	public DrugRow(Context context) {
		super(context);
	}
	
	public DrugRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
