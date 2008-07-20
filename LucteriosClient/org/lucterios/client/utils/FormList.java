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

package org.lucterios.client.utils;

import java.awt.Container;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class FormList implements Form.NotifyFrameList {
	class ShortCut {
		String mActionName = "";
		KeyStroke mShortCut = null;
		javax.swing.Action mActionListener = null;
	}

	private Vector mList;
	private Map mShortCut;

	public FormList() {
		mList = new Vector();
		mShortCut = new TreeMap();
	}

	public void clear() {
		mList.clear();
	}

	public void clearShortCut() {
		mShortCut.clear();
	}

	public void newShortCut(String aActionName, KeyStroke aShortCut,
			javax.swing.Action aActionListener) {
		ShortCut short_cut = new ShortCut();
		short_cut.mActionListener = aActionListener;
		short_cut.mActionName = aActionName;
		short_cut.mShortCut = aShortCut;
		mShortCut.put(aActionName, short_cut);
	}

	public void assignShortCut(Container aComp) {
		Set entrees = mShortCut.entrySet();
		Iterator iterateur = entrees.iterator();
		while (iterateur.hasNext()) {
			Map.Entry entree = (Map.Entry) iterateur.next();
			ShortCut short_cut = (ShortCut) entree.getValue();
			addShortCut(aComp, short_cut.mActionName, short_cut.mShortCut,
					short_cut.mActionListener);
		}
	}

	static public void addShortCut(Container aComp, String aActionName,
			KeyStroke aShortCut, javax.swing.Action aActionListener) {
		if (JComponent.class.isInstance(aComp)) {
			JComponent jcomp = (JComponent) aComp;
			InputMap inputMap = jcomp.getInputMap();
			inputMap.put(aShortCut, aActionName);
			ActionMap actionMap = jcomp.getActionMap();
			actionMap.put(aActionName, aActionListener);
		}
		for (int idx = 0; idx < aComp.getComponentCount(); idx++)
			if (Container.class.isInstance(aComp))
				addShortCut((Container) aComp.getComponent(idx), aActionName,
						aShortCut, aActionListener);
	}

	public Form create(String aId) {
		Form form = null;
		for (int idx = 0; (form == null) && idx < count(); idx++)
			if (get(idx).getName().equals(aId))
				form = get(idx);
		if (form == null) {
			form = new Form(aId);
			add(form);
		}
		return form;
	}

	private void add(Form aNewForm) {
		aNewForm.setNotifyFrameList(this);
		mList.add(aNewForm);
		if (mFormSelected == null)
			mFormSelected = aNewForm;
	}

	public void removeFrame(Form aForm) {
		aForm.setNotifyFrameList(null);
		mList.remove(aForm);
	}

	public int count() {
		return mList.size();
	}

	public Form get(int index) {
		return (Form) mList.get(index);
	}

	private Form mFormSelected = null;

	public void selectFrame(Form aForm) {
		mFormSelected = aForm;
	}

	public Form getFrameSelected() {
		return mFormSelected;
	}
}
