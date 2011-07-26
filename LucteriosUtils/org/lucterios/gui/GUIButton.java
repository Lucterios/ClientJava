package org.lucterios.gui;

import java.net.URL;

import org.lucterios.ui.GUIActionListener;

public interface GUIButton extends GUIComponent {

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);

	public String getTextString();
	
	public void setTextString(String text);

	public void setImage(URL image);
	
	public void setImage(AbstractImage image);

	public void setMnemonic(char c);

	public void setToggle(boolean isToogle);

	public boolean isSelected();

	public void setBackgroundColor(int color);

	public int getBackgroundColor();

}
