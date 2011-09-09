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
import org.lucterios.gui.GUICheckBox;

public class CmpCheck extends CmpAbstractEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUICheckBox cmp_Check;

	public CmpCheck() {
		super();
	}

	public void requestFocus() {
		cmp_Check.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_Check.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		if (cmp_Check.isSelected())
			tree_map.put(getName(), "o");
		else
			tree_map.put(getName(), "n");
		return tree_map;
	}

	protected void initComponent() {
		cmp_Check = mPanel.createCheckBox(mParam);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		String value = getXmlItem().getText().trim();
		cmp_Check.setTextString("");
		cmp_Check.setSelected("1".equals(value));
		cmp_Check.addFocusListener(this);
		cmp_Check.addActionListener(this);
	}

	protected boolean hasChanged() {
		String value = getXmlItem().getText().trim();
		return cmp_Check.isSelected() != "1".equals(value);
	}

}
