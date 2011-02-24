package org.lucterios.gui;

public interface GUIComponent {

	public interface GUIFocusListener {
	    public void focusLost();
	}
	
	public void addFocusListener(GUIFocusListener l);

	public void removeFocusListener(GUIFocusListener l);

	public void setVisible(boolean visible);
	
	public boolean isVisible();
	
	public void setEnabled(boolean enabled);
	
	public boolean isEnabled();
	
}
