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
import java.lang.ref.WeakReference;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.DecodeBase64ToInputStream;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class CmpImage extends Cmponent {
	private static final long serialVersionUID = 1L;
	private JLabel cmp_text;

	private String mType="";
	private String mVal="";
	
	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	public void setValue(SimpleParsing aXmlItem) throws LucteriosException {
		mXmlItem = new WeakReference<SimpleParsing>(aXmlItem);
		refreshComponent();
	}
	
	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new JLabel();
		cmp_text.setText("");
		cmp_text.setFocusable(false);
		cmp_text.setName("cmp_text");
		cmp_text.setBackground(this.getBackground());
		add(cmp_text, java.awt.BorderLayout.CENTER);
	}

	protected void refreshComponent() {
		mType = getXmlItem().getCDataOfFirstTag("TYPE");
		mVal = getXmlItem().getText();
		int height=getXmlItem().getAttributInt("height",0);
		int width=getXmlItem().getAttributInt("width",0);
		cmp_text.setPreferredSize(new Dimension(width,height));
	}
	
	public void initialize() throws LucteriosException {
		ImageIcon image;
		if (mType.equals( "" ))
			image = Singletons.Transport().getIcon(mVal,0);
		else {
			DecodeBase64ToInputStream decoder = new DecodeBase64ToInputStream(mVal);
			image = new ImageIcon(decoder.readData());
		}
		cmp_text.setIcon(image);
	}
}
