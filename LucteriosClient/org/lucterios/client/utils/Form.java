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

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.lucterios.client.application.Menu.FrameControle;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.graphic.ExceptionDlg;

public class Form extends JFrame {
	public interface NotifyFrameList {
		public void selectFrame(Form aFrame);

		public void removeFrame(Form aFrame);

		public void assignShortCut(Container aComp);
	}

	public interface NotifyFrameChange {
		public void Change();
	}

	public interface NotifyFrameObserver {
		public void close(boolean aMustRefreshParent);

		public void refresh() throws LucteriosException;

		public void eventForEnabled(boolean aBefore);
	}

	private static final long serialVersionUID = 1L;

	private NotifyFrameList mNotifyFrameList = null;
	private NotifyFrameChange mNotifyFrameChange = null;
	private NotifyFrameObserver mNotifyFrameObserver = null;

	public FrameControle mFrameControle;

	public Form(String aFrameID) {
		super();
		setName(aFrameID);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				activate();
			}

			public void windowClosed(WindowEvent e) {
				Close();
				Change();
				System.gc();
			}
		});
	}

	public void activate() {
		if (mNotifyFrameList != null)
			mNotifyFrameList.selectFrame(this);
	}

	public void Close() {
		if (mNotifyFrameObserver != null)
			mNotifyFrameObserver.close(true);
		if (mNotifyFrameList != null)
			mNotifyFrameList.removeFrame(this);
	}

	public void Change() {
		if (mNotifyFrameChange != null)
			mNotifyFrameChange.Change();
	}

	public void setNotifyFrameList(NotifyFrameList aNotifyFrameList) {
		mNotifyFrameList = aNotifyFrameList;
	}

	public void setNotifyFrameChange(NotifyFrameChange aNotifyFrameChange) {
		mNotifyFrameChange = aNotifyFrameChange;
	}

	public void setNotifyFrameObserver(NotifyFrameObserver aNotifyFrameObserver) {
		mNotifyFrameObserver = aNotifyFrameObserver;
	}

	public void setSelected(boolean aSelected) {
		if (aSelected)
			toFront();
		else
			toBack();
	}

	public void setVisible(boolean aVisible) {
		if (mNotifyFrameObserver != null)
			mNotifyFrameObserver.eventForEnabled(true);
		try {
			if (aVisible && (mNotifyFrameList != null))
				mNotifyFrameList.assignShortCut(getContentPane());
			super.setVisible(aVisible);
		} finally {
			if (mNotifyFrameObserver != null)
				mNotifyFrameObserver.eventForEnabled(false);
		}
	}

	public void refresh() {
		try {
			if (mNotifyFrameObserver != null)
				mNotifyFrameObserver.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

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

}
