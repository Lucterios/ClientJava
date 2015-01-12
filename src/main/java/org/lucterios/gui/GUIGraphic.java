package org.lucterios.gui;

public interface GUIGraphic {
	
    public void setColor(int color);
    
    public void fillRect(int x, int y, int width, int height);
    
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight);

    public boolean drawImage(AbstractImage img, int x, int y,int width, int height, GUIComponent owner);
    
}
