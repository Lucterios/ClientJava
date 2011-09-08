package org.lucterios.swing;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUILabel;
import org.lucterios.ui.GUIActionListener;

public class SLabel extends JLabel implements GUILabel {

	private static final long serialVersionUID = 1L;
	
	private CursorMouseListener mCursorMouseListener;

	public void addFocusListener(GUIFocusListener l) {}

	public void removeFocusListener(GUIFocusListener l) {}

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SLabel(GUIComponent aOwner){
        super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
		setStyle(0);
		setFocusable(false);
		addMouseListener(mCursorMouseListener);		
	}
	
	public AbstractImage getImage() {		
		return new SwingImage(getIcon());
	}

	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage) {
			setIcon((ImageIcon)image.getData());
		}
	}

	public void setImage(URL image) {
		setIcon(new ImageIcon(image));
	}
	
	public String getTextString() {
		return getText();
	}

	public void setTextString(String text) {
		setText(text);		
	}

	public void setStyle(int style) {
		Font font=getFont();
		setFont(new Font(font.getName(), style, font.getSize()));		
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
	
}
