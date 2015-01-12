package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

public class MockMenu extends ArrayList<MockMenu> implements GUIMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean mIsNode;
	public MockMenu(boolean isNode) {
		mIsNode=isNode;
	}
	
	public boolean isNode() {
		return mIsNode;
	}
	
	public boolean isPoint(){
		return !mIsNode;
	}
	
	public GUIMenu addMenu(boolean isNode) {
		MockMenu menu=null;
		if (isNode()) {
			menu=new MockMenu(isNode);
			add(menu);
			return menu;
		}
		else
			return null;
	}

	private String mDescription="";
	public String getDescription() {
		return mDescription;
	}

	public GUIMenu getMenu(int index) {
		if (getMenuCount()>index) {
			return get(index);
		}
		else
			return null;
	}

	public int getMenuCount() {
		if (isNode()) 
			return size();
		else
			return 0;
	}

	private GUIAction mAction=null;
	public void setAction(final GUIAction action) {
		mAction=action;
	}

	public void setDescription(String description) {
		mDescription=description;
	}

	public void addSeparator() {
		if (isNode()) {
			add(new MockMenu(false));
		}		
	}

	private String mName="";
	private int mTag;
	private String mText;
	private char mMnemonic;
	public String getName() {
		return mName;
	}

	public void setMenuImage(AbstractImage image,boolean showIcon) {
	}
	
	
	public String getAcceleratorText() {
		return "";
	}

	public GUIAction getAction() {
		return mAction;
	}

	public GUIActionListener getActionListener() {
		return mAction;
	}

	public AbstractImage getMenuImage() {
		return AbstractImage.Null;
	}

	public String getText() {
		return mText;
	}

	public void removeMenu(int index) {
		if (isNode()) {
			remove(index);
		}		
	}

	public char getMnemonic(){
		return mMnemonic;
	}
	
	public void setMnemonic(char c) {
		mMnemonic=c;
	}

	public void setName(String name) {
		mName=name;		
	}

	public void setText(String text) {
		mText=text;
	}

	public int getTag() {
		return mTag;
	}

	public void setTag(int tag) {
		mTag=tag;
	}

	public void removeAll() {
		if (isNode()) {
			clear();
		}		
	}

	public void setActionListener(GUIActionListener actionListener) {
	}

	public void setEnabled(boolean b) {
	}

	public void setVisible(boolean isVisible) {
	}
	
}
