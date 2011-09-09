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

package org.lucterios.engine.application.observer;

import java.io.UnsupportedEncodingException;

import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class ObserverMenu extends ObserverAbstract {

	final static public String ENCODE = "UTF-8";

	public interface ToolBarInterface {
		public void initialToolBar();

		public void newShortCut(String aActionName, String aShortCut,
				GUIAction aActionListener);

		public void terminatToolBar();

	}

	static public ToolBarInterface mToolBar = null;
	static public Observer mDefaultParent = null;
	
	public ObserverMenu() {
		super();
		setParent(mDefaultParent);
	}

	public String getObserverName() {
		return "Core.Menu";
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) {
		super.show(aTitle);
		fillMenuInFrame();
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}

	public void refresh() throws LucteriosException {
		// Do nothing
	}

	private void fillMenuNode(GUIMenu newMenu, String atext, String aIcon, int aSizeIcon, String aDescription,
			SimpleParsing[] aXml, boolean aMainMenu) {

		int pos;
		newMenu.setDescription(aDescription);
		pos = atext.indexOf(ActionImpl.MNEMONIC_CHAR);
		if (pos != -1)
			newMenu.setMnemonic(atext.charAt(pos + 1));
		String new_text = atext.replaceAll("&", "");
		while ((pos = new_text.indexOf(ActionImpl.MNEMONIC_CHAR)) != -1)
			new_text = new_text.substring(0, pos) + new_text.substring(pos + 1);
		newMenu.setText(new_text);
		newMenu.setVisible(new_text.length() > 0);
		
		newMenu.setMenuImage(Singletons.Transport().getIcon(aIcon, aSizeIcon),!aMainMenu);

		for (int menu_idx = 0; menu_idx < aXml.length; menu_idx++) {
			String help_text;
			try {
				help_text = java.net.URLDecoder.decode(aXml[menu_idx]
						.getCDataOfFirstTag("HELP"), ENCODE);
			} catch (UnsupportedEncodingException e) {
				help_text = "";
			}
			GUIMenu new_menu = null;
			SimpleParsing[] sub_xml_menus = aXml[menu_idx].getSubTag("MENU");
			if (sub_xml_menus.length > 0) {
				new_menu = newMenu.addMenu(true);
				fillMenuNode(new_menu,aXml[menu_idx].getText(), aXml[menu_idx].getAttribut("icon"), 
						aXml[menu_idx].getAttributInt("sizeicon", 0), help_text, 
						sub_xml_menus, false);
			}
			else {
				ActionImpl act = new ActionImpl();
				act.initialize(this, Singletons.Factory(), aXml[menu_idx]);
				act.setClose(false);
				new_menu = newMenu.addMenu(false);
				new_menu.setAction(act);
				new_menu.setDescription(help_text);
				if (new_menu.getAcceleratorText() != null)
					mToolBar.newShortCut(act.getID(), new_menu.getAcceleratorText(), act);
			}
		}
		
	}
	
	private void fillMenuInFrame() {
		GUIFrame aJPOwner=Singletons.getWindowGenerator().getFrame();
		
		if (mToolBar != null) {
			mToolBar.initialToolBar();
		}
		int menu_idx = 0;
		while (menu_idx < aJPOwner.getMenuCount()) {
			if (aJPOwner.getMenu(menu_idx).getTag()==0) {
				aJPOwner.removeMenu(menu_idx);
			} else {
				menu_idx++;
			}
		}
		fillMenuBar();
		aJPOwner.refreshSize();
		if (mToolBar != null) {
			Singletons.getWindowGenerator().invokeLater(new Runnable() {
				public void run() {
					mToolBar.terminatToolBar();
				}
			});
		}
	}

	private void fillMenuBar() {
		GUIFrame aJPOwner=Singletons.getWindowGenerator().getFrame();
		int begin_extra_menu=-1;
		int end_extra_menu=0;
		for(int extra_idx = 0;extra_idx < aJPOwner.getMenuCount();extra_idx++) {
			if (aJPOwner.getMenu(extra_idx).getTag()>=0) {
				if (begin_extra_menu==-1)
					begin_extra_menu=extra_idx;
				end_extra_menu=extra_idx;
			}
		}
		int menu_idx;
		SimpleParsing[] xml_menus = mContent.getFirstSubTag("MENUS").getSubTag("MENU");
		for (menu_idx = 0; menu_idx < xml_menus.length; menu_idx++) {
			String help_text;
			try {
				help_text = java.net.URLDecoder.decode(xml_menus[menu_idx]
						.getCDataOfFirstTag("HELP"), ENCODE);
			} catch (UnsupportedEncodingException e) {
				help_text = "";
			}
			GUIMenu new_menu = null;
			SimpleParsing[] sub_xml_menus = xml_menus[menu_idx].getSubTag("MENU");
			if (sub_xml_menus.length > 0) {
				new_menu = aJPOwner.addMenu(true);
				fillMenuNode(new_menu,xml_menus[menu_idx].getText(),
						xml_menus[menu_idx].getAttribut("icon"),
						xml_menus[menu_idx].getAttributInt("sizeicon", 0),
						help_text, sub_xml_menus, true);
			}
			else {
				ActionImpl act = new ActionImpl();
				act.initialize(this, Singletons.Factory(), xml_menus[menu_idx]);
				act.setClose(false);
				new_menu = aJPOwner.addMenu(false);
				new_menu.setAction(act);
				new_menu.setDescription(help_text);
			}
		}
		for(int extra_idx = begin_extra_menu;extra_idx <= end_extra_menu;extra_idx++) {
			aJPOwner.moveMenu(begin_extra_menu);
		}
	}
}
