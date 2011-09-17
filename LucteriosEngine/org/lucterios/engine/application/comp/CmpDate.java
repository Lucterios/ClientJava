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

package org.lucterios.engine.application.comp;

import java.util.Calendar;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.graphic.date.DatePickerSimple;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIComponent.GUIFocusListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;

public class CmpDate extends CmpAbstractEvent {
	private static final long serialVersionUID = 1L;

	private GUISpinEdit spe_day;
	private GUICombo cmp_month;
	private GUISpinEdit spe_year;

	private GUIButton edit_date;
	private DatePickerSimple date_simple;

	public CmpDate() {
		super();
		setWeightx(1.0);
	}

	public void requestFocus() {
		spe_day.requestFocusGUI();
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
		date_text = date_text
				+ DatePickerSimple.convertIntToStr(spe_year.getNumber(), 4);
		date_text = date_text
				+ "-"
				+ DatePickerSimple.convertIntToStr(
						cmp_month.getSelectedIndex() + 1, 2);
		date_text = date_text + "-"
				+ DatePickerSimple.convertIntToStr(spe_day.getNumber(), 2);
		tree_map.put(getName(), date_text);
		return tree_map;
	}

	protected void initComponent() {
		date_simple = new DatePickerSimple();
		Singletons.getWindowGenerator().newDialog(null).setDialogVisitor(date_simple);

		GUIParam param;
		param=new GUIParam(0,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_day = mPanel.createSpinEdit(param);
		spe_day.init(date_simple.day(), 1, 31);

		param=new GUIParam(1,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH);
		cmp_month = mPanel.createCombo(param);
		cmp_month.addList(DatePickerSimple.getMonthList().toArray());
		cmp_month.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
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

		param=new GUIParam(2,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_year = mPanel.createSpinEdit(param);
		spe_year.init(date_simple.year(),1000, 3000);

		param=new GUIParam(3,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
		edit_date = mPanel.createButton(param);
		edit_date.setTextString("...");
		edit_date.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				fillData();
				date_simple.setVisible(true);
				refreshData();
			}
		});

		mPanel.createLabel(new GUIParam(4,0));
	}

	protected void fillData() {
		date_simple.set((int) (spe_year.getNumber()),
				cmp_month.getSelectedIndex(), (int) (spe_day.getNumber()));
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

	protected void refreshComponent() {
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

	public void focusLost() {
		if ((mEventAction != null) && hasChanged()) {
			spe_day.removeFocusListener(this);
			cmp_month.removeFocusListener(this);
			spe_year.removeFocusListener(this);
			edit_date.removeFocusListener(this);
			getObsCustom().setNameComponentFocused(getName());
			mEventAction.actionPerformed();
		}
	}

	protected boolean hasChanged() {
		String init_value = getXmlItem().getText().trim();
		String current_value = getRequete("").get(getName()).toString();
		return !init_value.equals(current_value);
	}

	public void addFocusListener(GUIFocusListener aFocus) {
		spe_day.addFocusListener(aFocus);
		cmp_month.addFocusListener(aFocus);
		spe_year.addFocusListener(aFocus);
		edit_date.addFocusListener(aFocus);
	}

	public void removeFocusListener(GUIFocusListener aFocus) {
		spe_day.removeFocusListener(aFocus);
		cmp_month.removeFocusListener(aFocus);
		spe_year.removeFocusListener(aFocus);
		edit_date.removeFocusListener(aFocus);
	}

}
