package org.lucterios.engine.setting;

import java.io.IOException;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.StringDico;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GridInterface;
import org.lucterios.gui.IDialog;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIContainer.FillMode;
import org.lucterios.gui.GUIContainer.ReSizeMode;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.IDialog.DialogVisitor;

public class AssociationPanel implements GUISelectListener,GridInterface {

	private static final long serialVersionUID = 1L;

	class AssociationEditor implements DialogVisitor  {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private IDialog mOwner;
		
		private GUIContainer pnl_new_btn;
		private GUILabel lbl_name;
		private GUIEdit txt_name;
		private GUILabel lbl_application;
		private GUIEdit txt_application;

		private GUIContainer pnl_btn;
		private GUIButton btn_AddNew;
		private GUIButton btn_ExitNew;
		
		private String mExtName=null;
		private String mAppliName=null;

		public AssociationEditor(String aExtName,String aAppliName) {
			super();
			mExtName=aExtName;
			mAppliName=aAppliName;
		}

		public void execute(IDialog aOwner) {
			mOwner=aOwner;
			Initial();
			if ((mExtName!=null) && (mAppliName!=null)) { 
				mOwner.setTitle("Modifier");
				txt_name.setTextString(mExtName);
				txt_name.setEnabled(false);
				txt_application.setTextString(mAppliName);
			}
		}
		
		public void execute() {
			Initial();
		}
		
		private void Initial() {
			mOwner.setTitle("Ajouter");
			Init();
			InitBtn();
			mOwner.pack();
			int[] screen = Singletons.mDesktop.getScreenSize();
			mOwner.setLocation((screen[0] - mOwner.getSizeX()) / 2, (screen[1] - mOwner.getSizeY()) / 4);			
			GUIButton[] btns = { btn_AddNew, btn_ExitNew };
			mOwner.getContainer().calculBtnSize(btns);
			mOwner.setDefaultButton(btn_Add);
			mOwner.setResizable(false);
		}

		public void InitBtn() {
			pnl_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, 0, 1, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);

			btn_AddNew = pnl_btn.createButton(0,0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
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
			pnl_new_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, 0, 0, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);

			lbl_name = pnl_new_btn.createLabel(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_name.setTextString("Extension");
			// TODO Font des label et position
			/*lbl_name.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_name.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/
			
			txt_name = pnl_new_btn.createEdit(1, 0, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);

			lbl_application = pnl_new_btn.createLabel(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH);
			lbl_application.setTextString("Application associée");
			// TODO Font des label et position
			/*lbl_application.setFont(new java.awt.Font("Dialog", 0, 10));
			lbl_application.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lbl_application.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);*/

			txt_application = pnl_new_btn.createEdit(1,1, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH);
			// TODO Defaut size
			/*txt_application.setPreferredSize(new Dimension(100, 19));
			txt_application.setMinimumSize(new Dimension(100, 19));*/
		}

		private void btn_Add() {
			String name = txt_name.getTextString().trim();
			int dot_pos = name.lastIndexOf('.');
			if (dot_pos != -1)
				name = name.substring(dot_pos + 1);
			String appli = txt_application.getTextString().trim();
			if ((name.length() != 0) && (appli.length() != 0)) {
				if (mExtAssociation.containsKey(name))
					mExtAssociation.remove(name);
				mExtAssociation.put(name, appli);
				mOwner.dispose();
			}
		}

		private void btn_Exit() {
			mOwner.dispose();
		}

	}

	private IDialog mOwnerFrame;
	private GUIContainer mOwnerContainer;
	private StringDico mExtAssociation;

	private GUIContainer scr_pnl;
	private GUIGrid cmp_tbl;

	private GUIButton btn_Add;
	private GUIButton btn_Mod;
	private GUIButton btn_Del;

	public AssociationPanel(IDialog aOwnerFrame) {
		super();
		mOwnerFrame=aOwnerFrame;
	}
	
	public void fillContainer(GUIContainer aOwnerContainer){
		mOwnerContainer = aOwnerContainer;
		Init();
		InitGridBtn();
		Setup();
	}

	public StringDico getExtAssociation() {
		return mExtAssociation;
	}

	public void Init() {
		scr_pnl = mOwnerContainer.createContainer(ContainerType.CT_SCROLL, 0, 0, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		cmp_tbl = scr_pnl.createGrid(0, 0, 1, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		cmp_tbl.setMultiSelection(false);
		cmp_tbl.addSelectListener(this);
	}

	public void InitGridBtn() {
		GUIContainer pnl_grid_btn = mOwnerContainer.createContainer(ContainerType.CT_NORMAL, 1, 0, 1,1, 
				ReSizeMode.RSM_NONE, FillMode.FM_BOTH);

		btn_Mod = pnl_grid_btn.createButton(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Mod.setMnemonic('m');
		btn_Mod.setTextString("Modifier");
		btn_Mod.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_ModActionPerformed();
			}
		});

		btn_Add = pnl_grid_btn.createButton(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Add.setMnemonic('a');
		btn_Add.setTextString("Ajouter");
		btn_Add.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_AddActionPerformed();
			}
		});

		btn_Del = pnl_grid_btn.createButton(0, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
		btn_Del.setMnemonic('s');
		btn_Del.setTextString("Supprimer");
		btn_Del.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DelActionPerformed();
			}
		});
		pnl_grid_btn.createContainer(ContainerType.CT_NORMAL,0, 3, 2, 1, 
				ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
	}

	public void selectionChanged() {
		boolean select = (cmp_tbl.getSelectedRowCount() != 0);
		btn_Del.setEnabled(select);
		btn_Mod.setEnabled(select);
	}

	protected void btn_ModActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		IDialog new_dialog=mOwnerFrame.createDialog();
		AssociationEditor edt = new AssociationEditor((String) getValueAt(row, 0), (String) getValueAt(row, 1));
		new_dialog.setDialogVisitor(edt);
		new_dialog.setVisible(true);
		cmp_tbl.refreshData();
		cmp_tbl.setRowSelection(row);
		selectionChanged();
	}

	protected void btn_DelActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		mExtAssociation.remove(getKey(row));
		cmp_tbl.refreshData();
		cmp_tbl.setRowSelection(row);
		selectionChanged();
	}

	protected void btn_AddActionPerformed() {
		IDialog new_dialog=mOwnerFrame.createDialog();
		AssociationEditor edt = new AssociationEditor(null,null);
		new_dialog.setDialogVisitor(edt);
		new_dialog.setVisible(true);
		cmp_tbl.refreshData();
		cmp_tbl.setRowSelection(mExtAssociation.size()-1);
		selectionChanged();
	}

	public void Setup() {
		mExtAssociation = Singletons.LucteriosSettingFile
				.getValuesSection(DesktopInterface.ASSOCIATION_SECTION);
		cmp_tbl.setGridInterface(this);
		GUIButton[] btns_1 = { btn_Mod, btn_Del, btn_Add };
		mOwnerContainer.calculBtnSize(btns_1);
	}

	public void Save() throws IOException {
		Singletons.LucteriosSettingFile.clearSection(DesktopInterface.ASSOCIATION_SECTION);
		for (int idx = 0; idx < getRowCount(); idx++) {
			Singletons.LucteriosSettingFile.setValueSection(
					DesktopInterface.ASSOCIATION_SECTION, getKey(idx),
					(String) getValueAt(idx, 1));
		}
		Singletons.LucteriosSettingFile.save();
	}

	public Class<?> getColumnClass(int columnIndex) {
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
		Object[] keys = mExtAssociation.keySet().toArray();
		return (String) keys[rowIndex];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String current_key = getKey(rowIndex);
		switch (columnIndex) {
		case 0:
			return "*." + current_key;
		case 1:
			return mExtAssociation.get(current_key);
		default:
			return "??";
		}
	}

}
