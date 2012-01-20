package org.lucterios.graphic;

import java.util.ArrayList;
import java.util.Date;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.ui.GUIActionListener;

public class TimeLabel implements Runnable, GUIActionListener {

	public static final int REFRESH_PERIOD = 500;

	private Thread time_thread = null;
	private GUIContainer mOwner;
	private GUILabel mLabel;

	public TimeLabel(GUIContainer owner,GUIParam param) {
		mOwner=owner;
		mLabel=mOwner.createLabel(param);
		mLabel.setStyle(2);
		addActionListener(this);
		showTime();
	}

	public GUILabel getLabel() {
		return mLabel;
	}
	
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(REFRESH_PERIOD);
			} catch (InterruptedException e) {
			}
			for (GUIActionListener listener : mActionListeners) {
				listener.actionPerformed();
			}
			mLabel.repaint();
		}
	}

	private ArrayList<GUIActionListener> mActionListeners = new ArrayList<GUIActionListener>();

	public void addActionListener(GUIActionListener alistener) {
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

	private void showTime() {
		Date current_date = new Date();
		String message = java.text.DateFormat.getDateTimeInstance()
				.format(current_date);
		mLabel.setTextString(message);
	}
	
	public void actionPerformed() {
		mOwner.invokeLater(new Runnable() {
			public void run() {
				showTime();
			}
		});
	}


}
