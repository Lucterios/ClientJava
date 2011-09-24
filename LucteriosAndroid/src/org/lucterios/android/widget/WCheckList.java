package org.lucterios.android.widget;

import java.util.ArrayList;
import java.util.List;

import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WCheckList extends ListView implements GUICheckList,OnFocusChangeListener,android.widget.AdapterView.OnItemClickListener {

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>();
	private Object[] mlistData=new Object[0];
	private List<Object> mSelectedValues=new ArrayList<Object>();
	private GUIComponent mOwner;
	private String mName; 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}
			
	public WCheckList(Context context, WContainer owner) {
		super(context);
		mOwner=owner;
		setOnFocusChangeListener(this);
        setOnItemClickListener(this);
        setMultiSelection(true);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus && GUIComponent.class.isInstance(v))
			for(GUIFocusListener l:mFocusListener)
				l.focusLost(null,(GUIComponent)v);		
	}

	public Object[] getSelectedValues() {
		return mSelectedValues.toArray();
	}

	public void setListData(Object[] listData) {
		mlistData=listData;
		setAdapter(new ArrayAdapter<Object>(getContext(), android.R.layout.simple_list_item_multiple_choice,mlistData));
	}

	public void setMultiSelection(boolean isMultiSelection) {
		if (isMultiSelection)
			setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		else
			setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	public boolean isVisible() {
		return true;
	}

	public void setVisible(boolean visible) { }

	public void addSelectListener(GUISelectListener l) {
		// TODO Auto-generated method stub
	}

	public void clearList() {
		setListData(new Object[0]);
	}

	public void removeSelectListener(GUISelectListener l) {
		// TODO Auto-generated method stub
	}

	public void setSelectedIndices(int[] indices) {
        for(int index = 0; index < mlistData.length ; index++)
			setItemChecked(index, false);
		for(int index=0;index<indices.length;index++)
			setItemChecked(indices[index], true);
	}

	public void addActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public int getBackgroundColor() {
		// TODO Auto-generated method stub
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
		mName=name;
	}

	public void setToolTipText(String toolTip) {
		// TODO Auto-generated method stub
		
	}

	public void setNbClick(int mNbClick) { }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mSelectedValues.clear();
        SparseBooleanArray checked_items = getCheckedItemPositions();
        for(int index = 0; index < mlistData.length ; index++)
        {
            if (checked_items.valueAt(index)) {
            	mSelectedValues.add(mlistData[index]);
            }
        }
	}
}
