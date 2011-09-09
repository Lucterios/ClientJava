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

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIImage;
import org.lucterios.utils.DecodeBase64ToInputStream;

public class CmpImage extends Cmponent {
	private static final long serialVersionUID = 1L;
	private GUIImage cmp_img;

	public CmpImage(){
		super();
	}
	
	public boolean isFocusable() {
		return false;
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		cmp_img = mPanel.createImage(mParam);
	}

	protected void refreshComponent() {
		String type = getXmlItem().getCDataOfFirstTag("TYPE");
		int height = getXmlItem().getAttributInt("height", 0);
		int width = getXmlItem().getAttributInt("width", 0);
		String val = getXmlItem().getText();
		AbstractImage image;
		if (type.equals(""))
			image = Singletons.Transport().getIcon(val, 0);
		else {
			DecodeBase64ToInputStream decoder = new DecodeBase64ToInputStream(val);
			image = Singletons.getWindowGenerator().CreateImage(decoder.readData());
		}
		cmp_img.setImage(image.resize(height, width));
	}
}
