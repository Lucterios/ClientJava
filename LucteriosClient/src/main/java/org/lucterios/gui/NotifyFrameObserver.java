package org.lucterios.gui;

import org.lucterios.utils.LucteriosException;

public interface NotifyFrameObserver {
	public void close(boolean aMustRefreshParent);

	public void refresh() throws LucteriosException;

	public void eventForEnabled(boolean aBefore);
}
