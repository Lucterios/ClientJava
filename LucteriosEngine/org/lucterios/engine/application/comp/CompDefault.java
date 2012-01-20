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

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIMemo;
import org.lucterios.utils.SimpleParsing;

public class CompDefault extends Cmponent {
	private GUIMemo lbl_result;
	private String mComponentName;

	public CompDefault(String aComponentName) {
		mComponentName = aComponentName;
	}

	public MapContext getRequete(String aActionIdent) {
		return new MapContext();
	}

	protected void initComponent() {
		mParam.setPrefSizeY(50);
		mParam.setPrefSizeX(75);
		lbl_result = mPanel.createMemo(mParam);
		lbl_result.setValue("");
	}

	protected void refreshComponent() {
		String value = "";
		value = value + "ComponentName=" + mComponentName + "\n";
		value = value + "XmlItem=" + getXmlItem() + "\n";
		lbl_result.setValue(value);
	}

	public void init(GUIContainer aOwnerPanel, Observer aObsCustom,
			SimpleParsing aXmlItem) {
		super.init(aOwnerPanel, aObsCustom, aXmlItem);
	}
}
