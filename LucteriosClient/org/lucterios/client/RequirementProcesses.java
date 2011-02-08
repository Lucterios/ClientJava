package org.lucterios.client;

import java.io.IOException;
import java.util.ArrayList;

import org.lucterios.client.utils.ExternalProcess;
import org.lucterios.engine.presentation.Singletons.ApplicationTerminate;
import org.lucterios.utils.Logging;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;

public class RequirementProcesses implements
		ExternalProcess.ProcessNotification, ApplicationTerminate {

	private ArrayList<ExternalProcess> mProcessList = new ArrayList<ExternalProcess>();

	public final static String CONF_FILE_NAME = "RequirementProcesses.xml";

	public RequirementProcesses() {
		ExternalProcess.Notification = this;
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read() throws IOException {
		java.io.File conf_file = new java.io.File(CONF_FILE_NAME);
		if (conf_file.exists()) {
			java.io.FileInputStream input = new java.io.FileInputStream(
					conf_file);
			java.io.BufferedReader lecture = new java.io.BufferedReader(
					new java.io.InputStreamReader(input));
			String ligne;
			String contenu = "";
			while ((ligne = lecture.readLine()) != null) {
				contenu += ligne;
			}
			read(contenu);
		}
	}

	public void read(String aConfigText) throws IOException {
		String os_name = System.getProperty("os.name").toLowerCase();
		String os_arch = System.getProperty("os.arch").toLowerCase();
		Logging.getInstance().writeLog("RequirementProcesses",
				"OS Name=" + os_name + " - OS Archi=" + os_arch, 1);
		SimpleParsing root = new SimpleParsing();
		if (root.parse(aConfigText)) {
			SimpleParsing[] plateforms = root.getSubTag("PLATFORM");
			for (SimpleParsing plateform : plateforms)
				if (os_name.equals(plateform.getAttribut("name"))
						&& os_arch.equals(plateform.getAttribut("archi")))
					manageArchi(plateform);
		}
	}

	private void manageArchi(SimpleParsing aArchi) throws IOException {
		for (int index = 0; index < aArchi.getTagCount(); index++) {
			SimpleParsing action = aArchi.getSubTag(index);
			if ("CMD".equalsIgnoreCase(action.getTagName()))
				ExternalProcess.executeCommand(action.getText(), 60000);
			else if ("PROCESS".equalsIgnoreCase(action.getTagName()))
				addProcessor(action.getAttribut("name"), action.getText());
		}
	}

	private void addProcessor(String aName, String aCommand) {
		ExternalProcess proc = new ExternalProcess(aName, aCommand);
		mProcessList.add(proc);
	}

	public void exit() {
		for (ExternalProcess proc : mProcessList) {
			proc.Terminate();
		}
	}

	public void returnError(String error) {
		ExceptionDlg.show(ExceptionDlg.IMPORTANT, error, "", "");
	}

}
