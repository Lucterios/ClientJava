package org.lucterios.engine.application.comp;

import java.util.Calendar;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.graphic.DatePickerSimple;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIComponent.GUIFocusListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;

public class CmpDateTime extends CmpAbstractEvent {

	private GUISpinEdit spe_day;
	private GUICombo cmp_month;
	private GUISpinEdit spe_year;

	private GUIButton edit_date;
	private DatePickerSimple date_simple;

	private GUISpinEdit spe_hour;
	private GUILabel lbl_text;
	private GUISpinEdit spe_minute;
	
	public CmpDateTime() {
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
		spe_hour.setEnabled(aEnabled);
		spe_minute.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		fillData();
		MapContext tree_map = new MapContext();
		String date_text = "";
		date_text = date_text + DatePickerSimple.convertIntToStr(spe_year.getNumber(), 4);
		date_text = date_text + "-" + DatePickerSimple.convertIntToStr(cmp_month.getSelectedIndex() + 1, 2);
		date_text = date_text + "-" + DatePickerSimple.convertIntToStr(spe_day.getNumber(), 2);
		date_text = date_text + " " + DatePickerSimple.convertIntToStr(spe_hour.getNumber(),2);
		date_text = date_text + ":" + DatePickerSimple.convertIntToStr(spe_minute.getNumber(),2);
		tree_map.put(getName(), date_text);
		return tree_map;
	}

	private GUIDialog mDatePickerDialog;
	protected void initComponent() {
		date_simple = new DatePickerSimple();
		mDatePickerDialog=Singletons.getWindowGenerator().newDialog(null);
		mDatePickerDialog.setDialogVisitor(date_simple);

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
				mDatePickerDialog.setVisible(true);
				refreshData();
			}
		});

		param=new GUIParam(4,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_hour = mPanel.createSpinEdit(param);
		spe_hour.init(0, 0, 23);

		param=new GUIParam(5,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH);
		lbl_text = mPanel.createLabel(param);
		lbl_text.setTextString(" H ");

		param=new GUIParam(6,0,1,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH);
		spe_minute = mPanel.createSpinEdit(param);
		spe_minute.init(0, 0, 59);
		
		mPanel.createLabel(new GUIParam(7,0));	
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
		String[] datetime = getXmlItem().getText().trim().split(" ");
		if (datetime.length == 2) {
			String[] dates = datetime[0].split("-");
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
			String[] times = datetime[1].split(":");
			if (times.length == 3) {
				spe_hour.setNumber(Integer.parseInt(times[0]));
				spe_minute.setNumber(Integer.parseInt(times[1]));
			}
			
			fillData();
			spe_day.addFocusListener(this);
			cmp_month.addFocusListener(this);
			spe_year.addFocusListener(this);
			edit_date.addFocusListener(this);
			spe_hour.addFocusListener(this);
			spe_minute.addFocusListener(this);	
		}
	}

	public void focusLost() {
		if ((mEventAction != null) && hasChanged()) {
			spe_day.removeFocusListener(this);
			cmp_month.removeFocusListener(this);
			spe_year.removeFocusListener(this);
			edit_date.removeFocusListener(this);
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
		spe_day.addFocusListener(aFocus);
		cmp_month.addFocusListener(aFocus);
		spe_year.addFocusListener(aFocus);
		edit_date.addFocusListener(aFocus);
		spe_hour.addFocusListener(aFocus);
		spe_minute.addFocusListener(aFocus);		
	}

	public void removeFocusListener(GUIFocusListener aFocus) {
		spe_day.removeFocusListener(aFocus);
		cmp_month.removeFocusListener(aFocus);
		spe_year.removeFocusListener(aFocus);
		edit_date.removeFocusListener(aFocus);
		spe_hour.removeFocusListener(aFocus);
		spe_minute.removeFocusListener(aFocus);
	}

}
