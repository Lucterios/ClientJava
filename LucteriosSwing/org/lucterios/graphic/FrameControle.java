package org.lucterios.graphic;

import javax.swing.JMenuBar;

public interface FrameControle {
	public void setActive(boolean aIsActive);

	public JMenuBar getJMenuBar();

	public void refreshSize();
}

