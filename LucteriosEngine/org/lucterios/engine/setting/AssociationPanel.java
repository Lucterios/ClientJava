package org.lucterios.engine.setting;

import java.io.IOException;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.StringDico;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GridInterface;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.gui.GUIDialog.DialogVisitor;

public class AssociationPanel implements GUISelectListener,GridInterface {

	private static final long serialVersionUID = 1L;

	class AssociationEditor implements DialogVisitor  {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private GUIDialog mOwner;
		
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

		public void execute(GUIDialog aOwner) {
			mOwner=aOwner;
			Initial();
			if ((mExtName!=null) && (mAppliName!=null)) { 
				mOwner.setTextTitle("Modifier");
				txt_name.setTextString(mExtName);
				txt_name.setEnabled(false);
				txt_application.setTextString(mAppliName);
			}
		}
		
		private void Initial() {
			mOwner.setTextTitle("Ajouter");
			Init();
			InitBtn();
			mOwner.setResizable(false);
		}

		public void InitBtn() {
			pnl_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 1));

			btn_AddNew = pnl_btn.createButton(new GUIParam(0,0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
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
			GUIButton[] btns = { btn_AddNew, btn_ExitNew };
			mOwner.getContainer().calculBtnSize(btns);
			mOwner.setDefaultButton(btn_AddNew);
		}

		public void Init() {
			pnl_new_btn = mOwner.getContainer().createContainer(ContainerType.CT_NORMAL, new GUIParam(0, 0));

			lbl_name = pnl_new_btn.createLabel(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_name.setTextString("Extension");
			lbl_name.setStyle(1);
			
			txt_name = pnl_new_btn.createEdit(new GUIParam(1, 0, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));

			lbl_application = pnl_new_btn.createLabel(new GUIParam(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_BOTH));
			lbl_application.setTextString("Application associée");
			lbl_application.setStyle(1);

			txt_application = pnl_new_btn.createEdit(new GUIParam(1,1, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
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

		public void closing() { }

	}

	private GUIDialog mOwnerFrame;
	private GUIContainer mOwnerContainer;
	private StringDico mExtAssociation;

	private GUIContainer scr_pnl;
	private GUIGrid cmp_tbl;

	private GUIButton btn_Add;
	private GUIButton btn_Mod;
	private GUIButton btn_Del;

	public AssociationPanel(GUIDialog aOwnerFrame) {
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
		scr_pnl = mOwnerContainer.createContainer(ContainerType.CT_SCROLL, new GUIParam(0, 0));
		cmp_tbl = scr_pnl.createGrid(new GUIParam(0, 0,1,1,ReSizeMode.RSM_BOTH, FillMode.FM_BOTH, 400, 150));
		cmp_tbl.setMultiSelection(false);
		cmp_tbl.addSelectListener(this);
	}

	public void InitGridBtn() {
		GUIContainer pnl_grid_btn = mOwnerContainer.createContainer(ContainerType.CT_NORMAL, new GUIParam(1, 0, 1,1, 
				ReSizeMode.RSM_NONE, FillMode.FM_BOTH));

		btn_Mod = pnl_grid_btn.createButton(new GUIParam(0, 0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Mod.setMnemonic('m');
		btn_Mod.setTextString("Modifier");
		btn_Mod.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_ModActionPerformed();
			}
		});

		btn_Add = pnl_grid_btn.createButton(new GUIParam(0, 1, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Add.setMnemonic('a');
		btn_Add.setTextString("Ajouter");
		btn_Add.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_AddActionPerformed();
			}
		});

		btn_Del = pnl_grid_btn.createButton(new GUIParam(0, 2, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		btn_Del.setMnemonic('s');
		btn_Del.setTextString("Supprimer");
		btn_Del.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				btn_DelActionPerformed();
			}
		});
		pnl_grid_btn.createContainer(ContainerType.CT_NORMAL,new GUIParam(0, 3, 2, 1));
	}

	public void selectionChanged() {
		boolean select = (cmp_tbl.getSelectedRowCount() != 0);
		btn_Del.setEnabled(select);
		btn_Mod.setEnabled(select);
	}

	protected void btn_ModActionPerformed() {
		int row = cmp_tbl.getSelectedRows()[0];
		GUIDialog new_dialog=mOwnerFrame.createDialog();
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
		GUIDialog new_dialog=mOwnerFrame.createDialog();
		AssociationEditor edt = new AssociationEditor(null,null);
		new_dialog.setDialogVisitor(edt);
		new_dialog.setVisible(true);
		cmp_tbl.refreshData();
		cmp_tbl.setRowSelection(mExtAssociation.size()-1);
		selectionChanged();
	}

	public void Setup() {
		mExtAssociation = Singletons.getLucteriosSettingFile()
				.getValuesSection(DesktopInterface.ASSOCIATION_SECTION);
		cmp_tbl.setGridInterface(this);
		GUIButton[] btns_1 = { btn_Mod, btn_Del, btn_Add };
		mOwnerContainer.calculBtnSize(btns_1);
		selectionChanged();
	}

	public void Save() throws IOException {
		Singletons.getLucteriosSettingFile().clearSection(DesktopInterface.ASSOCIATION_SECTION);
		for (int idx = 0; idx < getRowCount(); idx++) {
			Singletons.getLucteriosSettingFile().setValueSection(
					DesktopInterface.ASSOCIATION_SECTION, getKey(idx),
					(String) getValueAt(idx, 1));
		}
		Singletons.getLucteriosSettingFile().save();
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
