package org.lucterios.gui;

public interface GridInterface {

	public abstract int getColumnCount();

	public abstract int getRowCount();

	public abstract String getColumnName(int col);

	public abstract Object getValueAt(int row, int col);

	public abstract Class<?> getColumnClass(int c);

}