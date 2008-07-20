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

import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.JAdvancePanel;

public class SelectPrintDlg extends JDialog 
{
 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MODE_NONE = 0;
	public static final int MODE_PRINT = 1;
	public static final int MODE_PREVIEW = 2;
	public static final int MODE_EXPORT = 3;
	public static Image FontImage=null; 
	
	private javax.swing.ButtonGroup btgrp;
	private javax.swing.JButton btn_Ok;
	private javax.swing.JButton btn_Cancel;
	private javax.swing.JButton btn_select;
	private javax.swing.JPanel pnl_btn;
	private javax.swing.JRadioButton rb_print;
	private javax.swing.JRadioButton rb_preview;
	private javax.swing.JRadioButton rb_export;
	private javax.swing.JTextField txt_FileName;
	private javax.swing.JLabel lbl_message;
	private JFrame m_ownerF=null;
	private JDialog m_ownerD=null;

	private JAdvancePanel m_MainPanel;
	
	public SelectPrintDlg()
	{
		super();
		setModal(true);
		initPrintDlg();
	}
	
	public SelectPrintDlg(javax.swing.JDialog aOwnerD)
	{
		super(aOwnerD, true);
		m_ownerD=aOwnerD;
		initPrintDlg();
	}
	
	public SelectPrintDlg(javax.swing.JFrame aOwnerF)
	{
		super(aOwnerF, true);
		m_ownerF=aOwnerF;
		initPrintDlg();
	}

    public static String getDefaultPDFFileName(String aTitle)
    {
		String homeDir = System.getProperty("user.home");
		java.io.File home_dir=new java.io.File(homeDir);
		if (new java.io.File(homeDir+"/Desktop").exists())
			home_dir=new java.io.File(homeDir+"/Desktop");
		if (new java.io.File(homeDir+"/Bureau").exists())
			home_dir=new java.io.File(homeDir+"/Bureau");
		String title=Tools.getFileNameWithoutForgottenChar(aTitle);
		java.io.File file_exp=new java.io.File(home_dir,title+".pdf");
		return file_exp.getAbsolutePath();
    }

    public static java.io.File getSelectedPDFFileName(String aInitFileName,JFrame aOwnerF,JDialog aOwnerD) 
	{
		java.io.File file_name=new java.io.File(aInitFileName);
		JFileChooser file_dlg;
		file_dlg = new JFileChooser(file_name.getParent());
		file_dlg.setSelectedFile(file_name);
		file_dlg.setFileFilter(new PDFFilter());
		int returnVal;
		if (aOwnerD!=null)
			returnVal = file_dlg.showSaveDialog(aOwnerD);
		else
			returnVal = file_dlg.showSaveDialog(aOwnerF);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	return file_dlg.getSelectedFile();
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
		txt_FileName = new javax.swing.JTextField();
		pnl_btn = new javax.swing.JPanel();
		btn_Cancel = new javax.swing.JButton();
		btn_Ok = new javax.swing.JButton();
		btn_select = new javax.swing.JButton();
		rb_print = new javax.swing.JRadioButton();
		rb_preview = new javax.swing.JRadioButton();
		rb_export = new javax.swing.JRadioButton();
	
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
	
		txt_FileName.setEnabled(false);
		txt_FileName.setMinimumSize(new java.awt.Dimension(250, 19));
		txt_FileName.setPreferredSize(new java.awt.Dimension(250, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		m_MainPanel.add(txt_FileName, gridBagConstraints);
	
		pnl_btn.setLayout(new java.awt.GridBagLayout());
	
		btn_Cancel.setMnemonic('a');
        btn_Cancel.setIcon(new javax.swing.ImageIcon(SelectPrintDlg.class.getResource("resources/cancel.png")));
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
        btn_Ok.setIcon(new javax.swing.ImageIcon(SelectPrintDlg.class.getResource("resources/ok.png")));
		btn_Ok.addActionListener(new java.awt.event.ActionListener() 
		{
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_OkActionPerformed(evt);
			}
		});

		javax.swing.JButton[] btns=new javax.swing.JButton[]{btn_Cancel,btn_Ok};
		org.lucterios.utils.graphic.Tools.calculBtnSize(btns);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		pnl_btn.add(btn_Ok, gridBagConstraints);
	
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 3;
		m_MainPanel.add(pnl_btn, gridBagConstraints);
	
		btn_select.setText("...");
		btn_select.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			btn_selectActionPerformed(evt);
		}
		});		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		m_MainPanel.add(btn_select, gridBagConstraints);
	
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
	
		btgrp.add(rb_export);
		rb_export.setMnemonic('e');
		rb_export.setText("Export PDF");
		rb_export.setOpaque(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		m_MainPanel.add(rb_export, gridBagConstraints);				
	}
	
	private void btn_selectActionPerformed(java.awt.event.ActionEvent evt) 
	{
		java.io.File file_exp=getSelectedPDFFileName(txt_FileName.getText(),m_ownerF,m_ownerD);
		if (file_exp!=null) {
			txt_FileName.setText(file_exp.getAbsolutePath());		
			rb_export.setSelected(true);
		}
	}
	
	private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) 
	{
		if (rb_print.isSelected())
			mChose=MODE_PRINT;
		else if (rb_preview.isSelected())
			mChose=MODE_PREVIEW;
		else if (rb_export.isSelected())
			mChose=MODE_EXPORT;
		else 			
			mChose=MODE_NONE;
		dispose();
	}
	
	private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) 
	{
		mChose=MODE_NONE;
		dispose();
	}

	public String getExportFile()
	{
		return txt_FileName.getText();
	}

	private int mChose=MODE_NONE;
	
	public int getChose()
	{
		txt_FileName.setText(getDefaultPDFFileName(mTitle));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);       		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); 
		setLocation((screen.width-getSize().width)/2,(screen.height-getSize().height)/2);			
		javax.swing.JButton[] btns={btn_Ok,btn_Cancel};
		org.lucterios.utils.graphic.Tools.calculBtnSize(btns);
		setVisible(true);
		return mChose;
	}	

}
