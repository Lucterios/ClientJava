package org.lucterios.client.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.lucterios.utils.graphic.MyDateSelectorPanel;

public class DatePickerSimple extends JDialog {
	private static final long serialVersionUID = 1L;

	static public int MIN_YEAR = 1930;
	static public int MAX_YEAR = 2015;
	
	MyDateSelectorPanel selector;

	public DatePickerSimple() {
		super();
		this.setVisible(false);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("Date");
		this.toFront();
		this.setLocationRelativeTo(null);
		buildGUI();
	}

	public MyDateSelectorPanel getSelector(){
		return selector;
	}
	
	public static Vector getMonthList(){
		Vector month_list = new Vector();
		month_list.add("Janvier");
		month_list.add("Février");
		month_list.add("Mars");
		month_list.add("Avril");
		month_list.add("Mai");
		month_list.add("Juin");
		month_list.add("Juillet");
		month_list.add("Août");
		month_list.add("Septembre");
		month_list.add("Octobre");
		month_list.add("Novembre");
		month_list.add("Décembre");
		return month_list;
	}

	public static String convertIntToStr(long value, int digit) {
		String result = new Long(value).toString();
		while (result.length() < digit)
			result = "0" + result;
		return result;
	}
	
	public String getDate() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Calendar cal = selector.get_calendar();
		return sdf.format(cal.getTime());
	}

	public void buildGUI() {
		selector = new MyDateSelectorPanel();
		JPanel pnl_select = selector.getTitledDateSelector(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		selector.set_from_calendar(java.util.Calendar.getInstance());
		getContentPane().add(pnl_select);
		pack();
	}

	public int day() {
		return selector.get_calendar().get(java.util.Calendar.DAY_OF_MONTH);
	}

	public int month() {
		return selector.get_calendar().get(java.util.Calendar.MONTH);
	}

	public int year() {
		return selector.get_calendar().get(java.util.Calendar.YEAR);
	}
}
