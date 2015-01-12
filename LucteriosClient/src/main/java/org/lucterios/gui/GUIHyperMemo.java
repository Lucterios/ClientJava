package org.lucterios.gui;

public interface GUIHyperMemo extends GUIComponent {

	void addStandardPopupMenu(boolean withFileAction);

	void addSpecialMenus(String menuName,Object[] variables,String tagBegin,String tagEnd);
	
	public GUIMenu getPopupMenu();

	void load(String source);
	
	void insertText(String text);

	String save();
	
}
