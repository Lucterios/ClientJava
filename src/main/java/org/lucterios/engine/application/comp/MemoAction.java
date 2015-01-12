package org.lucterios.engine.application.comp;

import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.SimpleParsing;

public class MemoAction implements GUIActionListener {
	public final static String ENCODE = "ISO-8859-1";
	public static final String SUB_MENU_SPECIAL = "@MENU@";
	
	public interface Insert {
		void insertText(String text);
		void actionPerformed();
	}	
	
	private String actionName = "";
	private Insert mOwner;
	
	public MemoAction(String actionName, Insert aOwner) {
		this.actionName=actionName;
		this.mOwner=aOwner;
	}
	
	public void actionPerformed() {
		if (actionName.startsWith(SUB_MENU_SPECIAL)) {
			String special_to_add = actionName.substring(SUB_MENU_SPECIAL.length());
			mOwner.insertText(special_to_add);
		} else
			mOwner.actionPerformed();
	}
	
	protected static GUIMenu getSubMenu(GUIMenu aMenu, String aName) {
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

	public static void fillSubMenu(MemoAction.Insert aOwner,GUIMenu aSubMenu,SimpleParsing[] aXmlSubMenu) {
		aSubMenu.removeAll();
		for (int index = 0; index < aXmlSubMenu.length; index++) {
			String type = aXmlSubMenu[index].getCDataOfFirstTag("TYPE");
			try {
				String name = java.net.URLDecoder.decode(aXmlSubMenu[index]
						.getCDataOfFirstTag("NAME"), ENCODE);
				String value = java.net.URLDecoder.decode(aXmlSubMenu[index]
						.getCDataOfFirstTag("VALUE"), ENCODE);
				GUIMenu current = getSubMenu(aSubMenu, type);
				if ("-".equals(name))
					current.addSeparator();
				else {
					GUIMenu variable_item = current.addMenu(false);
					variable_item.setText(name);
					variable_item.setActionListener(new MemoAction(SUB_MENU_SPECIAL+value,aOwner));
				}
			} catch (java.io.UnsupportedEncodingException e) {
			}
		}
	}

}
