package org.lucterios.client.utils;

public interface IDialog {

	public abstract void setTitle(String title);

	public abstract void setLocation(int x, int y);

	public abstract void setSize(int width, int height);

	public abstract void setResizable(boolean isResizable);
	
	public abstract void dispose();
	
	public abstract void setVisible(boolean aVisible);

	public abstract void close();

	public abstract void refresh();

	public abstract void setNotifyFrameClose(
			NotifyFrameObserver aNotifyFrameClose);

	public abstract void setActive(boolean aIsActive);

	public abstract void requestFocus();

	public abstract void toFront();

	public abstract void refreshSize();

}