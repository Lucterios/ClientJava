package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIGrid extends GUIComponent {

	public interface GUISelectListener {
	    public void selectionChanged();
	}

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);
	
	public void addSelectListener(GUISelectListener l);

	public void removeSelectListener(GUISelectListener l);
	
	public void setGridInterface(GridInterface gridInterface);
	
	public void refreshData();

	public int getSelectedRowCount();

	public int[] getSelectedRows();

	public void setMultiSelection(boolean isMultiSelection);

	public void setRowSelection(int row);

}
