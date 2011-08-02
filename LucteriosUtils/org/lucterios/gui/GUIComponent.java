package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIComponent {

	public interface GUIFocusListener {
	    public void focusLost();
	}
	
	public void addFocusListener(GUIFocusListener l);

	public void removeFocusListener(GUIFocusListener l);

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);
	
	public void setBackgroundColor(int color);
	
	public int getBackgroundColor();
	
	public void setVisible(boolean visible);
	
	public boolean isVisible();
	
	public void setEnabled(boolean enabled);
	
	public boolean isEnabled();
	
	public void repaint();

	public void setToolTipText(String toolTip);

	public void setActiveMouseAction(boolean isActive);
}
