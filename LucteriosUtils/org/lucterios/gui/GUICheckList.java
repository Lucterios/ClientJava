package org.lucterios.gui;

public interface GUICheckList extends GUIComponent {

	public Object[] getSelectedValues();

	public void setMultiSelection(boolean isMultiSelection);

	public abstract void setListData(Object[] listData);

}
