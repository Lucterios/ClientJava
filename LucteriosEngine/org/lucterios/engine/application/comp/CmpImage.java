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
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.utils.DecodeBase64ToInputStream;
import org.lucterios.utils.SimpleParsing;

public class CmpImage extends Cmponent {
	private static final long serialVersionUID = 1L;
	private GUILabel cmp_text;

	private String mType = "";
	private String mVal = "";
	
	public CmpImage(){
		super();
		mFill = FillMode.FM_BOTH;
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	public void setValue(SimpleParsing aXmlItem) {
		mXmlItem = aXmlItem;
		refreshComponent();
	}

	protected void initComponent() {
		mParam.setFill(FillMode.FM_BOTH);
		mParam.setReSize(ReSizeMode.RSM_BOTH);
		mParam.setPad(2);
		cmp_text = mPanel.createLabel(mParam);
	}

	protected void refreshComponent() {
		mType = getXmlItem().getCDataOfFirstTag("TYPE");
		mVal = getXmlItem().getText();
		int height = getXmlItem().getAttributInt("height", 0);
		int width = getXmlItem().getAttributInt("width", 0);
		cmp_text.setSize(width, height);
	}

	public void initialize() {
		AbstractImage image;
		if (mType.equals(""))
			image = Singletons.Transport().getIcon(mVal, 0);
		else {
			DecodeBase64ToInputStream decoder = new DecodeBase64ToInputStream(mVal);
			image = Singletons.getWindowGenerator().CreateImage(decoder.readData());
		}
		cmp_text.setImage(image);
	}
}
