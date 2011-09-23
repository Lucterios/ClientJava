package org.lucterios.android.widget;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUILabel;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.widget.TextView;

public class WLabel extends TextView implements GUILabel {

	public void addFocusListener(GUIFocusListener l){}

	public void removeFocusListener(GUIFocusListener l){}
	
	
	public WLabel(Context context, WContainer owner) {
		super(context);
	}

	public String getTextString() {
		return getText().toString();
	}

	public void setTextString(String text) {
		setText(text);
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public int getFontSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFontSize(int size) {
		// TODO Auto-generated method stub
		
	}

	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setStyle(int style) {
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

	public void setNbClick(int mNbClick) { }
}
