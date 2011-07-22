/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.swing;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.FrameControle;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.gui.GUIContainer.ContainerType;

public class SDialog extends JDialog implements GUIDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NotifyFrameObserver mNotifyFrameClose = null;
	private DialogVisitor mDialogVisitor=null;
	private SContainer mContainer;	

	public FrameControle mFrameControle;
	
	private GUIGenerator mGenerator;

	public GUIGenerator getGenerator() {
		return mGenerator;
	}
	
	public SDialog(GUIGenerator generator) {
		super();
		mGenerator=generator;
		initial();
		initialPosition();
	}

	public SDialog(SDialog aDialog,GUIGenerator generator) {
		super(aDialog);
		mGenerator=generator;
		initial();
	}

	public SDialog(SForm aForm,GUIGenerator generator) {
		super(aForm);
		mGenerator=generator;
		initial();
	}

	public SDialog(SFrame aFrame,GUIGenerator generator) {
		super(aFrame);
		mGenerator=generator;
		initial();
	}

	private void initial() {
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				close();
			}
		});
		mContainer=new SContainer(ContainerType.CT_NORMAL);
		mContainer.setName("");
		getContentPane().add(mContainer);
		javax.swing.SwingUtilities.updateComponentTreeUI(this);
	}

	private boolean isCreate=false; 
	public void setDialogVisitor(DialogVisitor dialogVisitor){
		mDialogVisitor=dialogVisitor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IDialog#setVisible(boolean)
	 */
	public void setVisible(boolean aVisible) {
		if (aVisible) {
			if ((!isCreate) && (mDialogVisitor!=null)) {
				mDialogVisitor.execute(this);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IDialog#close()
	 */
	public void close() {
		if (mDialogVisitor!=null)
			mDialogVisitor.closing();
		if (mNotifyFrameClose != null) {
			mNotifyFrameClose.close(true);
			mNotifyFrameClose = null;
		}
		mNotifyFrameClose = null;
		mFrameControle = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IDialog#refresh()
	 */
	public void refresh() {
		try {
			if (mNotifyFrameClose != null)
				mNotifyFrameClose.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lucterios.client.utils.IDialog#setNotifyFrameClose(org.lucterios.
	 * client.utils.NotifyFrameObserver)
	 */
	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose) {
		mNotifyFrameClose = aNotifyFrameClose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IDialog#setActive(boolean)
	 */
	public void setActive(boolean aIsActive) {
		Cursor current_cursor;
		if (aIsActive)
			current_cursor = Cursor.getDefaultCursor();
		else
			current_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		setCursor(current_cursor);
		if (mFrameControle != null)
			mFrameControle.setActive(aIsActive);
	}

	public void refreshSize() {
		java.awt.Dimension size = getSize();
		java.awt.Dimension pref_size = getPreferredSize();
		setPreferredSize(size);
		pack();
		setPreferredSize(pref_size);
		setSize(size);
	}

	public GUIContainer getContainer(){
		return mContainer;
	}

	public GUIDialog createDialog(){
		return new SDialog(this,getGenerator());
	}

	public int getSizeX() {
		return getSize().width;
	}

	public int getSizeY() {
		return getSize().height;
	}

	public void setDefaultButton(GUIButton btn) {
		if (btn instanceof JButton)
			this.getRootPane().setDefaultButton((JButton)btn);
	}

	public void initialPosition() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
	}

}
