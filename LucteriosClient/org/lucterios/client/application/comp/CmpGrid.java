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

package org.lucterios.client.application.comp;

import java.util.*;

import java.awt.event.*;
import java.awt.*;

import javax.swing.Icon;

import org.lucterios.client.application.Action;
import org.lucterios.client.application.ActionConstantes;
import org.lucterios.client.application.ActionImpl;
import org.lucterios.client.application.Button;
import org.lucterios.client.application.comp.CmpTableModel.GridRow;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

import sbrunner.gui.tableView.TableView;
import sbrunner.gui.tableView.TableView.IRowSelectCaller;

public class CmpGrid extends Cmponent implements IRowSelectCaller,
		java.awt.event.MouseListener, javax.swing.event.ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private TableView cmp_tbl;
	private CmpTableModel cmp_tbl_Model = null;
	private javax.swing.JPanel pnl_Btn;
	private javax.swing.JPanel pnl_Grid;
	private javax.swing.JPanel pnl_Pages;
	private javax.swing.JScrollPane scr_pnl;
	private javax.swing.JComboBox cmp_Pages;

	private int mSelectMode = ActionConstantes.SELECT_NONE;
	private int mPageMax=0;
	private int mPageNum=0;
	private Action mPageRefreshAction = null;
	private Action[] mActions = null;

	public CmpGrid() {
		super();
		mFill = GridBagConstraints.BOTH;
		mWeightx = 1.0;
		mWeighty = 1.0;
	}

	public void close() {
		cmp_tbl = null;
		cmp_tbl_Model.close();
		cmp_tbl_Model = null;
		pnl_Btn = null;
		pnl_Grid = null;
		pnl_Pages = null;
		scr_pnl = null;
		cmp_Pages = null;
		super.close();
	}
	
	public void requestFocus() {
		cmp_tbl.requestFocus();
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
		if (cmp_Pages!=null)
			tree_map.put("GRID_PAGE%"+getName(),new Integer(cmp_Pages.getSelectedIndex()));
		return tree_map;
	}

	private MapContext getRequeteForSelectRow(String[] id_list, Action current_action) {
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
		for (int idx = 0; idx < cmp_tbl.getSelectedRowCount(); idx++) {
			GridRow select_row = (GridRow) (cmp_tbl.getSelectedRowObjects()
					.get(idx));
			id_list[idx] = select_row.GetId();
		}
		return id_list;
	}

	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		selectChange();
	}

	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 2) && (cmp_tbl.getSelectedRowCount() > 0)) {
			Action first_action = null;
			for (int act_idx = 0; (first_action == null)
					&& (act_idx < mActions.length); act_idx++) {
				if (mActions[act_idx].getSelect() != ActionConstantes.SELECT_NONE)
					first_action = mActions[act_idx];
			}
			if (first_action != null)
				first_action.actionPerformed(null);
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	private LabelFormRenderer mLabelForm = new LabelFormRenderer();
	private ImageFormRenderer mImageForm = new ImageFormRenderer();

	private void initTableView() {
		pnl_Grid.removeAll();
		cmp_tbl = new TableView();
		cmp_tbl.setVisibleMenuSave(false);
		cmp_tbl.setVisibleAutoResizeColumn(false);
		cmp_tbl.setRowSelectCaller(this);
		cmp_tbl.setFocusable(true);
		cmp_tbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		cmp_tbl.setName("cmp_tbl");
		cmp_tbl.setRowSelectionAllowed(true);
		cmp_tbl.setDefaultRenderer(String.class, mLabelForm);
		cmp_tbl.setDefaultRenderer(Icon.class, mImageForm);
		javax.swing.ListSelectionModel rowSM = cmp_tbl.getSelectionModel();
		rowSM.addListSelectionListener(this);
		cmp_tbl.addMouseListener(this);
		cmp_tbl_Model = new CmpTableModel();

		pnl_Grid.add(cmp_tbl.getTableHeader(), java.awt.BorderLayout.PAGE_START);
		pnl_Grid.add(cmp_tbl, java.awt.BorderLayout.CENTER);
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		pnl_Grid = new javax.swing.JPanel();
		pnl_Grid.setOpaque(this.isOpaque());
		pnl_Grid.setLayout(new java.awt.BorderLayout());
		initTableView();
		scr_pnl = new javax.swing.JScrollPane(pnl_Grid);
		add(scr_pnl, java.awt.BorderLayout.CENTER);

		pnl_Btn = new javax.swing.JPanel();
		pnl_Btn.setOpaque(this.isOpaque());
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setLayout(new GridBagLayout());
		add(pnl_Btn, java.awt.BorderLayout.EAST);

		pnl_Pages=new javax.swing.JPanel();
		pnl_Pages.setOpaque(this.isOpaque());
		pnl_Pages.setName("pnl_Pages");
		pnl_Pages.setLayout(new GridBagLayout());
		add(pnl_Pages, java.awt.BorderLayout.NORTH);

		pnl_Btn.setFocusable(false);
		pnl_Grid.setFocusable(false);
		scr_pnl.setFocusable(false);
	}
	
	public void selectChange() {
		int nb_select = cmp_tbl.getSelectedRowCount();
		for (int btn_idx = 0; btn_idx < pnl_Btn.getComponentCount(); btn_idx++) {
			if (Button.class.isInstance(pnl_Btn.getComponent(btn_idx))) {
				Button btn = (Button) pnl_Btn.getComponent(btn_idx);
				switch (btn.getCurrentAction().getSelect()) {
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
			selectChange();
		else
			for (int btn_idx = 0; btn_idx < pnl_Btn.getComponentCount(); btn_idx++)
				if (Button.class.isInstance(pnl_Btn.getComponent(btn_idx))) {
					Button btn = (Button) pnl_Btn.getComponent(btn_idx);
					btn.setEnabled(false);
				}
	}

	protected void refreshComponent() {
		mPageRefreshAction = new ActionImpl();
		mPageRefreshAction.initialize(getObsCustom(),Singletons.Factory(), "",getObsCustom().getSourceExtension(), getObsCustom().getSourceAction());
		mPageRefreshAction.setFormType(ActionConstantes.FORM_REFRESH);
		mPageRefreshAction.setClose(false);
		mPageRefreshAction.setSelect(ActionConstantes.SELECT_NONE);
		
		action();
		initBtn();
		switch (mSelectMode) {
		case ActionConstantes.SELECT_NONE:
			cmp_tbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			break;
		case ActionConstantes.SELECT_SINGLE:
			cmp_tbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			break;
		case ActionConstantes.SELECT_MULTI:
			cmp_tbl.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			break;
		default:
			assert false;
			break;
		}
		scr_pnl.setPreferredSize(new Dimension(1,1));
	}

	public void initialize() throws LucteriosException {
		action();
		cmp_tbl_Model.setText(getXmlItem());
		cmp_tbl.setModel(cmp_tbl_Model);
		selectChange();
		cmp_tbl.setAutoResizeMode(TableView.AUTO_RESIZE_OFF);
		cmp_tbl.idealColumnSize();
	}
	
	public void fillActions(ArrayList<Action> atns) {
		for (int btn_idx = 0; btn_idx < pnl_Btn.getComponentCount(); btn_idx++)
			if (Button.class.isInstance(pnl_Btn.getComponent(btn_idx)))
				atns.add(((Button) pnl_Btn.getComponent(btn_idx)).mAction);
	}

	public void initBtn() {
		pnl_Btn.removeAll();
		SimpleParsing xml_item = getXmlItem().getFirstSubTag("ACTIONS");
		int nb = Button.fillPanelByButton(pnl_Btn, this.getObsCustom(), Singletons
				.Factory(), xml_item, false);
		mActions = new Action[nb];

		mSelectMode = ActionConstantes.SELECT_NONE;
		int action_idx = 0;
		for (int btn_idx = 0; btn_idx < pnl_Btn.getComponentCount(); btn_idx++)
			if (Button.class.isInstance(pnl_Btn.getComponent(btn_idx))) {
				Button btn = (Button) pnl_Btn.getComponent(btn_idx);
				mSelectMode = ActionConstantes.getMaxSelect(mSelectMode,
						btn.mAction.getSelect());
				btn.mAction.setCheckNull(false);
				mActions[action_idx++] = btn.mAction;
			}
		mPageMax=getXmlItem().getAttributInt("PageMax", 0);
		mPageNum=getXmlItem().getAttributInt("PageNum", 0);
		pnl_Pages.removeAll();
		if (mPageMax>1) {
			pnl_Pages.setVisible(nb==0);
			String[] values=new String[mPageMax];
			for(int idx=0;idx<mPageMax;idx++)
				values[idx]="Page N°"+(idx+1);
			cmp_Pages=new javax.swing.JComboBox(values);
			cmp_Pages.setSelectedIndex(mPageNum);
			cmp_Pages.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					mPageRefreshAction.actionPerformed(null);
				}
			});

			GridBagConstraints cst = new GridBagConstraints();
			cst.insets = new Insets(2, 5, 2, 5);
			if (nb>0) {
				cst.fill = GridBagConstraints.BOTH;
				cst.anchor = GridBagConstraints.CENTER;
				pnl_Btn.add(cmp_Pages, cst);
			}
			else {
				cmp_Pages.setPreferredSize(new Dimension(50,20));
				cst.fill = GridBagConstraints.NONE;
				cst.anchor = GridBagConstraints.EAST;
				pnl_Pages.add(cmp_Pages, cst);
			}
		}
		else {
			cmp_Pages=null;
			pnl_Pages.setVisible(false);
		}
	}

	public void action() {
		mLabelForm.clearTag();
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
