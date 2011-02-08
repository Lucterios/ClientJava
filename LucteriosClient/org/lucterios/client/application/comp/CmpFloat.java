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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.FloatField;
import org.lucterios.graphic.SpinEdit;

public class CmpFloat extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;

	private FloatField cmp_float;

	private SpinEdit cmp_int;

	private boolean mIsInteger = false;

	public CmpFloat() {
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		mWeightx = 1.0;
	}

	public void requestFocus() {
		if (mIsInteger)
			getCmpInt().requestFocus();
		else
			getCmpFloat().requestFocus();
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
			tree_map.put(getName(), getCmpFloat().getText());
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new GridLayout());
		cmp_float = null;
		cmp_int = null;
	}

	protected FloatField getCmpFloat() {
		if (cmp_float == null) {
			removeAll();
			cmp_float = new FloatField();
			cmp_float.setName("cmp_float");
			cmp_float.setEditable(true);
			cmp_float.setText("");
			cmp_float.setFocusable(true);
			add(cmp_float);
		}
		return cmp_float;
	}

	protected SpinEdit getCmpInt() {
		if (cmp_int == null) {
			removeAll();
			cmp_int = new SpinEdit();
			cmp_int.setName("cmp_int");
			cmp_int.setPreferredSize(new Dimension(20, 20));
			add(cmp_int);
		}
		return cmp_int;
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		double min_val = getXmlItem()
				.getAttributDouble("min", Double.MIN_VALUE);
		double max_val = getXmlItem()
				.getAttributDouble("max", Double.MAX_VALUE);
		int prec_val = getXmlItem().getAttributInt("prec", 2);
		double val = getXmlItem().getCDataDouble(min_val);
		mIsInteger = (prec_val == 0);
		if (mIsInteger) {
			getCmpInt().init(Math.round(val), Math.round(min_val),
					Math.round(max_val));
			getCmpInt().addFocusListener(this);
		} else {
			getCmpFloat().setRange(min_val, max_val, prec_val);
			getCmpFloat().setValue(val);
			getCmpFloat().addFocusListener(this);
		}
	}

	protected boolean hasChanged() {
		String init_value = getXmlItem().getText();
		String current_value = getRequete("").get(getName()).toString();
		return !init_value.equals(current_value);
	}

}
