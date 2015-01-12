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

	public void setMenuImage(AbstractImage image, boolean showIcon);

	public void setVisible(boolean isVisible);

	public void setTag(int tag);
	
	public String getDescription();

	public String getName();
	
	public String getText();
	
	public int getTag();

	public AbstractImage getMenuImage();
	
	public GUIActionListener getActionListener();
	
	public boolean isNode();

	public boolean isPoint();
	
	public int getMenuCount(); 
	
	public GUIMenu getMenu(int index);
	
	public GUIMenu addMenu(boolean isNode);

	public void addSeparator();

	public void removeMenu(int index);

	public void setEnabled(boolean b);

	public String getAcceleratorText();

	public GUIAction getAction();

	public void removeAll();

}
