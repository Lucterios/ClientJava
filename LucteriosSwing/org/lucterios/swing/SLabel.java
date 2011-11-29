package org.lucterios.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUILabel;
import org.lucterios.ui.GUIActionListener;

public class SLabel extends JLabel implements GUILabel {

	private static final long serialVersionUID = 1L;
	
	private CursorMouseListener mCursorMouseListener;

	public void clearFocusListener() {}
	
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

	public int getFontSize() {
		Font font=getFont();
		return font.getSize();
	}

	public void setFontSize(int size) {
		Font font=getFont();
		setFont(new Font(font.getName(), font.getStyle(), size));
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

	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
