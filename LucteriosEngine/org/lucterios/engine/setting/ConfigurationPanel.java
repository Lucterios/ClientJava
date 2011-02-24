package org.lucterios.engine.setting;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.IDialog;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIContainer.FillMode;
import org.lucterios.gui.GUIContainer.ReSizeMode;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.IDialog.DialogVisitor;

public class ConfigurationPanel implements GUISelectListener {
	private static final long serialVersionUID = 1L;

	class ServerEditor implements DialogVisitor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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

		private GUIContainer pnl_btn;
		private GUIButton btn_AddNew;
		private GUIButton btn_ExitNew;
		
		private IDialog mOwner;

		public ServerEditor(Server aServer) {
			super();
			mServer=aServer;
		}

		public void execute(IDialog aOwner) {
			mOwner=aOwner;
			Initial();
			if (mServer!=null) {
				mOwner.setTitle("Modifier");
				txt_name.setTextString(mServer.ServerName);
				txt_srv.setTextString(mServer.HostName);
				spe_port.setNumber(mServer.HostPort);
				txt_dir.setTextString(mServer.Directory);
				sel_mode.setSelectedIndex(mServer.ConnectionMode);
			}
		}

		private void Initial() {
			mOwner.setTitle("Ajouter");
			Init();
			InitBtn();
			spe_port.init(LucteriosConfiguration.DEFAULT_PORT, 10, 9999);
			sel_mode.setSelectedIndex(LucteriosConfiguration.MODE_NORMAL);
			mOwner.pack();
			int[] screen = Singletons.mDesktop.getScreenSize();
			mOwner.setLocation((screen[0] - mOwner.getSizeX()) / 2, (screen[1] - mOwner.getSizeY()) / 4);			
			GUIButton[] btns = { this.btn_AddNew, this.btn_ExitNew };
			mOwner.getContainer().calculBtnSize(btns);
			mOwner.setDefaultButton(btn_Add);
			mOwner.setResizable(false);
		}

		public void InitBtn() {
			pnl_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, 0, 1, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);

			btn_AddNew = pnl_btn.createButton(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			btn_AddNew.setImage(Singletons.mDesktop.CreateImage(Resources.class
					.getResource("ok.png")));
			btn_AddNew.setMnemonic('o');
			btn_AddNew.setTextString("OK");
			btn_AddNew.addActionListener(new GUIActionListener() {
				public void actionPerformed() {
					btn_Add();
				}
			});

			btn_ExitNew = pnl_btn.createButton(1, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			btn_ExitNew.setImage(Singletons.mDesktop.CreateImage(Resources.class
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
			pnl_new_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, 0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);

			lbl_name = pnl_new_btn.createLabel(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_name.setTextString("Nom");
			/*lbl_name.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_name.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			txt_name = pnl_new_btn.createEdit(1, 0, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);

			lbl_srv = pnl_new_btn.createLabel(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_srv.setTextString("Serveur");
			/*lbl_srv.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_srv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_srv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			txt_srv = pnl_new_btn.createEdit(1, 1, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);
			/*txt_srv.setPreferredSize(new Dimension(100, 19));
			txt_srv.setMinimumSize(new Dimension(100, 19));*/

			lbl_mode = pnl_new_btn.createLabel(0, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_mode.setTextString("Mode");
			/*lbl_mode.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_mode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_mode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			sel_mode = pnl_new_btn.createCombo(1, 2, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);
			sel_mode.addList(LucteriosConfiguration.MODE_TEXTS);

			lbl_port = pnl_new_btn.createLabel(2, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_port.setTextString("Port");
			/*lbl_port.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_port.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_port.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			spe_port = pnl_new_btn.createSpinEdit(3, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			/*spe_port.setPreferredSize(new Dimension(40, 0));
			spe_port.setMinimumSize(new Dimension(40, 0));*/

			lbl_dir = pnl_new_btn.createLabel(0, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_dir.setTextString("Répertoire");
			/*lbl_dir.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_dir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_dir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			txt_dir = pnl_new_btn.createEdit(1, 3, 3, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);
			txt_dir.setTextString("/");
		}

		private void btn_Add() {
			String name = txt_name.getTextString().trim();
			String srv = txt_srv.getTextString().trim();
			if ((name.length() != 0) && (srv.length() != 0)) {
				int mode = sel_mode.getSelectedIndex();
				mServer = newServer(name, srv, (int) (spe_port.getNumber()),
						txt_dir.getTextString(), mode);
				mOwner.dispose();
			}
		}

		private void btn_Exit() {
			mServer = null;
			mOwner.dispose();
		}

	}

	
	private IDialog mOwnerFrame;
	
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

	public ConfigurationPanel(IDialog aOwnerFrame) {
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
			int aConnectionMode) {
		return Singletons.Configuration.newServer(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode);
	}

	public void Init() {
		lbl_proxyaddr = mOwnerContainer.createLabel(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL);
		lbl_proxyaddr.setTextString("Proxy");
		
		// TODO Font des label et position
		/*lbl_proxyaddr.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD,10));
		lbl_proxyaddr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_proxyaddr.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

		txt_proxyaddr = mOwnerContainer.createEdit(1, 1, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL);
		txt_proxyaddr.setEnabled(true);
		txt_proxyaddr.setTextString("");

		lbl_proxyport  = mOwnerContainer.createLabel(2, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL);
		lbl_proxyport.setTextString("Port");
		// TODO Font des label et position
		/*lbl_proxyport.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD,10));
		lbl_proxyport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_proxyport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

		spe_proxyport = mOwnerContainer.createSpinEdit(3, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_HORIZONTAL);
	}

	public void InitGrid() {
		lbl_tbl = mOwnerContainer.createLabel(0, 2, 3, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
		lbl_tbl.setTextString("Serveurs");
		// TODO Font des label et position
		/*lbl_tbl.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		lbl_tbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_tbl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

		cmp_tbl = mOwnerContainer.createGrid(0, 3, 2, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		cmp_tbl.setMultiSelection(false);
		cmp_tbl.addSelectListener(this);	
	}

	public void InitGridBtn() {
		GUIContainer pnl_grid_btn = mOwnerContainer.createContainer(ContainerType.CT_NORMAL, 
				2, 3, 2, 1, ReSizeMode.RSM_VERTICAL, FillMode.FM_BOTH);

		btn_Mod = pnl_grid_btn.createButton(0, 0, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Mod.setMnemonic('m');
		btn_Mod.setTextString("Modifier");
		btn_Mod.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_ModActionPerformed();
			}
		});

		btn_Add = pnl_grid_btn.createButton(0, 1, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Add.setMnemonic('a');
		btn_Add.setTextString("Ajouter");
		btn_Add.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_AddActionPerformed();
			}
		});

		btn_Del = pnl_grid_btn.createButton(0, 2, 2, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Del.setMnemonic('s');
		btn_Del.setTextString("Supprimer");
		btn_Del.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DelActionPerformed();
			}
		});

		pnl_grid_btn.createContainer(ContainerType.CT_NORMAL,0, 3, 2, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		
		btn_Up = pnl_grid_btn.createButton(0, 4, 1, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Up.setMnemonic('h');
		btn_Up.setTextString("Haut");
		btn_Up.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_UpActionPerformed();
			}
		});

		btn_Down = pnl_grid_btn.createButton(1, 4, 1, 1,ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Down.setMnemonic('b');
		btn_Down.setTextString("Bas");
		btn_Down.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DownActionPerformed();
			}
		});

	}

	private void Setup() {
		txt_proxyaddr.setTextString(Singletons.Configuration.ProxyAdress);
		spe_proxyport.setNumber(Singletons.Configuration.ProxyPort);
		cmp_tbl.setGridInterface(Singletons.Configuration);
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
		btn_Down.setEnabled(select && ((row + 1) < Singletons.Configuration.ServerCount()));
	}

	private void btn_AddActionPerformed() {
		IDialog new_dialog=mOwnerFrame.createDialog();
		ServerEditor srv = new ServerEditor(null);
		new_dialog.setDialogVisitor(srv);
		new_dialog.setVisible(true);
		if (srv.mServer != null) {
			Singletons.Configuration.AddServer(srv.mServer);
			refreshGUI(Singletons.Configuration.ServerCount() - 1);
		}
	}

	public void refreshGUI(int aRow) {
		cmp_tbl.setGridInterface(Singletons.Configuration);
		cmp_tbl.setRowSelection(aRow);
	}

	private void btn_ModActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		IDialog new_dialog=mOwnerFrame.createDialog();
		ServerEditor srv = new ServerEditor(Singletons.Configuration.GetServer(row));
		new_dialog.setDialogVisitor(srv);
		new_dialog.setVisible(true);
		if (srv.mServer != null) {
			Singletons.Configuration.SetServer(row, srv.mServer);
			refreshGUI(row);
		}
	}

	private void btn_UpActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = Singletons.Configuration.GetServer(row);
		Server srv2 = Singletons.Configuration.GetServer(row - 1);
		Singletons.Configuration.SetServer(row, srv2);
		Singletons.Configuration.SetServer(row - 1, srv1);
		refreshGUI(row - 1);
	}

	private void btn_DownActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = Singletons.Configuration.GetServer(row);
		Server srv2 = Singletons.Configuration.GetServer(row + 1);
		Singletons.Configuration.SetServer(row, srv2);
		Singletons.Configuration.SetServer(row + 1, srv1);
		refreshGUI(row + 1);
	}

	private void btn_DelActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		Singletons.Configuration.DeleteServer(row);
		refreshGUI(row);
	}

	public String getProxyAddr() {
		return txt_proxyaddr.getTextString();
	}

	public int getProxyPort() {
		return (int) (spe_proxyport.getNumber());
	}


}
