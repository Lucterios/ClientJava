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

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUICombo;
import org.lucterios.utils.SimpleParsing;

public class CmpSelect extends CmpAbstractEvent {
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
	private GUICombo cmp_cnbb = null;

	public CmpSelect() {
		super();
		setWeightx(1.0);
	}

	public void close() {
		cmp_cnbb = null;
		super.close();
	}

	public void requestFocus() {
		cmp_cnbb.requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		if (cmp_cnbb != null)
			cmp_cnbb.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		if (cmp_cnbb != null) {
			ItemObj sel_item = (ItemObj) cmp_cnbb.getSelectedItem();
			if (sel_item != null)
				tree_map.put(getName(), sel_item.mID);
			else
				tree_map.put(getName(), "");
		}
		return tree_map;
	}

	protected void initComponent() {
		cmp_cnbb = mPanel.createCombo(mParam);
	}

	private ItemObj InitialItem = null;

	protected void refreshComponent() {
		super.refreshComponent();
		cmp_cnbb.removeActionListener(this);
		cmp_cnbb.removeFocusListener(this);
		int sel_item_idx = -1;
		SimpleParsing[] xml_items;
		xml_items = getXmlItem().getSubTag("CASE");
		cmp_cnbb.removeAllElements();
		String id = getXmlItem().getText().trim();
		String tmp = "[";
		xml_items = getXmlItem().getSubTag("CASE");
		int size=0;
		for (int case_idx = 0; case_idx < xml_items.length; case_idx++) {
			SimpleParsing case_item = xml_items[case_idx];
			ItemObj item_obj = new ItemObj(case_item.getText(), case_item
					.getAttribut("id"));
			if (item_obj.mID.equals(id))
				sel_item_idx = size;
			cmp_cnbb.addElement(item_obj);
			size++;
			tmp = tmp + " id=" + item_obj.mID + " val=" + item_obj.mText;
		}
		tmp = tmp + "]";
	
		if (size == 0)
			cmp_cnbb.setSelectedIndex(-1);
		else
			cmp_cnbb.setSelectedIndex(0);
		if (sel_item_idx != -1) {
			cmp_cnbb.setSelectedIndex(sel_item_idx);
			InitialItem = (ItemObj) cmp_cnbb.getSelectedItem();
		}
		cmp_cnbb.addActionListener(this);
		cmp_cnbb.addFocusListener(this);
	}

	protected boolean hasChanged() {
		ItemObj sel_item = (ItemObj) cmp_cnbb.getSelectedItem();
		if (sel_item != null)
			return !InitialItem.mID.equals(sel_item.mID);
		else
			return false;
	}
}
