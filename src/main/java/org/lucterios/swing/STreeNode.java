package org.lucterios.swing;

import javax.swing.tree.DefaultMutableTreeNode;

import org.lucterios.gui.GUITreeNode;

public class STreeNode extends DefaultMutableTreeNode implements GUITreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public STreeNode(Object userObject) {
		super(userObject);
	}
	
	public GUITreeNode newNode(Object userObject){
		STreeNode node=new STreeNode(userObject);
		add(node);
		return node;
	}
	
	public GUITreeNode getNode(int index){
		return (GUITreeNode)getChildAt(index);
	}

	private int mIconIndex=-1;
	public int getIconIndex(){
		return mIconIndex;
	}

	public void setIconIndex(int index){
		mIconIndex=index;
	}
	

}
