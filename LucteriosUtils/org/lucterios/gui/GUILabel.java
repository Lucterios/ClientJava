package org.lucterios.gui;

public interface GUILabel extends GUIComponent {

	public AbstractImage getImage();

	public void setImage(AbstractImage iamge);

	public String getTextString();

	public void setTextString(String text);

	public void setStyle(int style);
	
}
