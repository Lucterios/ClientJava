package org.lucterios.gui;

public interface GUIHyperMemo extends GUIComponent {

	void addStandardPopupMenu(boolean withFileAction);

	void addSpecialMenus(String menuName,Object[] variables,String tagBegin,String tagEnd);

	void load(String source);

	String save();
	
}
