package org.lucterios.gui;

import org.lucterios.gui.NotifyFrameChange;
import org.lucterios.gui.NotifyFrameList;
import org.lucterios.gui.NotifyFrameObserver;

public interface GUIForm extends GUIObject {

	public GUIGenerator getGenerator();	
	
	public abstract String getName();

	public abstract String getTitle();

	public abstract void setTitle(String title);
	
	public abstract void setLocation(int x, int y);

	public abstract void setSize(int width, int height);

	public abstract void requestFocus();

	public abstract void toFront();
	
	public abstract void activate();

	public abstract void Change();

	public abstract void setNotifyFrameList(NotifyFrameList aNotifyFrameList);

	public abstract void setNotifyFrameChange(NotifyFrameChange aNotifyFrameChange);

	public abstract void setNotifyFrameObserver(NotifyFrameObserver aNotifyFrameObserver);

	public abstract void setSelected(boolean aSelected);

	public abstract void refreshSize();

	public void Close();

}