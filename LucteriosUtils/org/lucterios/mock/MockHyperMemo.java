package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperMemo;

public class MockHyperMemo extends MockMemo implements GUIHyperMemo {

	private static final long serialVersionUID = 1L;

	public MockHyperMemo(GUIComponent owner) {
		super(owner);
	}
	
	public void addSpecialMenus(String menuName, Object[] variables,String tagBegin, String tagEnd) {
		
	}

	public void addStandardPopupMenu(boolean withFileAction) {	}

	public void load(String source) {	}

	public String save() {
		return "";
	}

}
