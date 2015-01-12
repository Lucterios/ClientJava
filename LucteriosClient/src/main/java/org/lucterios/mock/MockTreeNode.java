package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.GUITreeNode;

public class MockTreeNode extends ArrayList<MockTreeNode> implements GUITreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Object mUserObject;
	public MockTreeNode(Object userObject) {
		mUserObject=userObject;
	}
	
	public GUITreeNode newNode(Object userObject){
		MockTreeNode node=new MockTreeNode(userObject);
		add(node);
		return node;
	}
	
	public GUITreeNode getNode(int index){
		return (GUITreeNode)get(index);
	}

	private int mIconIndex=-1;
	public int getIconIndex(){
		return mIconIndex;
	}

	public void setIconIndex(int index){
		mIconIndex=index;
	}

	public Object getUserObject() {
		return mUserObject;
	}

}
