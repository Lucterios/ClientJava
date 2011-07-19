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

package org.lucterios.swing;

import java.net.URL;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.swing.SDialog;
import org.lucterios.swing.SForm;

public class SGenerator implements GUIGenerator {
	public SGenerator() {
		super();
	}

	public GUIForm newForm(String aActionId) {
		return new SForm(aActionId);
	}

	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerFrame) {
		GUIDialog new_dialog;
		if (aOwnerDialog != null)
			new_dialog = new SDialog((SDialog) aOwnerDialog);
		else
			new_dialog = new SDialog((SForm) aOwnerFrame);
		return new_dialog;
	}

	public GUIFrame newFrame() {
		return new SFrame();
	}

	public GUIDialog newDialog(GUIFrame aOwnerFrame) {
		return new SDialog((SFrame) aOwnerFrame);
	}

	public AbstractImage CreateImage(URL url) {
		AbstractImage new_image=new SwingImage();
		new_image.load(url);
		return new_image;
	}
	
}
