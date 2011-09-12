package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.ui.GUIActionListener;

public class MockFrame extends MockForm implements GUIFrame {

	private static final long serialVersionUID = 1L;

	private MockFormList mFormList;	
	
	private FrameVisitor mFrameVisitor;

	private boolean isCreate;
	
	public MockFrame(GUIGenerator generator) {
		super("FrameTest",generator);
		mFormList=new MockFormList(this.getGenerator());
	}

	public GuiFormList getFormList() {
		return mFormList;
	}
	
	public void addWindowClose(GUIActionListener windowsCloseAction) {	}
	
	public void Maximise() {
	}
	
	private ArrayList<MockMenu> mMenuList=new ArrayList<MockMenu>();
	public GUIMenu addMenu(boolean isNode) {
		MockMenu menu=new MockMenu(isNode);
		mMenuList.add(menu);
		return menu;
	}

	public void removeMenu(int menuIdx) {
		mMenuList.remove(menuIdx);
	}	
	
	public GUIMenu getMenu(int index) {		
		return mMenuList.get(index);
	}

	public int getMenuCount() {
		return mMenuList.size();
	}

	public void moveMenu(int menuIdx){
		MockMenu menu=(MockMenu)getMenu(menuIdx);
		removeMenu(menuIdx);
		mMenuList.add(menu);
	}
	
	public void setFrameVisitor(FrameVisitor frameVisitor) {
		mFrameVisitor=frameVisitor;
	}

	public void setVisible(boolean aVisible) {
		if (!isCreate) {
			if (mFrameVisitor!=null)
				mFrameVisitor.execute(this);
			this.pack();
			isCreate=true;
		}
	}

	public void pack() {	}

}
