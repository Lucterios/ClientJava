package org.lucterios.mock;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUIComponent;

public class MockCheckBox extends MockComponent implements GUICheckBox {
	public void setImage(AbstractImage image) {
	}

	public MockCheckBox(GUIComponent aOwner){
        super(aOwner);
	}

	
	private String mText="";
	public String getTextString() {
		return mText;
	}

	public void setTextString(String text) {
		mText=text;		
	}

	private boolean mSelect=false;
	public boolean isSelected() {
		return mSelect;
	}

	public void setSelected(boolean select) {
		mSelect=select;
	}
	
}
