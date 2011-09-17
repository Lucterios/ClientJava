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

package org.lucterios.engine.application.comp;

import java.io.UnsupportedEncodingException;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.GCTools;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;

public class CmpMemo extends CmpAbstractEvent {
	final static public String ENCODE = "ISO-8859-1";
	private static final long serialVersionUID = 1L;
	private static final String SUB_MENU_SPECIAL = "@MENU@";
	private GUIMemo cmp_text;
	private GUIMenu SubMenu;
	private boolean mEncode = false;

	class MemoAction implements GUIActionListener {

		private String actionName = "";
		
		public MemoAction(String actionName) {
			this.actionName=actionName;
		}
		
		public void actionPerformed() {
			if (actionName.startsWith(SUB_MENU_SPECIAL)) {
				String special_to_add = actionName.substring(SUB_MENU_SPECIAL.length());
				cmp_text.insertText(special_to_add);
			} else
				CmpMemo.this.actionPerformed();
		}
		
	}
	
	public CmpMemo() {
		super();
		mFill = FillMode.FM_BOTH;
		setWeightx(1.0);
		setWeighty(1.0);
	}

	public void requestFocus() {
		cmp_text.requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_text.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		String out_text = cmp_text.getValue().trim();
		if (mEncode)
			try {
				out_text = java.net.URLEncoder.encode(out_text.trim(), ENCODE);
			} catch (UnsupportedEncodingException e) {
				out_text = "";
				e.printStackTrace();
			}
		else
			out_text = Tools.replace(out_text, "\n", "{[newline]}");
		tree_map.put(getName(), out_text);
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (cmp_text.getValue().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocusGUI();
	}

	protected void initComponent() {
		cmp_text = mPanel.createMemo(mParam);
		cmp_text.setText("");

		SubMenu = cmp_text.getPopupMenu();
	}

	protected GUIMenu getSubMenu(GUIMenu aMenu, String aName) {
		GUIMenu sub_menu = null;
		for (int index = 0; (sub_menu == null)
				&& (index < aMenu.getMenuCount()); index++)
			if (aMenu.getMenu(index).isNode()) {
				GUIMenu current_menu = aMenu.getMenu(index);
				if (current_menu.getText().equals(aName))
					sub_menu = current_menu;
			}
		if (sub_menu == null) {
			sub_menu = aMenu.addMenu(true);
			sub_menu.setText(aName);
		}
		return sub_menu;
	}

	protected void fillSubMenu(SimpleParsing[] aSubMenu) {
		SubMenu.removeAll();
		for (int index = 0; index < aSubMenu.length; index++) {
			String type = aSubMenu[index].getCDataOfFirstTag("TYPE");
			try {
				String name = java.net.URLDecoder.decode(aSubMenu[index]
						.getCDataOfFirstTag("NAME"), ENCODE);
				String value = java.net.URLDecoder.decode(aSubMenu[index]
						.getCDataOfFirstTag("VALUE"), ENCODE);
				GUIMenu current = getSubMenu(SubMenu, type);
				if ("-".equals(name))
					current.addSeparator();
				else {
					GUIMenu variable_item = current.addMenu(false);
					variable_item.setText(name);
					variable_item.setActionListener(new MemoAction(SUB_MENU_SPECIAL+value));
				}
			} catch (java.io.UnsupportedEncodingException e) {
			}
		}
	}

	protected void refreshComponent() {
		super.refreshComponent();
		String in_text = getXmlItem().getText().trim();
		mEncode = (getXmlItem().getAttributInt("Encode", 0) != 0);
		if (mEncode)
			try {
				in_text = java.net.URLDecoder.decode(in_text.trim(), ENCODE);
			} catch (UnsupportedEncodingException e) {
				in_text = "";
				e.printStackTrace();
			}
		else
			in_text = Tools.replace(in_text, "{[newline]}", "\n");
		cmp_text.setText(in_text);
		cmp_text.setStringSize(getXmlItem().getAttributInt("stringSize", 0));
		cmp_text.setFirstLine(getXmlItem().getAttributInt("FirstLine", -1));
		cmp_text.addFocusListener(this);
		fillSubMenu(getXmlItem().getSubTag("SUBMENU"));
		SubMenu.setVisible(SubMenu.getMenuCount() > 0);
		GCTools.postOrderGC();
	}

	protected boolean hasChanged() {
		return !cmp_text.getValue().equals(getXmlItem().getText().trim());
	}

}
