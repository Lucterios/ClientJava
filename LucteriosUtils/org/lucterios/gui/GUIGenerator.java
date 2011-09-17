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

package org.lucterios.gui;

import java.io.File;
import java.net.URL;

public interface GUIGenerator {

	public interface FileFilter{
		public boolean accept(File aFile);
		public String getDescription();		
	}	
	
	public int[] getDefaultInsets();
	
	public int[] getScreenSize();
	
	public GUIForm newForm(String aActionId);

	public GUIFrame getFrame();
	
	public GUIDialog newDialog(GUIFrame aOwnerFrame);

	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerFrame);

	public AbstractImage CreateImage(URL url);

	public AbstractImage CreateImage(byte[] dataImage);
	
	public void showErrorDialog(String message, String title);

	public void showMessageDialog(String message, String title);	

	public boolean showConfirmDialog(String message, String title);
	
	public File selectOpenFileDialog(final FileFilter filter,final GUIObject aGUIOwner);

	public File selectSaveFileDialog(final FileFilter filter,final GUIObject aGUIOwner,String aDefaultFileName);
	
	public GUIWindows newWindows();

	public void invokeLater(Runnable runnable);

	public void invokeAndWait(Runnable runnable);

	public void runSubThread(Runnable runnable);
	
	public boolean isEventDispatchThread();

}
