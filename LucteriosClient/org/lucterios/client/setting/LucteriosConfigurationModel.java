package org.lucterios.client.setting;

import java.io.IOException;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.lucterios.engine.utils.LucteriosConfiguration;

public class LucteriosConfigurationModel extends LucteriosConfiguration implements TableModel {

	public LucteriosConfigurationModel() throws IOException {
		super();
	}

	public LucteriosConfigurationModel(LucteriosConfiguration configuration) {
		super(configuration);
	}

	public void addTableModelListener(TableModelListener l) {
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}
