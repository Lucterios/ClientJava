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

package org.lucterios.client.utils;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.lucterios.client.application.Menu.FrameControle;
import org.lucterios.engine.utils.IDialog;
import org.lucterios.engine.utils.NotifyFrameObserver;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.graphic.ExceptionDlg;

public class Dialog extends JDialog implements IDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NotifyFrameObserver mNotifyFrameClose = null;

	public FrameControle mFrameControle;

	public Dialog() {
		super();
		initial();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2);
	}

	public Dialog(Dialog aDialog) {
		super(aDialog);
		initial();
	}

	public Dialog(Form aForm) {
		super(aForm);
		initial();
	}

	public Dialog(JFrame aFrame) {
		super(aFrame);
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
	}

	/* (non-Javadoc)
	 * @see org.lucterios.client.utils.IDialog#setVisible(boolean)
	 */
	public void setVisible(boolean aVisible) {
		if (aVisible) {
			AbstractAction refresh_action = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent actionEvent) {
					refresh();
				}
			};
			FormList.addShortCut(getContentPane(), "refresh", KeyStroke
					.getKeyStroke("F5"), refresh_action);
		}
		super.setVisible(aVisible);
	}

	/* (non-Javadoc)
	 * @see org.lucterios.client.utils.IDialog#close()
	 */
	public void close() {
		if (mNotifyFrameClose != null) {
			mNotifyFrameClose.close(true);
			mNotifyFrameClose = null;
		}
        mNotifyFrameClose = null;
        mFrameControle = null;
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.lucterios.client.utils.IDialog#setNotifyFrameClose(org.lucterios.client.utils.NotifyFrameObserver)
	 */
	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose) {
		mNotifyFrameClose = aNotifyFrameClose;
	}

	/* (non-Javadoc)
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
	
}
