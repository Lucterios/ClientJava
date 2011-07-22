package org.lucterios.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JWindow;
import javax.swing.KeyStroke;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIWindows;
import org.lucterios.gui.GUIContainer.ContainerType;

public class SWindows extends JWindow implements GUIWindows {

	private static final long serialVersionUID = 1L;

	GUIGenerator mGenerator;

	private SContainer mContainer;
	
	public SWindows(GUIGenerator generator) {
		super();
		mGenerator=generator;
		initial();
	}
	
	private void initial() {
		mContainer=new SContainer(ContainerType.CT_NORMAL);
		getContentPane().add(mContainer);
		javax.swing.SwingUtilities.updateComponentTreeUI(this);
	}	

	public GUIContainer getContainer() {
		return mContainer;
	}

	public GUIGenerator getGenerator() {
		return mGenerator;
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
				this.pack();
				this.initialPosition();
				isCreate=true;
			}
			AbstractAction refresh_action = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent actionEvent) {
					refresh();
				}
			};
			SFormList.addShortCut(getContentPane(), "refresh", KeyStroke
					.getKeyStroke("F5"), refresh_action);
		}
		super.setVisible(aVisible);
	}
	
	public void initialPosition() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
	}

	public void refreshSize() {
		java.awt.Dimension size = getSize();
		java.awt.Dimension pref_size = getPreferredSize();
		setPreferredSize(size);
		pack();
		setPreferredSize(pref_size);
		setSize(size);
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));
	}

	public void setWaitingCursor() {
		setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
	}

	public void refresh() {
		
	}

	public void setActive(boolean aIsActive) {
		
	}

}
