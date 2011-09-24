package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.Tools;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class WMemo extends EditText implements GUIMemo,OnFocusChangeListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>();
	private String mName;
	private GUIComponent mOwner; 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
	
	public WMemo(Context context, WContainer owner) {
		super(context);
		mOwner=owner;
		setSingleLine(false);
		setLines(10);
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
		return Tools.replace(getText().toString(), "\n", "{[newline]}");
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

	public void setValue(String text) {
		super.setText(text);
	}

	public boolean isVisible() {
		return true;
	}

	public void setVisible(boolean visible) { }

	public GUIMenu getPopupMenu() {
		return null;
	}

	public void insertText(String specialToAdd) {
		// TODO Auto-generated method stub	
	}

	public void addActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public int getBackgroundColor() {
		return 0;
	}

	public String getName() {
		return mName;
	}

	public GUIComponent getOwner() {
		return mOwner;
	}

	public boolean isActive() {
		if (mOwner!=null)
			return mOwner.isActive();
		else
			return false;
	}

	public void removeActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public void repaint() {	}

	public void requestFocusGUI() {	}

	public void setActiveMouseAction(boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	public void setName(String name) {
		mName=name;
	}

	public void setToolTipText(String toolTip) {}

	public void setNbClick(int mNbClick) { }
}
