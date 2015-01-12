package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUICombo extends GUIComponent {

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);
	
	public void removeAllElements();
	
	public void addElement(Object obj);
	
	public Object getSelectedItem();

	public int getSelectedIndex();
	
	public void setSelectedIndex(int index);

	public void addList(Object[] Items);
	
}
