package org.lucterios.client.utils;

import org.lucterios.client.utils.NotifyFrameObserver;
import org.lucterios.client.utils.NotifyFrameChange;
import org.lucterios.client.utils.NotifyFrameList;

public interface IForm {

	public abstract String getName();

	public abstract String getTitle();

	public abstract void setTitle(String title);
	
	public abstract void setLocation(int x, int y);

	public abstract void setSize(int width, int height);

	public abstract void requestFocus();

	public abstract void toFront();
	
	public abstract void dispose();
	
	public abstract void activate();

	public abstract void Close();

	public abstract void Change();

	public abstract void setNotifyFrameList(NotifyFrameList aNotifyFrameList);

	public abstract void setNotifyFrameChange(NotifyFrameChange aNotifyFrameChange);

	public abstract void setNotifyFrameObserver(NotifyFrameObserver aNotifyFrameObserver);

	public abstract void setSelected(boolean aSelected);

	public abstract void setVisible(boolean aVisible);

	public abstract void refresh();

	public abstract void setActive(boolean aIsActive);

	public abstract void refreshSize();

}