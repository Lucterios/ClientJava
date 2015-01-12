package org.lucterios.gui;

public interface GUITreeNode {

	public GUITreeNode newNode(Object userObject);

	public GUITreeNode getNode(int index);

	public int getIconIndex();

	public void setIconIndex(int index);
	
	public Object getUserObject();

}
