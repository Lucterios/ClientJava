package org.lucterios.gui;

public interface GUIDialog extends GUIObject {
	
	public interface DialogVisitor {
		public void execute(GUIDialog dialog);
		public void closing();
	}
	
	public void setDialogVisitor(DialogVisitor dialogVisitor);
	
	public GUIDialog createDialog();

	public GUIGenerator getGenerator();
	
	public void setTextTitle(String title);

	public void setLocation(int x, int y);

	public void setSize(int width, int height);

	public void setResizable(boolean isResizable);
	
	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose);

	public void requestFocus();

	public void toFront();

	public void refreshSize();
	
	public GUIContainer getGUIContainer();

	public void pack();
	
	public void close();	

	public int getSizeY();

	public int getSizeX();

	public void setDefaultButton(GUIButton btnAdd);

	public void initialPosition();

	public void setPosition(double position);
	
	public String getTextTitle();
	
}