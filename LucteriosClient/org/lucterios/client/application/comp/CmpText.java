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

import java.awt.*;

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;

public class CmpText extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;
	private javax.swing.JTextField cmp_text;

	public CmpText() {
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

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		tree_map.put(getName(), cmp_text.getText());
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (cmp_text.getText().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocus();
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new javax.swing.JTextField();
		cmp_text.setEditable(true);
		// cmp_text.setFont(new java.awt.Font("Dialog", 0, 10));
		cmp_text.setText("");
		cmp_text.setFocusable(true);
		cmp_text.setName("cmp_text");
		cmp_text.addFocusListener(this);
		add(cmp_text, java.awt.BorderLayout.CENTER);
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		cmp_text.setText(getXmlItem().getText().trim());
		int dim = cmp_text.getColumns();
		cmp_text.setColumns(Math.max(25, dim));
		dim = cmp_text.getColumns();
	}

	protected boolean hasChanged() {
		return !cmp_text.getText().equals(getXmlItem().getText().trim());
	}
}
