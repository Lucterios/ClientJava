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

package org.lucterios.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.lucterios.graphic.Tools;
import org.lucterios.ui.GUIAction;

public class MenuItem extends JMenuItem {
	/**
	 * 
	 */
    public static final int CTRL_MASK		= 1 << 1;

    public static final int META_MASK		= 1 << 2;
	
	private static final long serialVersionUID = 1L;
	private GUIAction mAction;
	private String mDescription="";

	public MenuItem() {
		super();
	}

	public MenuItem(GUIAction aAction,String description) {
		super();
		setActionItem(aAction);
		setDescription(description);
	}
	
	public void setActionItem(GUIAction aAction) {
		mAction = aAction;
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
				new_modifier += getControlKey();
			this.setAccelerator(KeyStroke.getKeyStroke(key_code, new_modifier));
		}
		this.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				mAction.actionPerformed();
			}
		});
	}

	public GUIAction getActionItem() {
		return mAction;
	}
	
	public static int getControlKey() {
		String os_arch = System.getProperty("os.arch");
		if (os_arch.equalsIgnoreCase("PowerPC")
				|| os_arch.equalsIgnoreCase("ppc")) {
			return META_MASK;
		} else {
			return CTRL_MASK;
		}
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getDescription() {
		return mDescription;
	}

}
