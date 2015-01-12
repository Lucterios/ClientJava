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
import org.lucterios.utils.Tools;
import org.lucterios.gui.GUIHyperText;

public class CmpHyperLink extends Cmponent {
	private GUIHyperText cmp_text;

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		cmp_text = mPanel.createHyperText(mParam);
	}

	protected void refreshComponent() {
		String val = getXmlItem().getText();
		String url = getXmlItem().getCDataOfFirstTag("LINK");
		val = Tools.convertLuctoriosFormatToHtml(val);
		cmp_text.setHyperLink(url);
		cmp_text.setTextString(val);
	}

}
