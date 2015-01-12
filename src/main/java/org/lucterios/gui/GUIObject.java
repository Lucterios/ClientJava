package org.lucterios.gui;

public interface GUIObject {

	public abstract void setVisible(boolean aVisible);

	public abstract void setActive(boolean aIsActive);
	
	public abstract void refresh();
	
	public void dispose();
	
}
