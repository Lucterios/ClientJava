package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperText;

public class MockHyperText extends MockLabel implements GUIHyperText {
	
	private static final long serialVersionUID = 1L;

	public MockHyperText(GUIComponent aOwner){
        super(aOwner);
	}

	private String mHyperLink="";
	public void setHyperLink(String string) {
		mHyperLink=string;
	}

	public String getHyperLink(){
		return mHyperLink;
	}

	public String getToolTipText() {
		return mHyperLink;
	}
	
}
