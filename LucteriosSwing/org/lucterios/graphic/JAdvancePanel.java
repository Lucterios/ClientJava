package org.lucterios.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class JAdvancePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void traceButtonFrame(Graphics g,Color aTopLeft,Color aBottomRight) {
	   int dy = getSize ().height;
	   int dx = getSize ().width;		   
	   for(int dec=0;dec<3;dec++) {
		   g.setColor (aTopLeft);
		   g.drawLine(dec, dec, dec,dy-dec);
		   g.drawLine(dec, dec, dx-dec,dec);
		   g.setColor (aBottomRight);
		   g.drawLine(dec, dy-dec, dx-dec,dy-dec);
		   g.drawLine(dx-dec, dec, dx-dec,dy-dec);		
	   }
	}

	public void setFontImage(Image aImage,int aMode) {
	}
	public Image getFontImage() {
		return null;
	}
	
	public static final int NONE = 0;
	public static final int CENTRE = 1;
	public static final int TEXTURE = 2;
	public static final int STRETCH = 3;
	
	public int Tag=0;

}
