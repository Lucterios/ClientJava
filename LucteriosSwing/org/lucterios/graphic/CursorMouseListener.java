package org.lucterios.graphic;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class CursorMouseListener extends ArrayList<GUIActionListener> implements MouseListener,ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Component mComp;
		
	private Cursor handCursor=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor waitCursor=Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	
	public CursorMouseListener(Component comp,GUIComponent guiComp) {
		super();
		mComp=comp;
	}
	
	private boolean mIsActiveMouse=false;
	public void setActiveMouseAction(boolean isActive) {
		mIsActiveMouse=isActive;		
	}

	public void mouseEntered(MouseEvent e) {
		if (mIsActiveMouse) {
			if (!waitCursor.equals(mComp.getCursor()))
				mComp.setCursor(handCursor);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (mIsActiveMouse) {
			if (handCursor.equals(mComp.getCursor()))
				mComp.setCursor(Cursor.getDefaultCursor());
		}
	}

	public void mouseClicked(MouseEvent e) { 
		if (e.getClickCount()==2) {
			actionPerformed(null);
		}
	}

	public void actionPerformed(ActionEvent e) {
		//mComp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		for(GUIActionListener l:this)
			if (l!=null)
				l.actionPerformed();
	}
	
	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }

}
