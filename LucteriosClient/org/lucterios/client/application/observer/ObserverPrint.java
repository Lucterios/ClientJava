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

import java.awt.Toolkit;
import java.util.*;

import org.lucterios.Print.FopGenerator;
import org.lucterios.Print.SelectPrintDlg;
import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class ObserverPrint extends ObserverAbstract {
	String xml_content = null;
	String title = "";
	int type = 0;
	int mode = SelectPrintDlg.MODE_NONE;

	public String getObserverName() {
		return "Core.Print";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		xml_content = null;
		title = "";
		SimpleParsing fo_elements = mContent.getFirstSubTag("PRINT");
		if (fo_elements != null) {
			title = fo_elements.getAttribut("title");
			type = fo_elements.getAttributInt("type", 0);
			mode = fo_elements.getAttributInt("mode", SelectPrintDlg.MODE_NONE);
			xml_content = fo_elements.getCData();
		}
	}

	protected Map getRequete(String aActionIdent) {
		return new TreeMap();
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		SelectPrintDlg.FontImage = Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg"));

		Dialog owner_dialog = null;
		Form owner_frame = null;
		if (getParent() != null) {
			owner_dialog = getParent().getGUIDialog();
			owner_frame = getParent().getGUIFrame();
		} else {
			owner_dialog = getGUIDialog();
			owner_frame = getGUIFrame();
		}
		FopGenerator generator = new FopGenerator(xml_content, title, type == 0);
		generator.SelectPrintMedia(owner_frame, owner_dialog, mode, null);
	}

	public void show(String aTitle, Form new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, Dialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}
