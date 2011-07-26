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

package org.lucterios.swing;

import java.awt.Component;
import java.awt.Container;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.lucterios.graphic.Tools;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.style.ThemeMenu;
import org.lucterios.style.ThemeMenu.LookAndFeelCallBack;

public class SFormList extends GuiFormList implements LookAndFeelCallBack {
	class ShortCut {
		String mActionName = "";
		KeyStroke mShortCut = null;
		javax.swing.Action mActionListener = null;
	}

	private Map<String, ShortCut> mShortCutDico;
	private GUIGenerator mGenerator;
	private Object[] mObjects=null;

	public SFormList(GUIGenerator generator ) {
		super();
		mGenerator=generator; 
		mShortCutDico = new TreeMap<String, ShortCut>();
	}
	
	public GUIGenerator getGenerator(){
		return mGenerator;
	}

	public void clearShortCut() {
		mShortCutDico.clear();
		Tools.postOrderGC();
	}

	protected GUIForm newForm(String aId) {
		return new SForm(aId,mGenerator);
	}
	
	public void newShortCut(String aActionName, KeyStroke aShortCut,
			javax.swing.Action aActionListener) {
		ShortCut short_cut = new ShortCut();
		short_cut.mActionListener = aActionListener;
		short_cut.mActionName = aActionName;
		short_cut.mShortCut = aShortCut;
		mShortCutDico.put(aActionName, short_cut);
	}

	public void assignShortCut(Object aComp) {
		Set<Map.Entry<String, ShortCut>> entrees = mShortCutDico.entrySet();
		Iterator<Map.Entry<String, ShortCut>> iterateur = entrees.iterator();
		while (iterateur.hasNext()) {
			Map.Entry<String, ShortCut> entree = iterateur.next();
			ShortCut short_cut = (ShortCut) entree.getValue();
			addShortCut((Container) aComp, short_cut.mActionName,
					short_cut.mShortCut, short_cut.mActionListener);
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

	@Override
	public void addThemeMenuSelector(GUIMenu menu) {
		if (SMenu.class.isInstance(menu))
			ThemeMenu.getThemeMenu((SMenu)menu,this);
	}

	public Component[] getComponentsForLookAndFeel() {
		Component[] cmp = new Component[count() + ((mObjects!=null)?mObjects.length:0)];
		for (int frame_idx = 0; frame_idx < count(); frame_idx++)
			cmp[frame_idx] = (Component) get(frame_idx);
		if (mObjects!=null) {
			int index=count();
			for(Object item:mObjects)
				if(Component.class.isInstance(item)) 
					cmp[index++] = (Component)item;
				else
					cmp[index++] = null;
		}
		return cmp;
	}

	public void setObjects(Object[] mObjects) {
		this.mObjects = mObjects;
	}

}
