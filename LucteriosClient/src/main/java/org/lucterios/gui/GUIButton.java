package org.lucterios.gui;

import java.net.URL;

public interface GUIButton extends GUIComponent {

	public String getTextString();
	
	public void setTextString(String text);

	public void setImage(URL image);
	
	public void setImage(AbstractImage image);

	public void setMnemonic(char c);

	public void setToggle(boolean isToogle);

	public boolean isSelected();

	public void setBackgroundColor(int color);

	public int getBackgroundColor();

	public void setSelected(boolean select);

	public void doClick();

	public void setObject(Object aAction);

	public Object getObject();
}
