package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;

public class WMemo extends TextView implements GUIMemo,OnFocusChangeListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
	
	public WMemo(Context context, WContainer owner) {
		super(context);
		setOnFocusChangeListener(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public int getFirstLine() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFirstLine(int aFirstLine) {
		// TODO Auto-generated method stub
		
	}

	public void setStringSize(int aStringSize) {
		// TODO Auto-generated method stub
		
	}

	public void setTabs(int charactersPerTab) {
		// TODO Auto-generated method stub
		
	}

	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public GUIMenu getPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertText(String specialToAdd) {
		// TODO Auto-generated method stub
		
	}

	public void addActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public int getBackgroundColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public GUIComponent getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	public void requestFocusGUI() {
		// TODO Auto-generated method stub
		
	}

	public void setActiveMouseAction(boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	public void setToolTipText(String toolTip) {
		// TODO Auto-generated method stub
		
	}

}
