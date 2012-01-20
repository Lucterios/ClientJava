package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIEdit;

public class MockEdit extends MockComponent implements GUIEdit {

	double mMinVal=Double.MIN_VALUE;
    double mMaxVal=Double.MAX_VALUE;
    int mPrecVal=2;
    boolean mIsFloatEditor=false;   
    
	public MockEdit(GUIComponent aOwner){
        super(aOwner);
    }

	private String convertValue(Double init_value)
    {
        double Val_r = (double) Math
				.min(mMaxVal, Math.max(mMinVal, init_value));
		long val_i = Math.round(Val_r * Math.pow(10, mPrecVal));
		String val = new Double(val_i / Math.pow(10, mPrecVal)).toString();
		int pos = val.indexOf(".");
		if (pos != -1) {
			if (val.length() <= (pos + mPrecVal + 1))
				while (val.length() < (pos + mPrecVal + 1))
					val = val + "0";
			else if (mPrecVal > 0)
				val = val.substring(0, pos + mPrecVal + 1);
			else
				val = val.substring(0, pos);
		}
		return val;
    }
	
	private char mPassword=0;
	public void setPassword(char c) {
		mPassword=c;
	}	    
	public char getPassword() {
		return mPassword;
	}
	
	public boolean isFloatEditor() {
		return mIsFloatEditor;
	}

	public void setFloatEditor(boolean isFloatEditor) {
		this.mIsFloatEditor = isFloatEditor;
	}
	
    public void setRange(double aMinVal,double aMaxVal,int aPrecVal)
    {
        mMinVal=aMinVal;
        mMaxVal=aMaxVal;
        mPrecVal=Math.max(0,aPrecVal);
    }
                    
    public void setValue(Double aVal)
    {
    	setTextString(convertValue(aVal));
    }

    protected Double getValue(String aValue)
    {
        try
        {
                return new Double(aValue);
        }
        catch(NumberFormatException e)
        {
                return mMinVal;
        }
    }
    
    public Double getValue()
    {
        return getValue(getTextString());
    }
        
    private String mText="";

	private int mColNums;

	private int mCaretPosition;
	public String getTextString() {
		return mText;
	}

	public void setTextString(String text) {
		mText=text;		
	}

	public int[] getCaretPositions() {
		return new int[]{mCaretPosition};
	}
	public void setCaretPosition(int pos) { 
		mCaretPosition=pos;
	}


	public int getColumns() {
		return mColNums;
	}
	public void setColumns(int col) { 
		mColNums=col;
	}


	public void setSelectionEnd(int pos) { }

	public void setSelectionStart(int pos) { }	
	
}
