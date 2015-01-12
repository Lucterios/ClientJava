package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIHyperText extends GUIComponent {

	public String getTextString();

	public void setTextString(String text);

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);

	public void setHyperLink(String string);

	public String getToolTipText();

	
}
