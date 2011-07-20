package org.lucterios.gui;

public interface GUIButton extends GUIComponent {

	public interface GUIActionListener {
	    public void actionPerformed();
	}
	
	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);

	public String getTextString();
	
	public void setTextString(String text);

	public void setImage(AbstractImage image);

	public void setMnemonic(char c);

	public void setToggle(boolean isToogle);

	public boolean isSelected();
}
