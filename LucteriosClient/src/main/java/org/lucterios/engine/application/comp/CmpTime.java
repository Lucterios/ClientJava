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

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.graphic.DatePickerSimple;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIComponent.GUIFocusListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class CmpTime extends CmpAbstractEvent {
	private GUISpinEdit spe_hour;
	private GUILabel lbl_text;
	private GUISpinEdit spe_minute;

	public CmpTime() {
		super();
		setWeightx(1.0);
	}

	public void requestFocus() {
		spe_hour.requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		spe_hour.setEnabled(aEnabled);
		spe_minute.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		String time_hour = DatePickerSimple.convertIntToStr(spe_hour.getNumber(),2);
		String time_min = DatePickerSimple.convertIntToStr(spe_minute.getNumber(),2);
		MapContext tree_map = new MapContext();
		tree_map.put(getName(), time_hour + ":" + time_min);
		return tree_map;
	}

	protected void initComponent() {
		GUIParam param;
		param=new GUIParam(0,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_hour = mPanel.createSpinEdit(param);
		spe_hour.init(0, 0, 23);

		param=new GUIParam(1,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH);
		lbl_text = mPanel.createLabel(param);
		lbl_text.setTextString(" H ");

		param=new GUIParam(2,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_minute = mPanel.createSpinEdit(param);
		spe_minute.init(0, 0, 59);
		mPanel.createLabel(new GUIParam(3,0));
	}

	protected void refreshComponent() {
		super.refreshComponent();
		String[] times = getXmlItem().getText().trim().split(":");
		if (times.length == 3) {
			spe_hour.setNumber(Integer.parseInt(times[0]));
			spe_minute.setNumber(Integer.parseInt(times[1]));
		}
		spe_hour.addFocusListener(this);
		spe_minute.addFocusListener(this);
	}

	public void focusLost() {
		if ((mEventAction != null) && hasChanged()) {
			spe_hour.removeFocusListener(this);
			spe_minute.removeFocusListener(this);
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
		spe_hour.addFocusListener(aFocus);
		spe_minute.addFocusListener(aFocus);
	}

	public void removeFocusListener(GUIFocusListener aFocus) {
		spe_hour.removeFocusListener(aFocus);
		spe_minute.removeFocusListener(aFocus);
	}
	
}
