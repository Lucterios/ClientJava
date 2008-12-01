package org.lucterios.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.graphic.JAdvancePanel;
import org.lucterios.utils.graphic.Tools;

public class AssociationPanel extends JAdvancePanel implements TableModel {

	private static final long serialVersionUID = 1L;
	
	class AssociationEditor extends JDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JAdvancePanel pnl_new_btn;
		private javax.swing.JLabel lbl_name;
		private javax.swing.JTextField txt_name;
		private javax.swing.JLabel lbl_application;
		private javax.swing.JTextField txt_application;

		private JAdvancePanel pnl_btn;
		private javax.swing.JButton btn_AddNew;
		private javax.swing.JButton btn_ExitNew;

		public AssociationEditor(JDialog aOwner) {
			super(aOwner);
			Initial();
		}

		public AssociationEditor(JDialog aOwner, String aExtName, String aAppliName) {
			super(aOwner);
			Initial();
			setTitle("Modifier");
			txt_name.setText(aExtName);
			txt_name.setEnabled(false);
			txt_application.setText(aAppliName);
		}

		private void Initial() {
			setModal(true);
			setTitle("Ajouter");
			getContentPane().setLayout(new java.awt.GridBagLayout());
			Init();
			InitBtn();
			setResizable(true);
			setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
			pack();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((screen.width - getSize().width) / 2,
					(screen.height - getSize().height) / 4);
			javax.swing.JButton[] btns = {btn_AddNew, btn_ExitNew };
			Tools.calculBtnSize(btns);
			this.getRootPane().setDefaultButton(btn_Add);
		}

		public void InitBtn() {
			GridBagConstraints gridBagConstraints;
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
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			pnl_btn.add(btn_AddNew, gridBagConstraints);
			btn_ExitNew.setMnemonic('n');
			btn_ExitNew.setText("Annuler");
			btn_ExitNew.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					btn_Exit(evt);
				}
			});
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			pnl_btn.add(btn_ExitNew,gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			getContentPane().add(pnl_btn,gridBagConstraints);
		}

		public void Init() {
			GridBagConstraints gridBagConstraints;
			pnl_new_btn = new JAdvancePanel();
			pnl_new_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
			pnl_new_btn.setLayout(new java.awt.GridBagLayout());
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			getContentPane().add(pnl_new_btn,gridBagConstraints);

			lbl_name = new javax.swing.JLabel();
			txt_name = new javax.swing.JTextField();
			lbl_application = new javax.swing.JLabel();
			txt_application = new javax.swing.JTextField();
			btn_Del = new javax.swing.JButton();
			btn_Add = new javax.swing.JButton();

			lbl_name.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_name.setText("Extension");
			lbl_name.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			pnl_new_btn.add(lbl_name, gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);
			pnl_new_btn.add(txt_name,gridBagConstraints);

			lbl_application.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_application.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_application.setText("Application associée");
			lbl_application.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);			
			pnl_new_btn.add(lbl_application, gridBagConstraints);
			txt_application.setPreferredSize(new Dimension(100, 19));
			txt_application.setMinimumSize(new Dimension(100, 19));
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.insets =new Insets(1,1,1,1);			
			pnl_new_btn.add(txt_application, gridBagConstraints);
		}

		private void btn_Add(java.awt.event.ActionEvent evt) {
			String name = txt_name.getText().trim();
			int dot_pos=name.lastIndexOf('.');
			if (dot_pos!=-1)
				name=name.substring(dot_pos+1);
			String appli = txt_application.getText().trim();
			if ((name.length() != 0) && (appli.length() != 0)) {
				if (mExtAssociation.containsKey(name))
					mExtAssociation.remove(name); 
				mExtAssociation.put(name,appli); 
				dispose();
			}
		}

		private void btn_Exit(java.awt.event.ActionEvent evt) {
			dispose();
		}
	}
	
	private JDialog mOwnerFrame;
	private Image mFontImg;
	private TreeMap mExtAssociation;

	private javax.swing.JScrollPane scr_pnl;
	private javax.swing.JTable cmp_tbl;
	
	private javax.swing.JButton btn_Add;
	private javax.swing.JButton btn_Mod;
	private javax.swing.JButton btn_Del;	
	
	public AssociationPanel(JDialog aOwnerFrame,Image aFontImg) {
		super();
		mOwnerFrame=aOwnerFrame;
		mFontImg=aFontImg;
		Init();
		InitGridBtn();
		Setup();
	}

	public TreeMap getExtAssociation() {
		return mExtAssociation;
	}

	public void Init() {
		setFontImage(mFontImg, JAdvancePanel.TEXTURE);
		setLayout(new java.awt.GridBagLayout());

		scr_pnl = new javax.swing.JScrollPane();
		cmp_tbl = new javax.swing.JTable();
		cmp_tbl.setFocusable(true);
		cmp_tbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		cmp_tbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		cmp_tbl.setRowSelectionAllowed(true);
		selectListener();
		scr_pnl = new javax.swing.JScrollPane(cmp_tbl);
		scr_pnl.setPreferredSize(new Dimension(400, 140));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.insets =new Insets(1,1,1,1);
		add(scr_pnl, gridBagConstraints);
	}
	
	public void InitGridBtn() {
		GridBagConstraints gridBagConstraints;
		JAdvancePanel pnl_grid_btn = new JAdvancePanel();
		pnl_grid_btn.setFontImage(mFontImg, JAdvancePanel.TEXTURE);
		pnl_grid_btn.setLayout(new java.awt.GridBagLayout());

		btn_Del = new javax.swing.JButton();
		btn_Mod = new javax.swing.JButton();
		btn_Add = new javax.swing.JButton();

		btn_Mod.setMnemonic('m');
		btn_Mod.setText("Modifier");
		btn_Mod.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_ModActionPerformed(evt);
			}
		});		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets =new Insets(1,7,1,7);
		pnl_grid_btn.add(btn_Mod,gridBagConstraints);

		btn_Add.setMnemonic('a');
		btn_Add.setText("Ajouter");
		btn_Add.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_AddActionPerformed(evt);
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets =new Insets(1,7,1,7);
		pnl_grid_btn.add(btn_Add,gridBagConstraints);

		btn_Del.setMnemonic('s');
		btn_Del.setText("Supprimer");
		btn_Del.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_DelActionPerformed(evt);
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets =new Insets(1,7,1,7);
		pnl_grid_btn.add(btn_Del,gridBagConstraints);
		
		JPanel pnl = new JPanel();
		pnl.setOpaque(false);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		pnl_grid_btn.add(pnl,gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		add(pnl_grid_btn,gridBagConstraints);
	}	

	public void selectChange() {
		boolean select = (cmp_tbl.getSelectedRowCount() != 0);
		btn_Del.setEnabled(select);
		btn_Mod.setEnabled(select);
	}

	private void selectListener() {
		javax.swing.ListSelectionModel rowSM = cmp_tbl.getSelectionModel();
		rowSM.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(javax.swing.event.ListSelectionEvent e) {
						selectChange();
					}
				});
	}
	
	protected void btn_ModActionPerformed(ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		AssociationEditor edt = new AssociationEditor(mOwnerFrame,(String)getValueAt(row,0),(String)getValueAt(row,1));
		edt.setVisible(true);
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(row, row);
	}

	protected void btn_DelActionPerformed(ActionEvent evt) {
		int row = cmp_tbl.getSelectedRows()[0];
		mExtAssociation.remove(getKey(row));
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(row, row);
	}

	protected void btn_AddActionPerformed(ActionEvent evt) {
		AssociationEditor edt = new AssociationEditor(mOwnerFrame);
		edt.setVisible(true);
		refreshGrid();
		cmp_tbl.setRowSelectionInterval(mExtAssociation.size() - 1, mExtAssociation.size() - 1);
	}

	public void Setup() {
		mExtAssociation=Singletons.LucteriosSettingFile.getValuesSection(DesktopTools.ASSOCIATION_SECTION);
		refreshGrid();
		javax.swing.JButton[] btns_1 = { btn_Mod, btn_Del, btn_Add };
		Tools.calculBtnSize(btns_1);
	}
	
	private void refreshGrid() {
		cmp_tbl.setModel(new javax.swing.table.DefaultTableModel());
		cmp_tbl.setModel(this);
		selectChange();
		selectListener();
	}

	public void addTableModelListener(TableModelListener l) {}
	public void removeTableModelListener(TableModelListener l) {}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		return 2;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Extension";
			case 1:
				return "Application associée";
			default:
				return "??";
		}
	}

	public int getRowCount() {
		return mExtAssociation.size();
	}

	public String getKey(int rowIndex) {
		Object[] keys=mExtAssociation.keySet().toArray();
		return (String)keys[rowIndex];
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		String current_key=getKey(rowIndex);
		switch (columnIndex) {
			case 0:
				return "*."+current_key;
			case 1:
				return mExtAssociation.get(current_key);
			default:
				return "??";
		}
	}
	
}
