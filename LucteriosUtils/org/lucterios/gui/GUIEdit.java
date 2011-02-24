package org.lucterios.gui;

public interface GUIEdit extends GUIComponent {

	public void setTextString(String text);

	public String getTextString();

	public void setValue(double aVal);

	public void setRange(double aMinVal, double aMaxVal, int aPrecVal);

	public double getValue();

	public void setFloatEditor(boolean isFloatEditor);

	public boolean isFloatEditor();

}
