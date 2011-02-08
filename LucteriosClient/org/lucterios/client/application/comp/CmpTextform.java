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

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.graphic.LucteriosEditor;
import org.lucterios.graphic.PopupListener;

public class CmpTextform extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;
	private LucteriosEditor cmp_text;
	private PopupListener popupListener;

	public CmpTextform() {
		super();
		mFill = GridBagConstraints.BOTH;
		mWeightx = 1.0;
		mWeighty = 1.0;
	}

	public void close() {
		cmp_text.close();
		cmp_text = null;
		super.close();
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
		tree_map.put(getName(), cmp_text.save());
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new LucteriosEditor(false);
		cmp_text.setFocusable(false);
		cmp_text.setName("cmp_text");
		cmp_text.setBackground(this.getBackground());
		cmp_text.addFocusListener(this);
		cmp_text.addStandardPopupMenu(false);
		add(cmp_text, java.awt.BorderLayout.CENTER);

		popupListener = new PopupListener();
		popupListener.setActions(cmp_text.getActions());
		popupListener.addEditionMenu(true);
		cmp_text.addMouseListener(popupListener);
	}

	String replaceString(String aText, String aOrg, String aRes) {
		String val = aText;
		int len = aOrg.length();
		int pos = val.indexOf(aOrg);
		while (pos != -1) {
			val = val.substring(0, pos) + aRes + val.substring(pos + len);
			pos = val.indexOf(aOrg);
		}
		return val;
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		String val = getXmlItem().getText().trim();
		cmp_text.load(val);
	}

	protected boolean hasChanged() {
		String val = getXmlItem().getText().trim();
		return !cmp_text.save().equals(val);
	}
}
