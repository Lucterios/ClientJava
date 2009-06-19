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

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import org.lucterios.client.presentation.ObserverFactory;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.application.observer.CustomManager;
import org.lucterios.client.application.observer.LogonBox;
import org.lucterios.client.application.observer.ObserverAcknowledge;
import org.lucterios.client.application.observer.ObserverAuthentification;
import org.lucterios.client.application.observer.ObserverCustom;
import org.lucterios.client.application.observer.ObserverDialogBox;
import org.lucterios.client.application.observer.ObserverException;
import org.lucterios.client.application.observer.ObserverMenu;
import org.lucterios.client.application.observer.ObserverPrint;
import org.lucterios.client.application.observer.ObserverTemplate;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.graphic.ExceptionDlg;
import org.lucterios.utils.graphic.WaitingWindow;

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
		try {
			Singletons.initalize();
			Singletons.AppTerminate=new RequirementProcesses();
			ApplicationMain main;
			WaitingWindow ww = new WaitingWindow(
					"Chargement de l'application.<br>Veuillez patienter.",
					Singletons.Configuration.TitreDefault);
			ww.setVisible(true);
			ww.requestFocus();
			try {
				initalizeObserver();
				CustomManager.initalize();
				if (args.length == 1)
					Logging.getInstance().setDebugLevel(args[0]);
				main = new ApplicationMain();
				main.setIconImage(Toolkit.getDefaultToolkit().getImage(
						main.getClass().getResource("resources/connect.png")));
				ActionListener run_setup_dlg = main.getRunSetupDialog();
				ObserverAuthentification.mConnection = main;
				while ("".equals( Singletons.Transport().getSession() ))
					try {
						LogonBox logon_box = new LogonBox();
						logon_box.mActionSetUp = run_setup_dlg;
						logon_box.logon("");
                        logon_box.dispose();
					} catch (LucteriosException e) {
						ExceptionDlg.throwException(e);
					}
			} finally {
				ww.dispose();
			}
			main.setVisible(true);
		} catch (Exception e) {
			ExceptionDlg.throwException(e);
			Singletons.exit();
		}
	}
}
