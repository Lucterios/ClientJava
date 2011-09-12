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

import java.io.File;
import java.net.URL;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIObject;
import org.lucterios.gui.GUIWindows;
import org.lucterios.gui.test.TestImage;
import org.lucterios.mock.MockDialog;
import org.lucterios.mock.MockForm;

public class MockGenerator implements GUIGenerator {

	public MockGenerator() {
		super();
	}

	public int[] getDefaultInsets() {
		int[] result=new int[]{0,0,0,0};
		return result;
	}
	
	public int[] getScreenSize(){
		int[] result=new int[]{0,0};
		return result;
	}
	
	public GUIForm newForm(String aActionId) {
		return getFrame().getFormList().create(aActionId);
	}

	public GUIWindows newWindows() {
		return new MockWindows(this);
	}
	
	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerForm) {
		MockDialog new_dialog;
		if (aOwnerDialog != null)
			new_dialog = new MockDialog((MockDialog) aOwnerDialog,this);
		else if (aOwnerForm != null)
			new_dialog = new MockDialog((MockForm) aOwnerForm,this);
		else
			new_dialog = new MockDialog((MockFrame)getFrame(),this);
		return new_dialog;
	}

	public GUIFrame mFrame=null;
	public GUIFrame getFrame() {
		if (mFrame==null)
			mFrame=new MockFrame(this);
		return mFrame;
	}	

	public GUIDialog newDialog(GUIFrame aOwnerFrame) {
		return new MockDialog((MockFrame) aOwnerFrame,this);
	}

	public AbstractImage CreateImage(URL url) {
		AbstractImage new_image=new TestImage();
		new_image.load(url);
		return new_image;
	}

	public AbstractImage CreateImage(byte[] dataImage) {
		AbstractImage new_image=new TestImage(dataImage);
		return new_image;
	}
	
	public void showErrorDialog(String message, String title) {
		System.out.format("--- ErrorDialog [%s]:%s ---", title, message);		
	}

	public void showMessageDialog(String message, String title) {
		System.out.format("--- MessageDialog [%s]:%s ---", title, message);		
	}
	
	public boolean showConfirmDialog(String message, String title) {
		System.out.format("--- ConfirmDialog [%s]:%s ---", title, message);		
		return true;
	}
	
	public File selectOpenFileDialog(final FileFilter filter,final GUIObject aGUIOwner) {
		File result=new File(".");
		return result;
	}

	public File selectSaveFileDialog(final FileFilter filter,final GUIObject aGUIOwner,
			String aDefaultFileName) {
		File result=new File(".");
		return result;
	}
	
	public void invokeLater(Runnable runnable){
		runnable.run();
	}

	public void invokeAndWait(Runnable runnable){
		runnable.run();
	}
	
	public boolean isEventDispatchThread() {
		return true;
	}
	
}
