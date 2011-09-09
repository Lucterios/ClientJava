package org.lucterios.graphic;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.lucterios.gui.AbstractImage;

public class SwingImage extends AbstractImage {

	public SwingImage() {
		mObject=new ImageIcon();
	}	

	public SwingImage(byte[] imageData) {
		super(imageData);
	}	
	
	public SwingImage(ImageIcon iconImage) {
		mObject=iconImage;
	}	

	public SwingImage(Icon iconImage) {
		if (iconImage==null)
			mObject=null;
		else if( iconImage instanceof ImageIcon ) 
			mObject=iconImage;
		else {
			BufferedImage image = new BufferedImage( iconImage.getIconWidth() , iconImage.getIconHeight() , BufferedImage.TYPE_INT_RGB );
			iconImage.paintIcon(null, image.getGraphics() , 0 , 0 );
			mObject=new ImageIcon(image);
		}
	}	
	
	public SwingImage(AbstractImage image) {
		if ((image!=null) && (image instanceof SwingImage)) {
			mObject=new ImageIcon(((ImageIcon)((SwingImage)image).getData()).getImage());
		}
		else
			mObject=new ImageIcon();
	}
	
	@Override
	public int getHeight() {
		if (mObject!=null)
			return ((ImageIcon)mObject).getIconHeight();
		else
			return 0;
	}

	@Override
	public int getWidth() {
		if (mObject!=null)
			return ((ImageIcon)mObject).getIconWidth();
		else
			return 0;
	}

	@Override
	protected void initialize(byte[] imageData) {
		mObject=new ImageIcon(imageData);
	}

	@Override
	public boolean load(File file) {
		FileImageInputStream fiis;
		try {
			fiis = new FileImageInputStream(file);
			BufferedImage img=ImageIO.read(fiis);			
			mObject=new ImageIcon(img);
			return true;
		} catch (FileNotFoundException e) {
			mObject=new ImageIcon();
		} catch (IOException e) {
			mObject=new ImageIcon();
		}
		return false;
	}
	
	@Override
	public boolean load(URL url) {
		mObject=new ImageIcon(url);
		return true;
	}

	public AbstractImage resizeIcon(int aHeight,boolean aOnlyIfBigger) {
    	ImageIcon aIcon=(ImageIcon)mObject;
    	if ((aIcon!=null) && (!aOnlyIfBigger || (aIcon.getIconHeight()>aHeight))){
			int scaledW = (int)(aHeight * (double)aIcon.getIconWidth()/(double)aIcon.getIconHeight());
    		return resize(aHeight, scaledW);
    	}
    	else
    		return this;
	}

	@Override
	public AbstractImage resize(int height, int width) {
    	ImageIcon aIcon=(ImageIcon)mObject;
    	if ((aIcon!=null) && (height>0) && (width>0) && (height!=aIcon.getIconHeight()) && (width!=aIcon.getIconWidth())){
			Image inImage = aIcon.getImage();
			ImageIcon result_icon=new ImageIcon(inImage.getScaledInstance(width , height, Image.SCALE_SMOOTH));
			return new SwingImage(result_icon);	
    	}
    	else
    		return this;
	}
	
}
