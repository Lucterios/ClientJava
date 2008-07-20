package org.lucterios.client.utils;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Label;
import java.util.Date;

public class TimeLabel extends Label implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int REFRESH_PERIOD = 500;
	private Thread time_thread = null;

	public TimeLabel() throws HeadlessException {
		super();
		setFont(new Font("TimesRoman", Font.ITALIC, 9));
		setAlignment(Label.CENTER);
	}

	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(REFRESH_PERIOD);
			} catch (InterruptedException e) {
			}
			Date current_date = new Date();
			setText(java.text.DateFormat.getDateTimeInstance().format(
					current_date));
			repaint();
		}
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

}
