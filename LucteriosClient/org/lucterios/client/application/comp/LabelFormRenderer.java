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

import java.awt.Color;
import java.awt.Component;

import javax.swing.UIManager;
import javax.swing.table.TableColumn;

import org.lucterios.utils.StringList;
import org.lucterios.utils.Tools;

public class LabelFormRenderer extends javax.swing.JEditorPane implements
		javax.swing.table.TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public LabelFormRenderer() {
		setEditable(false);
		setFocusable(true);
		setContentType("text/html");
	}

	StringList cell_check = new StringList();

	public void clearTag() {
		cell_check.clear();
	}

	public Component getTableCellRendererComponent(javax.swing.JTable jTable,
			Object obj, boolean isSelected, boolean hasFocus, int row,
			int column) {
		String val;
		if ((obj instanceof Double) || (obj instanceof Integer)) {
			val = "<DIV align='right'>"+obj.toString()+"</DIV>";			
		}
		else {
			val = Tools.convertLuctoriosFormatToHtml(obj.toString());
			while (val.endsWith("<br>"))
				val = val.substring(0, val.length() - 4).trim();
		}
		setText(val);
		if (isSelected)
			setBackground(UIManager.getColor("Table.selectionBackground"));
		else {
		    if((row % 2)!=0)	    	
		    	setBackground(Color.WHITE);  
		    else  
		    	setBackground(new Color(220,220,220));
		}
		if (cell_check.indexOf(row + "-" + column) == -1) {
			int cell_height = Math.max(jTable.getRowHeight(row),
					(int) getPreferredSize().getHeight());
			jTable.setRowHeight(row, cell_height);

			TableColumn colonne = jTable.getColumnModel().getColumn(column);
			int col_width=colonne.getPreferredWidth();
			if (col_width<getPreferredSize().getWidth())
				colonne.setPreferredWidth((int)getPreferredSize().getWidth());
			cell_check.add(row + "-" + column);
		}
		return this;
	}
}
