package org.lucterios.client.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

import org.lucterios.engine.utils.AbstractImage;

public class SwingImage extends AbstractImage {

	public SwingImage() {
		mObject=new ImageIcon();
	}	
	
	public SwingImage(ImageIcon iconImage) {
		mObject=iconImage;
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

}
