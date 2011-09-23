package org.lucterios.gui;

import org.lucterios.ui.GUIActionListener;

public interface GUIFrame extends GUIObject {

	public interface FrameVisitor {
		public void execute(GUIFrame frame);
		public void menuCreate();
	}
	
	public GUIGenerator getGenerator();

	public GUIContainer getContainer();
	
	public GuiFormList getFormList();
	
	public void setFrameVisitor(FrameVisitor frameVisitor);
	
	public String getName();

	public String getTextTitle();
	
	public AbstractImage getImage();

	public void setTextTitle(String title);

	public void setImage(AbstractImage image);
		
	public void setLocation(int x, int y);

	public void setSize(int width, int height);
	
	public GUIMenu addMenu(boolean isNode);
	
	public GUIMenu getMenu(int index);
	
	public int getMenuCount();

	public void removeMenu(int menuIdx);

	public void moveMenu(int beginExtraMenu);

	public void refreshSize();

	public void pack();

	public void addWindowClose(GUIActionListener guiActionListener);

	public void toFront();

	public void Maximise();
	
	public boolean isActive();
}
