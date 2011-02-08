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

package org.lucterios.client.application;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.graphic.Tools;

public class MenuItem extends JMenuItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Action mAction;
	public String mDescription;

	public MenuItem(Action aAction, String aDescription) {
		super();
		mAction = aAction;
		if (mAction.getFormType() == ActionConstantes.FORM_REFRESH)
			mAction.setFormType(ActionConstantes.FORM_NOMODAL);
		this.setText(mAction.getTitle());
		if (mAction.getMnemonic() != 0)
			this.setMnemonic(mAction.getMnemonic());

		if (mAction.getIcon() != null)
			this.setIcon(Tools.resizeIcon((ImageIcon)mAction.getIcon().getData(), 24, true));
		else
			this.setIcon(null);
		KeyStroke key = KeyStroke.getKeyStroke(mAction.getKeyStroke());
		if (key != null) {
			int key_code = key.getKeyCode();
			int modifier = key.getModifiers();
			int new_modifier = 0;
			if ((modifier & java.awt.event.InputEvent.ALT_MASK) == java.awt.event.InputEvent.ALT_MASK)
				new_modifier += java.awt.event.InputEvent.ALT_MASK;
			if ((modifier & java.awt.event.InputEvent.SHIFT_MASK) == java.awt.event.InputEvent.SHIFT_MASK)
				new_modifier += java.awt.event.InputEvent.SHIFT_MASK;
			if ((modifier & java.awt.event.InputEvent.CTRL_MASK) == java.awt.event.InputEvent.CTRL_MASK)
				new_modifier += ActionConstantes.getControlKey();
			this.setAccelerator(KeyStroke.getKeyStroke(key_code, new_modifier));
		}
		mDescription = aDescription;
		this.addActionListener((ActionListener) mAction);
	}
}
