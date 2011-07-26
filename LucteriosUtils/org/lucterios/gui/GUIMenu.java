package org.lucterios.gui;

import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

public interface GUIMenu {

	public void setAction(GUIAction action);

	public void setActionListener(GUIActionListener actionListener);
	
	public void setDescription(String description);

	public void setText(String string);

	public void setMnemonic(char c);

	public void setName(String string);
	
	public String getDescription();

	public String getText();

	public AbstractImage getIcon();
	
	public GUIActionListener getActionListener();
	
	public boolean isNode();

	public boolean isPoint();
	
	public int getMenuCount(); 
	
	public GUIMenu getMenu(int index);
	
	public GUIMenu addMenu(boolean isNode);

	public void addSeparator();

	public void remove(int index);

	public void setEnabled(boolean b);

	public String getAcceleratorText();

	public GUIAction getAction();

}
