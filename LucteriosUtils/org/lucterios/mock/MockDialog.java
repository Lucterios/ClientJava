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

package org.lucterios.mock;

import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameObserver;

public class MockDialog extends MockForm implements GUIDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NotifyFrameObserver mNotifyFrameClose = null;
	private DialogVisitor mDialogVisitor=null;
	private boolean isCreate=false;
	private double mPosition=0.5; 

	
	public MockDialog(GUIGenerator generator) {
		super("TestDialog",generator);
		initialPosition();
	}

	public MockDialog(MockDialog aDialog,GUIGenerator generator) {
		super("TestDialog",generator);
	}

	public MockDialog(MockForm aForm,GUIGenerator generator) {
		super("TestDialog",generator);
	}

	public MockDialog(MockFrame aFrame,GUIGenerator generator) {
		super("TestDialog",generator);
	}

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
				isCreate=true;
			}
		}
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

	public GUIDialog createDialog(){
		return new MockDialog(this,getGenerator());
	}

	public int getSizeX() {
		return 0;
	}

	public int getSizeY() {
		return 0;
	}

	public void setPosition(double position) {
		mPosition=position;
	}

	public double getPosition() {
		return mPosition;
	}

	public void initialPosition() {	}

	public void pack() { }

	public void setResizable(boolean isResizable) { }

}
