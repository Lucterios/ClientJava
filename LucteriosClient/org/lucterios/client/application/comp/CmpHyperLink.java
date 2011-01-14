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

import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.PopupListener;
import org.lucterios.utils.graphic.WebLabel;

public class CmpHyperLink extends Cmponent implements ClipboardOwner {
	private static final long serialVersionUID = 1L;
	private WebLabel cmp_text;
	private PopupListener popupListener;

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.GridBagLayout());
		cmp_text = new WebLabel();
		cmp_text.setOpaque(this.isOpaque());
		cmp_text.setName("cmp_text");
		cmp_text.setBackground(this.getBackground());
		GridBagConstraints cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 0;
		cnt.fill = GridBagConstraints.NONE;
		cnt.weightx = 0;
		cnt.weighty = 0;
		add(cmp_text, cnt);

		popupListener = new PopupListener();
		popupListener.setActions(cmp_text.getActions());
		JMenuItem mi = new JMenuItem("Copier");
		mi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				copyToClipboard();
			}
		});
		popupListener.getPopup().add(mi);
		cmp_text.addMouseListener(popupListener);
	}

	private void copyToClipboard() {
		String value_to_copy=cmp_text.getToolTipText();
		if (value_to_copy.startsWith("mailto:"))
				value_to_copy=value_to_copy.substring(7);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(value_to_copy), this );
		System.out.print("COPY:"+value_to_copy);
	}		
	
	protected void refreshComponent() {
		String val = getXmlItem().getText();
		String url = getXmlItem().getCDataOfFirstTag("LINK");
		val = Tools.convertLuctoriosFormatToHtml(val);
		cmp_text.setURL(url, val);
	}

	public void lostOwnership(Clipboard arg0, Transferable arg1) {}
}
