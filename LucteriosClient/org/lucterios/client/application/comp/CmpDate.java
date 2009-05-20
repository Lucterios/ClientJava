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

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.client.utils.DatePickerSimple;
import org.lucterios.utils.LucteriosException;

public class CmpDate extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;

	private org.lucterios.utils.graphic.SpinEdit spe_day;
	private javax.swing.JComboBox cmp_month;
	private org.lucterios.utils.graphic.SpinEdit spe_year;

	private javax.swing.JButton edit_date;
	private DatePickerSimple date_simple;

	public CmpDate() {
		super();
		mFill = GridBagConstraints.HORIZONTAL;
		mWeightx = 1.0;
		setFocusable(false);
	}

	public void requestFocus() {
		spe_day.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		spe_day.setEnabled(aEnabled);
		cmp_month.setEnabled(aEnabled);
		spe_year.setEnabled(aEnabled);
		edit_date.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		fillData();
		MapContext tree_map = new MapContext();
		String date_text = "";
		date_text = date_text + DatePickerSimple.convertIntToStr(spe_year.getNumber(), 4);
		date_text = date_text + "-"
				+ DatePickerSimple.convertIntToStr(cmp_month.getSelectedIndex() + 1, 2);
		date_text = date_text + "-" + DatePickerSimple.convertIntToStr(spe_day.getNumber(), 2);
		tree_map.put(getName(), date_text);
		return tree_map;
	}

	protected void initComponent() {
		date_simple = new DatePickerSimple();
		date_simple.setFocusable(false);

		setLayout(new java.awt.GridBagLayout());
		GridBagConstraints gdbConstr;
		spe_day = new org.lucterios.utils.graphic.SpinEdit(date_simple.day(),
				1, 31);
		spe_day.setName("spe_day");
		spe_day.setMinimumSize(new Dimension(30, 0));
		spe_day.setPreferredSize(new Dimension(30, 0));
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 0;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(spe_day, gdbConstr);

		cmp_month = new javax.swing.JComboBox(DatePickerSimple.getMonthList().toArray());
		cmp_month.setName("cmp_month");
		cmp_month.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (cmp_month.getSelectedIndex() + 1) {
				case 2:
					if ((spe_year.getNumber() % 4) != 0)
						spe_day.setUpperLimit(28);
					else
						spe_day.setUpperLimit(29);
					break;
				case 4:
				case 6:
				case 9:
				case 11:
					spe_day.setUpperLimit(30);
					break;
				default:
					spe_day.setUpperLimit(31);
					break;
				}
				spe_day.setNumber(spe_day.getNumber());
			}
		});
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 1;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		add(cmp_month, gdbConstr);

		spe_year = new org.lucterios.utils.graphic.SpinEdit(date_simple.year(),
				1000, 3000);
		spe_year.setName("spe_year");
		spe_year.setMinimumSize(new Dimension(60, 0));
		spe_year.setPreferredSize(new Dimension(60, 0));
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 2;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 0.0;
		gdbConstr.weighty = 1.0;
		add(spe_year, gdbConstr);

		edit_date = new javax.swing.JButton("...");
		edit_date.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fillData();
				date_simple.setVisible(true);
				refreshData();
			}
		});
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 3;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		add(edit_date, gdbConstr);

		JLabel pnl_date = new JLabel();
		pnl_date.setFocusable(false);
		pnl_date.setOpaque(false);
		gdbConstr = new GridBagConstraints();
		gdbConstr.gridx = 4;
		gdbConstr.gridy = 0;
		gdbConstr.fill = GridBagConstraints.BOTH;
		gdbConstr.weightx = 1.0;
		gdbConstr.weighty = 1.0;
		add(pnl_date, gdbConstr);
	}

	protected void fillData() {
		date_simple.getSelector().set((int)(spe_year.getNumber()), cmp_month
				.getSelectedIndex(),(int)(spe_day.getNumber()));
	}

	protected void refreshData() {
		spe_day.setNumber(date_simple.day());
		if (date_simple.month() < 1)
			cmp_month.setSelectedIndex(0);
		else if (date_simple.month() > 12)
			cmp_month.setSelectedIndex(11);
		else
			cmp_month.setSelectedIndex(date_simple.month());
		spe_year.setNumber(date_simple.year());
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		String date = getXmlItem().getText().trim();
		String[] dates = date.split("-");
		if (dates.length == 3) {
			spe_year.setNumber(Integer.parseInt(dates[0]));
			cmp_month.setSelectedIndex(Integer.parseInt(dates[1]) - 1);
			spe_day.setNumber(Integer.parseInt(dates[2]));
		} else {
			Calendar today = Calendar.getInstance();
			spe_year.setNumber(today.get(Calendar.YEAR));
			cmp_month.setSelectedIndex(today.get(Calendar.MONTH));
			spe_day.setNumber(today.get(Calendar.DAY_OF_MONTH));
		}
		fillData();
		spe_day.addFocusListener(this);
		cmp_month.addFocusListener(this);
		spe_year.addFocusListener(this);
		edit_date.addFocusListener(this);
	}

	public void focusLost(FocusEvent aEvent) {
		if ((mEventAction != null) && hasChanged()) {
			Cmponent new_Cmponent_focused = getParentOfControle(aEvent
					.getOppositeComponent());
			if ((new_Cmponent_focused != null)
					&& !new_Cmponent_focused.getName().equals(getName())) {
				spe_day.removeFocusListener(this);
				cmp_month.removeFocusListener(this);
				spe_year.removeFocusListener(this);
				edit_date.removeFocusListener(this);
				getObsCustom().setNameComponentFocused(new_Cmponent_focused
						.getName());
				mEventAction.actionPerformed(null);
			}
		}
	}

	protected boolean hasChanged() {
		String init_value = getXmlItem().getText().trim();
		String current_value = getRequete("").get(getName()).toString();
		return !init_value.equals(current_value);
	}

	public void addFocusListener(FocusListener aFocus) {
		super.addFocusListener(aFocus);
		spe_day.addFocusListener(aFocus);
		cmp_month.addFocusListener(aFocus);
		spe_year.addFocusListener(aFocus);
		edit_date.addFocusListener(aFocus);
	}

	public void removeFocusListener(FocusListener aFocus) {
		super.removeFocusListener(aFocus);
		spe_day.removeFocusListener(aFocus);
		cmp_month.removeFocusListener(aFocus);
		spe_year.removeFocusListener(aFocus);
		edit_date.removeFocusListener(aFocus);
	}
	
}
