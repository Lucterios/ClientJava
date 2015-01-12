package org.lucterios.gui;

import java.net.URL;

public interface GUIImage extends GUIComponent {

	public AbstractImage getImage();

	public void setImage(AbstractImage iamge);

	public void setImage(URL image);

	public void setBackgroundColor(int color);

	public void setSize(int width, int height);

}
