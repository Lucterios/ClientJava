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
import org.lucterios.gui.GUIEdit;

public class CmpPasswd extends CmpAbstractEvent {
	private GUIEdit cmp_text;

	public CmpPasswd() {
		super();
		setWeightx(1.0);
	}

	public void requestFocus() {
		cmp_text.requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_text.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		tree_map.put(getName(), new String(cmp_text.getTextString()));
		return tree_map;
	}

	protected void initComponent() {
		cmp_text = mPanel.createEdit(mParam);
		cmp_text.setTextString("");
		cmp_text.setPassword('*');
		cmp_text.addFocusListener(this);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		cmp_text.setTextString(getXmlItem().getText().trim());
		int dim = cmp_text.getColumns();
		cmp_text.setColumns(Math.max(15, dim));
		dim = cmp_text.getColumns();
	}

	protected boolean hasChanged() {
		return !cmp_text.getTextString().equals(getXmlItem().getText().trim());
	}
}
