package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GridInterface;

public class MockGrid extends MockComponent implements GUIGrid {

	public void addSelectListener(GUISelectListener l) { }

	public void removeSelectListener(GUISelectListener l) {	}
		
	public MockGrid(GUIComponent aOwner){
        super(aOwner);
	}

	private GridInterface mGridInterface=null;
	public void setGridInterface(GridInterface gridInterface) {
		mGridInterface=gridInterface;
	}
	
	public void refreshData(){
	}

	public int getSelectedRowCount() {
		return 1;
	}

	public int[] getSelectedRows() {
		return new int[]{mRowSelect};
	}

	private boolean mMultiSelection=false;
	public void setMultiSelection(boolean isMultiSelection) {
		mMultiSelection=isMultiSelection;
	}
	public boolean getMultiSelection(){
		return mMultiSelection;
	}

	private int mRowSelect=-1;
	public void setRowSelection(int row) {
		if (row>=mGridInterface.getRowCount())
			row=mGridInterface.getRowCount()-1;
		if (row>=0)
			mRowSelect=row;
		else
			mRowSelect=-1;
	}

	public void clearTable(){
	}

}
