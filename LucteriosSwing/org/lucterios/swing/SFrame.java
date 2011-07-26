package org.lucterios.swing;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIContainer.ContainerType;

public class SFrame extends JFrame implements GUIFrame {

	private static final long serialVersionUID = 1L;

	private GUIGenerator mGenerator;

	private SContainer mContainer;

	private FrameVisitor mFrameVisitor;

	private boolean isCreate;
	
	public SFrame(GUIGenerator generator) {
		super();
		mGenerator=generator;
		mContainer=new SContainer(ContainerType.CT_NORMAL);
		getContentPane().add(mContainer);		
	}
	
	public GUIGenerator getGenerator() {
		return mGenerator;
	}
	
	private JMenuBar getMenu(){
		JMenuBar menuBar=getJMenuBar();
		if (menuBar==null) {
			menuBar = new JMenuBar();
			menuBar.setName("MainMenu");
			setJMenuBar(menuBar);
		}
		return menuBar;
	}

	public void refresh() {
		
	}

	public void setActive(boolean aIsActive) {
		
	}

	public GUIContainer getContainer() {
		return mContainer;
	}

	public GUIMenu addMenu(boolean isNode) {
		SMenu menu=new SMenu(isNode);
		getMenu().add(menu.getJMenuItem());
		return menu;
	}

	public GUIMenu getMenu(int index) {		
		return new SMenu(getMenu().getMenu(index));
	}

	public int getMenuCount() {
		return getMenu().getMenuCount();
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
		super.setVisible(aVisible);
	}	
}
