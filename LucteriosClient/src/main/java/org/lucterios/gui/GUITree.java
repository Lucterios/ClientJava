package org.lucterios.gui;

import java.net.URL;

import org.lucterios.ui.GUIActionListener;

public interface GUITree extends GUIComponent {

	public void clearPopupMenu();
	
	public GUIMenu newPopupMenu(boolean isNode);

	public void setMenuOpenningListenner(final GUIActionListener actionListener);

	public Object getLastSelectedObject();
	
	public GUITreeNode newRootNode(Object obj);

	public GUITreeNode getRoot();

	public void setSelectNodeAndExpand(GUITreeNode current);

	public void setImagePaths(URL[] Urls);
}
