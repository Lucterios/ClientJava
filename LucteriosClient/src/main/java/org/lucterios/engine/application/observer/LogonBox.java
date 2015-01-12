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

import java.io.IOException;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransport;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class LogonBox implements GUIDialog.DialogVisitor {
	GUIContainer editPnl;
	GUIContainer btnPnl;
	GUIButton btn_SetUp;
	GUIButton btn_OK;
	GUIButton btn_Cancel;
	GUIHyperText lb_Reason;
	GUILabel lb_Server;
	GUILabel lb_PassWord;
	GUILabel lb_User;
	GUILabel lb_savepassword;
	GUILabel txt_Server;
	GUICombo cmp_Server;
	GUIEdit txt_User;
	GUIEdit txt_PassWord;
	GUICheckBox chk_savepassword;

	private static String mLastUserLogon = "";
	private String mLastPassword = "";

	private String mReason="";

	public int mModalResult = 0;

	public GUIActionListener mActionSetUp = null;
	
	private void refresh() {
		btn_SetUp.setEnabled(mActionSetUp != null);
		cmp_Server.setVisible(false);
		txt_Server.setVisible(false);
		if (Singletons.getConfiguration().ServerCount() == 0) {
			Singletons.setLastServer(null);
			txt_Server.setTextString("");
			txt_Server.setVisible(true);
			btn_OK.setEnabled(false);
		} else if (Singletons.getConfiguration().ServerCount() == 1) {
			Singletons.setLastServer(Singletons.getConfiguration().GetServer(0));
			txt_Server.setTextString(Singletons.getLastServer().ServerName);
			txt_Server.setVisible(true);
			btn_OK.setEnabled(true);
		} else {
			cmp_Server.removeAllElements();
			for (int server_idx=0;server_idx<Singletons.getConfiguration().ServerCount();server_idx++)
				cmp_Server.addElement(Singletons.getConfiguration().GetServer(server_idx));
			if (Singletons.getLastServer() == null)
				Singletons.setLastServer(Singletons.getConfiguration().GetServer(0));
			int index=Singletons.getConfiguration().getServers().indexOf(Singletons.getLastServer());
			cmp_Server.setSelectedIndex(Math.max(0,index));
			cmp_Server.setVisible(true);
			btn_OK.setEnabled(true);
		}
	}
	
	private void changeServerSelect() {
		Server selected_server = (Server) cmp_Server.getSelectedItem();
		if (selected_server!=null) {
			IniFileManager pref_server = selected_server.getPreference();
			if (pref_server!=null) {			
				txt_User.setTextString(pref_server.getValueSection(Server.CONNEXION_CAT, Server.LOGON_CNP));
				txt_PassWord.setTextString(pref_server.getValueSection(Server.CONNEXION_CAT, Server.PASSWORD_CNP));
				if ((txt_User.getTextString().length()==0) && (txt_PassWord.getTextString().length()==0)) {
					chk_savepassword.setSelected(false);				
					txt_User.setTextString(mLastUserLogon);
					txt_PassWord.setTextString("");
				}
				else
					chk_savepassword.setSelected(true);
			}
		}		
	}

	private void setReason() {
		lb_Reason.setVisible(true);
		String text="";
		if ("BADAUTH".equals(mReason))
			text="Alias ou Mot de passe incorrect!";
		else if ("BADSESS".equals(mReason))
			text="Session expirée!";
		else if ("BADFROMLOCATION".equals(mReason))
			text="Localisation de connection interdite!";
		if (text!="")
			lb_Reason.setTextString("<center><font color=red>"+text+"</font></center>");
		else {
			lb_Reason.setTextString("");
			lb_Reason.setVisible(false);
		}
	}

	public boolean logon(String aReason) {
		boolean result=true;
		mReason=aReason;
		
		GUIDialog dialog=Singletons.getWindowGenerator().newDialog(Singletons.getWindowGenerator().getFrame());
		dialog.setResizable(false);
		dialog.setDialogVisitor(this);
		dialog.setVisible(true);
		if (mModalResult == 1) {
			HttpTransport transp = Singletons.Transport();
			transp.setProxy(Singletons.getConfiguration().ProxyAdress,
					Singletons.getConfiguration().ProxyPort);
			Server selected_server = Singletons.getLastServer();
			transp.connectToServer(
					selected_server.HostName,
					selected_server.Directory,
					selected_server.HostPort,
					selected_server.ConnectionMode == LucteriosConfiguration.MODE_SECURITY,
					selected_server.UseProxy);
			try {
				Singletons.Factory().setAuthentification(mLastUserLogon, mLastPassword);
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
				result=false;
			}
		} else {
			Singletons.exit();
			result=false;
		}
		return result;
	}

	public LogonBox() {
		super();
		Singletons.getConfiguration().addRefreshListener(new GUIActionListener() {
			public void actionPerformed() {
				LogonBox.this.refresh();
			}
		});
	}
	
	private GUIContainer mContainer;
	private GUIDialog mDialog;
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		mContainer=dialog.getGUIContainer();
		
		dialog.setVisible(false);

		String title = Singletons.getConfiguration().TitreDefault + " Connexion";
		dialog.setTextTitle(title.trim());
		
		editPnl = mContainer.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,0));

		lb_Reason = editPnl.createHyperText(new GUIParam(0,0,2,1,ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		lb_Reason.setTextString("");

		lb_Server = editPnl.createLabel(new GUIParam(0,1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		lb_Server.setStyle(1);
		lb_Server.setTextString("Serveur");


		cmp_Server = editPnl.createCombo(new GUIParam(1,1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,150, 19));
		cmp_Server.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				changeServerSelect();
			}
		});
		
		txt_Server = editPnl.createLabel(new GUIParam(1,1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,150, 19));
		txt_Server.setTextString("");
		txt_Server.setVisible(false);
		txt_Server.setBackgroundColor(cmp_Server.getBackgroundColor());
		
		lb_User = editPnl.createLabel(new GUIParam(0,2,1,1,ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		lb_User.setStyle(1);
		lb_User.setTextString("Alias");

		txt_User = editPnl.createEdit(new GUIParam(1,2,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,150, 19));
		txt_User.setTextString("");

		lb_PassWord = editPnl.createLabel(new GUIParam(0,3,1,1,ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		lb_PassWord.setStyle(1);
		lb_PassWord.setTextString("Mot de passe");

		txt_PassWord = editPnl.createEdit(new GUIParam(1,3,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,150, 19));
		txt_PassWord.setPassword('*');
		txt_PassWord.setTextString("");
		
		lb_savepassword = editPnl.createLabel(new GUIParam(0,4,1,1,ReSizeMode.RSM_NONE,FillMode.FM_HORIZONTAL));
		lb_savepassword.setStyle(1);
		lb_savepassword.setTextString("Se souvenir?");
		chk_savepassword = editPnl.createCheckBox(new GUIParam(1,4,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
		chk_savepassword.setSelected(false);
		
		btnPnl = mContainer.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1));

		btn_OK = btnPnl.createButton(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,100, 25));
		btn_OK.setImage(this.getClass().getResource("/ok.png"));
		btn_OK.setMnemonic('o');
		btn_OK.setTextString("Ok");
		btn_OK.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_OK_actionPerformed();
				refresh();
			}
		});

		btn_SetUp = btnPnl.createButton(new GUIParam(1,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,100, 25));
		btn_SetUp.setImage(this.getClass().getResource("/configure.png"));
		btn_SetUp.setMnemonic('c');
		btn_SetUp.setTextString("Configurer");
		btn_SetUp.addActionListener(mActionSetUp);
				
		btn_Cancel = btnPnl.createButton(new GUIParam(2,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE,100, 25));
		btn_Cancel.setImage(this.getClass().getResource("/cancel.png"));
		btn_Cancel.setMnemonic('a');
		btn_Cancel.setTextString("Annuler");
		btn_Cancel.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_Cancel_actionPerformed();
			}
		});
		
		dialog.setDefaultButton(btn_OK);

		setReason();	
		changeServerSelect();
		
		dialog.setPosition(1.0/3.0);
		
		refresh();
	}

	void btn_Cancel_actionPerformed() {
		mModalResult = 2;
		mDialog.setVisible(false);
	}

	void btn_OK_actionPerformed() {
		mModalResult = 1;
		Server selected_server = (Server) cmp_Server.getSelectedItem();
		if (selected_server==null) {
			selected_server = Singletons.getConfiguration().GetServer(0);			
		}
		Singletons.setLastServer(selected_server);
		mLastUserLogon = txt_User.getTextString();
		mLastPassword = txt_PassWord.getTextString();				
		if (selected_server!=null) {
			IniFileManager pref_server = selected_server.getPreference();
			if (pref_server!=null) {			
				if (chk_savepassword.isSelected()) {
					pref_server.setValueSection(Server.CONNEXION_CAT, Server.LOGON_CNP, mLastUserLogon);
					pref_server.setValueSection(Server.CONNEXION_CAT, Server.PASSWORD_CNP, mLastPassword);
				}
				else {
					pref_server.setValueSection(Server.CONNEXION_CAT, Server.LOGON_CNP, "");
					pref_server.setValueSection(Server.CONNEXION_CAT, Server.PASSWORD_CNP, "");
				}
				try {
					pref_server.save();
				} catch (IOException e) {
				}
			}
		}
		mDialog.setVisible(false);
	}

	public void closing() { }

}
