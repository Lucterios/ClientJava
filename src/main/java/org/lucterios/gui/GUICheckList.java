package org.lucterios.gui;

import org.lucterios.gui.GUIGrid.GUISelectListener;

public interface GUICheckList extends GUIComponent {

	public void addSelectListener(GUISelectListener l);
	
	public void removeSelectListener(GUISelectListener l);
	
	public Object[] getSelectedValues();

	public void setMultiSelection(boolean isMultiSelection);

	public void setListData(Object[] listData);

	public void clearList();
	
	public void setSelectedIndices(int[] indices);

}
