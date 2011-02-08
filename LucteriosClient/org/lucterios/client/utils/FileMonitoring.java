package org.lucterios.client.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class FileMonitoring implements Runnable {
	
	public interface MonitoringCallback {
		void fileModified();
	}

	private MonitoringCallback m_Callback=null;
	private File m_file;
	private long m_ref_date;
	private Thread m_Monitor;
	private boolean mTerminated=false;

	public FileMonitoring(File aFile,MonitoringCallback aCallback) {
		m_Callback=aCallback;
		m_file=aFile;
		m_ref_date=m_file.lastModified(); 
		m_Monitor=new Thread(this);
		start();
	}
		
	public void start() {
		mTerminated=false;
		m_Monitor.start();
	}

	public void stop() {
		mTerminated=true;
	}
	
	private boolean mflag=false;
	private void notifyCaller() {
		if (!mflag) 
		try{
			mflag=true;
			try {
				javax.swing.SwingUtilities.invokeAndWait(new Runnable(){
					public void run() {
						m_Callback.fileModified();
					}
				});
			} catch (InterruptedException e) {}
			catch (InvocationTargetException e) {}			
		}finally{
			mflag=false;
		}
	}
	
	public void run() {
		try {
			while (!mTerminated) {
				Thread.sleep(1000);
				if (m_ref_date!=m_file.lastModified()) {
					notifyCaller();
					m_ref_date=m_file.lastModified();
				}
			}
		} catch (InterruptedException e) {}
	}

}
