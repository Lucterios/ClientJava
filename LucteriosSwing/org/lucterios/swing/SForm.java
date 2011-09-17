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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameChange;
import org.lucterios.gui.NotifyFrameList;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.gui.GUIContainer.ContainerType;

public class SForm extends JFrame implements GUIForm {

	private static final long serialVersionUID = 1L;

	private NotifyFrameList mNotifyFrameList = null;
	private NotifyFrameChange mNotifyFrameChange = null;
	private NotifyFrameObserver mNotifyFrameObserver = null;

	private SContainer mContainer;
	private GUIGenerator mGenerator;
	
	public SForm(String aFrameID,GUIGenerator generator) {
		super();
		mGenerator=generator;
		mContainer=new SContainer(ContainerType.CT_NORMAL,null);
		getContentPane().add(mContainer);		
		setName(aFrameID);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				activate();
			}

			public void windowClosed(WindowEvent e) {
				Close();
			}
		});
	}

	public GUIGenerator getGenerator() {
		return mGenerator;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#activate()
	 */
	public void activate() {
		if (mNotifyFrameList != null)
			mNotifyFrameList.selectFrame(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#Close()
	 */
	public void Close() {
		if (mNotifyFrameObserver != null) {
			mNotifyFrameObserver.close(true);
			mNotifyFrameObserver = null;
		}
		if (mNotifyFrameList != null) {
			mNotifyFrameList.removeFrame(this);
			mNotifyFrameList = null;
		}
		Change();
		mNotifyFrameChange = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#Change()
	 */
	public void Change() {
		if (mNotifyFrameChange != null)
			mNotifyFrameChange.Change();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lucterios.client.utils.IForm#setNotifyFrameList(org.lucterios.client
	 * .utils.Form.NotifyFrameList)
	 */
	public void setNotifyFrameList(NotifyFrameList aNotifyFrameList) {
		mNotifyFrameList = aNotifyFrameList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lucterios.client.utils.IForm#setNotifyFrameChange(org.lucterios.client
	 * .utils.Form.NotifyFrameChange)
	 */
	public void setNotifyFrameChange(NotifyFrameChange aNotifyFrameChange) {
		mNotifyFrameChange = aNotifyFrameChange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lucterios.client.utils.IForm#setNotifyFrameObserver(org.lucterios
	 * .client.utils.NotifyFrameObserver)
	 */
	public void setNotifyFrameObserver(NotifyFrameObserver aNotifyFrameObserver) {
		mNotifyFrameObserver = aNotifyFrameObserver;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#setSelected(boolean)
	 */
	public void setSelected(boolean aSelected) {
		if (aSelected)
			toFront();
		else
			toBack();
	}

	private boolean isCreate=false;
	
	private FormVisitor mFormVisitor; 
	public void setFormVisitor(FormVisitor formVisitor){
		mFormVisitor=formVisitor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#setVisible(boolean)
	 */
	public void setVisible(boolean aVisible) {
		if (!isCreate) {
			if (mFormVisitor!=null)
				mFormVisitor.execute(this);
			this.pack();
			this.initialPosition();
			isCreate=true;
		}
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

	public void setImage(AbstractImage image){
		if (SwingImage.class.isInstance(image))
			setIconImage(((ImageIcon)image.getData()).getImage());
	}

	public AbstractImage getImage() {
		return new SwingImage(new ImageIcon(getIconImage()));
	}


	private void initialPosition() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) (8.5*screen.width/10.0),(int) (8.5*screen.height/10.0));
		setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#refresh()
	 */
	public void refresh() {
		try {
			if (mNotifyFrameObserver != null)
				mNotifyFrameObserver.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lucterios.client.utils.IForm#setActive(boolean)
	 */
	public void setActive(boolean aIsActive) {
		Cursor current_cursor;
		if (aIsActive)
			current_cursor = Cursor.getDefaultCursor();
		else
			current_cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		setCursor(current_cursor);
		mContainer.setActive(aIsActive);
		if (mGenerator.getFrame() != null)
			mGenerator.getFrame().setActive(aIsActive);
	}

	public void refreshSize() {
		java.awt.Dimension size = getSize();
		java.awt.Dimension pref_size = getPreferredSize();
		setPreferredSize(size);
		pack();
		setPreferredSize(pref_size);
		setSize(size);
		Change();
		toFront();
	}

	public GUIContainer getContainer(){
		return mContainer;
	}
	
	public GUIDialog createDialog(){
		return new SDialog(this,getGenerator());
	}

	public void setDefaultButton(GUIButton defaultBtn) {
		if (defaultBtn instanceof JButton)
			this.getRootPane().setDefaultButton((JButton)defaultBtn);
	}

	public String getTextTitle() {
		return getTitle();
	}

	public void setTextTitle(String title) {
		setTitle(title);
	}

}
