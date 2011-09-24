package org.lucterios.swing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenu;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.TextCode;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIActionListener;

public class SMemo extends TextCode implements GUIMemo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CursorMouseListener mCursorMouseListener;   
	private FocusListenerList mFocusListener=new FocusListenerList();
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
    
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SMemo(GUIComponent aOwner){
    	super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
        addMouseListener(mCursorMouseListener);
        addFocusListener(mFocusListener);
    }             
    
	public void setSize(Dimension d)
	{
		if (d.width < getSize().width)
			d.width = getSize().width;
		super.setSize(d);
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}
	
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) { }

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
	
	public GUIMenu getPopupMenu() {
		JMenu menu=new JMenu();
		getPopupListener().getPopup().add(menu);
		return new SMenu(menu);
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(1);
	}

}
