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
import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.gui.GUIParam.FillMode;

public class CmpTextform extends CmpAbstractEvent  {
	private static final long serialVersionUID = 1L;

	private GUIHyperMemo cmp_text;
	
	public CmpTextform() {
		super();
		mFill = FillMode.FM_BOTH;
		setWeightx(1.0);
		setWeighty(1.0);
		
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
		String out_text = cmp_text.save();
		tree_map.put(getName(), out_text);
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (cmp_text.save().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocusGUI();
	}

	protected void initComponent() {
		cmp_text = mPanel.createHyperMemo(mParam);
		cmp_text.load("");
	}

	protected void refreshComponent() {
		super.refreshComponent();
		String in_text = getXmlItem().getText().trim();
		cmp_text.load(in_text);
		cmp_text.addFocusListener(this);
	}

	protected boolean hasChanged() {
		return !cmp_text.save().equals(getXmlItem().getText().trim());
	}
	
}
