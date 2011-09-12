package org.lucterios.mock;

import java.net.URL;
import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUITree;
import org.lucterios.gui.GUITreeNode;
import org.lucterios.ui.GUIActionListener;

public class MockTree extends MockComponent implements GUITree {

	private static final long serialVersionUID = 1L;

	public MockTree(GUIComponent aOwner){
		super(aOwner);
	}

	private ArrayList<MockMenu> mMenuList=new ArrayList<MockMenu>();
	public void clearPopupMenu() {
		mMenuList.clear();;
	}
	
	public void setMenuOpenningListenner(final GUIActionListener actionListener) {
	}
	
	public GUIMenu newPopupMenu(boolean isNode) {
		MockMenu menu=new MockMenu(isNode);
		mMenuList.add(menu);
		return menu;
	}
	
	public void setImagePaths(URL[] Urls){
	}

	private MockTreeNode mRoot=null;
	public GUITreeNode newRootNode(Object obj){
		mRoot=new MockTreeNode(obj);
		return mRoot;
	}
	
	public Object getLastSelectedObject() {
		Object result=null;
		return result;
	}

	public GUITreeNode getRoot(){
		return mRoot;
	}
	
	public void setSelectNodeAndExpand(GUITreeNode current){
	}
	
}
