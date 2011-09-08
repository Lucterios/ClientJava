package org.lucterios.graphic.date;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.StringList;

public class DatePickerSimple implements DialogVisitor {
	private static final long serialVersionUID = 1L;

	static public int MIN_YEAR = 1930;
	static public int MAX_YEAR = 2015;

	public static final int DAYS_IN_WEEK = 7; // days in a week
	public static final int MAX_WEEKS = 6; // maximum weeks in any month

	private int mSelectColor = 0x00000A;
	private int mUnselectColor = 0xFFFFFF;

	private Date selected = null;

	private GUILabel title=null;

	private String[] months = { "Janvier", "Febrier", "Mars", "Avril", "Mai", "Juin",
			"Juillet", "Août", "Septembre", "Octobre", "Novembre", "Decembre" };

	private static final String[] ImageList = new String[]{"arrow.left.double.png",
		"arrow.left.png","center.png","arrow.right.png","arrow.right.double.png"};

	private GUIContainer navigation = null;
	
	private Calendar calendar = Calendar.getInstance();
	{
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
	}

	private final Calendar today = Calendar.getInstance();

	private GUIButton[] dayList=new GUIButton[MAX_WEEKS*DAYS_IN_WEEK];
	private String[] dayText=new String[]{"L","M","M","J","V","S","D"};
	private GUIContainer mPanel;

	private GUIDialog mDialog;
	
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		dialog.setTitle("Date");
		mPanel=dialog.getContainer();
		buildGUI();
	}

	public static StringList getMonthList() {
		StringList month_list = new StringList();
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

	public void buildGUI() {
		mSelectColor = 0xFF0000;
		mUnselectColor = 0xFFFFFF;
		for (int dayIdx = 0; dayIdx < DAYS_IN_WEEK; ++dayIdx) {
			GUILabel new_lbl=mPanel.createLabel(new GUIParam(dayIdx,1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
			new_lbl.setBackgroundColor(mUnselectColor);
			new_lbl.setTextString(dayText[dayIdx]);
		}
		
		int nb=0;		
		for (int weekIdx = 0; weekIdx < MAX_WEEKS; ++weekIdx) {
			for (int dayIdx = 0; dayIdx < DAYS_IN_WEEK; ++dayIdx) {
				GUIButton new_btn=mPanel.createButton(new GUIParam(dayIdx,weekIdx+2));
				new_btn.addActionListener(new ButtonHandler(new_btn));
				new_btn.setBackgroundColor(mUnselectColor);
				dayList[nb++]=new_btn;				
			}
		}	
		title = mPanel.createLabel(new GUIParam(0, 0, DAYS_IN_WEEK,
				1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		navigation = mPanel.createContainer(ContainerType.CT_NORMAL,
				new GUIParam(0, MAX_WEEKS + 2,DAYS_IN_WEEK, 1,
						ReSizeMode.RSM_HORIZONTAL, FillMode.FM_BOTH));
		make_navigation_button(0);
		make_navigation_button(1);
		make_navigation_button(2);
		make_navigation_button(3);
		make_navigation_button(4);
		update_calendar_display();
		mPanel.calculBtnSize(dayList);	

		addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				try{
					mDialog.setVisible(false);
				}catch (Exception e) {
					ExceptionDlg.throwException(e);
				}
			}
		});
		setFromCalendar(java.util.Calendar.getInstance());
	}

	public int day() {
		return getCalendar().get(java.util.Calendar.DAY_OF_MONTH);
	}

	public int month() {
		return getCalendar().get(java.util.Calendar.MONTH);
	}

	public int year() {
		return getCalendar().get(java.util.Calendar.YEAR);
	}

	public void close() {
		mDialog.close();
	}

	public void setVisible(boolean isVisible) {
		try{
			mDialog.setVisible(isVisible);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}
	
	// -------------
	
	private class NavigationHandler implements GUIActionListener {
		private int direction;

		public NavigationHandler(int direction) {
			this.direction = direction;
		}

		public void actionPerformed() {
			if (direction == 4)
				roll(Calendar.YEAR, true);
			else if (direction == 0)
				roll(Calendar.YEAR, false);
			else if (direction == 3) {
				roll(Calendar.MONTH, true);
				if (get(Calendar.MONTH) == Calendar.JANUARY)
					roll(Calendar.YEAR, true);
			} else if (direction == 1) {
				roll(Calendar.MONTH, false);
				if (get(Calendar.MONTH) == Calendar.DECEMBER)
					roll(Calendar.YEAR, false);
			} else {
				calendar.setTime(today.getTime());
				update_calendar_display();
			}
		}
	}

	private GUIButton make_navigation_button(int imageId) {
		URL image = getClass().getResource(ImageList[imageId]);
		GUIButton b = navigation.createButton(new GUIParam(navigation.count(),
				0, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
		if (image != null)
			b.setImage(image);
		else
			b.setTextString(""+imageId);
		b.addActionListener(new NavigationHandler(imageId));
		return b;
	}
	
	/************************************************************************
	 * List of observers.
	 */

	private java.util.List<Object> subscribers = new ArrayList<Object>();

	/**
	 * Add a listener that's notified when the user scrolls the selector or
	 * picks a date.
	 * 
	 * @see Date_selector
	 */
	public synchronized void addActionListener(GUIActionListener l) {
		subscribers.add(l);
	}

	/**
	 * Remove a listener.
	 * 
	 * @see Date_selector
	 */
	public synchronized void removeActionListener(GUIActionListener l) {
		subscribers.remove(l);
	}

	/**
	 * Notify the listeners of a scroll or select
	 */
	private void fireActionEvent() {
		for(Object item:subscribers)
			((GUIActionListener)item).actionPerformed();
	}

	/***********************************************************************
	 * Handle clicks from the buttons that represent calendar days.
	 */
	private class ButtonHandler implements GUIActionListener {
		
		private GUIButton mOvner;
		
		public ButtonHandler(GUIButton ovner){
			mOvner=ovner;
		}
		
		public void actionPerformed() {
			String text = mOvner.getTextString();
			if (text.length() > 0) // <=0 means click on blank square.
			{
				calendar.set(calendar.get(Calendar.YEAR), // Reset the calendar
						calendar.get(Calendar.MONTH), // to be the choosen
						Integer.parseInt(text) // date.
						);
				selected = calendar.getTime();
				fireActionEvent();
			}
		}
	}

	// ----------------------------------------------------------------------

	private GUIButton highlighted = null;

	private void clear_highlight() {
		if (highlighted != null) {
			highlighted.setBackgroundColor(mUnselectColor);
			highlighted = null;
		}
	}

	private void highlight(GUIButton cell) {
		highlighted = cell;
		cell.setBackgroundColor(mSelectColor);
	}

	// ----------------------------------------------------------------------

	/**
	 * Redraw the buttons that comprise the calandar to display the current
	 * month
	 */

	private void update_calendar_display() {
		clear_highlight();
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		title.setTextString(months[month] + " " + year);
		calendar.set(year, month, 1);
		int first_day_offset = calendar.get(Calendar.DAY_OF_WEEK)-2;
		if (first_day_offset<0)
			first_day_offset=6;
		for (int day_num=0; day_num < dayList.length; day_num++) {
			dayList[day_num].setTextString("");
			if ((day_num >= first_day_offset) && (first_day_offset>=0)) {		
				if (calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
						&& calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
						&& calendar.get(Calendar.DATE) == today.get(Calendar.DATE)) {
					highlight(dayList[day_num]);
				}
				int day_of_month = calendar.get(Calendar.DATE);
				dayList[day_num].setTextString(String.valueOf(day_of_month));
				calendar.roll(Calendar.DATE,true);
				if (calendar.get(Calendar.DATE)==1)
					first_day_offset=-1;
			}
		}
	}

	/**
	 * Returns the {@link Date Date} selected by the user or null if the window
	 * was closed without selecting a date. The returned Date has hours,
	 * minutes, and seconds values of 0.
	 */

	public Date getSelectedDate() {
		return selected;
	}

	/** Returns the currently displayed {@link Date Date}. */
	public Date getCurrentDate() {
		return calendar.getTime();
	}

	/** Works just like {@link Calendar#roll(int,boolean)}. */
	public void roll(int field, boolean up) {
		calendar.roll(field, up);
		update_calendar_display();
	}

	/** Works just like {@link Calendar#roll(int,int)}. */
	public void roll(int field, int amount) {
		calendar.roll(field, amount);
		update_calendar_display();
	}

	/**
	 * Works just like {@link Calendar#set(int,int,int)} Sets "today" (which is
	 * higlighted) to the indicated day.
	 */
	public void set(int year, int month, int date) {
		calendar.set(year, month, date);
		today.set(year, month, date);
		if (title!=null)
			update_calendar_display();
	}

	/** Works just like {@link Calendar#get(int)} */
	public int get(int field) {
		return calendar.get(field);
	}

	/**
	 * Return a Calendar object that represents the currently-displayed month
	 * and year. Modifying this object will not affect the current panel.
	 * 
	 * @return a Calendar representing the panel's state.
	 */

	public Calendar getCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(calendar.getTime());
		return c;
	}

	/**
	 * Change the display to match the indicated calendar. This Calendar
	 * argument is used only to provide the new date/time information. Modifying
	 * it after a call to the current method will not affect the
	 * Date_selector_panel at all. Sets "today" (which is higlighted) to the
	 * indicated day.
	 * 
	 * @param calendar
	 *            A calendar positioned t the date to display.
	 */

	public void setFromCalendar(Calendar calendar) {
		this.calendar.setTime(calendar.getTime());
		this.calendar.setFirstDayOfWeek(Calendar.MONDAY);
		today.setTime(calendar.getTime());
		update_calendar_display();
	}

	public void closing() { }
	
}
