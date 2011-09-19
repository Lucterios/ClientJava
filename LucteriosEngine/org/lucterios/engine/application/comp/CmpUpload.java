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

package org.lucterios.engine.application.comp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.EncodeBase64FromInputStream;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.ZipManager;
import org.lucterios.graphic.FilesFilter;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class CmpUpload extends CmpAbstractEvent {

	private static File CurrentDirectory = null;
	public final static String SUFFIX_FILE_NAME = "_FILENAME";

	private static final long serialVersionUID = 1L;
	private GUIEdit txt_FileName;
	private GUIButton btn_select;

	private String mHelpText;
	
	private boolean m_isCompress = false;
	private boolean m_isHttpFile = false;
	private int m_maxsize = 1048576;

	public CmpUpload() {
		super();
		if (CurrentDirectory == null) {
			String homeDir = System.getProperty("user.home");
			CurrentDirectory = new java.io.File(homeDir);
			if (new java.io.File(homeDir + "/Desktop").exists())
				CurrentDirectory = new java.io.File(homeDir + "/Desktop");
			if (new java.io.File(homeDir + "/Bureau").exists())
				CurrentDirectory = new java.io.File(homeDir + "/Bureau");
		}
	}

	public static Object getFileContentBase64(File aFile, boolean aIsCompress,
			boolean aIsHttpFile, int aMaxSize) throws LucteriosException {
		String content = "";
		try {
			File tmp_file;
			if (aIsCompress) {
				tmp_file = File.createTempFile("tmp", ".zip", new File(
						Singletons.TEMP_DIR));
				tmp_file.delete();
				ZipManager zip_manager = new ZipManager(tmp_file);
				zip_manager.compressSimpleFile(aFile);
			} else
				tmp_file = aFile;
			if (tmp_file.length() > aMaxSize) {
				float size = aMaxSize / (1024 * 1024);
				throw new LucteriosException(
						"Téléchargement impossible<br/>Le fichier doit faire moins de "
								+ size + "Mo ", false);
			}
			if (aIsHttpFile)
				return tmp_file;
			InputStream file_stream = new java.io.FileInputStream(tmp_file);
			EncodeBase64FromInputStream encoder = new EncodeBase64FromInputStream(
					file_stream);
			content = aFile.getName() + ";" + encoder.encodeString();
		} catch (IOException ioe) {
		}
		return content;
	}

	public MapContext getRequete(String aActionIdent) throws LucteriosException {
		MapContext tree_map = new MapContext();
		;
		if (txt_FileName.getTextString().length() > 0) {
			File file = new File(txt_FileName.getTextString());
			tree_map.put(getName(), getFileContentBase64(file, m_isCompress,
					m_isHttpFile, m_maxsize));
			if (m_isCompress)
				tree_map.put(getName() + SUFFIX_FILE_NAME, file.getName());
		}
		return tree_map;
	}

	public boolean isEmpty() {
		File file = new File(txt_FileName.getTextString());
		return mNeeded && !file.exists();
	}

	protected void initComponent() {
		GUIParam editParam=new GUIParam(0,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_HORIZONTAL);
		editParam.setPrefSizeX(100);
		editParam.setPrefSizeY(20);
		txt_FileName = mPanel.createEdit(editParam);
		txt_FileName.setEnabled(false);

		btn_select = mPanel.createButton(new GUIParam(1,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
		btn_select.setTextString("...");
		btn_select.addActionListener(this);
	}

	public void actionPerformed() {
		java.io.File file_exp = Singletons.getWindowGenerator().selectOpenFileDialog(new FilesFilter(filters, mHelpText),this.getObsCustom().getGUIObject());
		if (file_exp!=null) {
			CurrentDirectory = file_exp.getParentFile();
			txt_FileName.setTextString(file_exp.getAbsolutePath());
		}
	}

	String[] filters = null;

	protected void refreshComponent() {
		super.refreshComponent();
		m_isCompress = (getXmlItem().getAttributeInt("Compress", 0) != 0);
		m_isHttpFile = (getXmlItem().getAttributeInt("HttpFile", 0) != 0);
		m_maxsize = getXmlItem().getAttributeInt("maxsize", 1048576);
		mHelpText=getXmlItem().getText().trim();
		txt_FileName.setToolTipText(mHelpText);
		SimpleParsing[] filer_list = getXmlItem().getSubTag("FILTER");
		filters = new String[filer_list.length];
		for (int index = 0; index < filer_list.length; index++)
			filters[index] = filer_list[index].getText();
	}

	protected boolean hasChanged() {
		return false;
	}
}
