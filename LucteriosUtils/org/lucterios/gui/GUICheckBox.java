package org.lucterios.gui;

import org.lucterios.gui.GUIButton.GUIActionListener;

public interface GUICheckBox extends GUIComponent {

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);
	
	public boolean isSelected();

	public void setSelected(boolean b);

	public String getTextString();
	
	public void setTextString(String text);	
}
