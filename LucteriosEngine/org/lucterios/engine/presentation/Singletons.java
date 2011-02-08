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
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.Tools;

public class Singletons {
	
	static final public String LUCTERIOS_CONFIG = ".LucteriosSetting";
	static final public String TEMP_DIR=".LucteriosTemp";
	
	public interface ApplicationTerminate{
		void exit();
	}

	static public IniFileManager LucteriosSettingFile=null;
	
	static public DesktopInterface mDesktop=null;
	
	static public ApplicationTerminate AppTerminate=null;
	
	static public Class<? extends HttpTransport> HttpTransportClass=null;

	static public Class<? extends Action> ActionClass=null;
	
	static public HttpTransport Transport() {
		HttpTransport new_transport = null;
		try {
			new_transport = (HttpTransport) HttpTransportClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new_transport;
	}

	static public Action CreateAction() {
		Action new_action=null;
		try {
			new_action = (Action) ActionClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new_action;
	}

	static private ObserverFactory mFactory=null;

	static public ObserverFactory Factory() {
		mFactory.setHttpTransport(Transport());
		return mFactory;
	}

	static public LucteriosConfiguration Configuration = null;

	static public void initalize() throws IOException {
		Configuration = new LucteriosConfiguration();
		mFactory = new ObserverFactoryImpl();
		File cache_dir=new File(TEMP_DIR);
		if (cache_dir.isDirectory())
			Tools.deleteDir(cache_dir);
		if (!cache_dir.isDirectory())
			cache_dir.mkdir();
		loadSetting();
	}

	static public void loadSetting() throws IOException {
		LucteriosSettingFile=new IniFileManager(LUCTERIOS_CONFIG);
		mDesktop.initApplicationsSetting(LucteriosSettingFile);
	}
	
	static public void exit(){
		if (AppTerminate!=null)
			AppTerminate.exit();
		System.exit(0);
	}
}
