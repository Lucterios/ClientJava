package org.lucterios.client.application.comp;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;

public class ImageFormRenderer extends JLabel implements
		javax.swing.table.TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public ImageFormRenderer() {
		setFocusable(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Icon my_icon = (Icon) value;
		if (my_icon != null) {
			setIcon(my_icon);
			if (isSelected)
				setBackground(UIManager.getColor("controlShadow"));
			else
				setBackground(UIManager.getColor("controlHighlight"));
			setPreferredSize(new Dimension(my_icon.getIconWidth(), my_icon
					.getIconHeight()));
			int cell_height = Math.max(table.getRowHeight(row),
					(int) getPreferredSize().getHeight());
			table.setRowHeight(row, cell_height);
		}
		return this;
	}

}
