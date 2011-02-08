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

package org.lucterios.Print;

import java.awt.*;

import javax.swing.*;

import org.lucterios.Print.resources.Resources;
import org.lucterios.utils.Tools;
import org.lucterios.graphic.JAdvancePanel;

public class SelectPrintDlg extends JDialog 
{
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MODE_NONE = 0;
	public static final int MODE_PRINT = 1;
	public static final int MODE_PREVIEW = 2;
	public static final int MODE_EXPORT_PDF = 3;
	public static final int MODE_EXPORT_CSV = 4;
	public static Image FontImage=null; 
	
	private javax.swing.ButtonGroup btgrp;
	private javax.swing.JButton btn_Ok;
	private javax.swing.JButton btn_Cancel;
	private javax.swing.JButton btn_select_pdf;
	private javax.swing.JButton btn_select_csv;
	private javax.swing.JPanel pnl_btn;
	private javax.swing.JRadioButton rb_print;
	private javax.swing.JRadioButton rb_preview;
	private javax.swing.JRadioButton rb_export_pdf;
	private javax.swing.JTextField txt_FileName_pdf;
	private javax.swing.JRadioButton rb_export_csv;
	private javax.swing.JTextField txt_FileName_csv;
	private javax.swing.JLabel lbl_message;
	private JFrame m_ownerF=null;
	private JDialog m_ownerD=null;

	private JAdvancePanel m_MainPanel;
	
	private boolean mWithExportText=false;
	
	public SelectPrintDlg(boolean aWithExportText)
	{
		super();
		mWithExportText=aWithExportText;
		setModal(true);
		initPrintDlg();
	}
	
	public SelectPrintDlg(javax.swing.JDialog aOwnerD,boolean aWithExportText)
	{
		super(aOwnerD, true);
		mWithExportText=aWithExportText;
		m_ownerD=aOwnerD;
		initPrintDlg();
	}
	
	public SelectPrintDlg(javax.swing.JFrame aOwnerF,boolean aWithExportText)
	{
		super(aOwnerF, true);
		mWithExportText=aWithExportText;
		m_ownerF=aOwnerF;
		initPrintDlg();
	}

    public static String getDefaultFileName(String aTitle,String aExtFile)
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

    public static java.io.File getSelectedFileName(String aInitFileName,JFrame aOwnerF,JDialog aOwnerD,String aExtFile) 
	{
		java.io.File file_name=new java.io.File(aInitFileName);
		JFileChooser file_dlg;
		file_dlg = new JFileChooser(file_name.getParent());
		file_dlg.setSelectedFile(file_name);
		file_dlg.setFileFilter(new ExtensionFilter(aExtFile));
		int returnVal;
		if (aOwnerD!=null)
			returnVal = file_dlg.showSaveDialog(aOwnerD);
		else
			returnVal = file_dlg.showSaveDialog(aOwnerF);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	java.io.File select=file_dlg.getSelectedFile();
	    	if (select.getName().toLowerCase().endsWith(aExtFile))
	    		return select;
	    	else {
	    		return new java.io.File(select.getAbsolutePath()+aExtFile);
	    	}
	    }
	    return null;
	}
    
	private String mTitle="";
    public void setTitle(String aTitle)
    {
    	super.setTitle("Imprimer '"+aTitle+"'");
    	mTitle=aTitle;
    }
	
	private void initPrintDlg()
	{	
		java.awt.GridBagConstraints gridBagConstraints;

		m_MainPanel=new JAdvancePanel();
		m_MainPanel.setFontImage(FontImage, JAdvancePanel.TEXTURE);	
		getContentPane().setLayout(new java.awt.BorderLayout());
		getContentPane().add(m_MainPanel, java.awt.BorderLayout.CENTER);		

		btgrp = new javax.swing.ButtonGroup();
		lbl_message = new javax.swing.JLabel();
		txt_FileName_pdf = new javax.swing.JTextField();
		txt_FileName_csv = new javax.swing.JTextField();
		pnl_btn = new javax.swing.JPanel();
		btn_Cancel = new javax.swing.JButton();
		btn_Ok = new javax.swing.JButton();
		btn_select_pdf = new javax.swing.JButton();
		btn_select_csv = new javax.swing.JButton();
		rb_print = new javax.swing.JRadioButton();
		rb_preview = new javax.swing.JRadioButton();
		rb_export_pdf = new javax.swing.JRadioButton();
		rb_export_csv = new javax.swing.JRadioButton();
		
		m_MainPanel.setLayout(new java.awt.GridBagLayout());
	
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		lbl_message.setFont(new java.awt.Font("Dialog", 1, 14));
		lbl_message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lbl_message.setText("Séléctionnez le mode d'édition");
		lbl_message.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lbl_message.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		m_MainPanel.add(lbl_message, gridBagConstraints);
	
		txt_FileName_pdf.setEnabled(false);
		txt_FileName_pdf.setMinimumSize(new java.awt.Dimension(250, 19));
		txt_FileName_pdf.setPreferredSize(new java.awt.Dimension(250, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		m_MainPanel.add(txt_FileName_pdf, gridBagConstraints);

		txt_FileName_csv.setEnabled(false);
		txt_FileName_csv.setVisible(mWithExportText);
		txt_FileName_csv.setMinimumSize(new java.awt.Dimension(250, 19));
		txt_FileName_csv.setPreferredSize(new java.awt.Dimension(250, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		m_MainPanel.add(txt_FileName_csv, gridBagConstraints);

		pnl_btn.setLayout(new java.awt.GridBagLayout());
	
		btn_Cancel.setMnemonic('a');
        btn_Cancel.setIcon(new javax.swing.ImageIcon(Resources.class.getResource("cancel.png")));
		btn_Cancel.setText("Annuler");
	        btn_Cancel.addActionListener(new java.awt.event.ActionListener() 
		{
            		public void actionPerformed(java.awt.event.ActionEvent evt) {
                		btn_CancelActionPerformed(evt);
            		}
        	});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		pnl_btn.add(btn_Cancel, gridBagConstraints);
	
		btn_Ok.setMnemonic('o');
		btn_Ok.setText("OK");
        btn_Ok.setIcon(new javax.swing.ImageIcon(Resources.class.getResource("ok.png")));
		btn_Ok.addActionListener(new java.awt.event.ActionListener() 
		{
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_OkActionPerformed(evt);
			}
		});

		javax.swing.JButton[] btns=new javax.swing.JButton[]{btn_Cancel,btn_Ok};
		org.lucterios.graphic.Tools.calculBtnSize(btns);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		pnl_btn.add(btn_Ok, gridBagConstraints);
	
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 3;
		m_MainPanel.add(pnl_btn, gridBagConstraints);
	
		btn_select_pdf.setText("...");
		btn_select_pdf.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			btn_selectActionPerformed(evt);
		}
		});		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		m_MainPanel.add(btn_select_pdf, gridBagConstraints);
	
		btn_select_csv.setText("...");
		btn_select_csv.setVisible(mWithExportText);
		btn_select_csv.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			btn_selectActionPerformed(evt);
		}
		});		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		m_MainPanel.add(btn_select_csv, gridBagConstraints);
		
		btgrp.add(rb_print);
		rb_print.setMnemonic('i');
		rb_print.setSelected(true);
		rb_print.setText("Imprimer");
		rb_print.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		m_MainPanel.add(rb_print, gridBagConstraints);
	
		btgrp.add(rb_preview);
		rb_preview.setMnemonic('p');
		rb_preview.setText("Previsualisation");
		rb_preview.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		m_MainPanel.add(rb_preview, gridBagConstraints);
	
		btgrp.add(rb_export_pdf);
		rb_export_pdf.setMnemonic('e');
		rb_export_pdf.setText("Export PDF");
		rb_export_pdf.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		m_MainPanel.add(rb_export_pdf, gridBagConstraints);				

		btgrp.add(rb_export_csv);
		rb_export_csv.setMnemonic('c');
		rb_export_csv.setText("Export CSV");
		rb_export_csv.setOpaque(false);
		rb_export_csv.setVisible(mWithExportText);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		m_MainPanel.add(rb_export_csv, gridBagConstraints);				
	}
	
	private void btn_selectActionPerformed(java.awt.event.ActionEvent evt) 
	{
		java.io.File file_exp;
		if (evt.getSource() instanceof JButton) {
			JButton btn=(JButton)evt.getSource();
			if (btn==btn_select_pdf) {			
				file_exp=getSelectedFileName(txt_FileName_pdf.getText(),m_ownerF,m_ownerD,ExtensionFilter.EXTENSION_EXPORT_PDF);
				if (file_exp!=null) {
					txt_FileName_pdf.setText(file_exp.getAbsolutePath());		
					rb_export_pdf.setSelected(true);
				}
			}
			if (btn==btn_select_csv) {
   			    file_exp=getSelectedFileName(txt_FileName_csv.getText(),m_ownerF,m_ownerD,ExtensionFilter.EXTENSION_EXPORT_CSV);
				if (file_exp!=null) {
					txt_FileName_csv.setText(file_exp.getAbsolutePath());		
					rb_export_csv.setSelected(true);
				}
			}
		}
	}
	
	private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) 
	{
		if (rb_print.isSelected())
			mChose=MODE_PRINT;
		else if (rb_preview.isSelected())
			mChose=MODE_PREVIEW;
		else if (rb_export_pdf.isSelected())
			mChose=MODE_EXPORT_PDF;
		else if (rb_export_csv.isSelected())
			mChose=MODE_EXPORT_CSV;
		else 			
			mChose=MODE_NONE;
		dispose();
	}
	
	private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) 
	{
		mChose=MODE_NONE;
		dispose();
	}

	public String getExportFile(int aType)
	{
		switch(aType) {
			case  SelectPrintDlg.MODE_EXPORT_PDF:
			{
				return txt_FileName_pdf.getText();
			}
			case  SelectPrintDlg.MODE_EXPORT_CSV:
			{
				return txt_FileName_csv.getText();
			}
			default:
				return "";
		}
	}

	private int mChose=MODE_NONE;
	
	public int getChose()
	{
		txt_FileName_pdf.setText(getDefaultFileName(mTitle,ExtensionFilter.EXTENSION_EXPORT_PDF));
		txt_FileName_csv.setText(getDefaultFileName(mTitle,ExtensionFilter.EXTENSION_EXPORT_CSV));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); 
		setLocation((screen.width-getSize().width)/2,(screen.height-getSize().height)/2);			
		javax.swing.JButton[] btns={btn_Ok,btn_Cancel};
		org.lucterios.graphic.Tools.calculBtnSize(btns);
		setVisible(true);
		return mChose;
	}	

}
