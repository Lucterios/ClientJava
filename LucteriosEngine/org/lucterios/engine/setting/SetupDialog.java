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

package org.lucterios.engine.setting;

import java.io.IOException;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.IDialog;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIContainer.FillMode;
import org.lucterios.gui.GUIContainer.ReSizeMode;
import org.lucterios.gui.IDialog.DialogVisitor;


public class SetupDialog implements DialogVisitor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUILabel lbl_Title;
	private GUIEdit txt_Title;

	private GUIContainer pnl_main;
	private GUIContainer pnl_Tab;
	private ConfigurationPanel conf_pnl;
	private AssociationPanel asso_pnl;
	private GUIContainer pnl_btn;
	private GUIButton btn_import;
	private GUIButton btn_Ok;
	private GUIButton btn_Cancel;

	private IDialog mDialog;

	public SetupDialog() {
		super();
	}
	
	public void execute(IDialog dialog) {
		mDialog=dialog;
		Init();
		InitSubPanel();
		Setup();
		mDialog.pack();
		int[] screen = Singletons.mDesktop.getScreenSize();
		mDialog.setLocation((screen[0] - mDialog.getSizeX()) / 2, (screen[1] - mDialog.getSizeY()) / 4);
		GUIButton[] btns = { btn_Ok, btn_Cancel, btn_import };
		mDialog.getContainer().calculBtnSize(btns);
		mDialog.setDefaultButton(btn_Ok);
	}

	public LucteriosConfiguration.Server newServer(String aServerName,
			String aHostName, int aHostPort, String aDirectory,
			int aConnectionMode) {
		return Singletons.Configuration.newServer(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode);
	}

	public void Init() {
		pnl_main = mDialog.getContainer().createContainer(ContainerType.CT_NORMAL, 
				0, 0, 4, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		mDialog.setTitle("Configuration");

		lbl_Title = pnl_main.createLabel(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
		lbl_Title.setTextString("Titre de l'application");
		// TODO Font des label et position
		/*lbl_Title.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		lbl_Title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_Title.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

		txt_Title = pnl_main.createEdit(1, 0, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);
		txt_Title.setEnabled(true);
		txt_Title.setTextString("Lucterios");

		pnl_btn = mDialog.getContainer().createContainer(ContainerType.CT_NORMAL,
				1, 4, 4, 1, ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);

		btn_Ok = pnl_btn.createButton(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
		btn_Ok.setMnemonic('o');
		btn_Ok.setTextString("OK");
		btn_Ok.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_OkActionPerformed();
			}
		});
		btn_Ok.setImage(Singletons.mDesktop.CreateImage(Resources.class.getResource("ok.png")));

		btn_Cancel = pnl_btn.createButton(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
		btn_Cancel.setMnemonic('n');
		btn_Cancel.setTextString("Annuler");
		btn_Cancel.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_CancelActionPerformed();
			}
		});
		btn_Cancel.setImage(Singletons.mDesktop.CreateImage(Resources.class
				.getResource("cancel.png")));

		btn_import = pnl_btn.createButton(2, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
		btn_import.setMnemonic('o');
		btn_import.setTextString("Import");
		btn_import.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_importActionPerformed();
			}
		});
		btn_import.setImage(Singletons.mDesktop.CreateImage(Resources.class
				.getResource("import.png")));
	}

	public void InitSubPanel() {
		pnl_Tab = pnl_main.createContainer(ContainerType.CT_TAB, 0, 1, 2, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		// pnl_Tab.setPreferredSize(new Dimension(600, 200));
		conf_pnl = new ConfigurationPanel(mDialog);
		conf_pnl.fillContainer(pnl_Tab.addTab(ContainerType.CT_NORMAL, "Connexions"));
		asso_pnl = new AssociationPanel(mDialog);
		asso_pnl.fillContainer(pnl_Tab.addTab(ContainerType.CT_NORMAL, "Extensions de fichiers"));
	}

	private void Setup() {
		txt_Title.setTextString(Singletons.Configuration.TitreDefault);
	}

	private void btn_importActionPerformed() {
		// TODO Selecteur de fichier
		/*
		JFileChooser file_dlg;
		file_dlg = new JFileChooser();
		file_dlg.setFileFilter(new FileFilter() {
			public boolean accept(File aFile) {
				return aFile.isDirectory()
						|| aFile.getName().equalsIgnoreCase(
								LucteriosConfiguration.CONF_FILE_NAME);
			}

			public String getDescription() {
				return "Fichier de configuration";
			}

		});
		if (file_dlg.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				java.io.File file_exp = file_dlg.getSelectedFile();
				LucteriosConfiguration conf_import = new LucteriosConfiguration(new File("."));
				conf_import.read(file_exp);
				for (int conf_idx = 0; conf_idx < conf_import.ServerCount(); conf_idx++)
					Singletons.Configuration.AddServer(conf_import.GetServer(conf_idx));
				conf_pnl.refreshGUI(Singletons.Configuration.ServerCount() - 1);
			} catch (IOException e) {
				ExceptionDlg.throwException(e);
			}
		}
		*/
	}

	private void btn_CancelActionPerformed() {
		try {
			Singletons.Configuration.read();
		} catch (IOException e) {
			Singletons.mDesktop.throwException(e);
		}
		mDialog.dispose();
	}

	private void btn_OkActionPerformed() {
		try {
			Singletons.Configuration.TitreDefault = txt_Title.getTextString().trim();
			Singletons.Configuration.ProxyAdress = conf_pnl.getProxyAddr();
			Singletons.Configuration.ProxyPort = conf_pnl.getProxyPort();
			Singletons.Configuration.write();
			asso_pnl.Save();
			mDialog.dispose();
			Singletons.loadSetting(Singletons.Configuration.getStoragePath());
		} catch (java.io.IOException ioe) {
			Singletons.mDesktop.throwException(ioe);
		}
	}

	public static void run(IDialog aDialog) {
		SetupDialog dlg = new SetupDialog();
		aDialog.setDialogVisitor(dlg);
		aDialog.setVisible(true);
	}

}
