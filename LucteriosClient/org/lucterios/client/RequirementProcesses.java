package org.lucterios.client;

import java.io.IOException;
import java.util.Vector;

import org.lucterios.client.presentation.Singletons.ApplicationTerminate;
import org.lucterios.utils.ExternalProcess;
import org.lucterios.utils.Logging;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.ExceptionDlg;

public class RequirementProcesses implements ExternalProcess.ProcessNotification,ApplicationTerminate {
	
	private Vector mProcessList=new Vector(); 

	public final static String CONF_FILE_NAME = "RequirementProcesses.xml";
	
	public RequirementProcesses(){
		ExternalProcess.Notification=this;
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read() throws IOException{
		java.io.File conf_file = new java.io.File(CONF_FILE_NAME);
		if (conf_file.exists()) {
			java.io.FileInputStream input = new java.io.FileInputStream(conf_file);
			java.io.BufferedReader lecture = new java.io.BufferedReader(new java.io.InputStreamReader(input));
			String ligne;
			String contenu = "";
			while ((ligne = lecture.readLine()) != null) {
				contenu += ligne;
			}
			read(contenu);
		}		
	}

	public void read(String aConfigText) throws IOException {
		String os_name=System.getProperty("os.name").toLowerCase();
		String os_arch=System.getProperty("os.arch").toLowerCase();
		Logging.getInstance().writeLog("RequirementProcesses", "OS Name="+os_name+" - OS Archi="+os_arch, 1);
		SimpleParsing root = new SimpleParsing();
		if (root.parse(aConfigText)) {
			SimpleParsing[] plateform=root.getSubTag("PLATFORM");
			for(int index=0;index<plateform.length;index++)
				if (os_name.equals(plateform[index].getAttribut("name")) && os_arch.equals(plateform[index].getAttribut("archi")))
					manageArchi(plateform[index]);
		}
	}
	
	private void manageArchi(SimpleParsing aArchi) throws IOException{
		for(int index=0;index<aArchi.getTagCount();index++)
		{
			SimpleParsing action=aArchi.getSubTag(index);
			if ("CMD".equalsIgnoreCase(action.getTagName()))
				ExternalProcess.executeCommand(action.getCData(),60000);
			else if ("PROCESS".equalsIgnoreCase(action.getTagName()))
				addProcessor(action.getAttribut("name"),action.getCData());			
		}
	}
	
	private void addProcessor(String aName,String aCommand){
		ExternalProcess proc=new ExternalProcess(aName,aCommand);
		mProcessList.add(proc);
	}	
	
	public void exit() {
		for(int index=0;index<mProcessList.size();index++){
			ExternalProcess proc=(ExternalProcess)mProcessList.get(index);
			proc.Terminate();
		}
	}

	public void returnError(String error) {
		ExceptionDlg.show(ExceptionDlg.IMPORTANT,error,"","");
	}

}
