package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;

public class WCheckBox extends CheckBox implements GUICheckBox,OnFocusChangeListener,OnClickListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}
			
	public WCheckBox(Context context, WContainer owner) {
		super(context);
		setOnFocusChangeListener(this);
		setOnClickListener(this);
	}

	public void onClick(View v) {
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
	}
	
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public String getTextString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTextString(String text) {
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
