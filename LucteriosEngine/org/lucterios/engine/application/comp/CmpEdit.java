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

import java.util.regex.Pattern;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIEdit;
import org.lucterios.ui.GUIActionListener;

public class CmpEdit extends CmpAbstractEvent implements GUIActionListener {
	private static final long serialVersionUID = 1L;
	private GUIEdit cmp_text;
	private String m_RegularExpression = "";
	private int m_StringSize = 0;

	public CmpEdit() {
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
		tree_map.put(getName(), cmp_text.getTextString());
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (cmp_text.getTextString().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocusGUI();
	}

	protected void initComponent() {
		cmp_text = mPanel.createEdit(mParam);
		cmp_text.setTextString("");
		cmp_text.addActionListener(this);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		m_RegularExpression = getXmlItem().getCDataOfFirstTag("REG_EXPR");
		m_StringSize = getXmlItem().getAttributInt("stringSize", 0);
		cmp_text.setTextString(getXmlItem().getText().trim());
		int dim = cmp_text.getColumns();
		cmp_text.setColumns(Math.max(15, dim));
		dim = cmp_text.getColumns();
		cmp_text.addFocusListener(this);
	}

	protected boolean hasChanged() {
		return !cmp_text.getTextString().equals(getXmlItem().getText().trim());
	}

	public void actionPerformed() {
		String current_text = cmp_text.getTextString();		
		boolean accept = true;
		if (m_RegularExpression.length() > 0) {
			Pattern pat = Pattern.compile(m_RegularExpression);
			accept = pat.matcher(current_text).matches();
		}
		if (accept && (m_StringSize > 0))
			accept = (current_text.length() <= m_StringSize);
		if (!accept) {
			int car[] = cmp_text.getCaretPositions();
			current_text = current_text.substring(0, Math.min(car[0], car[1]))
					+ current_text.substring(Math.max(car[0], car[1]));
			cmp_text.setTextString(current_text);
			cmp_text.setCaretPosition(car[1]);
			cmp_text.setSelectionStart(car[1]);
			cmp_text.setSelectionEnd(car[1]);
		}
	}
}
