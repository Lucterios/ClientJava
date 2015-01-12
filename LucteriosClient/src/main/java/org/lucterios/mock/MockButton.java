package org.lucterios.mock;

import java.net.URL;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIComponent;

public class MockButton extends MockComponent implements GUIButton {
	public MockButton(GUIComponent aOwner){
        super(aOwner);
	}

	public void doClick() {		
	}
	
	private String mText="";
	public String getTextString() {
		return mText;
	}

	public void setTextString(String text) {
		mText=text;		
	}

	public void setToggle(boolean isToogle) {
	}

	private boolean mSelect=false;
	public boolean isSelected() {
		return mSelect;
	}

	public void setSelected(boolean select) {
		mSelect=select;
	}
	
	public void setMnemonic(char c) {
	}

	public void setImage(URL image) {
	}

	public void setImage(AbstractImage image) {
	}

	private Object mObject=null;
	public Object getObject() {
		return mObject;
	}

	public void setObject(Object object) {
		mObject=object;
	}

	
}
