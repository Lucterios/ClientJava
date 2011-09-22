package org.lucterios.swing;

import java.awt.Color;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.LucteriosEditor;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.ui.GUIActionListener;

public class SHyperMemo extends LucteriosEditor implements GUIHyperMemo {

	private static final long serialVersionUID = 1L;

	private CursorMouseListener mCursorMouseListener;   
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	private GUIComponent mOwner=null;
	
	public SHyperMemo(GUIComponent owner) {
		super(false);
		mOwner=owner;
        addFocusListener(mFocusListener);
        addMouseListener(mCursorMouseListener);
	}
	
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) { }

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}
	
	public int getBackgroundColor() {
		return getBackground().getRGB();
	}

	public boolean isActive() {
		return getOwner().isActive();
	}


	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
