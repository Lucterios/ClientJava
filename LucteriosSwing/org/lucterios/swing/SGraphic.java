package org.lucterios.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.ImageIcon;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGraphic;

public class SGraphic implements GUIGraphic {
	
	private Graphics mGraphic;
	
	public SGraphic(Graphics graphic){
		mGraphic=graphic;
	}

	public boolean drawImage(AbstractImage img, int x, int y, int width,
			int height, GUIComponent owner) {
		ImageIcon icon=(ImageIcon)img.getData();
		return mGraphic.drawImage(icon.getImage(), x, y, width, height, (Component)owner);
	}

	public void fillRect(int x, int y, int width, int height) {
		mGraphic.fillRect(x, y, width, height);
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		mGraphic.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	public void setColor(int color) {
		mGraphic.setColor(new Color(color));
	}

}
