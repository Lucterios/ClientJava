/*
*    This file is part of Lucterios.
*
*    Lucterios is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Lucterios is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lucterios; if not, write to the Free Software
*    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
*	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/

package org.lucterios.graphic;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

public class Tools {

    static public void calculBtnSize(JButton[] btns)
    {
        int wbtn=0;
        int hbtn=0;
        for(javax.swing.JButton btn:btns) {
            wbtn=Math.max(wbtn,btn.getPreferredSize().width);
            hbtn=Math.max(hbtn,btn.getPreferredSize().height);
        }
        for(javax.swing.JButton btn:btns) {
            btn.setPreferredSize(new java.awt.Dimension(wbtn,hbtn));
            btn.setMaximumSize(new java.awt.Dimension(wbtn,hbtn));
            btn.setMinimumSize(new java.awt.Dimension(wbtn,hbtn));
        }
    }
    
    static public ImageIcon resizeIcon(ImageIcon aIcon,int aHeight,boolean aOnlyIfBigger)
    {
    	ImageIcon result_icon=aIcon;
    	if ((aIcon!=null) && (!aOnlyIfBigger || (aIcon.getIconHeight()>aHeight))){
			Image inImage = aIcon.getImage();
			int scaledW = (int)(aHeight * (double)inImage.getWidth(null)/(double)inImage.getHeight(null));
			result_icon=new ImageIcon(inImage.getScaledInstance(scaledW , aHeight, Image.SCALE_SMOOTH));
    	}
		return result_icon;
    }

 // This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
    
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
    
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
    
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
    
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
    
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);
    
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
    
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
    
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }

    static public String getKeyString(KeyStroke aKey)
    {
		String key_string = "";
		if (aKey != null)
			key_string += " [" + aKey.toString() + "]";
		key_string=org.lucterios.utils.Tools.replace(key_string,"pressed"," ");
		return key_string;
    }

    private final static int TIME_TO_ORDER=2*1000; // 2sec 

    private static Date postOrderGCdate = null;
    public static void postOrderGC(){
    	long current_time=new Date().getTime(); 
    	postOrderGCdate=new Date(current_time+TIME_TO_ORDER);
    }       
    
    public static ActionListener createOrderGCAction(){
    	ActionListener action=new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (postOrderGCdate!=null) {
					long current_time=new Date().getTime();
					if (postOrderGCdate.getTime()<current_time){
						org.lucterios.utils.Tools.clearGC();
						postOrderGCdate=null;
					}
				}
				
			}
    	};
    	return action;
    }

    public static Insets convertcoordToInsets(int[] coord){
    	if (coord.length==4)
    		return new Insets(coord[0],coord[1],coord[2],coord[3]);
    	else
    		return null;
    }
}
