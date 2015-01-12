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

package org.lucterios.engine.presentation;

import java.io.File;
import java.io.IOException;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.transport.HttpTransport;
import org.lucterios.engine.utils.LucteriosConfiguration;
import org.lucterios.engine.utils.LucteriosSetting;
import org.lucterios.engine.utils.LucteriosConfiguration.Server;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.Tools;

public class Singletons {
	
	private static final String ERREUR_SINGLETON_NULL = "Erreur Singleton:%s null";
	static final public String LUCTERIOS_CONFIG = ".LucteriosSetting";
	static final public String TEMP_DIR=".LucteriosTemp";
	
	public interface ApplicationTerminate{
		void exit();
	}

	private static LucteriosSetting gLucteriosSettingFile=null;
	
	private static DesktopInterface gDesktop=null;
	
	private static ApplicationTerminate gAppTerminate=null;
	
	private static Class<? extends HttpTransport> gHttpTransportClass=null;

	private static Class<? extends Action> gActionClass=null;

	private static ObserverFactory gFactory=null;

	private static LucteriosConfiguration gConfiguration = null;

	private static GUIGenerator gWindowGenerator = null;

	private static Server gLastServer = null;

	//------------------------------------------------------------------
	
	public static void setLastServer(Server lastServer) {
		if (lastServer!=null)
			gLastServer = lastServer;
	}

	public static Server getLastServer() {
		return gLastServer;
	}
	
	public static LucteriosConfiguration getConfiguration() {
		if (gConfiguration==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gConfiguration"));
		return gConfiguration;
	}

	public static void setActionClass(Class<? extends Action> gActionClass) {
		Singletons.gActionClass = gActionClass;
	}

	public static void setHttpTransportClass(Class<? extends HttpTransport> gHttpTransportClass) {
		Singletons.gHttpTransportClass = gHttpTransportClass;
	}

	public static void setAppTerminate(ApplicationTerminate gAppTerminate) {
		Singletons.gAppTerminate = gAppTerminate;
	}

	public static void setDesktop(DesktopInterface gDesktop) {
		Singletons.gDesktop = gDesktop;
		Singletons.gDesktop.setTempPath(TEMP_DIR);
	}

	public static DesktopInterface getDesktop() {
		if (gDesktop==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gDesktop"));
		return gDesktop;
	}

	public static LucteriosSetting getLucteriosSettingFile() {
		if (gLucteriosSettingFile==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gLucteriosSettingFile"));
		return gLucteriosSettingFile;
	}


	public static void setWindowGenerator(GUIGenerator gWindowGenerator) {
		Singletons.gWindowGenerator = gWindowGenerator;
	}

	public static GUIGenerator getWindowGenerator() {
		if (gWindowGenerator==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gWindowGenerator"));
		return gWindowGenerator;
	}
	
	//------------------------------------------------------------------
	
	static public HttpTransport Transport() {
		if (gHttpTransportClass==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gHttpTransportClass"));
		HttpTransport new_transport = null;
		try {
			new_transport = (HttpTransport) gHttpTransportClass.newInstance();
			new_transport.setDesktop(gDesktop);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new_transport;
	}
	

	static public Action CreateAction() {
		if (gActionClass==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gActionClass"));
		Action new_action=null;
		try {
			new_action = (Action) gActionClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new_action;
	}
	
	
 	static public ObserverFactory Factory() {
		if (gFactory==null)
			throw new RuntimeException(String.format(ERREUR_SINGLETON_NULL,"gFactory"));
		gFactory.setHttpTransport(Transport());
		return gFactory;
	}
 	
 	static public void initalize(LucteriosConfiguration config,ObserverFactory factory){
		gConfiguration=config;
		gFactory=factory;
 	}
 	
 	static public void initalize(File storagePath) throws IOException {
		gConfiguration=new LucteriosConfiguration(storagePath);
		gFactory=new ObserverFactoryImpl();
		File cache_dir=new File(storagePath,TEMP_DIR);
		if (cache_dir.isDirectory())
			Tools.deleteDir(cache_dir);
		if (!cache_dir.isDirectory())
			cache_dir.mkdir();
		loadSetting(storagePath);
	}
 	

 	static public void loadSetting(File storagePath) throws IOException {
		gLucteriosSettingFile=new LucteriosSetting(storagePath.getAbsolutePath()+LUCTERIOS_CONFIG);
		if (gDesktop!=null)
			gDesktop.initApplicationsSetting(gLucteriosSettingFile);
	}
	

 	static public void exit(){
		if (gAppTerminate!=null)
			gAppTerminate.exit();
		System.exit(0);
	}
 	
}
