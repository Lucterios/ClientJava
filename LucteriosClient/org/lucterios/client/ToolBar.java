package org.lucterios.client;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.lucterios.client.application.Action;
import org.lucterios.utils.graphic.Tools;

public class ToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean mAdded;

	private Vector mActions;

	public ToolBar() {
		super(HORIZONTAL);
		setFloatable(false);
		mActions = new Vector();
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
			newAction((Action) mActions.get(idx), null);
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
		button.addActionListener(aAction);
		button.setIcon(Tools.resizeIcon(aAction.getIcon(), 16, true));
		add(button);
		mAdded = true;
	}

}
