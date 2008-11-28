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

import org.lucterios.utils.LucteriosException;

public class CmpPasswd extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;
	private javax.swing.JPasswordField cmp_text;

	public CmpPasswd() {
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		mWeightx = 1.0;
	}

	public void requestFocus() {
		cmp_text.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_text.setEnabled(aEnabled);
	}

	public Map getRequete(String aActionIdent) {
		TreeMap tree_map = new TreeMap();
		tree_map.put(getName(), new String(cmp_text.getPassword()));
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new javax.swing.JPasswordField();
		cmp_text.setText("");
		cmp_text.setName("cmp_text");
		cmp_text.addFocusListener(this);
		add(cmp_text, java.awt.BorderLayout.CENTER);
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		cmp_text.setText(mXmlItem.getText().trim());
		int dim = cmp_text.getColumns();
		cmp_text.setColumns(Math.max(15, dim));
		dim = cmp_text.getColumns();
	}

	protected boolean hasChanged() {
		return !cmp_text.getPassword().equals(mXmlItem.getText().trim());
	}
}
