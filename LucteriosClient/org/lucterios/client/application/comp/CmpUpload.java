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

package org.lucterios.client.application.comp;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.EncodeBase64FromInputStream;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.ZipManager;
import org.lucterios.utils.graphic.FilesFilter;
import org.lucterios.utils.graphic.ImagePreview;

public class CmpUpload extends CmpAbstractEvent {
	
	private static File CurrentDirectory=null;
	public final static String SUFFIX_FILE_NAME="_FILENAME";
		
	private static final long serialVersionUID = 1L;
	private JPanel pnl_Btn;
	private JLabel lbl_message;
	private JTextField txt_FileName;
	private JButton btn_select;

	private boolean m_isCompress=false; 
	private boolean m_isHttpFile=false; 
	
	public CmpUpload() {
		super();
		if (CurrentDirectory==null) {
			String homeDir = System.getProperty("user.home");
			CurrentDirectory = new java.io.File(homeDir);
			if (new java.io.File(homeDir+"/Desktop").exists())
				CurrentDirectory = new java.io.File(homeDir+"/Desktop");
			if (new java.io.File(homeDir+"/Bureau").exists())
				CurrentDirectory = new java.io.File(homeDir+"/Bureau");
		}
	}

	public static Object getFileContentBase64(File aFile,boolean aIsCompress,boolean aIsHttpFile){
		String content="";
		try {
			File tmp_file;
			if (aIsCompress) {
				tmp_file=File.createTempFile("tmp",".zip",new File(Singletons.TEMP_DIR));
				tmp_file.delete();
				ZipManager zip_manager=new ZipManager(tmp_file);
				zip_manager.compressSimpleFile(aFile);
			}
			else
				tmp_file=aFile;		
			if (aIsHttpFile) 
				return tmp_file; 
			InputStream file_stream = new java.io.FileInputStream(tmp_file);
			EncodeBase64FromInputStream encoder = new EncodeBase64FromInputStream(file_stream);
			content=aFile.getName() + ";" + encoder.encodeString();
		} catch (IOException ioe) {}
		catch (LucteriosException le) {}
		return content;
	}
	
	public Map getRequete(String aActionIdent) {
		TreeMap tree_map = new TreeMap();
		if (txt_FileName.getText().length()>0) {
			File file = new File(txt_FileName.getText());
			tree_map.put(getName(), getFileContentBase64(file,m_isCompress,m_isHttpFile));
			if (m_isCompress) 
				tree_map.put(getName()+SUFFIX_FILE_NAME,file.getName());
		}
		return tree_map;
	}

	public boolean isEmpty() {
		File file = new File(txt_FileName.getText());
		return mNeeded && !file.exists();
	}

	protected void initComponent() {
		java.awt.GridBagConstraints gridBagConstraints;
		pnl_Btn = new JPanel();
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setOpaque(this.isOpaque());
		pnl_Btn.setLayout(new GridBagLayout());
		add(pnl_Btn, java.awt.BorderLayout.CENTER);

		lbl_message = new JLabel();
		lbl_message.setFont(new java.awt.Font("Dialog", 1, 14));
		lbl_message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lbl_message
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		pnl_Btn.add(lbl_message, gridBagConstraints);

		txt_FileName = new JTextField();
		txt_FileName.setEnabled(false);
		txt_FileName.setMinimumSize(new java.awt.Dimension(250, 19));
		txt_FileName.setPreferredSize(new java.awt.Dimension(250, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		pnl_Btn.add(txt_FileName, gridBagConstraints);

		btn_select = new JButton();
		btn_select.setText("...");
		btn_select.addActionListener(this);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0;
		pnl_Btn.add(btn_select, gridBagConstraints);
	}

	public void actionPerformed(ActionEvent event) {
		JFileChooser file_dlg;
		file_dlg = new JFileChooser(CurrentDirectory);
		java.io.File file_name = new java.io.File(txt_FileName.getText());
		file_dlg.setSelectedFile(file_name);
		file_dlg.setAccessory(new ImagePreview(file_dlg));
		file_dlg.setFileFilter(new FilesFilter(filters,"Fichier à télécharger"));
		int returnVal;
		if (this.mObsCustom.getGUIDialog() != null)
			returnVal = file_dlg.showOpenDialog(this.mObsCustom.getGUIDialog());
		else
			returnVal = file_dlg.showOpenDialog(this.mObsCustom.getGUIFrame());
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	java.io.File file_exp = file_dlg.getSelectedFile();
	    	CurrentDirectory=file_exp.getParentFile();
	    	txt_FileName.setText(file_exp.getAbsolutePath());
	    }
	}

	String[] filters = null;

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		m_isCompress=(mXmlItem.getAttributInt("Compress",0)!=0);
		m_isHttpFile=(mXmlItem.getAttributInt("HttpFile",0)!=0);
		lbl_message.setText(mXmlItem.getText().trim());
		SimpleParsing[] filer_list = mXmlItem.getSubTag("FILTER");
		filters = new String[filer_list.length];
		for (int index = 0; index < filer_list.length; index++)
			filters[index] = filer_list[index].getText();
	}

	protected boolean hasChanged() {
		return false;
	}
}
