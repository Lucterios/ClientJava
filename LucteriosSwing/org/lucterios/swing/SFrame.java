package org.lucterios.swing;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.ui.GUIActionListener;

public class SFrame extends JFrame implements GUIFrame {

	private static final long serialVersionUID = 1L;

	private SFormList mFormList;	
	
	private GUIGenerator mGenerator;

	private SContainer mContainer;

	private FrameVisitor mFrameVisitor;

	private boolean isCreate;
	
	public SFrame(GUIGenerator generator) {
		super();
		mGenerator=generator;
		mContainer=new SContainer(ContainerType.CT_NORMAL,null);
		mFormList=new SFormList(this.getGenerator());
		
		getContentPane().add(mContainer);	
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				if (mWindowsCloseAction!=null)
					mWindowsCloseAction.actionPerformed();
			}
		});
	}

	public GuiFormList getFormList() {
		return mFormList;
	}

	
	public GUIGenerator getGenerator() {
		return mGenerator;
	}

	private GUIActionListener mWindowsCloseAction=null;
	public void addWindowClose(GUIActionListener windowsCloseAction) {
		mWindowsCloseAction=windowsCloseAction;
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
		java.awt.Cursor current_cursor;
		if (aIsActive)
			current_cursor = java.awt.Cursor.getDefaultCursor();
		else
			current_cursor = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
		setCursor(current_cursor);
		mContainer.setCursor(current_cursor);
		mContainer.setActive(aIsActive);
	}
	
	public boolean isActive() {
		return java.awt.Cursor.getDefaultCursor().equals(getCursor());
	}
	
	public void Maximise() {
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	public GUIContainer getContainer() {
		return mContainer;
	}

	public GUIMenu addMenu(boolean isNode) {
		SMenu menu=new SMenu(isNode);
		getMenu().add(menu.getJMenuItem());
		return menu;
	}

	public void removeMenu(int menuIdx) {
		JMenuBar menu_bar=getMenu();
		menu_bar.remove(menu_bar.getMenu(menuIdx));
	}	
	
	public GUIMenu getMenu(int index) {		
		return new SMenu(getMenu().getMenu(index));
	}

	public int getMenuCount() {
		return getMenu().getMenuCount();
	}

	public void moveMenu(int menuIdx){
		JMenuBar menu_bar=getMenu();
		menu_bar.add(menu_bar.getMenu(menuIdx));
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
	
	public void setImage(AbstractImage image){
		if (SwingImage.class.isInstance(image))
			setIconImage(((ImageIcon)image.getData()).getImage());
	}
	
	public AbstractImage getImage() {
		if (getIconImage()!=null)
			return new SwingImage(new ImageIcon(getIconImage()));
		else
			return AbstractImage.Null;
	}
	
	public void refreshSize() {
		Dimension dim = getSize();
		pack();
		setSize(dim);
	}

	public String getTextTitle() {
		return getTitle();
	}

	public void setTextTitle(String title) {
		setTitle(title);
	}
	
}
