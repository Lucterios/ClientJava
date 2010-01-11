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

package org.lucterios.client.setting;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.resources.Resources;
import org.lucterios.client.utils.LucteriosConfiguration;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.JAdvancePanel;
import org.lucterios.utils.graphic.Tools;

public class SetupDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Image mFontImg;

	private javax.swing.JLabel lbl_Title;
	private javax.swing.JTextField txt_Title;

	private JAdvancePanel pnl_main;
	private JTabbedPane pnl_Tab;	
	private ConfigurationPanel conf_pnl;
	private AssociationPanel asso_pnl;
	private JAdvancePanel pnl_btn;
	private javax.swing.JButton btn_import;
	private javax.swing.JButton btn_Ok;
	private javax.swing.JButton btn_Cancel;

	private LucteriosConfiguration mConf;

	public SetupDialog(JFrame aFrame, LucteriosConfiguration aConf) {
		super(aFrame);
		mFontImg = Toolkit.getDefaultToolkit().getImage(Resources.class.getResource("MainFont.jpg"));
		mConf = aConf;
		Init();
		InitSubPanel();
		Setup();
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 4);
	}

	public LucteriosConfiguration.Server newServer(String aServerName,
			String aHostName, int aHostPort, String aDirectory,int aConnectionMode) {
		return mConf.newServer(aServerName, aHostName, aHostPort, aDirectory, aConnectionMode);
	}

	public GridBagConstraints getCnt(int x, int y, int w, int h, int fill,
			double we) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridheight = h;
		gridBagConstraints.gridwidth = w;
		gridBagConstraints.weightx = we;
		gridBagConstraints.weighty = we;
		gridBagConstraints.fill = fill;
		return gridBagConstraints;
	}

	public void Init() {
		pnl_main = new JAdvancePanel();
		pnl_main.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
		pnl_main.setLayout(new java.awt.GridBagLayout());
		setModal(true);
		setTitle("Configuration");

		lbl_Title = new javax.swing.JLabel();
		txt_Title = new javax.swing.JTextField();

		pnl_btn = new JAdvancePanel();
		pnl_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
		btn_import = new javax.swing.JButton();
		btn_Ok = new javax.swing.JButton();
		btn_Cancel = new javax.swing.JButton();

		getContentPane().setLayout(new java.awt.GridBagLayout());
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		lbl_Title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		lbl_Title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_Title.setText("Titre de l'application");
		lbl_Title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		pnl_main.add(lbl_Title, getCnt(0, 0, 1, 1, GridBagConstraints.BOTH, 0));
		txt_Title.setEnabled(true);
		txt_Title.setText("Lucterios");
		pnl_main.add(txt_Title, getCnt(1, 0, 1, 1, GridBagConstraints.BOTH, 1));

		getContentPane().add(pnl_main,getCnt(0, 0, 4, 1, GridBagConstraints.BOTH, 1));

		pnl_btn.setLayout(new java.awt.GridBagLayout());
		
		btn_Ok.setMnemonic('o');
		btn_Ok.setText("OK");
		btn_Ok.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_OkActionPerformed(evt);
			}
		});
		btn_Ok.setIcon(new ImageIcon(Resources.class.getResource("ok.png")));
		pnl_btn.add(btn_Ok, getCnt(0, 0, 1, 1, GridBagConstraints.BOTH, 0));
		
		btn_Cancel.setMnemonic('n');
		btn_Cancel.setText("Annuler");
		btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_CancelActionPerformed(evt);
			}
		});
		btn_Cancel.setIcon(new ImageIcon(Resources.class.getResource("cancel.png")));
		pnl_btn.add(btn_Cancel, getCnt(1, 0, 1, 1, GridBagConstraints.BOTH, 0));

		btn_import.setMnemonic('o');
		btn_import.setText("Import");
		btn_import.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_importActionPerformed(evt);
			}
		});
		btn_import.setIcon(new ImageIcon(Resources.class.getResource("import.png")));
		pnl_btn.add(btn_import, getCnt(2, 0, 1, 1, GridBagConstraints.BOTH, 0));

		getContentPane().add(pnl_btn,getCnt(0, 4, 4, 1, GridBagConstraints.BOTH, 1));
	}

	public void InitSubPanel() {
		pnl_Tab=new JTabbedPane();	
		//pnl_Tab.setPreferredSize(new Dimension(600, 200));
		conf_pnl=new ConfigurationPanel(this,mFontImg,mConf);
		asso_pnl=new AssociationPanel(this,mFontImg);
		pnl_Tab.addTab("Connexions",conf_pnl);
		pnl_Tab.addTab("Extensions de fichiers",asso_pnl);
		pnl_main.add(pnl_Tab, getCnt(0, 1, 2, 1, GridBagConstraints.BOTH, 1));
	}

	private void Setup() {
		txt_Title.setText(mConf.TitreDefault);
		javax.swing.JButton[] btns_2 = { btn_Ok, btn_Cancel };
		Tools.calculBtnSize(btns_2);
		this.getRootPane().setDefaultButton(btn_Ok);
		setResizable(true);
		setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
	}
	

	private void btn_importActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser file_dlg;
		file_dlg = new JFileChooser();
		file_dlg.setFileFilter(new FileFilter(){
			public boolean accept(File aFile) {
				return aFile.isDirectory() || aFile.getName().equalsIgnoreCase(LucteriosConfiguration.CONF_FILE_NAME);
			}
			public String getDescription() {
				return "Fichier de configuration";
			}
			
		});
		if (file_dlg.showOpenDialog(this)== JFileChooser.APPROVE_OPTION) {
	    	try {
		    	java.io.File file_exp = file_dlg.getSelectedFile();
				LucteriosConfiguration conf_import=new LucteriosConfiguration();
				conf_import.read(file_exp);
				for(int conf_idx=0;conf_idx<conf_import.ServerCount();conf_idx++)
					mConf.AddServer(conf_import.GetServer(conf_idx));
				conf_pnl.refreshGUI(mConf.ServerCount()-1);
			} catch (IOException e) {
				ExceptionDlg.throwException(e);
			} 	    	
		}
		
	}

	private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			mConf.read();
		} catch (IOException e) {
			ExceptionDlg.throwException(e);
		}
		dispose();
	}

	private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			mConf.TitreDefault = txt_Title.getText().trim();
			mConf.ProxyAdress = conf_pnl.getProxyAddr();
			mConf.ProxyPort = conf_pnl.getProxyPort();
			mConf.write();

			FileWriter file_conf = new FileWriter(Singletons.LUCTERIOS_CONFIG);
			file_conf.write("["+DesktopTools.ASSOCIATION_SECTION+"]\n");
			for(int idx=0;idx<asso_pnl.getRowCount();idx++) {			
				file_conf.write(asso_pnl.getKey(idx));
				file_conf.write("=");
				file_conf.write((String)asso_pnl.getValueAt(idx,1));
				file_conf.write("\n");
			}
			file_conf.flush();
			file_conf.close();
			
			dispose();
			Singletons.loadSetting();
		} catch (java.io.IOException ioe) {
			ExceptionDlg.throwException(ioe);
		}
	}

	public static void run(JFrame aFrame, LucteriosConfiguration aConf) {
		SetupDialog dlg = new SetupDialog(aFrame, aConf);
		dlg.setVisible(true);
	}
}
