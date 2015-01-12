package org.lucterios.gui;

public interface GUILabel extends GUIComponent {

	public String getTextString();

	public void setTextString(String text);

	public void setStyle(int style);
	
	public void setBackgroundColor(int color);

	public void setSize(int width, int height);

	public int getFontSize();
	
	public void setFontSize(int size);
	
}
