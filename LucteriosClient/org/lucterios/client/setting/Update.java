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

package org.lucterios.client.setting;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransport;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;

public class Update {
	class StreamThreader extends Thread {
		InputStream is;
		String type;

		StreamThreader(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					System.out.println(type + ">" + line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private HttpTransport mHttpTransport;

	public Update(HttpTransport httpTransport) {
		mHttpTransport = httpTransport;
		readVersion();
	}

	private int mVersionMax;
	private int mVersionMin;
	private int mVersionRev;
	private int mVersionBuild;

	private int getValue(String[] versions, int num) {
		try {
			if (num < versions.length)
				return Integer.parseInt(versions[num].trim());
			else
				return 0;
		} catch(Exception e){
			return 0;			
		}
	}

	public void readVersion() {
		try {
			String is = mHttpTransport.transfertFileFromServerString(
					"UpdateClients/java/version.txt", null);
			String[] versions = is.split(" ");
			mVersionMax = getValue(versions, 0);
			mVersionMin = getValue(versions, 1);
			mVersionRev = getValue(versions, 2);
			mVersionBuild = getValue(versions, 3);
		} catch (LucteriosException e) {
			mVersionMax = -1;
			mVersionMin = -1;
			mVersionRev = -1;
			mVersionBuild = -1;
		}
	}

	public String getVersion() {
		if (mVersionMax != -1)
			return mVersionMax + "." + mVersionMin + "." + mVersionRev + "."
					+ mVersionBuild;
		else
			return "";
	}

	public boolean isArchiveMostRecent(String currentVersion) {
		if (currentVersion=="")
			return false;
		else {
			String[] versions = currentVersion.replace('.', ' ').split(" ");
			int vMax = getValue(versions, 0);
			int vMin = getValue(versions, 1);
			int vRev = getValue(versions, 2);
			int vBuild = getValue(versions, 3);
			return isArchiveMostRecent(vMax, vMin, vRev, vBuild);
		}
	}

	public boolean isArchiveMostRecent(int vMax, int vMin, int vRev, int vBuild) {
		if (vMax < mVersionMax)
			return true;
		else if (vMax == mVersionMax) {
			if (vMin < mVersionMin)
				return true;
			else if (vMin == mVersionMin) {
				if (vRev < mVersionRev)
					return true;
				else if (vRev == mVersionRev) {
					if (vBuild < mVersionBuild)
						return true;
					else
						return false;
				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

	public boolean importFiles(String path) {
		try {
			mHttpTransport.saveFiles("UpdateClients/java/LucteriosUpdate.jar", path+"lib/LucteriosUpdate.jar");
			mHttpTransport.saveFiles("UpdateClients/java/JavaClient.zip", path+ "JavaClient.zip");
			return true;
		} catch (LucteriosException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean extractAndExit(String path, String AppName) {
		Runtime rt = Runtime.getRuntime();
		try {
			String java_dir = System.getProperty("java.home");
			String[] file_jar = { java_dir + "/bin/java", "-jar",
					path + "lib/LucteriosUpdate.jar", path + "JavaClient.zip",
					path, AppName };
			Logging.getInstance().writeLog("Update", file_jar.toString(), 1);
			Process proc = rt.exec(file_jar);
			if (proc != null) {
				StreamThreader ost = new StreamThreader(proc.getErrorStream(),
						"ERROR");
				ost.start();
				Logging.getInstance().writeLog("Update", "Exit", 1);
				Singletons.exit();
				return true;
			} else
				return false;
		} catch (IOException e) {
			return false;
		}
	}

	public void runDownloadAndExtract(String AppName) {
		Logging.getInstance().writeLog("Update", "Download and extract", 1);
		File file = new File("");
		String path = file.getAbsolutePath();
		if (path.charAt(path.length() - 1) != '/')
			path += "/";
		boolean good_download = importFiles(path);
		file = new File(path + "lib/LucteriosUpdate.jar");
		good_download = good_download && file.exists();
		file = new File(path + "JavaClient.zip");
		good_download = good_download && file.exists();
		if (good_download)
			extractAndExit(path, AppName);
		else
			JOptionPane.showMessageDialog(null, "Erreur de téléchargement",
					"Mise à jours", JOptionPane.ERROR_MESSAGE);
		JOptionPane.showMessageDialog(null,
				"Echec pour extraire la mise à jours", "Mise à jours",
				JOptionPane.ERROR_MESSAGE);
	}
}
