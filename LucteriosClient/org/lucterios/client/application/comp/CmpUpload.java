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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lucterios.utils.EncodeBase64FromInputStream;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class CmpUpload extends CmpAbstractEvent {
	
	private static File CurrentDirectory=null;
	
	class FilesFilter extends javax.swing.filechooser.FileFilter {
		private String[] files = null;

		public FilesFilter(String[] afiles) {
			super();
			files = afiles;
		}

		public boolean accept(File aFile) {
			if (aFile.isDirectory())
				return true;
			String name=aFile.getName();
			if ((files != null) && (files.length > 0)) {
				for (int index = 0; index < files.length; index++)
					if (name.length() > files[index].length()) {
						String ext = name.substring(
								name.length() - files[index].length())
								.toLowerCase();
						if (ext.equalsIgnoreCase(files[index]))
							return true;
					}
				return false;
			} else
				return true;
		}

		public String getDescription() {
			return "Fichier export";
		}
	}

	class ImagePreview extends JComponent implements PropertyChangeListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ImageIcon thumbnail = null;
		File file = null;
		
		public ImagePreview(JFileChooser fc) {
			setPreferredSize(new Dimension(100, 50));
			fc.addPropertyChangeListener(this);
		}

		public void loadImage() {
			if (file == null) {
				thumbnail = null;
				return;
			}

			ImageIcon tmpIcon = new ImageIcon(file.getPath());
			if (tmpIcon != null) {
				if (tmpIcon.getIconWidth() > 90) {
					thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1,Image.SCALE_DEFAULT));
				} else { //no need to miniaturize
					thumbnail = tmpIcon;
				}
			}
		}

		public void propertyChange(PropertyChangeEvent e) {
			boolean update = false;
			String prop = e.getPropertyName();
			//If the directory changed, don't show an image.
			if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
				file = null;
				update = true;

				//If a file became selected, find out which one.
			} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
				file = (File) e.getNewValue();
				update = true;
			}

			//Update the preview accordingly.
			if (update) {
				thumbnail = null;
				if (isShowing()) {
					loadImage();
					repaint();
				}
			}
		}

		protected void paintComponent(Graphics g) {
			if (thumbnail == null) {
				loadImage();
			}
			if (thumbnail != null) {
				int x = getWidth()/2 - thumbnail.getIconWidth()/2;
				int y = getHeight()/2 - thumbnail.getIconHeight()/2;

				if (y < 0) {
					y = 0;
				}

				if (x < 5) {
					x = 5;
				}
				thumbnail.paintIcon(this, g, x, y);
			}
		}
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pnl_Btn;
	private JLabel lbl_message;
	private JTextField txt_FileName;
	private JButton btn_select;

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

	public Map getRequete(String aActionIdent) {
		TreeMap tree_map = new TreeMap();
		try {
			File file = new File(txt_FileName.getText());
			InputStream file_stream = new java.io.FileInputStream(file);
			EncodeBase64FromInputStream encoder = new EncodeBase64FromInputStream(
					file_stream);
			tree_map.put(getName(), file.getName() + ";"
					+ encoder.encodeString());
		} catch (IOException e) {
		}
		return tree_map;
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
		file_dlg.setFileFilter(new FilesFilter(filters));
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
