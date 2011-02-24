package org.lucterios.gui;

public interface IDialog {
	
	public interface DialogVisitor {
		public void execute(IDialog dialog);
	}
	
	public void setDialogVisitor(DialogVisitor dialogVisitor);

	public void setTitle(String title);

	public void setLocation(int x, int y);

	public void setSize(int width, int height);

	public void setResizable(boolean isResizable);
	
	public void dispose();
	
	public void setVisible(boolean aVisible);

	public void close();

	public void refresh();

	public void setNotifyFrameClose(
			NotifyFrameObserver aNotifyFrameClose);

	public void setActive(boolean aIsActive);

	public void requestFocus();

	public void toFront();

	public void refreshSize();
	
	public IDialog createDialog();
	
	public GUIContainer getContainer();

	public void pack();

	public int getSizeY();

	public int getSizeX();

	public void setDefaultButton(GUIButton btnAdd);
	
}