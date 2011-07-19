package org.lucterios.client.utils;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TimeLabel extends JLabel implements Runnable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int REFRESH_PERIOD = 500;

	private Thread time_thread = null;

	public TimeLabel() throws HeadlessException {
		super();
		setFont(new Font("TimesRoman", Font.ITALIC, 9));
		setVerticalAlignment(CENTER);
		addActionListener(this);
	}

	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(REFRESH_PERIOD);
			} catch (InterruptedException e) {
			}
			for (ActionListener listener : mActionListeners) {
				listener.actionPerformed(null);
			}
			repaint();
		}
	}

	private ArrayList<ActionListener> mActionListeners = new ArrayList<ActionListener>();

	public void addActionListener(ActionListener alistener) {
		mActionListeners.add(alistener);
	}

	public void start() {
		stop();
		time_thread = new Thread(this);
		time_thread.setPriority(Thread.MAX_PRIORITY);
		time_thread.start();
	}

	public void stop() {
		if (time_thread != null) {
			time_thread.interrupt();
			time_thread = null;
		}
	}

	public void actionPerformed(ActionEvent arg0) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Date current_date = new Date();
				String message = java.text.DateFormat.getDateTimeInstance()
						.format(current_date);
				setText(message);
			}
		});
	}

}
