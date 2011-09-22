package org.lucterios.swing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JScrollPane;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.TextCode;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIActionListener;

public class SMemo extends JScrollPane implements GUIMemo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CursorMouseListener mCursorMouseListener;   
	private FocusListenerList mFocusListener=new FocusListenerList();
	
	private TextCode cmp_text;
	
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
    	cmp_text=new TextCode();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(cmp_text,this);
        cmp_text.addFocusListener(mFocusListener);
        cmp_text.addMouseListener(mCursorMouseListener);       
        cmp_text.setFocusable(true);
        cmp_text.setAutoscrolls(true);
		setViewportView(cmp_text);
		setFocusable(false);
    }             
    
	public void setSize(Dimension d)
	{
		if (d.width < getParent().getSize().width)
			d.width = getParent().getSize().width;

		super.setSize(d);
	}

	public void setBackgroundColor(int color) {
		cmp_text.setBackground(new Color(color));	
	}
	
	public int getBackgroundColor(){
		return cmp_text.getBackground().getRGB();
	}

	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) { }

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		cmp_text.setFocusable(aFlag);
	}

	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		cmp_text.requestFocus();
	}

	public int getFirstLine() {
		return cmp_text.getFirstLine();
	}

	public GUIMenu getPopupMenu() {
		JMenu menu=new JMenu();
		cmp_text.getPopupListener().getPopup().add(menu);
		return new SMenu(menu);
	}

	public String getValue() {
		return cmp_text.getValue();
	}

	public void insertText(String specialToAdd) {
		cmp_text.insertText(specialToAdd);
	}

	public void setFirstLine(int aFirstLine) {
		cmp_text.setFirstLine(aFirstLine);
	}

	public void setStringSize(int aStringSize) {
		cmp_text.setStringSize(aStringSize);
	}

	public void setTabs(int charactersPerTab) {
		cmp_text.setTabs(charactersPerTab);	
	}

	public void setText(String text) {
		cmp_text.setText(text);
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
