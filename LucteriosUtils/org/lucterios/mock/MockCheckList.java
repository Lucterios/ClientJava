package org.lucterios.mock;

import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid.GUISelectListener;

public class MockCheckList extends MockComponent implements GUICheckList {
	public void addSelectListener(GUISelectListener l){
	}

	public void removeSelectListener(GUISelectListener l) {
	}	
	
	public MockCheckList(GUIComponent aOwner){
        super(aOwner);
	}
	
	private Object[] mData=new Object[]{};
	public void setListData(Object[] listData){
		mData=listData;
	}

	public void clearList() {
		mData=new Object[]{};
	}
	
	private boolean mIsMultiSelection=true;
	public void setMultiSelection(boolean isMultiSelection) {
		mIsMultiSelection=isMultiSelection;
	}
	public boolean isMultiSelection(){
		return mIsMultiSelection;
	}
	
	public Object[] getSelectedValues(){
		return mData;
	}

	public void setSelectedIndices(int[] indices) {
	}

	
}
