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

package org.lucterios.engine.application.comp;

import java.util.*;

import org.lucterios.engine.application.comp.CmpFastTableModel.GridRow;
import org.lucterios.engine.gui.GraphicTool;
import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.SimpleParsing;

public class CmpFastGrid extends Cmponent 
		implements GUIActionListener,GUISelectListener {
	private GUIGrid cmp_tbl;
	private CmpFastTableModel cmp_tbl_Model = null;
	private GUIContainer pnl_Btn;
	private GUIContainer pnl_Grid;
	private GUIContainer pnl_Pages;
	private GUICombo cmp_Pages;

	private int mSelectMode = ActionConstantes.SELECT_NONE;
	private int mPageMax = 0;
	private int mPageNum = 0;
	private Action mPageRefreshAction = null;
	private Action[] mActions = null;

	public CmpFastGrid() {
		super();
		mFill = FillMode.FM_BOTH;
		VMin=100;
		HMin=100;
		setWeightx(1.0);
		setWeighty(1.0);
	}

	public void close() {
		cmp_tbl = null;
		cmp_tbl_Model.close();
		cmp_tbl_Model = null;
		pnl_Btn = null;
		pnl_Grid = null;
		pnl_Pages = null;
		cmp_Pages = null;
		super.close();
	}

	public void requestFocus() {
		cmp_tbl.requestFocusGUI();
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		if ((mSelectMode != ActionConstantes.SELECT_NONE)
				&& (cmp_tbl.getSelectedRowCount() > 0)) {
			String[] id_list = getListIds();
			boolean grid_action = false;
			Action current_action = null;
			for (int act_idx = 0; !grid_action && (act_idx < mActions.length); act_idx++) {
				current_action = mActions[act_idx];
				grid_action = (grid_action || ((aActionIdent
						.equals(current_action.getID()) && (current_action
						.getSelect() != ActionConstantes.SELECT_NONE))));
			}
			if (grid_action && (current_action != null))
				tree_map = getRequeteForSelectRow(id_list, current_action);
		}
		if (cmp_Pages != null)
			tree_map.put("GRID_PAGE%" + getName(), new Integer(cmp_Pages
					.getSelectedIndex()));
		return tree_map;
	}

	private MapContext getRequeteForSelectRow(String[] id_list,
			Action current_action) {
		MapContext tree_map = new MapContext();
		switch (current_action.getSelect()) {
		case ActionConstantes.SELECT_SINGLE:
			tree_map.put(getName(), id_list[0]);
			break;
		case ActionConstantes.SELECT_MULTI:
			String ids = id_list[0];
			for (int idx = 1; idx < id_list.length; idx++)
				ids += ";" + id_list[idx];
			tree_map.put(getName(), ids);
			break;
		default:
			assert false;
			break;
		}
		return tree_map;
	}

	private String[] getListIds() {
		String[] id_list = new String[cmp_tbl.getSelectedRowCount()];
		int[] index_list = cmp_tbl.getSelectedRows();
		int idx = 0;
		for (int index : index_list) {
			GridRow select_row = cmp_tbl_Model.getRowObject(index);
			id_list[idx++] = select_row.GetId();
		}
		return id_list;
	}

	public void actionPerformed() {
			GUIAction first_action = null;
			for (int act_idx = 0; (first_action == null)
					&& (act_idx < mActions.length); act_idx++) {
				if ((mActions[act_idx]!=null) && (mActions[act_idx].getSelect() != ActionConstantes.SELECT_NONE))
					first_action = mActions[act_idx];
			}
			if (first_action != null)
				first_action.actionPerformed();
	}

	private void initTableView() {
		pnl_Grid.removeAll();
		cmp_tbl = pnl_Grid.createGrid(mParam);
		cmp_tbl.addActionListener(this);
		cmp_tbl.addSelectListener(this);
		cmp_tbl_Model = new CmpFastTableModel();
	}

	protected void initComponent() {
		pnl_Grid = mPanel.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,1));
		initTableView();
		pnl_Btn = mPanel.createContainer(ContainerType.CT_NORMAL,new GUIParam(1,1,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_NONE));
		pnl_Pages = mPanel.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,0,2,1));
	}

	public void selectionChanged() {
		int nb_select = cmp_tbl.getSelectedRowCount();
		for (int btn_idx = 0; btn_idx < pnl_Btn.count(); btn_idx++) {
			if (GUIButton.class.isInstance(pnl_Btn.get(btn_idx))) {
				GUIButton btn = (GUIButton) pnl_Btn.get(btn_idx);
				switch (((Action)btn.getObject()).getSelect()) {
				case ActionConstantes.SELECT_NONE:
					btn.setEnabled(true);
					break;
				case ActionConstantes.SELECT_SINGLE:
					btn.setEnabled(nb_select == 1);
					break;
				case ActionConstantes.SELECT_MULTI:
					btn.setEnabled(nb_select > 0);
					break;
				default:
					assert false;
					break;
				}
			}
		}
	}

	public void setEnabled(boolean aEnabled) {
		if (aEnabled)
			selectionChanged();
		else
			for (int btn_idx = 0; btn_idx < pnl_Btn.count(); btn_idx++)
				if (GUIButton.class.isInstance(pnl_Btn.get(btn_idx))) {
					GUIButton btn = (GUIButton) pnl_Btn.get(btn_idx);
					btn.setEnabled(false);
				}
	}

	protected void refreshComponent() {
		mPageRefreshAction = new ActionImpl();
		mPageRefreshAction.initialize(getObsCustom(), Singletons.Factory(), "",
				getObsCustom().getSourceExtension(), getObsCustom()
						.getSourceAction());
		mPageRefreshAction.setFormType(ActionConstantes.FORM_REFRESH);
		mPageRefreshAction.setClose(false);
		mPageRefreshAction.setSelect(ActionConstantes.SELECT_NONE);

		cmp_tbl.clearTable();
		initBtn();
		switch (mSelectMode) {
		case ActionConstantes.SELECT_NONE:
			cmp_tbl.setMultiSelection(false);
			break;
		case ActionConstantes.SELECT_SINGLE:
			cmp_tbl.setMultiSelection(false);
			break;
		case ActionConstantes.SELECT_MULTI:
			cmp_tbl.setMultiSelection(true);
			break;
		default:
			assert false;
			break;
		}
	}

	public void initialize() {
		cmp_tbl.clearTable();
		if (cmp_tbl_Model != null)
			cmp_tbl_Model.close();
		cmp_tbl_Model = null;

		cmp_tbl_Model = new CmpFastTableModel();
		cmp_tbl_Model.setText(getXmlItem());
		cmp_tbl.setGridInterface(cmp_tbl_Model);
		selectionChanged();
	}

	public void fillActions(ArrayList<Action> atns) {
		for (int btn_idx = 0; btn_idx < pnl_Btn.count(); btn_idx++)
			if (GUIButton.class.isInstance(pnl_Btn.get(btn_idx)))
				atns.add((Action)((GUIButton) pnl_Btn.get(btn_idx)).getObject());
	}

	public void initBtn() {
		pnl_Btn.removeAll();
		SimpleParsing xml_item = getXmlItem().getFirstSubTag("ACTIONS");
		GraphicTool.fillPanelByButton(pnl_Btn, this.getObsCustom(),
				Singletons.Factory(), xml_item, false);
		int nb=Math.max(0,pnl_Btn.count()-1);
		mActions = new Action[nb];

		mSelectMode = ActionConstantes.SELECT_NONE;
		int action_idx = 0;
		for (int btn_idx = 0; btn_idx < pnl_Btn.count(); btn_idx++)
			if (GUIButton.class.isInstance(pnl_Btn.get(btn_idx))) {
				GUIButton btn = (GUIButton) pnl_Btn.get(btn_idx);
				Action current_act=(Action)btn.getObject();
				mSelectMode = ActionConstantes.getMaxSelect(mSelectMode,current_act.getSelect());
				current_act.setCheckNull(false);
				mActions[action_idx++] = current_act;
			}
		mPageMax = getXmlItem().getAttributeInt("PageMax", 0);
		mPageNum = getXmlItem().getAttributeInt("PageNum", 0);
		pnl_Pages.removeAll();
		if (mPageMax > 1) {
			pnl_Pages.setVisible(nb == 0);
			String[] values = new String[mPageMax];
			for (int idx = 0; idx < mPageMax; idx++)
				values[idx] = "Page N°" + (idx + 1);
			GUIParam param=new GUIParam(0,0);
			param.setPad(3);
			param.setReSize(ReSizeMode.RSM_NONE);
			if (nb > 0) {
				cmp_Pages = pnl_Btn.createCombo(param);
			}
			else{
				param.setFill(FillMode.FM_NONE);
				cmp_Pages = pnl_Pages.createCombo(param);
			}
			cmp_Pages.addList(values);
			cmp_Pages.setSelectedIndex(mPageNum);
			cmp_Pages.addActionListener(new GUIActionListener() {
				public void actionPerformed() {
					mPageRefreshAction.actionPerformed();
				}
			});
		} else {
			cmp_Pages = null;
			pnl_Pages.setVisible(false);
		}
	}

	/*
	 * <HEADER name='val1'> Premier champ </HEADER> <HEADER name='val2'
	 * type='int'> deuxieme champ </HEADER> <HEADER name='val3' type='float'>
	 * troisieme champ </HEADER> <HEADER name='val4' type='bool'> quatrieme
	 * champ </HEADER> <RECORD id='2'> <VALUE name='val1'> aaaz </VALUE> <VALUE
	 * name='val2'> 459 </VALUE> <VALUE name='val3'> 0 </VALUE> <VALUE
	 * name='val4'> false </VALUE> </RECORD> <RECORD id='8'> <VALUE name='val1'>
	 * fgsdfg </VALUE> <VALUE name='val2'> 0 </VALUE> <VALUE name='val3'> 51.234
	 * </VALUE> <VALUE name='val4'> true </VALUE> </RECORD> <RECORD id='13'>
	 * <VALUE name='val1'> bsssf kmp </VALUE> <VALUE name='val2'> 13 </VALUE>
	 * <VALUE name='val3'> 0.1858 </VALUE> <VALUE name='val4'> </VALUE>
	 * </RECORD>
	 */
}
