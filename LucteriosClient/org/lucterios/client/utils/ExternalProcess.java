package org.lucterios.client.utils;

import java.lang.Thread;
import java.lang.Runtime;
import java.lang.Process;
import java.io.IOException;
import java.lang.IllegalThreadStateException;

import org.lucterios.engine.presentation.Singletons;

public class ExternalProcess extends Thread
{
	public interface ProcessNotification
	{
		void returnError(String aError);
	}
	
	public static ProcessNotification Notification=null;
	private static final int NO_EXIT=Integer.MIN_VALUE;
	private static final int NO_TIMEOUT=Integer.MIN_VALUE;
	
	private Process mProc=null;
	private String mName="";
	private boolean mFinish=false;

	public ExternalProcess(String aCommand)
	{
		super();
		InitProcess(aCommand);
	}
	
	public ExternalProcess(String aName,String aCommand)
	{
		super();
		InitProcess(aCommand);
		mName=aName;
		start();
	}

	private void InitProcess(String aCommand)
	{
		try
		{
			mProc=Runtime.getRuntime().exec(aCommand);
		}catch(IOException e){
			mProc=null;
		}
	}

	public void Terminate()
	{
		mFinish=true;
	}

	private void returnErrorAndExit(final String aError)
	{
		Singletons.getWindowGenerator().invokeLater(new Runnable(){
			public void run(){			
				if (Notification!=null)
					Notification.returnError(aError);
				Runtime.getRuntime().exit(0);
			}
		});
	}

	private int loopOnExit(int aTimeOut) {
	    int timeout = aTimeOut;
	    int exitValue = NO_EXIT;
		mFinish=false;
		while (!mFinish && (mProc!=null)){
	        try {
	            exitValue = mProc.exitValue();
	            mFinish = true;
	        }
	        catch (IllegalThreadStateException e) {
	            try {
	                Thread.sleep(300);
	                if (aTimeOut!=NO_TIMEOUT){
		                timeout = timeout - 300;
		                if (timeout < 0 && timeout >= -300) {
		                	mProc.destroy();
		                }
		                else if (timeout <0) {
		                    Thread.sleep(1000);
		                }
	                }
	            } catch (InterruptedException e1) {
	            }
	        }    
	    }
	    return exitValue;
	}
	
	public void run() {
		int exit=loopOnExit(NO_TIMEOUT);
		if (exit!=NO_EXIT)
			returnErrorAndExit("Fin du programme "+mName);
		else if (mProc==null)
			returnErrorAndExit("Le programme "+mName+" ne démarre pas!");
		else
			mProc.destroy();
    }
	
	public static boolean executeCommand(String exCommand, int exTimeout) {
		ExternalProcess proc=new ExternalProcess(exCommand);
		int exitValue = proc.loopOnExit(exTimeout);
	    if (proc.mFinish & (exitValue != 0)) 
	            System.out.println("Exit code " + exitValue + " - " + exCommand);
	    return (exitValue == 0);
	}
	
}