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

package org.lucterios.utils.graphic;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Date;
import java.util.Calendar;
import com.holub.ui.*;

public class MyDateSelectorPanel extends JPanel implements Date_selector
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] months =
	{	"Jan","Feb", "Mars","Avr", "Mai","Juin",
		"Juil","Aout","Sept","Oct","Nov","Dec"
	};

	private static final int DAYS_IN_WEEK = 7;	// days in a week
	private static final int MAX_WEEKS    = 6;	// maximum weeks in any month

	private Date selected = null;

	private Calendar calendar = Calendar.getInstance();
	{	
                calendar.set( Calendar.HOUR, 	0 );
		calendar.set( Calendar.MINUTE,	0 );
		calendar.set( Calendar.SECOND,	0 );
	}

	// The calendar that's displayed on the screen

	private final Calendar today = Calendar.getInstance();

	// An ActionListener that fields all events coming in from the
	// calendar
	//
	private final Button_handler day_listener  = new Button_handler();

	// "days" is not a two-dimensional array. I drop buttons into
	// a gridLayout and let the layout manager worry about
	// what goes where. The first buttion is the first day of the
	// first week on the grid, the 8th button is the first day of the
	// second week of the grid, and so forth.

	private JButton[] days = new JButton[ DAYS_IN_WEEK * MAX_WEEKS ];
	{	for( int i = 0; i <days.length; i++ )
		{
			JButton day = new JButton("--");
			days[i] = day;
			day.setBorder			(new EmptyBorder(1,2,1,2));
			day.setFocusPainted		(false);
			day.setActionCommand	("D");
			day.addActionListener	(day_listener);
			day.setOpaque			(false);
		}
	}

	/** Create a Date_selector representing the current date.
	 */
	public MyDateSelectorPanel()
	{	
		JPanel calendar_display = new JPanel();
		calendar_display.setOpaque(false);
		calendar_display.setBorder( BorderFactory.createEmptyBorder(5,3,0,1) );
		calendar_display.setLayout(new GridLayout(MAX_WEEKS /*rows*/, DAYS_IN_WEEK /*columns*/ ));

		for( int i = 0; i < days.length; ++i )
			calendar_display.add(days[i]);

		setOpaque( false );
		setLayout( new BorderLayout() );
		add(calendar_display, BorderLayout.CENTER);
		update_calendar_display();
	}

	public JPanel getTitledDateSelector(ActionListener aActionListener)
	{
		Titled_date_selector selector=new Titled_date_selector(new com.holub.ui.Navigable_date_selector(this));
		selector.addActionListener(aActionListener);
		return selector;
	}
	
	/** Create a Date_selector_panel for an arbitrary date.
	 *  @param initial_date Calendar will display this date. The specified
	 *  					date is highlighted as "today".
	 *  @see #Date_selector_panel(int,int,int)
	 */

	public MyDateSelectorPanel(Date initial_date)
	{	this();
		calendar.setTime( initial_date );
		today.	 setTime( initial_date );
		update_calendar_display();
	}

	/** Create a Date_selector_panel for an arbitrary date.
	 * @param year the full year (e.g. 2003)
	 * @param month the month id (0=january, 1=feb, etc. [this is the
	 * 			convention supported by the other date classes])
	 * @param day the day of the month. This day will be highlighted
	 * 			as "today" on the displayed calendar. Use 0 to suppress
	 * 			the highlighting.
	 *  @see #Date_selector_panel(Date)
	 */

	public MyDateSelectorPanel( int year, int month, int day )
	{	this();
		calendar.set(year,month,day);
		if( day != 0 )
			today.set(year,month,day);
		update_calendar_display();
	}

	/************************************************************************
	 * List of observers.
	 */

	private ActionListener subscribers = null;

	/** Add a listener that's notified when the user scrolls the
	 *  selector or picks a date.
	 *  @see Date_selector
	 */
    public synchronized void addActionListener(ActionListener l)
	{	subscribers = AWTEventMulticaster.add(subscribers, l);
    }

	/** Remove a listener.
	 *  @see Date_selector
	 */
    public synchronized void removeActionListener(ActionListener l)
	{	subscribers = AWTEventMulticaster.remove(subscribers, l);
	}

	/** Notify the listeners of a scroll or select
	 */
	private void fire_ActionEvent( int id, String command )
	{	if (subscribers != null)
			 subscribers.actionPerformed(new ActionEvent(this, id, command) );
	}

	/***********************************************************************
	 * Handle clicks from the buttons that represent calendar days.
	 */
	private class Button_handler implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{
			if ("D".equals( e.getActionCommand() ))
			{	String text = ((JButton) e.getSource()).getText();

				if(text.length() > 0)  //  <=0 means click on blank square. Ignore.
				{	calendar.set
					(	calendar.get(Calendar.YEAR),	// Reset the calendar
						calendar.get(Calendar.MONTH),	// to be the choosen
						Integer.parseInt(text)			// date.
					);
					selected = calendar.getTime();
					fire_ActionEvent( SELECT_ACTION, selected.toString() );
				}
			}
		}
	}

	//----------------------------------------------------------------------

	private JButton highlighted = null;

	private void clear_highlight()
	{	
		if( highlighted != null )
		{	highlighted.setBackground( Color.WHITE );
			highlighted.setForeground( Color.BLACK );
			highlighted.setOpaque(false);
			highlighted = null;
		}
	}

	private void highlight( JButton cell )
	{	
		highlighted = cell;
		cell.setBackground( com.holub.ui.Colors.DARK_RED );
		cell.setForeground( Color.WHITE );
		cell.setOpaque( true );
	}
	//----------------------------------------------------------------------

	/** Redraw the buttons that comprise the calandar to display the current month */

	private void update_calendar_display()
	{
		setVisible(false);	// improves paint speed & reduces flicker

		clear_highlight();

		// The buttons that comprise the calendar are in a single
		// dimentioned array that was added to a 6x7 grid layout in
		// order. Because of the linear structure, it's easy to
		// lay out the calendar just by changing the labels on
		// the buttons. Here's the algorithm used below
		//
		// 	1) find out the offset to the first day of the month.
		// 	2) clear everything up to that offset
		// 	3) add the days of the month
		// 	4) clear everything else

		int month = calendar.get(Calendar.MONTH);
		int year  = calendar.get(Calendar.YEAR);

		fire_ActionEvent( CHANGE_ACTION, months[month] + " " + year );

		calendar.set( year, month, 1 ); // first day of the current month.

		int first_day_offset = calendar.get(Calendar.DAY_OF_WEEK)-1;		/* 1 */
                if (first_day_offset<Calendar.SUNDAY)
                   first_day_offset=Calendar.SATURDAY;

		int i = 0;
		while( i < first_day_offset-1 )									/* 2 */
			days[i++].setText("");

		int day_of_month = 1;
		for(; i < days.length; ++i )									/* 3 */
		{	
			// Can't get calendar.equals(today) to work, so do it manually

			if(	calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH)
			&&	calendar.get(Calendar.YEAR )==today.get(Calendar.YEAR )
			&&	calendar.get(Calendar.DATE )==today.get(Calendar.DATE ) )
			{	highlight( days[i] );
			}

			days[i].setText( String.valueOf(day_of_month) );

			calendar.roll( Calendar.DATE, /*up=*/ true );	// forward one day

			day_of_month = calendar.get(Calendar.DATE);
			if( day_of_month == 1 )
				break;
		}

		// Note that we break out of the previous loop with i positioned
		// at the last day we added, thus the following ++ *must* be a
		// preincrement becasue we want to start clearing at the cell
		// after that.

		while( ++i < days.length )										/* 4 */
			days[i].setText("");

		setVisible(true);
	}

	/** Create a naviagion button with an image appropriate to the caption.
	 *	The <code>caption</code> argument is used as the button's "action command."
	 *	This method is public only because it has to be. (It overrides a public
	 *	method.) Pretend it's not here.
	 */

	public void addNotify()
	{	
		super.addNotify();
		int month = calendar.get(Calendar.MONTH);
		int year  = calendar.get(Calendar.YEAR);
		fire_ActionEvent( CHANGE_ACTION, months[month] + " " + year );
	}

	/**	Returns the {@link Date Date} selected by the user or null if
	 *  the window was closed without selecting a date. The returned
	 *  Date has hours, minutes, and seconds values of 0.
	 */

	public Date get_selected_date()
	{	return selected;
	}

	/** Returns the currently displayed {@link Date Date}.  */
	public Date get_current_date()
	{	return calendar.getTime();
	}

	/** Works just like {@link Calendar#roll(int,boolean)}.  */
	public void roll(int field, boolean up)
	{	calendar.roll(field,up);
		update_calendar_display();
	}

	/** Works just like {@link Calendar#roll(int,int)}.  */
	public void roll(int field, int amount)
	{	calendar.roll(field,amount);
		update_calendar_display();
	}

	/** Works just like {@link Calendar#set(int,int,int)} 
	 *	Sets "today" (which is higlighted) to the indicated day.
	 */
	public void set( int year, int month, int date )
	{	
                calendar.set(year,month,date);
		today.set(year,month,date);
		update_calendar_display();
	}

	/** Works just like {@link Calendar#get(int)} */
	public int get( int field )
	{	return calendar.get(field);
	}

	/** Works just like {@link Calendar#setTime(Date)},
	 *	Sets "today" (which is higlighted) to the indicated day.
	 */
	public void setTime( Date d )
	{	calendar.setTime(d);
		today.setTime(d);
		update_calendar_display();
	}

	/** Works just like {@link Calendar#getTime} */
	public Date getTime( )
	{	return calendar.getTime();
	}

	/** Return a Calendar object that represents the currently-displayed
	 *  month and year. Modifying this object will not affect the
	 *  current panel.
	 *  @return a Calendar representing the panel's state.
	 */

	public Calendar get_calendar()
	{	Calendar c = Calendar.getInstance();
		c.setTime( calendar.getTime() );
		return c;
	}

	/** Change the display to match the indicated calendar. This Calendar
	 *  argument is used only to provide the new date/time information.
	 *  Modifying it after a call to the current method will not affect
	 *  the Date_selector_panel at all.
	 *	Sets "today" (which is higlighted) to the indicated day.
	 *  @param calendar A calendar positioned t the date to display.
	 */

	public void set_from_calendar(Calendar calendar)
	{	
                this.calendar.setTime( calendar.getTime() );
                this.calendar.setFirstDayOfWeek(Calendar.MONDAY);
		today.setTime( calendar.getTime() );
		update_calendar_display();
	}
	//----------------------------------------------------------------------
	/*private static class Test
	{	public static void main( String[] args )
		{	JFrame frame = new JFrame();
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			frame.getContentPane().setLayout( new FlowLayout() );

			Date_selector left 	 = new Titled_date_selector(new Navigable_date_selector());
			Date_selector center = new Navigable_date_selector();
			Date_selector right  = new Date_selector_panel(1900,1,2);

			((Navigable_date_selector)center).change_navigation_bar_color(null); // transparent

			ActionListener l =
				new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{	System.out.println( e.getActionCommand() );
					}
				};

			left.addActionListener	(l);
			center.addActionListener(l);
			right.addActionListener	(l);

			JPanel white = new JPanel();				// proove that it's transparent.
			white.setBackground(Color.WHITE);
			white.add( (JPanel)center );

			frame.getContentPane().add( (JPanel)left    );	// I hate these casts, but they're
			frame.getContentPane().add( 		white );	// mandated by the fact that
			frame.getContentPane().add( (JPanel)right    );	// Component is not an interface.

			frame.pack();
			frame.show();
		}
	}*/
}



