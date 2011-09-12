package org.lucterios.mock;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGraphic;

public class MockGraphic implements GUIGraphic {
	
	public MockGraphic(){
		super();
	}

	public boolean drawImage(AbstractImage img, int x, int y, int width,
			int height, GUIComponent owner) {
		return true;
	}

	public void fillRect(int x, int y, int width, int height) {
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
	}

	public void setColor(int color) {
	}

}
