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
import org.lucterios.gui.GUISpinEdit;

public class CmpFloat extends CmpAbstractEvent {
	private GUIEdit cmp_float;

	private GUISpinEdit cmp_int;

	private boolean mIsInteger = false;

	public CmpFloat() {
		super();
		setWeightx(1.0);
		mParam.setPrefSizeX(20);
		mParam.setPrefSizeY(20);
	}

	public void requestFocus() {
		if (mIsInteger)
			getCmpInt().requestFocusGUI();
		else
			getCmpFloat().requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		if (mIsInteger)
			getCmpInt().setEnabled(aEnabled);
		else
			getCmpFloat().setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		if (mIsInteger)
			tree_map.put(getName(), new Long(getCmpInt().getNumber())
					.toString());
		else
			tree_map.put(getName(), getCmpFloat().getValue().toString());
		return tree_map;
	}

	protected void initComponent() {
		cmp_float = null;
		cmp_int = null;
	}

	protected GUIEdit getCmpFloat() {
		if (cmp_int != null) {
			cmp_int.setVisible(true);
		}
		if (cmp_float == null) {
			mPanel.removeAll();
			cmp_float = mPanel.createEdit(mParam);
			cmp_float.setTextString("");
			cmp_float.setVisible(true);
			cmp_float.addFocusListener(this);
			cmp_int=null;
		}
		return cmp_float;
	}

	protected GUISpinEdit getCmpInt() {
		if (cmp_float != null) {
			cmp_float.setVisible(true);
		}
		if (cmp_int == null) {
			mPanel.removeAll();
			cmp_int = mPanel.createSpinEdit(mParam);
			cmp_int.setVisible(true);
			cmp_int.addFocusListener(this);
			cmp_float=null;
		}
		return cmp_int;
	}

	protected void refreshComponent() {
		super.refreshComponent();
		double min_val = getXmlItem()
				.getAttributeDouble("min", Double.MIN_VALUE);
		double max_val = getXmlItem()
				.getAttributeDouble("max", Double.MAX_VALUE);
		int prec_val = getXmlItem().getAttributeInt("prec", 2);
		double val = getXmlItem().getCDataDouble(min_val);
		mIsInteger = (prec_val == 0);
		if (mIsInteger) {
			getCmpInt().init(Math.round(val), Math.round(min_val),
					Math.round(max_val));
		} else {
			getCmpFloat().setRange(min_val, max_val, prec_val);
			getCmpFloat().setFloatEditor(true);
			getCmpFloat().setValue(val);
		}
	}

	protected boolean hasChanged() {
		String init_value = getXmlItem().getText();
		String current_value = getRequete("").get(getName()).toString();
		return !init_value.equals(current_value);
	}

}
