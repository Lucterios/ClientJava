package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIEdit;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class WEdit extends EditText implements GUIEdit,OnFocusChangeListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	
	public void clearFocusListener(){
		mFocusListener.clear();
	}

	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
	
	public WEdit(Context context, WContainer owner) {
		super(context);
		// TODO Auto-generated constructor stub
		setOnFocusChangeListener(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isFloatEditor() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFloatEditor(boolean isFloatEditor) {
		// TODO Auto-generated method stub
		
	}

	public void setRange(double aMinVal, double aMaxVal, int aPrecVal) {
		// TODO Auto-generated method stub
		
	}


	public void setValue(double aVal) {
		// TODO Auto-generated method stub
		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public String getTextString() {
		return getText().toString();
	}

	public void setTextString(String text) {
		setText(text);
	}

	public int[] getCaretPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCaretPosition(int pos) {
		// TODO Auto-generated method stub
		
	}

	public void setColumns(int col) {
		// TODO Auto-generated method stub
		
	}

	public void setPassword(char c) {
		// TODO Auto-generated method stub
		
	}

	public void setSelectionEnd(int pos) {
		// TODO Auto-generated method stub
		
	}

	public void setSelectionStart(int pos) {
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
