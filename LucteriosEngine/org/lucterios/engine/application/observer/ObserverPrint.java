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

package org.lucterios.engine.application.observer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.lucterios.Print.ExtensionFilter;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.DecodeBase64ToInputStream;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;

public class ObserverPrint extends ObserverAbstract {
	public final static int PRINT_TYPE_FOP = 0;

	public final static int PRINT_TYPE_XML = 1;

	public final static int PRINT_TYPE_BIN = 2;

	public static final int MODE_NONE = 0;
	public static final int MODE_PRINT = 1;
	public static final int MODE_PREVIEW = 2;
	public static final int MODE_EXPORT_PDF = 3;
	public static final int MODE_EXPORT_CSV = 4;
	
	String xml_content = null;

	String title = "";

	int type = 0;

	int mode = MODE_NONE;

	boolean withTextExport = false;

	public String getObserverName() {
		return "Core.Print";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		xml_content = null;
		title = "";
		SimpleParsing fo_elements = mContent.getFirstSubTag("PRINT");
		if (fo_elements != null) {
			title = fo_elements.getAttribute("title");
			title+= fo_elements.getCDataOfFirstTag("TITLE");
			type = fo_elements.getAttributeInt("type", 0);
			mode = fo_elements.getAttributeInt("mode",MODE_PREVIEW);
			withTextExport = (fo_elements.getAttributeInt("withTextExport", 0) != 0);
			xml_content = fo_elements.getText();
		}
	}

	protected Map<String, Object> getRequete(String aActionIdent) {
		return new TreeMap<String, Object>();
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) {
		super.show(aTitle);

		GUIDialog owner_dialog = null;
		GUIForm owner_frame = null;
		if (getParent() != null) {
			owner_dialog = getParent().getGUIDialog();
			owner_frame = getParent().getGUIFrame();
		} else {
			owner_dialog = getGUIDialog();
			owner_frame = getGUIFrame();
		}
		if (type == PRINT_TYPE_BIN) {
			openOrSavePrintReport(owner_dialog, owner_frame);
		} else {
			ExceptionDlg.throwException(new LucteriosException(
					"Impression impossible"));
		}
	}

	private void openOrSavePrintReport(GUIDialog owner_dialog, GUIForm owner_form){
		try {
			InputStream print_stream = new DecodeBase64ToInputStream(
					xml_content);
			switch (mode) {
			case MODE_EXPORT_PDF: {
				File pdf_file = getSelectedFileName(getDefaultFileName(title,ExtensionFilter.EXTENSION_EXPORT_PDF),
						owner_form, owner_dialog,
						ExtensionFilter.EXTENSION_EXPORT_PDF);
				if (pdf_file!=null)
					saveFile(pdf_file, print_stream);
				break;
			}
			case MODE_EXPORT_CSV: {
				File csv_file = getSelectedFileName(getDefaultFileName(title,ExtensionFilter.EXTENSION_EXPORT_CSV),
						owner_form, owner_dialog,
						ExtensionFilter.EXTENSION_EXPORT_CSV);
				if (csv_file!=null)
					saveFile(csv_file, print_stream);
				break;
			}
			default:
				File pdf_file = new File(Singletons.TEMP_DIR, Tools
						.getFileNameWithoutForgottenChar(title)
						+ ExtensionFilter.EXTENSION_EXPORT_PDF);
				saveFile(pdf_file, print_stream);
				if (mode==MODE_PRINT)
					DesktopInterface.getInstance().printFilePDF(pdf_file.toURI().toURL().toString());
				else
					DesktopInterface.getInstance().openFile(pdf_file.toURI().toURL().toString());
				break;
			}
		} catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	private void saveFile(File fileName, InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		try {
			byte[] buf = new byte[8192];
			int len;
			while ((len = is.read(buf)) >= 0) {
				fos.write(buf, 0, len);
			}
		} finally {
			fos.close();
		}
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
	
    public String getDefaultFileName(String aTitle,String aExtFile)
    {
		String homeDir = System.getProperty("user.home");
		java.io.File home_dir=new java.io.File(homeDir);
		if (new java.io.File(homeDir+"/Desktop").exists())
			home_dir=new java.io.File(homeDir+"/Desktop");
		if (new java.io.File(homeDir+"/Bureau").exists())
			home_dir=new java.io.File(homeDir+"/Bureau");
		String title=Tools.getFileNameWithoutForgottenChar(aTitle);
		java.io.File file_exp=new java.io.File(home_dir,title+aExtFile);
		return file_exp.getAbsolutePath();
    }

    public java.io.File getSelectedFileName(String aInitFileName,GUIForm aOwnerF,GUIDialog aOwnerD,String aExtFile) 
	{
		java.io.File select=Singletons.getWindowGenerator().selectSaveFileDialog(new ExtensionFilter(aExtFile),(aOwnerD!=null)?aOwnerD:aOwnerF,aInitFileName);
	    if (select!=null) {
	    	if (select.getName().toLowerCase().endsWith(aExtFile))
	    		return select;
	    	else {
	    		return new java.io.File(select.getAbsolutePath()+aExtFile);
	    	}
	    }
	    return null;
	}
	
}
