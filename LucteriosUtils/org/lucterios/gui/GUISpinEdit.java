package org.lucterios.gui;

import org.lucterios.gui.GUIButton.GUIActionListener;

public interface GUISpinEdit extends GUIComponent {

	public void addActionListener(GUIActionListener l);

	public void removeActionListener(GUIActionListener l);

	public long getBottomLimit();

	public void setBottomLimit(long limit);

	public long getUpperLimit();

	public void setUpperLimit(int limit);

	public boolean isReverse();

	public void setReverse(boolean r);

	public void setNumber(long value);

	public long getNumber();

	public void init(long num, long bottomL, long upperL);
	
}
