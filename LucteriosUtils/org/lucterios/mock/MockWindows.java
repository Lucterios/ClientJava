package org.lucterios.mock;

import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIWindows;

public class MockWindows extends MockForm implements GUIWindows {

	private static final long serialVersionUID = 1L;

	public MockWindows(GUIGenerator generator) {
		super("WindowsTest",generator);
	}

	private boolean isCreate=false;

	private WindowVisitor mWindowVisitor; 
	public void setWindowVisitor(WindowVisitor windowVisitor){
		mWindowVisitor=windowVisitor;
	}
		
	public void setVisible(boolean aVisible) {
		if (aVisible) {
			if ((!isCreate) && (mWindowVisitor!=null)) {
				mWindowVisitor.execute(this);
				isCreate=true;
			}
		}
	}
	
	private int mColor=0;
	public void setBackgroundColor(int color) {
		mColor=color;
	}

	public int getBackgroundColor() {
		return mColor;
	}
	
	public void setWaitingCursor() {
	}


}
