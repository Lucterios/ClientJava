package org.lucterios.client.application.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ImageFormRenderer extends JPanel implements
		javax.swing.table.TableCellRenderer {

	private static final long serialVersionUID = 1L;

	JLabel img = new JLabel();

	public ImageFormRenderer() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cst = new GridBagConstraints();
		cst.fill = GridBagConstraints.NONE;
		cst.anchor = GridBagConstraints.CENTER;
		img.setFocusable(true);
		img.setOpaque(true);
		img.setHorizontalAlignment(SwingConstants.CENTER);
		img.setVerticalAlignment(SwingConstants.CENTER);
		this.add(img, cst);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Icon my_icon = (Icon) value;
		if (my_icon != null) {
			img.setIcon(my_icon);
			if (isSelected)
				setBackground(UIManager.getColor("Table.selectionBackground"));
			else {
				if ((row % 2) != 0)
					setBackground(Color.WHITE);
				else
					setBackground(new Color(220, 220, 220));
			}
			setPreferredSize(new Dimension(my_icon.getIconWidth(), my_icon
					.getIconHeight()));
			int cell_height = Math.max(table.getRowHeight(row),
					(int) getPreferredSize().getHeight());
			table.setRowHeight(row, cell_height);
		}
		return this;
	}

}
