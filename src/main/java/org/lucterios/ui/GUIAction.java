package org.lucterios.ui;

import org.lucterios.gui.AbstractImage;

public interface GUIAction extends GUIActionListener {

	public abstract String getTitle();

	public abstract char getMnemonic();

	public abstract AbstractImage getIcon();

	public abstract String getKeyStroke();

	public abstract void actionPerformed();

}