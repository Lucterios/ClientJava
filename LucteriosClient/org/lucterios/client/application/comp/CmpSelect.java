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

import java.awt.*;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
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
	class ItemList extends ArrayList<ItemObj> implements ComboBoxModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Object m_selected = null;

		public Object getSelectedItem() {
			return m_selected;
		}

		public void setSelectedItem(Object anItem) {
			m_selected = anItem;
		}

		public void addListDataListener(ListDataListener l) {
		}

		public Object getElementAt(int index) {
			if ((index >= 0) && (index < size()))
				return this.get(index);
			else
				return null;
		}

		public int getSize() {
			return this.size();
		}

		public void removeListDataListener(ListDataListener l) {
		}

	}

	private static final long serialVersionUID = 1L;
	private javax.swing.JComboBox cmp_cnbb = null;
	private DefaultComboBoxModel m_comboModel;

	public CmpSelect() {
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		m_comboModel = new DefaultComboBoxModel();
		m_comboModel.removeAllElements();
	}

	public void close() {
		if (m_comboModel!=null)
			m_comboModel.removeAllElements();
		m_comboModel=null;
		cmp_cnbb = null;
		super.close();
	}
	
	public void requestFocus() {
		cmp_cnbb.requestFocus();
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
		setLayout(new java.awt.BorderLayout());
		cmp_cnbb = new javax.swing.JComboBox();
		cmp_cnbb.setName("cmp_cnbb");
		cmp_cnbb.setModel(m_comboModel);
		add(cmp_cnbb, java.awt.BorderLayout.CENTER);
	}

	private ItemObj InitialItem = null;

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		cmp_cnbb.removeActionListener(this);
		cmp_cnbb.removeFocusListener(this);
		ItemObj sel_item = null;
		SimpleParsing[] xml_items;
		xml_items = getXmlItem().getSubTag("CASE");
		m_comboModel.removeAllElements();
		try {
			String id = getXmlItem().getText().trim();
			String tmp = "[";
			xml_items = getXmlItem().getSubTag("CASE");
			for (int case_idx = 0; case_idx < xml_items.length; case_idx++) {
				SimpleParsing case_item = xml_items[case_idx];
				ItemObj item_obj = new ItemObj(case_item.getText(), case_item
						.getAttribut("id"));
				if (item_obj.mID.equals(id))
					sel_item = item_obj;
				m_comboModel.addElement(item_obj);
				tmp = tmp + " id=" + item_obj.mID + " val=" + item_obj.mText;
			}
			tmp = tmp + "]";
		} catch (Exception e) {
			throw new LucteriosException("Erreur de selection", e);
		}
		cmp_cnbb.setModel(m_comboModel);
		if (m_comboModel.getSize() == 0)
			cmp_cnbb.setSelectedIndex(-1);
		else
			cmp_cnbb.setSelectedIndex(0);
		if (sel_item != null) {
			cmp_cnbb.setSelectedItem(sel_item);
			InitialItem = (ItemObj) cmp_cnbb.getSelectedItem();
		}
		cmp_cnbb.addActionListener(this);
		cmp_cnbb.addFocusListener(this);
	}

	protected boolean hasChanged() {
		ItemObj sel_item = (ItemObj) cmp_cnbb.getSelectedItem();
		if (sel_item!=null)
			return !InitialItem.mID.equals( sel_item.mID );
		else
			return false;
	}
}
