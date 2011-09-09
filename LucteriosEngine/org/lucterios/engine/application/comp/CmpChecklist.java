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

import java.util.ArrayList;

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class CmpChecklist extends CmpAbstractEvent implements GUISelectListener {
	class ItemObj {
		public String mText;
		public String mID;

		public ItemObj(String aText, String aID) {
			mText = aText.trim();
			mID = aID.trim();
		}

		public String toString() {
			return mText;
		}
	}

	private static final long serialVersionUID = 1L;
	private GUICheckList cmp_list;
	private boolean mSimple = false;

	public CmpChecklist() {
		super();
		mFill=FillMode.FM_BOTH;
		setWeightx(1.0);
		setWeighty(1.0);
	}

	public void close() {
		cmp_list = null;
		super.close();
	}

	public void requestFocus() {
		cmp_list.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_list.setEnabled(aEnabled);
	}

	public String getValues() {
		String value = "";
		ItemObj item_obj;
		Object[] objs = cmp_list.getSelectedValues();
		for (int obj_idx = 0; obj_idx < objs.length; obj_idx++) {
			item_obj = (ItemObj) objs[obj_idx];
			if (value.length() > 0)
				value = value + ";";
			value = value + item_obj.mID;
		}
		return value;
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		tree_map.put(getName(), getValues());
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (getValues().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_list.requestFocus();
	}

	protected void initComponent() {
		mParam.setH(100);
		mParam.setW(30);
		mParam.setFill(FillMode.FM_BOTH);
		mParam.setReSize(ReSizeMode.RSM_BOTH);
		cmp_list = mPanel.createCheckList(mParam);
	}

	public void init(GUIContainer aOwnerPanel, Observer aObsCustom,
			SimpleParsing aXmlItem) {
		if (aXmlItem.getAttributInt("simple", 0) != 0) {
			mFill = FillMode.FM_BOTH;
			setWeighty(1.0);
		}
		super.init(aOwnerPanel, aObsCustom, aXmlItem);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		mSimple = (getXmlItem().getAttributInt("simple", 0) != 0);
		ArrayList<ItemObj> ItemObjs = new ArrayList<ItemObj>();
		int[] indices = null;
		cmp_list.clearList();
		try {
			SimpleParsing[] xml_items;

			xml_items = getXmlItem().getSubTag("CASE");

			int nb_selected = 0;
			boolean[] sel_itx = new boolean[xml_items.length];
			for (int case_idx = 0; case_idx < xml_items.length; case_idx++) {
				SimpleParsing xml_item = xml_items[case_idx];
				ItemObj item_obj = new ItemObj(xml_item.getText(), xml_item
						.getAttribut("id"));
				sel_itx[case_idx] = (xml_item.getAttributInt("checked", 0) != 0);
				if (sel_itx[case_idx])
					nb_selected++;
				ItemObjs.add(item_obj);
			}
			indices = new int[nb_selected];
			int nb = 0;
			for (int idx = 0; idx < sel_itx.length; idx++)
				if (sel_itx[idx]) {
					indices[nb] = idx;
					nb++;
				}
		} catch (Exception e) {
		}
		cmp_list.setListData(ItemObjs.toArray());
		if (mSimple) {
			cmp_list.setMultiSelection(false);
			cmp_list.addSelectListener(this);
		} else {
			cmp_list.setMultiSelection(true);
			cmp_list.addFocusListener(this);
		}
		if (indices != null)
			cmp_list.setSelectedIndices(indices);
	}

	public void selectionChanged() {
		try {
			runJavaScript();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
		if (mEnabled && (mEventAction != null) && hasChanged())
			mEventAction.actionPerformed();
	}

	protected boolean hasChanged() {
		return true;
	}

}
