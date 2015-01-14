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
import org.lucterios.gui.test.TestImage;

public class MockForm implements GUIForm {

	private NotifyFrameList mNotifyFrameList = null;
	private NotifyFrameChange mNotifyFrameChange = null;
	private NotifyFrameObserver mNotifyFrameObserver = null;

	private MockContainer mContainer;
	private GUIGenerator mGenerator;
	
	private String mFrameId;	
	public MockForm(String aFrameID,GUIGenerator generator) {
		super();
		mFrameId=aFrameID;
		mGenerator=generator;
		mContainer=new MockContainer(ContainerType.CT_NORMAL,null);
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
	public void setSelected(boolean aSelected) { }

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
			isCreate=true;
		}
		if (mNotifyFrameObserver != null)
			mNotifyFrameObserver.eventForEnabled(true);
	}

	private TestImage mImage=null;
	public void setImage(AbstractImage image){
		if (TestImage.class.isInstance(image))
			mImage=(TestImage)image;
	}
	
	public AbstractImage getImage() {
		return mImage;
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

	private boolean mIsActive=true;
	public void setActive(boolean aIsActive) {
		mIsActive=aIsActive;
		mContainer.setActive(aIsActive);
	}
	
	public boolean isActive() {
		return mIsActive;
	}
	
	public void refreshSize() {	}

	public GUIContainer getGUIContainer(){
		return mContainer;
	}
	
	public GUIDialog createDialog(){
		return new MockDialog(this,getGenerator());
	}

	private GUIButton mDefaultBtn=null; 
	public void setDefaultButton(GUIButton defaultBtn) {
		mDefaultBtn=defaultBtn;
	}

	public GUIButton getDefaultButton() {
		return mDefaultBtn;
	}

	public String getName() {
		return mFrameId;
	}

	private String mTitle="";
	public void setTextTitle(String title) {
		mTitle=title;
	}
	
	public String getTextTitle() {
		return mTitle;
	}


	public void requestFocus() { }

	public void setLocation(int x, int y) { }

	public void setSize(int width, int height) { }

	public void toFront() { }

	public void dispose() { }

}
