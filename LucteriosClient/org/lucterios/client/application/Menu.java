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

import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.lucterios.client.presentation.Observer;
import org.lucterios.client.presentation.ObserverFactory;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.Tools;

public class Menu extends JMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static public String ENCODE = "UTF-8";

	public interface ToolBar {
		public void newShortCut(String aActionName, KeyStroke aShortCut,
				javax.swing.Action aActionListener);

		public void terminatToolBar();
	}

	public interface FrameControle {
		public void setActive(boolean aIsActive);

		public JMenuBar getJMenuBar();

		public void refreshSize();
	}

	private String mIcon;
	public String mDescription;

	Menu(String atext, String aIcon, String aDescription, SimpleParsing[] aXml,
			Observer aOwner, ObserverFactory aFactory, boolean aMainMenu) {
		super();
		int pos;
		mIcon = aIcon;
		mDescription = aDescription;
		pos = atext.indexOf(ActionImpl.MNEMONIC_CHAR);
		if (pos != -1)
			this.setMnemonic(atext.charAt(pos + 1));
		String new_text = atext.replaceAll("&", "");
		while ((pos = new_text.indexOf(ActionImpl.MNEMONIC_CHAR)) != -1)
			new_text = new_text.substring(0, pos) + new_text.substring(pos + 1);
		this.setText(new_text);

		if (!aMainMenu && (mIcon.length() > 0)) {
			this.setIcon(Tools.resizeIcon(
					Singletons.Transport().getIcon(mIcon), 24, true));
		} else
			this.setIcon(null);

		for (int menu_idx = 0; menu_idx < aXml.length; menu_idx++) {
			String help_text;
			try {
				help_text = java.net.URLDecoder.decode(aXml[menu_idx]
						.getCDataOfFirstTag("HELP"), ENCODE);
			} catch (UnsupportedEncodingException e) {
				help_text = "";
			}
			SimpleParsing[] sub_xml_menus = aXml[menu_idx].getSubTag("MENU");
			JMenuItem new_menu = null;
			if (sub_xml_menus.length > 0)
				new_menu = new Menu(aXml[menu_idx].getCData(), aXml[menu_idx]
						.getAttribut("icon"), help_text, sub_xml_menus, aOwner,
						aFactory, false);
			else {
				ActionImpl act = new ActionImpl();
				act.initialize(aOwner, aFactory, aXml[menu_idx]);
				new_menu = new MenuItem(act, help_text);
				if (new_menu.getAccelerator() != null)
					mToolBar.newShortCut(act.getID(),
							new_menu.getAccelerator(), act);
			}
			add(new_menu);
		}
	}

	public String getIconName() {
		return mIcon;
	}

	public ImageIcon getMenuIcon() {
		return Singletons.Transport().getIcon(mIcon);
	}

	static public ToolBar mToolBar = null;

	static public void fillMenuInFrame(FrameControle aJPOwner, Observer aOwner,
			ObserverFactory aFactory, SimpleParsing aXml) {
		JMenuBar menu_bar = aJPOwner.getJMenuBar();
		int extra_menu_idx = -1;
		int extra_menu_nb = 0;
		int menu_idx = 0;
		while (menu_idx < menu_bar.getMenuCount()) {
			if (MenuItem.class.isInstance(menu_bar.getMenu(menu_idx))
					|| Menu.class.isInstance(menu_bar.getMenu(menu_idx))) {
				menu_bar.remove(menu_bar.getMenu(menu_idx));
			} else {
				String menu_name = menu_bar.getMenu(menu_idx).getName();
				if ((menu_name != null)
						&& (("helpMenu".equals( menu_name ) || "windowsMenu".equals( menu_name )))) {
					if (extra_menu_idx == -1)
						extra_menu_idx = menu_idx;
					extra_menu_nb++;
				}
				menu_idx++;
			}
		}
		fillMenuBar(aOwner, aFactory, aXml, menu_bar, extra_menu_idx,
				extra_menu_nb);
		aJPOwner.refreshSize();
		if (mToolBar != null)
			mToolBar.terminatToolBar();
	}

	private static void fillMenuBar(Observer aOwner, ObserverFactory aFactory,
			SimpleParsing aXml, JMenuBar menu_bar, int extraMenuIdx,
			int extraMenuNb) {
		int menu_idx;
		SimpleParsing[] xml_menus = aXml.getSubTag("MENU");
		for (menu_idx = 0; menu_idx < xml_menus.length; menu_idx++) {
			String help_text;
			try {
				help_text = java.net.URLDecoder.decode(xml_menus[menu_idx]
						.getCDataOfFirstTag("HELP"), ENCODE);
			} catch (UnsupportedEncodingException e) {
				help_text = "";
			}
			JMenuItem new_menu = null;
			SimpleParsing[] sub_xml_menus = xml_menus[menu_idx]
					.getSubTag("MENU");
			if (sub_xml_menus.length > 0)
				new_menu = new Menu(xml_menus[menu_idx].getCData(),
						xml_menus[menu_idx].getAttribut("icon"), help_text,
						sub_xml_menus, aOwner, aFactory, true);
			else {
				ActionImpl act = new ActionImpl();
				act.initialize(aOwner, aFactory, xml_menus[menu_idx]);
				new_menu = new MenuItem(act, help_text);
			}
			menu_bar.add(new_menu);
		}
		if (extraMenuIdx != -1) {
			for (int extra_idx = 0; extra_idx < extraMenuNb; extra_idx++)
				menu_bar.add(menu_bar.getMenu(extraMenuIdx));
		}
	}

}
