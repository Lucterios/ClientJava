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

package org.lucterios.client.application.observer;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.lucterios.Print.ExtensionFilter;
import org.lucterios.Print.SelectPrintDlg;
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

	String xml_content = null;

	String title = "";

	int type = 0;

	int mode = SelectPrintDlg.MODE_NONE;

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
			title = fo_elements.getAttribut("title");
			type = fo_elements.getAttributInt("type", 0);
			mode = fo_elements.getAttributInt("mode",
					SelectPrintDlg.MODE_PREVIEW);
			withTextExport = (fo_elements.getAttributInt("withTextExport", 0) != 0);
			xml_content = fo_elements.getText();
		}
	}

	protected Map<String, Object> getRequete(String aActionIdent) {
		return new TreeMap<String, Object>();
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		SelectPrintDlg.FontImage = Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg"));

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

	private void openOrSavePrintReport(GUIDialog owner_dialog, GUIForm owner_frame)
			throws LucteriosException {
		try {
			InputStream print_stream = new DecodeBase64ToInputStream(
					xml_content);
			switch (mode) {
			case SelectPrintDlg.MODE_EXPORT_PDF: {
				File pdf_file = SelectPrintDlg.getSelectedFileName(
						SelectPrintDlg.getDefaultFileName(title,
								ExtensionFilter.EXTENSION_EXPORT_PDF),
						(JFrame) owner_frame, (JDialog) owner_dialog,
						ExtensionFilter.EXTENSION_EXPORT_PDF);
				saveFile(pdf_file, print_stream);
				break;
			}
			case SelectPrintDlg.MODE_EXPORT_CSV: {
				File csv_file = SelectPrintDlg.getSelectedFileName(
						SelectPrintDlg.getDefaultFileName(title,
								ExtensionFilter.EXTENSION_EXPORT_CSV),
						(JFrame) owner_frame, (JDialog) owner_dialog,
						ExtensionFilter.EXTENSION_EXPORT_CSV);
				saveFile(csv_file, print_stream);
				break;
			}
			default:
				File pdf_file = new File(Singletons.TEMP_DIR, Tools
						.getFileNameWithoutForgottenChar(title)
						+ ExtensionFilter.EXTENSION_EXPORT_PDF);
				saveFile(pdf_file, print_stream);
				if (mode==SelectPrintDlg.MODE_PRINT)
					DesktopInterface.getInstance().printFilePDF(pdf_file.toURI().toURL().toString());
				else
					DesktopInterface.getInstance().openFile(pdf_file.toURI().toURL().toString());
				break;
			}
		} catch (Exception e) {
			throw new LucteriosException("Echec de l'impression:"
					+ e.getMessage(), e);
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
}
