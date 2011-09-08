package org.lucterios.gui;

public interface GUIEdit extends GUIComponent {

	public void setTextString(String text);

	public String getTextString();

	public void setValue(double aVal);

	public void setRange(double aMinVal, double aMaxVal, int aPrecVal);

	public double getValue();

	public void setFloatEditor(boolean isFloatEditor);

	public boolean isFloatEditor();

	public void setPassword(char c);

	public int getColumns();

	public void setColumns(int col);

	public int[] getCaretPositions();

	public void setCaretPosition(int pos);

	public void setSelectionStart(int pos);

	public void setSelectionEnd(int pos);

}
