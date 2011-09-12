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

package org.lucterios.mock;

import java.util.Map;
import java.util.TreeMap;

import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.ui.GUIAction;
import org.lucterios.utils.GCTools;

public class MockFormList extends GuiFormList {

	class ShortCut {
		String mActionName = "";
		GUIAction mActionListener = null;
		String mShortCut;
	}

	private Map<String, ShortCut> mShortCutDico;
	private GUIGenerator mGenerator;
	private Object[] mObjects=null;

	public MockFormList(GUIGenerator generator ) {
		super();
		mGenerator=generator; 
		mShortCutDico = new TreeMap<String, ShortCut>();
	}
	
	@Override
	public GUIForm create(String aId) {
		MockForm form=(MockForm)super.create(aId);
		form.setNotifyFrameChange(this);
		form.setImage(mGenerator.getFrame().getImage());
		return form;
	}

	
	public GUIGenerator getGenerator(){
		return mGenerator;
	}

	public void clearShortCut() {
		mShortCutDico.clear();
		GCTools.postOrderGC();
	}

	protected GUIForm newForm(String aId) {
		return new MockForm(aId,mGenerator);
	}
	
	public void newShortCut(String aActionName, String aShortCut,
			GUIAction aActionListener) {
		ShortCut short_cut = new ShortCut();
		short_cut.mActionListener = aActionListener;
		short_cut.mActionName = aActionName;
		short_cut.mShortCut = aShortCut;
		mShortCutDico.put(aActionName, short_cut);
	}

	public void assignShortCut(Object aComp) {}
	
	@Override
	public void addThemeMenuSelector(GUIMenu menu) {
	}

	public void setObjects(Object[] mObjects) {
		this.mObjects = mObjects;
	}
	public Object[] getObjects() {
		return mObjects;
	}

}
