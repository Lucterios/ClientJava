package org.lucterios.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIImage;
import org.lucterios.ui.GUIActionListener;

public class SImage extends JComponent implements GUIImage {

	private static final long serialVersionUID = 1L;
	
	private CursorMouseListener mCursorMouseListener;

	public void addFocusListener(GUIFocusListener l) {}

	public void removeFocusListener(GUIFocusListener l) {}
	
	private SwingImage mImage=new SwingImage();

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SImage(GUIComponent aOwner){
        super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
		setFocusable(false);
		addMouseListener(mCursorMouseListener);		
	}
	
	public AbstractImage getImage() {		
		return mImage;
	}

	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage) {
			mImage=(SwingImage)image;
			Dimension dim=new Dimension(mImage.getWidth(),mImage.getHeight());
			setSize(dim);
			setPreferredSize(dim);
			setMinimumSize(dim);
			setMaximumSize(dim);
		}
	}

	public void setImage(URL image) {
		SwingImage img=new SwingImage();
		img.load(image);
		setImage(img);
	}
	
	public void setBackgroundColor(int color) {
		setBackground(new Color(color));		
	}
	
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
	}

	public boolean isActive() {
		return getOwner().isActive();
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (mImage.getData()!=null)
			g.drawImage(((ImageIcon)mImage.getData()).getImage(), 0, 0, this);
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}
}
