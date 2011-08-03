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

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.lucterios.graphic.HtmlLabel;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIObject;
import org.lucterios.gui.GUIWindows;
import org.lucterios.swing.SDialog;
import org.lucterios.swing.SForm;

public class SGenerator implements GUIGenerator {

	public SGenerator() {
		super();
		HtmlLabel.changeFontSize(0.9f);
	}

	public int[] getDefaultInsets() {
		int[] result=new int[]{0,0,0,0};
		try {
			GraphicsDevice[] gdev_list=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			for (int gs_index=0;gs_index<gdev_list.length;gs_index++)
			{
				GraphicsDevice gs=gdev_list[gs_index];
				GraphicsConfiguration[] gconf_list=gs.getConfigurations();
				if (gconf_list.length>0) {
					Insets inset=(Toolkit.getDefaultToolkit().getScreenInsets(gconf_list[0]));
					result=new int[]{inset.top,inset.left,inset.bottom,inset.right};
				}
			}
		}
		catch (HeadlessException h) {
			System.err.println("Error: Headless error (you aren't running a GUI)");
		}
		return result;
	}
	
	public int[] getScreenSize(){
		int[] result=new int[]{0,0};
		Toolkit kit = Toolkit.getDefaultToolkit();
		java.awt.Dimension screen = kit.getScreenSize();
		result[0]=screen.width;
		result[1]=screen.height;
		return result;
	}
	
	public GUIForm newForm(String aActionId) {
		return getFrame().getFormList().create(aActionId);
	}

	public GUIWindows newWindows() {
		return new SWindows(this);
	}
	
	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerFrame) {
		GUIDialog new_dialog;
		if (aOwnerDialog != null)
			new_dialog = new SDialog((SDialog) aOwnerDialog,this);
		else
			new_dialog = new SDialog((SForm) aOwnerFrame,this);
		return new_dialog;
	}

	public GUIFrame mFrame=null;
	public GUIFrame getFrame() {
		if (mFrame==null)
			mFrame=new SFrame(this);
		return mFrame;
	}	

	public GUIDialog newDialog(GUIFrame aOwnerFrame) {
		return new SDialog((SFrame) aOwnerFrame,this);
	}

	public AbstractImage CreateImage(URL url) {
		AbstractImage new_image=new SwingImage();
		new_image.load(url);
		return new_image;
	}

	public void showErrorDialog(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,JOptionPane.ERROR_MESSAGE);		
	}

	public void showMessageDialog(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);		
	}
	
	public boolean showConfirmDialog(String message, String title) {
		return JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	public File selectOpenFileDialog(final FileFilter filter,final GUIObject aGUIOwner) {
		File result=null;
		JFileChooser file_dlg;
		file_dlg = new JFileChooser();
		file_dlg.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File aFile) {
				return filter.accept(aFile);
			}
			public String getDescription() {
				return filter.getDescription();
			}

		});
		if (file_dlg.showOpenDialog((Component)aGUIOwner) == JFileChooser.APPROVE_OPTION)
			result = file_dlg.getSelectedFile();
		return result;
	}

	public void invokeLater(Runnable runnable){
		SwingUtilities.invokeLater(runnable);
	}

	public boolean isEventDispatchThread() {
		return SwingUtilities.isEventDispatchThread();
	}
	
}
