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

import java.util.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

import org.lucterios.utils.LucteriosException;

public class CmpTime extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;
	private org.lucterios.utils.graphic.SpinEdit spe_hour;
	private javax.swing.JLabel lbl_text;
	private org.lucterios.utils.graphic.SpinEdit spe_minute;

	public CmpTime() {
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		mWeightx = 1.0;
	}

	public void requestFocus() {
		spe_hour.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		spe_hour.setEnabled(aEnabled);
		spe_minute.setEnabled(aEnabled);
	}

	public Map getRequete(String aActionIdent) {
		String time_hour = new Long(spe_hour.getNumber()).toString();
		if (time_hour.length() == 1)
			time_hour = "0" + time_hour;
		String time_min = new Long(spe_minute.getNumber()).toString();
		if (time_min.length() == 1)
			time_min = "0" + time_min;
		TreeMap tree_map = new TreeMap();
		tree_map.put(getName(), time_hour + ":" + time_min);
		return tree_map;
	}

	protected void initComponent() {
		setLayout(new java.awt.GridBagLayout());
		GridBagConstraints gdbConstr;

		spe_hour = new org.lucterios.utils.graphic.SpinEdit(0, 0, 23);
		spe_hour.setMinimumSize(new Dimension(30, 0));
		spe_hour.setPreferredSize(new Dimension(30, 0));
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 0;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(spe_hour, gdbConstr);

		lbl_text = new javax.swing.JLabel();
		lbl_text.setText(" H ");
		lbl_text.setName("lbl_text");
		lbl_text.setFocusable(false);
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 1;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.NONE;
		add(lbl_text, gdbConstr);

		spe_minute = new org.lucterios.utils.graphic.SpinEdit(0, 0, 59);
		spe_minute.setMinimumSize(new Dimension(30, 0));
		spe_minute.setPreferredSize(new Dimension(30, 0));
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 2;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(spe_minute, gdbConstr);

		JPanel pnl_date = new JPanel();
		pnl_date.setOpaque(false);
		pnl_date.setFocusable(false);
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 3;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 1.0;
		gdbConstr.weighty = 1.0;
		add(pnl_date, gdbConstr);
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		String[] times = mXmlItem.getText().trim().split(":");
		if (times.length == 3) {
			spe_hour.setNumber(Integer.parseInt(times[0]));
			spe_minute.setNumber(Integer.parseInt(times[1]));
		}
		spe_hour.addFocusListener(this);
		spe_minute.addFocusListener(this);
	}

	public void focusLost(FocusEvent aEvent) {
		if ((mEventAction != null) && hasChanged()) {
			Cmponent new_Cmponent_focused = getParentOfControle(aEvent
					.getOppositeComponent());
			if ((new_Cmponent_focused != null)
					&& !new_Cmponent_focused.getName().equals(getName())) {
				spe_hour.removeFocusListener(this);
				spe_minute.removeFocusListener(this);
				mObsCustom.setNameComponentFocused(new_Cmponent_focused
						.getName());
				mEventAction.actionPerformed(null);
			}
		}
	}

	protected boolean hasChanged() {
		String init_value = mXmlItem.getText().trim();
		;
		String current_value = getRequete("").get(getName()).toString();
		return !init_value.equals(current_value);
	}
	
	public void addFocusListener(FocusListener aFocus) {
		super.addFocusListener(aFocus);
		spe_hour.addFocusListener(aFocus);
		spe_minute.addFocusListener(aFocus);
	}

	public void removeFocusListener(FocusListener aFocus) {
		super.removeFocusListener(aFocus);
		spe_hour.removeFocusListener(aFocus);
		spe_minute.removeFocusListener(aFocus);
	}
	
}
