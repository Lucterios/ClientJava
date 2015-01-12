package org.lucterios.graphic;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class CheckFormRenderer extends javax.swing.JCheckBox implements
		javax.swing.table.TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckFormRenderer() {
		super();
		// setEnabled(false);
		setFocusable(true);
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Boolean val = (Boolean) value;
		setSelected(val.booleanValue());
		setForeground(Color.black);
		if (isSelected)
			setBackground(UIManager.getColor("Table.selectionBackground"));
		else {
			if ((row % 2) != 0)
				setBackground(Color.WHITE);
			else
				setBackground(new Color(220, 220, 220));
		}
		return this;
	}
}
