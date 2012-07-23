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

import java.io.File;
import java.io.IOException;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIGenerator.FileFilter;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;

public class SetupDialog implements DialogVisitor {
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

	private GUIDialog mDialog;

	public SetupDialog() {
		super();
	}
	
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		Init();
		InitSubPanel();
		Setup();
		GUIButton[] btns = { btn_Ok, btn_Cancel, btn_import };
		mDialog.getGUIContainer().calculBtnSize(btns);
		mDialog.setDefaultButton(btn_Ok);
		mDialog.pack();
		mDialog.initialPosition();
	}

	public void closing(){}
	
	public LucteriosConfiguration.Server newServer(String aServerName,
			String aHostName, int aHostPort, String aDirectory,
			int aConnectionMode, boolean aUseProxy) {
		return Singletons.getConfiguration().newServer(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode, aUseProxy);
	}

	public void Init() {
		pnl_main = mDialog.getGUIContainer().createContainer(ContainerType.CT_NORMAL,new GUIParam(0, 0, 4, 1));
		mDialog.setTextTitle("Configuration");

		lbl_Title = pnl_main.createLabel(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		lbl_Title.setTextString("Titre de l'application");
		lbl_Title.setStyle(1);

		txt_Title = pnl_main.createEdit(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
		txt_Title.setEnabled(true);
		txt_Title.setTextString("Lucterios");

		pnl_btn = mDialog.getGUIContainer().createContainer(ContainerType.CT_NORMAL,
				new GUIParam(1, 4, 4, 1, ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));

		btn_Ok = pnl_btn.createButton(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		btn_Ok.setMnemonic('o');
		btn_Ok.setTextString("OK");
		btn_Ok.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_OkActionPerformed();
			}
		});
		btn_Ok.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class.getResource("ok.png")));

		btn_Cancel = pnl_btn.createButton(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		btn_Cancel.setMnemonic('n');
		btn_Cancel.setTextString("Annuler");
		btn_Cancel.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_CancelActionPerformed();
			}
		});
		btn_Cancel.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class
				.getResource("cancel.png")));

		btn_import = pnl_btn.createButton(new GUIParam(2, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		btn_import.setMnemonic('o');
		btn_import.setTextString("Import");
		btn_import.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_importActionPerformed();
			}
		});
		btn_import.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class
				.getResource("import.png")));
	}

	public void InitSubPanel() {
		pnl_Tab = pnl_main.createContainer(ContainerType.CT_TAB, new GUIParam(0, 1, 2, 1));
		conf_pnl = new ConfigurationPanel(mDialog);
		conf_pnl.fillContainer(pnl_Tab.addTab(ContainerType.CT_NORMAL, "Connexions", AbstractImage.Null));
		asso_pnl = new AssociationPanel(mDialog);
		asso_pnl.fillContainer(pnl_Tab.addTab(ContainerType.CT_NORMAL, "Extensions de fichiers", AbstractImage.Null));
	}

	private void Setup() {
		txt_Title.setTextString(Singletons.getConfiguration().TitreDefault);
	}

	private void btn_importActionPerformed() {
		java.io.File file_exp = Singletons.getWindowGenerator().selectOpenFileDialog(new FileFilter() {
			public String getDescription() {
				return "Fichier de configuration";
			}
			
			public boolean accept(File aFile) {
				return aFile.isDirectory()
				|| aFile.getName().equalsIgnoreCase(LucteriosConfiguration.CONF_FILE_NAME);
			}
		}, mDialog);
		if (file_exp!=null) {
			try {
				LucteriosConfiguration conf_import = new LucteriosConfiguration(new File("."));
				conf_import.read(file_exp);
				for (int conf_idx = 0; conf_idx < conf_import.ServerCount(); conf_idx++)
					Singletons.getConfiguration().AddServer(conf_import.GetServer(conf_idx));
				conf_pnl.refreshGUI(Singletons.getConfiguration().ServerCount() - 1);
			} catch (IOException e) {
				Singletons.getDesktop().throwException(e);
			}
		}
	}

	private void btn_CancelActionPerformed() {
		try {
			Singletons.getConfiguration().read();
		} catch (IOException e) {
			Singletons.getDesktop().throwException(e);
		}
		mDialog.dispose();
	}

	private void btn_OkActionPerformed() {
		try {
			Singletons.getConfiguration().TitreDefault = txt_Title.getTextString().trim();
			Singletons.getConfiguration().ProxyAdress = conf_pnl.getProxyAddr();
			Singletons.getConfiguration().ProxyPort = conf_pnl.getProxyPort();
			Singletons.getConfiguration().write();
			asso_pnl.Save();
			mDialog.dispose();
			Singletons.loadSetting(Singletons.getConfiguration().getStoragePath());
		} catch (java.io.IOException ioe) {
			Singletons.getDesktop().throwException(ioe);
		}
	}

	public static void run(GUIDialog aDialog) {
		try{
			SetupDialog dlg = new SetupDialog();
			aDialog.setDialogVisitor(dlg);
			aDialog.setVisible(true);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

}
