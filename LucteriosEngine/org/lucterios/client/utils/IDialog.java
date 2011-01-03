package org.lucterios.client.utils;

public interface IDialog {

	public abstract void setVisible(boolean aVisible);

	public abstract void close();

	public abstract void refresh();

	public abstract void setNotifyFrameClose(
			NotifyFrameObserver aNotifyFrameClose);

	public abstract void setActive(boolean aIsActive);

}