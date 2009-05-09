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

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.UIManager;

import org.lucterios.utils.Tools;

public class LabelFormRenderer extends javax.swing.JEditorPane implements
		javax.swing.table.TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public LabelFormRenderer() {
		setEditable(false);
		setFocusable(true);
		setContentType("text/html");
	}

	ArrayList cell_check = new ArrayList();

	public void clearTag() {
		cell_check.clear();
	}

	public Component getTableCellRendererComponent(javax.swing.JTable jTable,
			Object obj, boolean isSelected, boolean hasFocus, int row,
			int column) {
		String val = Tools.convertLuctoriosFormatToHtml((String) obj);
		while (val.endsWith("<br>"))
			val = val.substring(0, val.length() - 4).trim();
		setText(val);
		if (isSelected)
			setBackground(UIManager.getColor("controlShadow"));
		else
			setBackground(UIManager.getColor("controlHighlight"));
		if (cell_check.indexOf(row + "-" + column) == -1) {
			int cell_height = Math.max(jTable.getRowHeight(row),
					(int) getPreferredSize().getHeight());
			jTable.setRowHeight(row, cell_height);
			cell_check.add(row + "-" + column);
		}
		return this;
	}
}
