package org.lucterios.engine.setting;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.ui.GUIActionListener;

public class ConfigurationPanel implements GUISelectListener {
	class ServerEditor implements DialogVisitor {
		public LucteriosConfiguration.Server mServer = null;

		private GUIContainer pnl_new_btn;
		private GUILabel lbl_name;
		private GUIEdit txt_name;
		private GUILabel lbl_srv;
		private GUIEdit txt_srv;
		private GUILabel lbl_port;
		private GUISpinEdit spe_port;
		private GUILabel lbl_mode;
		private GUICombo sel_mode;
		private GUILabel lbl_dir;
		private GUIEdit txt_dir;
		private GUILabel lbl_proxy;
		private GUICheckBox chk_proxy;

		private GUIContainer pnl_btn;
		private GUIButton btn_AddNew;
		private GUIButton btn_ExitNew;
		
		private GUIDialog mOwner;

		public ServerEditor(Server aServer) {
			super();
			mServer=aServer;
		}

		public void execute(GUIDialog aOwner) {
			mOwner=aOwner;
			Initial();
			if (mServer!=null) {
				mOwner.setTextTitle("Modifier");
				txt_name.setTextString(mServer.ServerName);
				txt_srv.setTextString(mServer.HostName);
				spe_port.setNumber(mServer.HostPort);
				txt_dir.setTextString(mServer.Directory);
				sel_mode.setSelectedIndex(mServer.ConnectionMode);
				chk_proxy.setSelected(mServer.UseProxy);
			}
		}

		public void closing(){}

		private void Initial() {
			mOwner.setTextTitle("Ajouter");
			Init();
			InitBtn();
			spe_port.init(LucteriosConfiguration.DEFAULT_PORT, 10, 9999);
			sel_mode.setSelectedIndex(LucteriosConfiguration.MODE_NORMAL);
			chk_proxy.setSelected(true);
			mOwner.pack();
			mOwner.initialPosition();
			GUIButton[] btns = { this.btn_AddNew, this.btn_ExitNew };
			mOwner.getGUIContainer().calculBtnSize(btns);
			mOwner.setDefaultButton(btn_Add);
			mOwner.setResizable(false);
		}

		public void InitBtn() {
			pnl_btn = mOwner.getGUIContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 1));

			btn_AddNew = pnl_btn.createButton(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			btn_AddNew.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class
					.getResource("ok.png")));
			btn_AddNew.setMnemonic('o');
			btn_AddNew.setTextString("OK");
			btn_AddNew.addActionListener(new GUIActionListener() {
				public void actionPerformed() {
					btn_Add();
				}
			});

			btn_ExitNew = pnl_btn.createButton(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			btn_ExitNew.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class
					.getResource("cancel.png")));
			btn_ExitNew.setMnemonic('n');
			btn_ExitNew.setTextString("Annuler");
			btn_ExitNew.addActionListener(new GUIActionListener() {
				public void actionPerformed() {
					btn_Exit();
				}
			});
		}

		public void Init() {
			pnl_new_btn = mOwner.getGUIContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));

			lbl_name = pnl_new_btn.createLabel(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_name.setTextString("Nom");
			lbl_name.setStyle(1);

			txt_name = pnl_new_btn.createEdit(new GUIParam(1, 0, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));

			lbl_srv = pnl_new_btn.createLabel(new GUIParam(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_srv.setTextString("Serveur");
			lbl_srv.setStyle(1);

			txt_srv = pnl_new_btn.createEdit(new GUIParam(1, 1, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));

			lbl_mode = pnl_new_btn.createLabel(new GUIParam(0, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_mode.setTextString("Mode");
			lbl_mode.setStyle(1);

			sel_mode = pnl_new_btn.createCombo(new GUIParam(1, 2, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
			sel_mode.addList(LucteriosConfiguration.MODE_TEXTS);

			lbl_port = pnl_new_btn.createLabel(new GUIParam(2, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_port.setTextString("Port");
			lbl_port.setStyle(1);

			spe_port = pnl_new_btn.createSpinEdit(new GUIParam(3, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			
			lbl_dir = pnl_new_btn.createLabel(new GUIParam(0, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_dir.setTextString("Répertoire");
			lbl_dir.setStyle(1);

			txt_dir = pnl_new_btn.createEdit(new GUIParam(1, 3, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
			txt_dir.setTextString("/");

			lbl_proxy = pnl_new_btn.createLabel(new GUIParam(0, 4, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_proxy.setTextString("Proxy");
			lbl_proxy.setStyle(1);
			
			chk_proxy = pnl_new_btn.createCheckBox(new GUIParam(1, 4, 3, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			chk_proxy.setSelected(true);
		}

		private void btn_Add() {
			String name = txt_name.getTextString().trim();
			String srv = txt_srv.getTextString().trim();
			if ((name.length() != 0) && (srv.length() != 0)) {
				int mode = sel_mode.getSelectedIndex();
				mServer = newServer(name, srv, (int) (spe_port.getNumber()),
						txt_dir.getTextString(), mode, chk_proxy.isSelected());
				mOwner.dispose();
			}
		}

		private void btn_Exit() {
			mServer = null;
			mOwner.dispose();
		}

	}

	
	private GUIDialog mOwnerFrame;
	
	private GUILabel lbl_proxyaddr;
	private GUIEdit txt_proxyaddr;
	private GUILabel lbl_proxyport;
	private GUISpinEdit spe_proxyport;

	private GUILabel lbl_tbl;
	private GUIGrid cmp_tbl;

	private GUIButton btn_Add;
	private GUIButton btn_Mod;
	private GUIButton btn_Del;

	private GUIButton btn_Up;
	private GUIButton btn_Down;

	private GUIContainer mOwnerContainer = null;

	public ConfigurationPanel(GUIDialog aOwnerFrame) {
		super();
		mOwnerFrame = aOwnerFrame;
	}
	
	public void fillContainer(GUIContainer aOwnerContainer){
		mOwnerContainer=aOwnerContainer;
		Init();
		InitGrid();
		InitGridBtn();
		Setup();
	}

	public LucteriosConfiguration.Server newServer(String aServerName,
			String aHostName, int aHostPort, String aDirectory,
			int aConnectionMode, boolean aUseProxy) {
		return Singletons.getConfiguration().newServer(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode, aUseProxy);
	}

	public void Init() {
		lbl_proxyaddr = mOwnerContainer.createLabel(new GUIParam(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL));
		lbl_proxyaddr.setTextString("Proxy");
		lbl_proxyaddr.setStyle(1);

		txt_proxyaddr = mOwnerContainer.createEdit(new GUIParam(1, 1, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
		txt_proxyaddr.setEnabled(true);
		txt_proxyaddr.setTextString("");

		lbl_proxyport  = mOwnerContainer.createLabel(new GUIParam(2, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL));
		lbl_proxyport.setTextString("Port");
		lbl_proxyport.setStyle(1);

		spe_proxyport = mOwnerContainer.createSpinEdit(new GUIParam(3, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL));
	}

	public void InitGrid() {
		lbl_tbl = mOwnerContainer.createLabel(new GUIParam(0, 2, 3, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
		lbl_tbl.setTextString("Serveurs");
		lbl_tbl.setStyle(1);

		cmp_tbl = mOwnerContainer.createGrid(new GUIParam(0, 3, 2, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH, 400, 150));
		cmp_tbl.setMultiSelection(false);
		cmp_tbl.addSelectListener(this);
		cmp_tbl.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_ModActionPerformed();
			}
		});
	}

	public void InitGridBtn() {
		GUIContainer pnl_grid_btn = mOwnerContainer.createContainer(ContainerType.CT_NORMAL, 
				new GUIParam(2, 3, 2, 1, ReSizeMode.RSM_VERTICAL, FillMode.FM_BOTH));

		btn_Mod = pnl_grid_btn.createButton(new GUIParam(0, 0, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Mod.setMnemonic('m');
		btn_Mod.setTextString("Modifier");
		btn_Mod.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_ModActionPerformed();
			}
		});

		btn_Add = pnl_grid_btn.createButton(new GUIParam(0, 1, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Add.setMnemonic('a');
		btn_Add.setTextString("Ajouter");
		btn_Add.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_AddActionPerformed();
			}
		});

		btn_Del = pnl_grid_btn.createButton(new GUIParam(0, 2, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Del.setMnemonic('s');
		btn_Del.setTextString("Supprimer");
		btn_Del.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DelActionPerformed();
			}
		});

		pnl_grid_btn.createContainer(ContainerType.CT_NORMAL,new GUIParam(0, 3, 2, 1));
		
		btn_Up = pnl_grid_btn.createButton(new GUIParam(0, 4, 1, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Up.setMnemonic('h');
		btn_Up.setTextString("Haut");
		btn_Up.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_UpActionPerformed();
			}
		});

		btn_Down = pnl_grid_btn.createButton(new GUIParam(1, 4, 1, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Down.setMnemonic('b');
		btn_Down.setTextString("Bas");
		btn_Down.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DownActionPerformed();
			}
		});

	}

	private void Setup() {
		txt_proxyaddr.setTextString(Singletons.getConfiguration().ProxyAdress);
		spe_proxyport.setNumber(Singletons.getConfiguration().ProxyPort);
		cmp_tbl.setGridInterface(Singletons.getConfiguration());
		selectionChanged();
		GUIButton[] btns_1 = { btn_Mod, btn_Del, btn_Add };
		mOwnerContainer.calculBtnSize(btns_1);
		GUIButton[] btns_3 = { btn_Up, btn_Down };
		mOwnerContainer.calculBtnSize(btns_3);
	}

	public void selectionChanged() {
		boolean select = (cmp_tbl.getSelectedRowCount() != 0);
		btn_Del.setEnabled(select);
		btn_Mod.setEnabled(select);
		int row = 0;
		if (select)
			row = cmp_tbl.getSelectedRows()[0];
		btn_Up.setEnabled(select && (row > 0));
		btn_Down.setEnabled(select && ((row + 1) < Singletons.getConfiguration().ServerCount()));
	}

	private void btn_AddActionPerformed() {
		try
		{
			GUIDialog new_dialog=mOwnerFrame.createDialog();
			ServerEditor srv = new ServerEditor(null);
			new_dialog.setDialogVisitor(srv);
			new_dialog.setVisible(true);
			if (srv.mServer != null) {
				Singletons.getConfiguration().AddServer(srv.mServer);
				refreshGUI(Singletons.getConfiguration().ServerCount() - 1);
			}
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void refreshGUI(int aRow) {
		cmp_tbl.setGridInterface(Singletons.getConfiguration());
		cmp_tbl.setRowSelection(aRow);
	}

	private void btn_ModActionPerformed() {
		try {
			int row = cmp_tbl.getSelectedRows()[0];
			GUIDialog new_dialog=mOwnerFrame.createDialog();
			ServerEditor srv = new ServerEditor(Singletons.getConfiguration().GetServer(row));
			new_dialog.setDialogVisitor(srv);
			new_dialog.setVisible(true);
			if (srv.mServer != null) {
				Singletons.getConfiguration().SetServer(row, srv.mServer);
				refreshGUI(row);
			}
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	private void btn_UpActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = Singletons.getConfiguration().GetServer(row);
		Server srv2 = Singletons.getConfiguration().GetServer(row - 1);
		Singletons.getConfiguration().SetServer(row, srv2);
		Singletons.getConfiguration().SetServer(row - 1, srv1);
		refreshGUI(row - 1);
	}

	private void btn_DownActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = Singletons.getConfiguration().GetServer(row);
		Server srv2 = Singletons.getConfiguration().GetServer(row + 1);
		Singletons.getConfiguration().SetServer(row, srv2);
		Singletons.getConfiguration().SetServer(row + 1, srv1);
		refreshGUI(row + 1);
	}

	private void btn_DelActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Singletons.getConfiguration().DeleteServer(row);
		refreshGUI(row);
	}

	public String getProxyAddr() {
		return txt_proxyaddr.getTextString();
	}

	public int getProxyPort() {
		return (int) (spe_proxyport.getNumber());
	}


}
