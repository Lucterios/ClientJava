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
import javax.swing.JPanel;

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.SimpleParsing;

public class CompDefault extends Cmponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JTextArea lbl_result;
	private String mComponentName;

	public CompDefault(String aComponentName) {
		mComponentName = aComponentName;
	}

	public MapContext getRequete(String aActionIdent) {
		return new MapContext();
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		lbl_result = new javax.swing.JTextArea();
		lbl_result.setEditable(false);
		lbl_result.setFont(new java.awt.Font("Dialog", 0, 10));
		lbl_result.setText("");
		lbl_result.setFocusable(false);
		lbl_result.setLineWrap(true);
		lbl_result.setWrapStyleWord(true);
		lbl_result.setName("lbl_result");
		add(lbl_result, java.awt.BorderLayout.CENTER);
	}

	protected void refreshComponent() {
		String value = "";
		value = value + "ComponentName=" + mComponentName + "\n";
		value = value + "XmlItem=" + getXmlItem() + "\n";
		lbl_result.setText(value);
	}

	public void init(JPanel aOwnerPanel, Observer aObsCustom,
			SimpleParsing aXmlItem) {
		super.init(aOwnerPanel, aObsCustom, aXmlItem);
		this.setMinimumSize(new Dimension(75, 50));
		this.setPreferredSize(new Dimension(75, 50));
	}
}
