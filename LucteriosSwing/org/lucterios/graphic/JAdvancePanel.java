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

	//private int mMode=NONE;
	//private BufferedImage mFontImage=null;
	//private Image mImage=null;
	public void setFontImage(Image aImage,int aMode) {
/*		mImage=aImage;
		if (aImage!=null) {
			aImage = new ImageIcon(aImage).getImage();
			mFontImage = new BufferedImage( aImage.getWidth(null), aImage.getHeight(null), BufferedImage.TYPE_INT_RGB); 
			Graphics graph = mFontImage.createGraphics(); 
			graph.setColor(Color.white); 
			graph.fillRect(0, 0, aImage.getWidth(null),aImage.getHeight(null)); 
			graph.drawImage(aImage, 0, 0, null); 
			graph.dispose();		
			mMode=aMode;
		}
		else {
			mFontImage = null;
			mMode=NONE;
		}*/			
	}
	public Image getFontImage() {
		return null;
		//return mImage;
	}
	
	public static final int NONE = 0;
	public static final int CENTRE = 1;
	public static final int TEXTURE = 2;
	public static final int STRETCH = 3;
	
/*	public void paintComponent(Graphics g)
	{	
		switch( mMode )
		{	
			case TEXTURE :
				TexturePaint texture = new TexturePaint(mFontImage,new Rectangle(0, 0, mFontImage.getWidth(), mFontImage.getHeight()));
				Graphics2D g2d = (Graphics2D)g; 
				g2d.setPaint(texture);
				g2d.fillRect(0, 0, getWidth(), getHeight() );
				break;
			case CENTRE :
				g.setColor(this.getBackground());
				g.fillRect(0,0,getWidth(), getHeight() );
				g.drawImage(mFontImage,(getWidth()-mFontImage.getWidth())/2,(getHeight()-mFontImage.getHeight())/2,null);
				break;
			case STRETCH:
				g.setColor(this.getBackground());
				g.fillRect(0,0,getWidth(), getHeight() );
				g.drawImage(mFontImage,0,0,getWidth(),getHeight(),null);
				break;
			default:
				super.paintComponents(g);
				break;				
		}
	}*/

	public int Tag=0;

}
