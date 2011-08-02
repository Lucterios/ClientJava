package org.lucterios.gui;

import java.net.URL;

public interface GUILabel extends GUIComponent {

	public AbstractImage getImage();

	public void setImage(AbstractImage iamge);

	public String getTextString();

	public void setTextString(String text);

	public void setStyle(int style);
	
	public void setImage(URL image);

	public void setBackgroundColor(int color);

	public void setSize(int width, int height);
	
}
