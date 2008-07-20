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
import java.util.List;

import java.awt.event.*;
import java.awt.*;

import javax.swing.Icon;

import org.lucterios.client.application.Action;
import org.lucterios.client.application.ActionConstantes;
import org.lucterios.client.application.Button;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

import sbrunner.gui.tableView.AbstractTableViewModel;
import sbrunner.gui.tableView.TableView;
import sbrunner.gui.tableView.TableViewColumn;
import sbrunner.gui.tableView.TableView.IRowSelectCaller;

public class CmpGrid extends Cmponent implements IRowSelectCaller,
		java.awt.event.MouseListener, javax.swing.event.ListSelectionListener {
	private static final long serialVersionUID = 1L;

	class GridColomn implements TableViewColumn {
		public final static int TypeString = 0;
		public final static int TypeInt = 1;
		public final static int TypeFloat = 2;
		public final static int TypeBool = 3;
		public final static int TypeIcon = 4;

		public final static String TAGNAME = "HEADER";

		public GridColomn(SimpleParsing aXmlItem) {
			if (aXmlItem.getTagName().equalsIgnoreCase(TAGNAME)) {
				mHeaderId = aXmlItem.getAttribut("name");
				mName = aXmlItem.getCData();
				String type = aXmlItem.getAttribut("type");
				if (type == null)
					mHeaderType = TypeString;
				else if ("int".equals( type ))
					mHeaderType = TypeInt;
				else if ("float".equals( type ))
					mHeaderType = TypeFloat;
				else if ("bool".equals( type ))
					mHeaderType = TypeBool;
				else if ("icon".equals( type ))
					mHeaderType = TypeIcon;
				else
					mHeaderType = TypeString;
			} else
				mHeaderId = "";
		}

		public Comparator getComparator() {
			return new Comparator() {
				public int compare(Object o1, Object o2) {
					GridRow row1 = (GridRow) o1;
					GridRow row2 = (GridRow) o2;
					String val1 = row1.GetCell(mHeaderId);
					String val2 = row2.GetCell(mHeaderId);
					return val1.compareTo(val2);
				}
			};
		}

		private String mHeaderId = "";

		public String getHeaderId() {
			return mHeaderId;
		}

		private String mName = "";

		public String getName() {
			return mName;
		}

		public int mHeaderType = 0;

		public Class getColumnClass() {
			switch (mHeaderType) {
			case TypeInt:
				return Integer.class;
			case TypeFloat:
				return Float.class;
			case TypeBool:
				return Boolean.class;
			case TypeIcon:
				return Icon.class;
			default:
				return String.class;
			}
		}

		public boolean isCellEditable(Object arg0) {
			return false;
		}

		public Object getValue(Object row) {
			String valuetxt = ((GridRow) row).GetCell(mHeaderId);
			try {
				switch (mHeaderType) {
				case TypeInt:
					return new Integer(valuetxt);
				case TypeFloat:
					return new Float(valuetxt);
				case TypeBool:
					return new Boolean(valuetxt.toLowerCase().equalsIgnoreCase(
							"oui"));
				case TypeIcon:
					Icon new_icon;
					new_icon = (Icon) Singletons.Transport().getIcon(valuetxt);
					return new_icon;
				default:
					return valuetxt;
				}
			} catch (Exception e) {
				return getDefaultValue();
			}
		}

		private Object getDefaultValue() {
			switch (mHeaderType) {
			case TypeInt:
				return new String("?");
			case TypeFloat:
				return new String("??");
			case TypeBool:
				return new String("!");
			case TypeIcon:
				return new String("***");
			default:
				return new String("!!");
			}
		}

		public void setValue(Object arg0, Object arg1) {
		}

		public boolean isSortable() {
			return true;
		}

		public boolean isSearchable() {
			return true;
		}

		public boolean isDefaultVisible() {
			return true;
		}

	}

	class GridRow {
		public final static String TAGNAME = "RECORD";

		public GridRow(SimpleParsing aXmlItem) {
			mCells.clear();
			if (aXmlItem.getTagName().equalsIgnoreCase(TAGNAME)) {
				mId = aXmlItem.getAttribut("id");

				SimpleParsing[] xml_items = aXmlItem.getSubTag("VALUE");
				for (int val_idx = 0; val_idx < xml_items.length; val_idx++) {
					String field_name = xml_items[val_idx].getAttribut("name");
					String value = xml_items[val_idx].getCData().trim();
					mCells.put(field_name, value);
				}
			} else
				mId = "-1";
		}

		private String mId;

		public String GetId() {
			return mId;
		}

		private TreeMap mCells = new TreeMap();

		public String GetCell(String colName) {
			if (mCells.containsKey(colName))
				return (String) mCells.get(colName);
			else
				return null;
		}
	}

	class CmpTableModel extends AbstractTableViewModel {
		private static final long serialVersionUID = 1L;

		private List mColumns = new ArrayList();
		private List mContent = new ArrayList();

		public void setText(SimpleParsing aXmlItem) {
			mColumns.clear();
			mContent.clear();
			SimpleParsing[] xml_items;

			xml_items = aXmlItem.getSubTag("HEADER");
			for (int col_idx = 0; col_idx < xml_items.length; col_idx++) {
				GridColomn item = new GridColomn(xml_items[col_idx]);
				mColumns.add(item);
			}

			xml_items = aXmlItem.getSubTag("RECORD");
			for (int row_idx = 0; row_idx < xml_items.length; row_idx++) {
				GridRow new_row = new GridRow(xml_items[row_idx]);
				mContent.add(new_row);
			}
		}

		public int getRowCount() {
			return mContent.size();
		}

		public TableViewColumn[] getColumns() {
			TableViewColumn[] result = new TableViewColumn[mColumns.size()];
			for (int colidx = 0; colidx < mColumns.size(); colidx++)
				result[colidx] = (TableViewColumn) mColumns.get(colidx);
			return result;
		}

		public Object getRowObject(int pRowIndex) {
			return mContent.get(pRowIndex);
		}
	}

	private TableView cmp_tbl;
	private CmpTableModel cmp_tbl_Model = null;
	private javax.swing.JPanel pnl_Btn;
	private javax.swing.JPanel pnl_Grid;
	private javax.swing.JScrollPane scr_pnl;

	private int mSelectMode = ActionConstantes.SELECT_NONE;
	private Action[] mActions = null;

	public CmpGrid() {
		super();
		mFill = GridBagConstraints.BOTH;
		mWeightx = 1.0;
		mWeighty = 1.0;
	}

	public void requestFocus() {
		cmp_tbl.requestFocus();
	}

	public Map getRequete(String aActionIdent) {
		Map tree_map = new TreeMap();
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
		return tree_map;
	}

	private Map getRequeteForSelectRow(String[] id_list, Action current_action) {
		TreeMap tree_map = new TreeMap();
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
				//TODO: Implement 'default' statement
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

		pnl_Grid
				.add(cmp_tbl.getTableHeader(), java.awt.BorderLayout.PAGE_START);
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
		//scr_pnl.setMinimumSize(new Dimension(HMin, VMin));
		//scr_pnl.setMaximumSize(new Dimension(HMax, VMax));

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
						//TODO: Implement 'default' statement
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
			//TODO: Implement 'default' statement
			break;
		}
		scr_pnl.setPreferredSize(new Dimension(1,1));
	}

	public void initialize() throws LucteriosException {
		action();
		cmp_tbl_Model.setText(mXmlItem);
		cmp_tbl.setModel(cmp_tbl_Model);
		selectChange();
		cmp_tbl.setAutoResizeMode(TableView.AUTO_RESIZE_OFF);
		cmp_tbl.idealColumnSize();
	}
	
	public void fillActions(Vector atns) {
		for (int btn_idx = 0; btn_idx < pnl_Btn.getComponentCount(); btn_idx++)
			if (Button.class.isInstance(pnl_Btn.getComponent(btn_idx)))
				atns.add(((Button) pnl_Btn.getComponent(btn_idx)).mAction);
	}

	public void initBtn() {
		pnl_Btn.removeAll();
		SimpleParsing xml_item = mXmlItem.getFirstSubTag("ACTIONS");
		int nb = Button.fillPanelByButton(pnl_Btn, this.mObsCustom, Singletons
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
