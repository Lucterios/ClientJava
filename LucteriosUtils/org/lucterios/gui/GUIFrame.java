package org.lucterios.gui;


public interface GUIFrame extends GUIObject {
	public interface FrameVisitor {
		public void execute(GUIFrame frame);
	}
	
	public GUIGenerator getGenerator();

	public GUIContainer getContainer();
	
	public void setFrameVisitor(FrameVisitor frameVisitor);
	
	public String getName();

	public String getTitle();

	public void setTitle(String title);
	
	public void setLocation(int x, int y);

	public void setSize(int width, int height);
	
	public GUIMenu addMenu(boolean isNode);
	
	public GUIMenu getMenu(int index);
	
	public int getMenuCount();
}
