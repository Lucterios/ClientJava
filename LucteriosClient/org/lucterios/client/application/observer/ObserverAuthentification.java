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

package org.lucterios.client.application.observer;

import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.application.Connection;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class ObserverAuthentification extends ObserverAbstract {
	static public Connection mConnection = null;
	static public boolean refreshMenu = true;

	public String getObserverName() {
		return "CORE.Auth";
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		String cdate = mContent.getText();
		if (!cdate.equalsIgnoreCase("OK")
				|| "".equals(Singletons.Transport().getSession())) {
			Singletons.Transport().setSession("");
			LogonBox logon_box = new LogonBox();
			logon_box.logon(cdate);
			logon_box.dispose();
			refreshMenu = true;
		} else if (mConnection != null) {
			SimpleParsing xml_connection = mContent
					.getFirstSubTag("CONNECTION");
			ApplicationDescription desc = new ApplicationDescription(
					xml_connection.getCDataOfFirstTag("TITLE"), xml_connection
							.getCDataOfFirstTag("COPYRIGHT"), xml_connection
							.getCDataOfFirstTag("LOGONAME"), xml_connection
							.getCDataOfFirstTag("VERSION"), xml_connection
							.getCDataOfFirstTag("SERVERVERSION"));
			desc.setSupportEmail(xml_connection
					.getCDataOfFirstTag("SUPPORT_EMAIL"));
			desc
					.setInfoServer(xml_connection
							.getCDataOfFirstTag("INFO_SERVER"));
			mConnection.setValue(desc, xml_connection
					.getCDataOfFirstTag("SUBTITLE"), xml_connection
					.getCDataOfFirstTag("LOGIN"), xml_connection
					.getCDataOfFirstTag("REALNAME"), refreshMenu);
			refreshMenu = false;
		}
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void close() {
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}
