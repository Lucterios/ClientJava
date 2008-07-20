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

package org.lucterios.client;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lucterios.client.utils.LucteriosConfiguration;
import org.lucterios.client.utils.LucteriosConfiguration.Server;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.JAdvancePanel;
import org.lucterios.utils.graphic.Tools;

public class SetupDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_PORT = 80;

	protected Image mFontImg;

	class NewServer extends JDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LucteriosConfiguration.Server mServer = null;

		private JAdvancePanel pnl_new_btn;
		private javax.swing.JLabel lbl_name;
		private javax.swing.JTextField txt_name;
		private javax.swing.JLabel lbl_srv;
		private javax.swing.JTextField txt_srv;
		private javax.swing.JLabel lbl_port;
		private org.lucterios.utils.graphic.SpinEdit spe_port;
		private javax.swing.JLabel lbl_dir;
		private javax.swing.JTextField txt_dir;

		private JAdvancePanel pnl_btn;
		private javax.swing.JButton btn_AddNew;
		private javax.swing.JButton btn_ExitNew;

		public NewServer(JDialog aOwner) {
			super(aOwner);
			Initial();
		}

		public NewServer(JDialog aOwner, Server aServer) {
			super(aOwner);
			Initial();
			txt_name.setText(aServer.ServerName);
			txt_srv.setText(aServer.HostName);
			spe_port.setNumber(aServer.HostPort);
			txt_dir.setText(aServer.Directory);
		}

		private void Initial() {
			setModal(true);
			setTitle("Nouveau");
			getContentPane().setLayout(new java.awt.GridBagLayout());
			Init();
			InitBtn();
			spe_port.init(DEFAULT_PORT, 10, 9999);
			setResizable(true);
			setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
			pack();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((screen.width - getSize().width) / 2,
					(screen.height - getSize().height) / 3);
			javax.swing.JButton[] btns = { this.btn_AddNew, this.btn_ExitNew };
			Tools.calculBtnSize(btns);
			this.getRootPane().setDefaultButton(btn_Add);
		}

		public void InitBtn() {
			pnl_btn = new JAdvancePanel();
			pnl_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);

			btn_AddNew = new javax.swing.JButton();
			btn_ExitNew = new javax.swing.JButton();
			btn_AddNew.setIcon(new ImageIcon(this.getClass().getResource(
					"resources/ok.png")));
			btn_ExitNew = new javax.swing.JButton();
			btn_ExitNew.setIcon(new ImageIcon(this.getClass().getResource(
					"resources/cancel.png")));
			pnl_btn.setLayout(new java.awt.GridBagLayout());
			btn_AddNew.setMnemonic('o');
			btn_AddNew.setText("OK");
			btn_AddNew.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					btn_Add(evt);
				}
			});
			pnl_btn.add(btn_AddNew, getCnt(0, 0, 1, 1, GridBagConstraints.BOTH,
					0));
			btn_ExitNew.setMnemonic('n');
			btn_ExitNew.setText("Annuler");
			btn_ExitNew.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					btn_Exit(evt);
				}
			});
			pnl_btn.add(btn_ExitNew, getCnt(1, 0, 1, 1,
					GridBagConstraints.BOTH, 0));
			getContentPane().add(pnl_btn,
					getCnt(0, 1, 1, 1, GridBagConstraints.BOTH, 1));
		}

		public void Init() {
			GridBagConstraints cnt;
			pnl_new_btn = new JAdvancePanel();
			pnl_new_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
			pnl_new_btn.setLayout(new java.awt.GridBagLayout());
			getContentPane().add(pnl_new_btn,
					getCnt(0, 0, 1, 1, GridBagConstraints.BOTH, 1));

			btn_Del = new javax.swing.JButton();
			lbl_name = new javax.swing.JLabel();
			txt_name = new javax.swing.JTextField();
			lbl_srv = new javax.swing.JLabel();
			txt_srv = new javax.swing.JTextField();
			btn_Add = new javax.swing.JButton();

			lbl_port = new javax.swing.JLabel();
			spe_port = new org.lucterios.utils.graphic.SpinEdit();
			lbl_dir = new javax.swing.JLabel();
			txt_dir = new javax.swing.JTextField();

			lbl_name.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_name.setText("Nom");
			lbl_name
					.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			pnl_new_btn.add(lbl_name, getCnt(0, 0, 1, 1,
					GridBagConstraints.BOTH, 0));
			cnt = getCnt(1, 0, 3, 1, GridBagConstraints.BOTH, 0);
			cnt.weightx = 1;
			pnl_new_btn.add(txt_name, cnt);

			lbl_srv.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_srv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_srv.setText("Serveur");
			lbl_srv
					.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			pnl_new_btn.add(lbl_srv, getCnt(0, 1, 1, 1,
					GridBagConstraints.BOTH, 0));
			txt_srv.setPreferredSize(new Dimension(100, 19));
			txt_srv.setMinimumSize(new Dimension(100, 19));
			cnt = getCnt(1, 1, 1, 1, GridBagConstraints.BOTH, 0);
			cnt.weightx = 1;
			pnl_new_btn.add(txt_srv, cnt);

			lbl_port.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_port.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_port.setText("Port");
			lbl_port
					.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			pnl_new_btn.add(lbl_port, getCnt(2, 1, 1, 1,
					GridBagConstraints.BOTH, 0));
			spe_port.setPreferredSize(new Dimension(40, 0));
			spe_port.setMinimumSize(new Dimension(40, 0));
			cnt = getCnt(3, 1, 1, 1, GridBagConstraints.BOTH, 0);
			cnt.weightx = 1;
			pnl_new_btn.add(spe_port, cnt);

			lbl_dir.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_dir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_dir.setText("Répertoire");
			lbl_dir
					.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			pnl_new_btn.add(lbl_dir, getCnt(0, 2, 1, 1,
					GridBagConstraints.BOTH, 0));
			txt_dir.setText("/");
			cnt = getCnt(1, 2, 3, 1, GridBagConstraints.BOTH, 0);
			cnt.weightx = 1;
			pnl_new_btn.add(txt_dir, cnt);
		}

		private void btn_Add(java.awt.event.ActionEvent evt) {
			String name = txt_name.getText().trim();
			String srv = txt_srv.getText().trim();
			if ((name.length() != 0) && (srv.length() != 0)) {
				mServer = newServer(name, srv, spe_port.getNumber(), txt_dir
						.getText());
				dispose();
			}
		}

		private void btn_Exit(java.awt.event.ActionEvent evt) {
			mServer = null;
			dispose();
		}
	}

	private javax.swing.JLabel lbl_Title;
	private javax.swing.JTextField txt_Title;

	private javax.swing.JLabel lbl_proxyaddr;
	private javax.swing.JTextField txt_proxyaddr;
	private javax.swing.JLabel lbl_proxyport;
	private org.lucterios.utils.graphic.SpinEdit spe_proxyport;

	private javax.swing.JLabel lbl_tbl;
	private javax.swing.JScrollPane scr_pnl;
	private javax.swing.JTable cmp_tbl;

	private javax.swing.JButton btn_Add;
	private javax.swing.JButton btn_Mod;
	private javax.swing.JButton btn_Del;

	private javax.swing.JButton btn_Up;
	private javax.swing.JButton btn_Down;

	private JAdvancePanel pnl_main;
	private JAdvancePanel pnl_btn;
	private javax.swing.JButton btn_Ok;
	private javax.swing.JButton btn_Cancel;

	private LucteriosConfiguration mConf;

	public SetupDialog(JFrame aFrame, LucteriosConfiguration aConf) {
		super(aFrame);
		mFontImg = Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("resources/MainFont.jpg"));
		mConf = aConf;
		Init();
		InitGrid();
		InitGridBtn();
		this.getRootPane().setDefaultButton(btn_Ok);
		Setup();
		setResizable(true);
		setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 3);
	}

	public LucteriosConfiguration.Server newServer(String aServerName,
			String aHostName, int aHostPort, String aDirectory) {
		return mConf.newServer(aServerName, aHostName, aHostPort, aDirectory);
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
		lbl_proxyaddr = new javax.swing.JLabel();
		txt_proxyaddr = new javax.swing.JTextField();
		lbl_proxyport = new javax.swing.JLabel();
		spe_proxyport = new org.lucterios.utils.graphic.SpinEdit();

		pnl_btn = new JAdvancePanel();
		pnl_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
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
		pnl_main.add(txt_Title, getCnt(1, 0, 3, 1, GridBagConstraints.BOTH, 1));

		lbl_proxyaddr.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD,
				10));
		lbl_proxyaddr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_proxyaddr.setText("Proxy");
		lbl_proxyaddr
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		pnl_main.add(lbl_proxyaddr, getCnt(0, 1, 1, 1, GridBagConstraints.BOTH,
				0));
		txt_proxyaddr.setEnabled(true);
		txt_proxyaddr.setText("");
		pnl_main.add(txt_proxyaddr, getCnt(1, 1, 1, 1, GridBagConstraints.BOTH,
				1));

		lbl_proxyport.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD,
				10));
		lbl_proxyport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_proxyport.setText("Port");
		lbl_proxyport
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		pnl_main.add(lbl_proxyport, getCnt(2, 1, 1, 1, GridBagConstraints.BOTH,
				0));
		pnl_main.add(spe_proxyport, getCnt(3, 1, 1, 1, GridBagConstraints.BOTH,
				1));
		getContentPane().add(pnl_main,
				getCnt(0, 0, 4, 1, GridBagConstraints.BOTH, 1));

		pnl_btn.setLayout(new java.awt.GridBagLayout());
		btn_Ok.setMnemonic('o');
		btn_Ok.setText("OK");
		btn_Ok.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_OkActionPerformed(evt);
			}
		});
		btn_Ok
				.setIcon(new ImageIcon(getClass().getResource(
						"resources/ok.png")));
		pnl_btn.add(btn_Ok, getCnt(0, 0, 1, 1, GridBagConstraints.BOTH, 0));
		btn_Cancel.setMnemonic('n');
		btn_Cancel.setText("Annuler");
		btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_CancelActionPerformed(evt);
			}
		});
		btn_Cancel.setIcon(new ImageIcon(getClass().getResource(
				"resources/cancel.png")));
		pnl_btn.add(btn_Cancel, getCnt(1, 0, 1, 1, GridBagConstraints.BOTH, 0));

		getContentPane().add(pnl_btn,
				getCnt(0, 4, 4, 1, GridBagConstraints.BOTH, 1));
	}

	public void InitGrid() {
		lbl_tbl = new javax.swing.JLabel();
		scr_pnl = new javax.swing.JScrollPane();
		cmp_tbl = new javax.swing.JTable();

		lbl_tbl.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
		lbl_tbl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lbl_tbl.setText("Serveurs");
		lbl_tbl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		pnl_main.add(lbl_tbl, getCnt(0, 2, 3, 1, GridBagConstraints.BOTH, 0));
		cmp_tbl.setFocusable(true);
		cmp_tbl
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		cmp_tbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		cmp_tbl.setRowSelectionAllowed(true);
		selectListener();
		scr_pnl = new javax.swing.JScrollPane(cmp_tbl);
		scr_pnl.setPreferredSize(new Dimension(400, 150));
		pnl_main.add(scr_pnl, getCnt(0, 3, 2, 1, GridBagConstraints.BOTH, 1));
	}

	public void InitGridBtn() {
		JAdvancePanel pnl_grid_btn = new JAdvancePanel();
		pnl_grid_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
		pnl_grid_btn.setLayout(new java.awt.GridBagLayout());

		btn_Del = new javax.swing.JButton();
		btn_Mod = new javax.swing.JButton();
		btn_Add = new javax.swing.JButton();

		btn_Up = new javax.swing.JButton();
		btn_Down = new javax.swing.JButton();

		btn_Add.setMnemonic('a');
		btn_Add.setText("Ajouter");
		btn_Add.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_AddActionPerformed(evt);
			}
		});
		pnl_grid_btn.add(btn_Add,
				getCnt(0, 0, 2, 1, GridBagConstraints.NONE, 0));

		btn_Mod.setMnemonic('m');
		btn_Mod.setText("Modifier");
		btn_Mod.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_ModActionPerformed(evt);
			}
		});
		pnl_grid_btn.add(btn_Mod,
				getCnt(0, 1, 2, 1, GridBagConstraints.NONE, 0));

		btn_Del.setMnemonic('s');
		btn_Del.setText("Supprimer");
		btn_Del.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_DelActionPerformed(evt);
			}
		});
		pnl_grid_btn.add(btn_Del,
				getCnt(0, 2, 2, 1, GridBagConstraints.NONE, 0));

		btn_Up.setMnemonic('h');
		btn_Up.setText("Haut");
		btn_Up.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_UpActionPerformed(evt);
			}
		});
		pnl_grid_btn
				.add(btn_Up, getCnt(0, 4, 1, 1, GridBagConstraints.NONE, 0));
		btn_Down.setMnemonic('b');
		btn_Down.setText("Bas");
		btn_Down.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_DownActionPerformed(evt);
			}
		});
		pnl_grid_btn.add(btn_Down, getCnt(1, 4, 1, 1, GridBagConstraints.NONE,
				0));

		JPanel pnl = new JPanel();
		pnl.setOpaque(false);
		pnl_grid_btn.add(pnl, getCnt(0, 3, 2, 1, GridBagConstraints.BOTH, 1));
		pnl_main.add(pnl_grid_btn, getCnt(2, 3, 2, 1, GridBagConstraints.BOTH,
				0));
	}

	private void Setup() {
		txt_Title.setText(mConf.TitreDefault);
		txt_proxyaddr.setText(mConf.ProxyAdress);
		spe_proxyport.setNumber(mConf.ProxyPort);
		cmp_tbl.setModel(mConf);
		selectChange();
		javax.swing.JButton[] btns_1 = { btn_Mod, btn_Del, btn_Add };
		Tools.calculBtnSize(btns_1);
		javax.swing.JButton[] btns_2 = { btn_Ok, btn_Cancel };
		Tools.calculBtnSize(btns_2);
		javax.swing.JButton[] btns_3 = { btn_Up, btn_Down };
		Tools.calculBtnSize(btns_3);
	}

	public void selectChange() {
		boolean select = (cmp_tbl.getSelectedRowCount() != 0);
		btn_Del.setEnabled(select);
		btn_Mod.setEnabled(select);
		int row = 0;
		if (select)
			row = cmp_tbl.getSelectedRows()[0];
		btn_Up.setEnabled(select && (row > 0));
		btn_Down.setEnabled(select && ((row + 1) < mConf.ServerCount()));
	}

	private void selectListener() {
		javax.swing.ListSelectionModel rowSM = cmp_tbl.getSelectionModel();
		rowSM
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent e) {
						selectChange();
					}
				});
	}

	private void btn_AddActionPerformed(java.awt.event.ActionEvent evt) {
		NewServer srv = new NewServer(this);
		srv.setVisible(true);
		if (srv.mServer != null) {
			mConf.AddServer(srv.mServer);
			refreshGrid();
			cmp_tbl.setRowSelectionInterval(mConf.ServerCount() - 1, mConf
					.ServerCount() - 1);
		}
	}

	private void refreshGrid() {
		cmp_tbl.setModel(new javax.swing.table.DefaultTableModel());
		cmp_tbl.setModel(mConf);
		selectListener();
	}

	private void btn_ModActionPerformed(java.awt.event.ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		NewServer srv = new NewServer(this, mConf.GetServer(row));
		srv.setVisible(true);
		if (srv.mServer != null) {
			mConf.SetServer(row, srv.mServer);
			refreshGrid();
			cmp_tbl.setRowSelectionInterval(row, row);
		}
	}

	private void btn_UpActionPerformed(java.awt.event.ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = mConf.GetServer(row);
		Server srv2 = mConf.GetServer(row - 1);
		mConf.SetServer(row, srv2);
		mConf.SetServer(row - 1, srv1);
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(row - 1, row - 1);
	}

	private void btn_DownActionPerformed(java.awt.event.ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		Server srv1 = mConf.GetServer(row);
		Server srv2 = mConf.GetServer(row + 1);
		mConf.SetServer(row, srv2);
		mConf.SetServer(row + 1, srv1);
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(row + 1, row + 1);
	}

	private void btn_DelActionPerformed(java.awt.event.ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		mConf.DeleteServer(row);
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(row, row);
	}

	private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void btn_OkActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			mConf.TitreDefault = txt_Title.getText().trim();
			mConf.ProxyAdress = txt_proxyaddr.getText();
			mConf.ProxyPort = spe_proxyport.getNumber();
			mConf.write();
			dispose();
		} catch (java.io.IOException ioe) {
			ExceptionDlg.throwException(ioe);
		}
	}

	public static void run(JFrame aFrame, LucteriosConfiguration aConf) {
		SetupDialog dlg = new SetupDialog(aFrame, aConf);
		dlg.setVisible(true);
	}
}
