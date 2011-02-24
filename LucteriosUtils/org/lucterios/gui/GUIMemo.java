package org.lucterios.gui;

public interface GUIMemo extends GUIComponent {

	public void setStringSize(int aStringSize);

	public int getFirstLine();

	public void setFirstLine(int aFirstLine);

	public void setTabs(int charactersPerTab);
	
	public void setText(String text);
	
	public String getValue();
	
}
