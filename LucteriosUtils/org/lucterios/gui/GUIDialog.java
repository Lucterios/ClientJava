package org.lucterios.gui;

public interface GUIDialog extends GUIObject {
	
	public interface DialogVisitor {
		public void execute(GUIDialog dialog);
		public void closing();
	}
	
	public void setDialogVisitor(DialogVisitor dialogVisitor);

	public GUIGenerator getGenerator();
	
	public void setTitle(String title);

	public void setLocation(int x, int y);

	public void setSize(int width, int height);

	public void setResizable(boolean isResizable);
	
	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose);

	public void requestFocus();

	public void toFront();

	public void refreshSize();
	
	public GUIDialog createDialog();
	
	public GUIContainer getContainer();

	public void pack();
	
	public void close();	

	public int getSizeY();

	public int getSizeX();

	public void setDefaultButton(GUIButton btnAdd);

	public void initialPosition();

	public void setPosition(double position);
	
}