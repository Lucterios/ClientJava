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

package org.lucterios.client;

import java.io.File;
import java.io.IOException;

import org.lucterios.engine.application.observer.CustomManager;
import org.lucterios.engine.application.observer.LogonBox;
import org.lucterios.engine.application.observer.ObserverAcknowledge;
import org.lucterios.engine.application.observer.ObserverAuthentification;
import org.lucterios.engine.application.observer.ObserverCustom;
import org.lucterios.engine.application.observer.ObserverDialogBox;
import org.lucterios.engine.application.observer.ObserverException;
import org.lucterios.engine.application.observer.ObserverMenu;
import org.lucterios.engine.application.observer.ObserverPrint;
import org.lucterios.engine.application.observer.ObserverTemplate;
import org.lucterios.client.gui.ApplicationMain;
import org.lucterios.client.setting.Constants;
import org.lucterios.client.utils.DesktopTools;
import org.lucterios.engine.transport.HttpTransportImpl;
import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.ImageCache;
import org.lucterios.style.ThemeMenu;
import org.lucterios.swing.SGenerator;
import org.lucterios.swing.SWindows;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.Logging;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.graphic.SwingImage;
import org.lucterios.graphic.WaitingWindow;

class Main {
	static private void initalizeObserver() {
		ObserverFactory fact = Singletons.Factory();
		fact.AddObserver("CORE.Auth", ObserverAuthentification.class);
		fact.AddObserver("CORE.Menu", ObserverMenu.class);
		fact.AddObserver("Core.Acknowledge", ObserverAcknowledge.class);
		fact.AddObserver("CORE.Exception", ObserverException.class);
		fact.AddObserver("Core.DialogBox", ObserverDialogBox.class);
		fact.AddObserver("Core.Custom", ObserverCustom.class);
		fact.AddObserver("Core.Print", ObserverPrint.class);
		fact.AddObserver("Core.Template", ObserverTemplate.class);
		fact.AddObserver("", ObserverAcknowledge.class);
	}

	public static void main(String args[]) {
		if ((args.length>0) && "-version".equals(args[0])){
			System.out.println("Lucterios Client Java");
			System.out.println(" Version:"+Constants.Version());
		}
		else {
			String debugArgs="";
			if (args.length>0)
				debugArgs=args[0];
			mainClient(debugArgs);
		}
	}
		
	public static void mainClient(String debugArgs) {		
		try {
			System.out.println("Lucterios Client Java - Version:"+Constants.Version());
			initializeSingleton();
			ThemeMenu.initializedTheme();
			ApplicationMain main;
			SWindows wind=(SWindows)Singletons.getWindowGenerator().newWindows();
			WaitingWindow ww = new WaitingWindow(
					"Chargement de l'application.<br>Veuillez patienter.",
					Singletons.getConfiguration().TitreDefault);
			wind.setWindowVisitor(ww);
			wind.setVisible(true);
			wind.requestFocus();
			try {
				initalizeObserver();
				CustomManager.initalize();
				if (debugArgs.length()>0)
					Logging.getInstance().setDebugLevel(debugArgs);
				main = new ApplicationMain(Singletons.getWindowGenerator());
				GUIActionListener run_setup_dlg = main.getRunSetupDialog();
				ObserverAuthentification.mConnection = main;
				while ("".equals(Singletons.Transport().getSession()))
				{
					LogonBox logon_box = new LogonBox();
					logon_box.mActionSetUp = run_setup_dlg;
					logon_box.logon("");
				}
			} finally {
				wind.dispose();
			}
			main.show();
		} catch (Exception e) {
			ExceptionDlg.throwException(e);
			Singletons.exit();
		}
	}

	private static void initializeSingleton() throws IOException {
		Singletons.setActionClass(ActionImpl.class);
		Singletons.setHttpTransportClass(HttpTransportImpl.class);
		Singletons.setDesktop(new DesktopTools());
		Singletons.setWindowGenerator(new SGenerator());
		Singletons.initalize(new File("."));
		Singletons.setAppTerminate(new RequirementProcesses());
		ExceptionDlg.mGenerator=Singletons.getWindowGenerator();
		ImageCache.ImageClass=SwingImage.class;
		ThemeMenu.setDefaultThemeCallBack(Singletons.getLucteriosSettingFile());
		ApplicationDescription.gVersion=Constants.Version();
	}
}
