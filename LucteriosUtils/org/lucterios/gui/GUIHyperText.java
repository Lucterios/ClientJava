package org.lucterios.gui;

import org.lucterios.gui.GUIButton.GUIActionListener;

public interface GUIHyperText {

	public String getTextString();

	public void setTextString(String text);

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);

	public void setHyperLink(String string);
	
}
