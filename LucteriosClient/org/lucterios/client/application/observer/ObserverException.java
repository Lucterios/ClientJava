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

import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;

public class ObserverException extends ObserverAbstract {
	String message = "";
	int code = 0;
	int mode = 0;
	String debug_info = "";
	String type = "";
	String user_info = "";

	public String getObserverName() {
		return "CORE.Exception";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		SimpleParsing xml_error = mContent.getFirstSubTag("EXCEPTION");
		message = xml_error.getCDataOfFirstTag("MESSAGE");
		code = new Integer("0" + xml_error.getCDataOfFirstTag("CODE"))
				.intValue();
		mode = new Integer("0" + xml_error.getCDataOfFirstTag("MODE"))
				.intValue();
		debug_info = xml_error.getCDataOfFirstTag("DEBUG_INFO");
		type = xml_error.getCDataOfFirstTag("TYPE");
		user_info = xml_error.getCDataOfFirstTag("USER_INFO");
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		setActive(true);
		ExceptionDlg.show(code, message, debug_info, type);
		close(true);
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}
