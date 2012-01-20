package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUILabel;

public class MockLabel extends MockComponent implements GUILabel {

	public MockLabel(GUIComponent aOwner){
        super(aOwner);
		setStyle(0);
	}
	
	private String mText="";
	public String getTextString() {
		return mText;
	}

	public void setTextString(String text) {
		mText=text;		
	}

	private int mStyle=0;
	private int mSize=0;
	public void setStyle(int style) {
		mStyle=style;		
	}	
	public int getStyle(){
		return mStyle;
	}

	public int getFontSize() {
		return mSize;
	}

	public void setFontSize(int size) {
		mSize=size;
	}

	public void setSize(int width, int height) { }
	
}
