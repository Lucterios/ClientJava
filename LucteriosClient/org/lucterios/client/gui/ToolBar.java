package org.lucterios.client.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.utils.graphic.Tools;

public class ToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ICON_SIZE=16;

	private boolean mAdded;

	private ActionList mActions;

	public ToolBar() {
		super(HORIZONTAL);
		setFloatable(false);
		mActions = new ActionList();
	}

	public void addAction(Action aNewAction) {
		mActions.add(aNewAction);
	}

	public void refresh(JMenuBar aMenuBar) {
		removeAll();
		for (int index = 0; (aMenuBar != null)
				&& (index < aMenuBar.getMenuCount()); index++)
			if (org.lucterios.client.application.Menu.class.isInstance(aMenuBar
					.getMenu(index))) {
				mAdded = false;
				org.lucterios.client.application.Menu current_menu = (org.lucterios.client.application.Menu) aMenuBar
						.getMenu(index);
				parseSubMenu(current_menu);
				if (mAdded)
					addSeparator();
			}
		for (int idx = 0; idx < mActions.size(); idx++)
			newAction(mActions.get(idx), null);
	}

	private void parseSubMenu(org.lucterios.client.application.Menu aMenu) {
		for (int index = 0; index < aMenu.getMenuComponentCount(); index++) {
			if (org.lucterios.client.application.Menu.class.isInstance(aMenu
					.getMenuComponent(index))) {
				org.lucterios.client.application.Menu current_menu = (org.lucterios.client.application.Menu) aMenu
						.getMenuComponent(index);
				parseSubMenu(current_menu);
			}
			if (org.lucterios.client.application.MenuItem.class
					.isInstance(aMenu.getMenuComponent(index))) {
				org.lucterios.client.application.MenuItem current_menu = (org.lucterios.client.application.MenuItem) aMenu
						.getMenuComponent(index);
				if (current_menu.mAction.getIconName().length() > 0) {
					KeyStroke key = current_menu.getAccelerator();
					if (key != null)
						newButton(current_menu);
				}
			}
		}
	}

	private void newButton(org.lucterios.client.application.MenuItem aMenu) {
		newAction(aMenu.mAction, aMenu.getAccelerator());
		mAdded = true;
	}

	private void newAction(Action aAction, KeyStroke aKey) {
		JButton button = new JButton();
		String key_string = aAction.getTitle();
		key_string += Tools.getKeyString(aKey);
		button.setToolTipText(key_string);
		button.addActionListener((ActionListener)aAction);
		button.setIcon(Tools.resizeIcon(aAction.getIcon(), ICON_SIZE, true));
		add(button);
		mAdded = true;
	}

}
