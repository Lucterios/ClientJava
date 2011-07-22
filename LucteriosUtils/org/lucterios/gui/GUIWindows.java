package org.lucterios.gui;

public interface GUIWindows extends GUIObject {

	public interface WindowVisitor {
		public void execute(GUIWindows windows);
	}
	
	public GUIGenerator getGenerator();
	
	public void setWindowVisitor(WindowVisitor windowVisitor);
	
	public abstract String getName();

	public abstract void setLocation(int x, int y);

	public abstract void setSize(int width, int height);

	public abstract void requestFocus();

	public abstract void toFront();
	
	public abstract void refreshSize();

	public void setWaitingCursor();

	public void setBackgroundColor(int frameColor);

	public GUIContainer getContainer();

}