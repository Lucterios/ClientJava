package org.lucterios.swing;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class SCheckBox extends JCheckBox implements GUICheckBox {
	private static final long serialVersionUID = 1L;

	private CursorMouseListener mCursorMouseListener;
	
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	
	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage)
			setIcon((ImageIcon)image.getData());
	}

	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
	}

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SCheckBox(GUIComponent aOwner){
        super();
        mCursorMouseListener=new CursorMouseListener(this,this);
        mOwner=aOwner;
        addFocusListener(mFocusListener);	
        addActionListener(mCursorMouseListener);
        addMouseListener(mCursorMouseListener);
	}

	public String getTextString() {
		return getText();
	}

	public void setTextString(String text) {
		setText(text);
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}

	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		setFocusable(aFlag);
	}
	
	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
