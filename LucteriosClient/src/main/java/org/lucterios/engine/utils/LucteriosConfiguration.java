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

package org.lucterios.engine.utils;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.lucterios.gui.GridInterface;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.SimpleParsing;

public class LucteriosConfiguration implements GridInterface {
	public final static String CONF_FILE_NAME = "LucteriosClient.conf";
	public final static int DEFAULT_PORT = 80;

	public final static int MODE_NORMAL = 0;
	public final static int MODE_SECURITY = 1;

	public final static String[] MODE_TEXTS = new String[] { "Normal", "Sécurisé" };

	public class Server {
		
		public final static String CONNEXION_CAT = "connexion";
		public final static String LOGON_CNP = "logon";
		public final static String PASSWORD_CNP = "password";
		
		public Server(String aServerName, String aHostName, int aHostPort,
				String aDirectory, int aConnectionMode, boolean aUseProxy) {
			ServerName = aServerName;
			HostName = aHostName;
			HostPort = aHostPort;
			Directory = aDirectory;
			if ((Directory.length() == 0)
					|| (Directory.charAt(Directory.length() - 1) != '/'))
				Directory = Directory + "/";
			ConnectionMode = aConnectionMode;
			UseProxy = aUseProxy;
		}

		public String ServerName;
		public String HostName;
		public int HostPort;
		public String Directory;
		public int ConnectionMode;
		public boolean UseProxy;

		public String toString() {
			return ServerName;
		}
		
		public IniFileManager getPreference() {
			try {
				StringBuilder filename = new StringBuilder();
				for (char my_char : ServerName.toCharArray()) {
				  if (my_char=='.' || Character.isJavaIdentifierPart(my_char)) {
				    filename.append(my_char);
				  }
				}				
				return new IniFileManager(getStoragePath()+String.format("%s.pref", filename));
			} catch (IOException e) {
				return null;
			}
		}
	}

	private ArrayList<Server> mServers;
	private File mStoragePath;
	private ArrayList<GUIActionListener> mRefreshListener=new ArrayList<GUIActionListener>();

	public void addRefreshListener(GUIActionListener l){
		mRefreshListener.add(l);
	}

	public void removeRefreshListener(GUIActionListener l){
		mRefreshListener.remove(l);
	}
	
	public void refreshPerformed() {
		for(GUIActionListener l:mRefreshListener)
			if (l!=null)
				l.actionPerformed();
	}

	public Server newServer(String aServerName, String aHostName,
			int aHostPort, String aDirectory, int aConnectionMode, boolean aUseProxy) {
		return new Server(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode, aUseProxy);
	}

	public LucteriosConfiguration(File storagePath) throws IOException {
		mStoragePath=storagePath;
		mServers = new ArrayList<Server>();
		if (!new java.io.File(storagePath,CONF_FILE_NAME).exists()) {
			AddServer("Demo sd-libre.fr","demo.sd-libre.fr",443,"/",MODE_SECURITY,true);
			write();
		}
		read();
	}

	public LucteriosConfiguration(LucteriosConfiguration configuration) {
		mServers = new ArrayList<Server>();
		mServers.addAll(configuration.mServers);
		TitreDefault = configuration.TitreDefault;
		ProxyAdress = configuration.ProxyAdress;
		ProxyPort = configuration.ProxyPort;
		mStoragePath=configuration.getStoragePath();
	}

	public String TitreDefault = "Lucterios";
	public String ProxyAdress = "";
	public int ProxyPort = 0;

	public File	 getStoragePath(){
		return mStoragePath;
	}
	
	public int ServerCount() {
		return mServers.size();
	}

	public Server GetServer(int index) {
		return mServers.get(index);
	}

	public void SetServer(int index, Server server) {
		mServers.set(index, server);
	}

	public void DeleteServer(int index) {
		if ((index >= 0) && (index < mServers.size()))
			mServers.remove(index);
	}

	public void AddServer(Server aServer) {
		mServers.add(aServer);
	}

	public void AddServer(String aServerName, String aHostName, int aHostPort,
			String aDirectory, int aConnectionMode, boolean aUseProxy) {
		mServers.add(new Server(aServerName, aHostName, aHostPort, aDirectory,
				aConnectionMode, aUseProxy));
	}

	public void read() throws IOException {
		read(new java.io.File(mStoragePath,CONF_FILE_NAME));
	}

	public void read(java.io.File aConFile) throws IOException {
		mServers.clear();
		if (aConFile.exists()) {
			java.io.FileInputStream input = new java.io.FileInputStream(
					aConFile);
			java.io.BufferedReader lecture = new java.io.BufferedReader(
					new java.io.InputStreamReader(input));
			String ligne;
			String contenu = "";
			while ((ligne = lecture.readLine()) != null) {
				contenu += ligne;
			}
			lecture.close();
			read(contenu);
		}
	}

	public void read(String aConfigText) {
		SimpleParsing root = new SimpleParsing();
		root.parse(aConfigText);
		SimpleParsing title = root.getFirstSubTag("TITLE");
		TitreDefault = "";
		if (title != null)
			TitreDefault = title.getText();
		SimpleParsing proxy = root.getFirstSubTag("PROXY");
		ProxyAdress = "";
		if (proxy != null)
			ProxyAdress = proxy.getText();
		SimpleParsing proxy_port = root.getFirstSubTag("PROXY_PORT");
		ProxyPort = 0;
		if (proxy_port != null)
			ProxyPort = proxy_port.getCDataInt(0);
		SimpleParsing[] servers = root.getSubTag("SERVER");
		for (int item_idx = 0; item_idx < servers.length; item_idx++) {
			int port_host = servers[item_idx].getAttributeInt("port",
					DEFAULT_PORT);
			int mode = servers[item_idx].getAttributeInt("mode", MODE_NORMAL);
			boolean use_proxy = (servers[item_idx].getAttributeInt("proxy", 1)==1);
			AddServer(servers[item_idx].getAttribute("name"), servers[item_idx]
					.getAttribute("host"), port_host, servers[item_idx]
					.getAttribute("dir"), mode, use_proxy);
		}
	}

	public void write() throws java.io.IOException {
		FileWriter file_conf = new FileWriter(new File(mStoragePath,CONF_FILE_NAME));
		file_conf.write("<?xml version='1.0' encoding='ISO-8859-1'?>");
		file_conf.write("<CONFIG>\n");
		file_conf.write("\t<TITLE>" + TitreDefault + "</TITLE>\n");
		file_conf.write("\t<PROXY>" + ProxyAdress + "</PROXY>\n");
		file_conf.write("\t<PROXY_PORT>" + ProxyPort + "</PROXY_PORT>\n");
		for (int index = 0; index < ServerCount(); index++) {
			Server serv = GetServer(index);
			file_conf.write("\t<SERVER name='" + serv.ServerName + "' host='"
					+ serv.HostName + "' port='" + serv.HostPort + "' dir='"
					+ serv.Directory + "' mode='" + serv.ConnectionMode
					+ "' proxy='" + (serv.UseProxy?1:0)
					+ "'/>\n");
		}
		file_conf.write("</CONFIG>\n");
		file_conf.flush();
		file_conf.close();
		refreshPerformed();
	}

	public Vector<Server> getServers() {
		Vector<Server> servers = new Vector<Server>();
		for (int index = 0; index < ServerCount(); index++) {
			Server serv = GetServer(index);
			servers.add(serv);
		}
		return servers;
	}

	public int getColumnCount() {
		return 6;
	}

	public int getRowCount() {
		return ServerCount();
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Nom";
		case 1:
			return "Serveur";
		case 2:
			return "Mode";
		case 3:
			return "Port";
		case 4:
			return "Répertoire";
		case 5:
			return "Proxy";
		default:
			return "???";
		}
	}

	public String getId(int row) {
		return new Integer(row).toString();
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return GetServer(row).ServerName;
		case 1:
			return GetServer(row).HostName;
		case 2:
			return MODE_TEXTS[GetServer(row).ConnectionMode];
		case 3:
			return new Integer(GetServer(row).HostPort);
		case 4:
			return GetServer(row).Directory;
		case 5:
			return GetServer(row).UseProxy;
		default:
			return null;
		}
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
