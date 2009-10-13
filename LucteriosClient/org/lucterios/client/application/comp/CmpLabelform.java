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

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.Tools;

public class CmpLabelform extends Cmponent {
	private static final long serialVersionUID = 1L;
	private javax.swing.JEditorPane cmp_text;

	public boolean isFocusable() {
		return false;
	}
	
	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new javax.swing.JEditorPane();
		cmp_text.setEditable(false);
		cmp_text.setOpaque(this.isOpaque());
		cmp_text.setText("");
		cmp_text.setFocusable(false);
		cmp_text.setName("cmp_text");
		cmp_text.setContentType("text/html");
		cmp_text.setBackground(this.getBackground());
		add(cmp_text, java.awt.BorderLayout.CENTER);
	}

	protected void refreshComponent() {
		String val = getXmlItem().getText();
		val = Tools.convertLuctoriosFormatToHtml(val);
		cmp_text.setText(val);
	}
}
