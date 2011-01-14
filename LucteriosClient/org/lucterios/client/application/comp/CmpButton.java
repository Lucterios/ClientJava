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
import java.awt.GridBagLayout;
import java.awt.Insets;

import org.lucterios.client.application.Button;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;

public class CmpButton extends CmpAbstractEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel pnl_Btn;
	private Button actbtn;

	public CmpButton() {
		super();
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		return tree_map;
	}

	protected void initComponent() {
		pnl_Btn = new javax.swing.JPanel();
		pnl_Btn.setName("pnl_Btn");
		pnl_Btn.setOpaque(this.isOpaque());
		pnl_Btn.setLayout(new GridBagLayout());
		add(pnl_Btn, java.awt.BorderLayout.CENTER);
		actbtn = null;
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		if (actbtn != null)
			actbtn.setEnabled(aEnabled);
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		GridBagConstraints gdbConstr_btn;
		actbtn = null;
		pnl_Btn.removeAll();
		if (mEventAction != null) {
			gdbConstr_btn = new GridBagConstraints();
			gdbConstr_btn.gridx = 0;
			gdbConstr_btn.gridy = 0;
			gdbConstr_btn.gridwidth = 1;
			gdbConstr_btn.gridheight = 1;
			gdbConstr_btn.fill = GridBagConstraints.NONE;
			gdbConstr_btn.anchor = GridBagConstraints.NORTH;
			gdbConstr_btn.weightx = 0.0;
			gdbConstr_btn.weighty = 0.0;
			gdbConstr_btn.insets = new Insets(1, 0, 1, 0);
			actbtn = new Button(mEventAction);
			pnl_Btn.add(actbtn, gdbConstr_btn);
			pnl_Btn.setFocusable(false);
		}
		pnl_Btn.repaint();
	}

	protected boolean hasChanged() {
		return false;
	}
}
