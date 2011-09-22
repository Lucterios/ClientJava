package org.lucterios.android.widget;

import java.util.ArrayList;

import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;

public class WCheckList extends RelativeLayout implements GUICheckList,OnFocusChangeListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
			
	public WCheckList(Context context, WContainer owner) {
		super(context);
		setOnFocusChangeListener(this);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public Object[] getSelectedValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setListData(Object[] listData) {
		// TODO Auto-generated method stub
		
	}

	public void setMultiSelection(boolean isMultiSelection) {
		// TODO Auto-generated method stub
		
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	public void addSelectListener(GUISelectListener l) {
		// TODO Auto-generated method stub
		
	}

	public void clearList() {
		// TODO Auto-generated method stub
		
	}

	public void removeSelectListener(GUISelectListener l) {
		// TODO Auto-generated method stub
		
	}

	public void setSelectedIndices(int[] indices) {
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
