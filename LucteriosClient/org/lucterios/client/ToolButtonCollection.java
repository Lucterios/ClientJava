package org.lucterios.client;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.lucterios.utils.graphic.Tools;

public class ToolButtonCollection extends ToolButton implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public interface ChangeAction {
		void changeMainAction(ToolButtonCollection aAction);
	}

	private ChangeAction mOwner;
	private ToolButtonCollection mParent;
	private ToolButton mButton = null;
	private Vector mSubActions = new Vector();

	ToolButtonCollection(ChangeAction aOwner, ToolButtonCollection aParent,
			org.lucterios.client.application.Menu aMenu) {
		super(aMenu.getText(), aMenu.mDescription, aMenu.getText(), aMenu
				.getMenuIcon());
		mOwner = aOwner;
		mParent = aParent;
		setMenu(aMenu);
	}

	ToolButtonCollection(ChangeAction aOwner, String aText,
			String aDescription, javax.swing.ImageIcon aIcon, JMenuBar aMenuBar) {
		super(null, aText, aDescription, "", aIcon);
		mOwner = aOwner;
		mParent = null;
		for (int index = 0; (aMenuBar != null)
				&& (index < aMenuBar.getMenuCount()); index++)
			if (org.lucterios.client.application.Menu.class.isInstance(aMenuBar
					.getMenu(index))) {
				org.lucterios.client.application.Menu current_menu = (org.lucterios.client.application.Menu) aMenuBar
						.getMenu(index);
				if (current_menu.getIconName().length() > 0)
					mSubActions.add(new ToolButtonCollection(mOwner, this,
							current_menu));
				else
					setMenu(current_menu);
			}
	}

	public ToolButtonCollection getButtonsParent() {
		return mParent;
	}

	public void setMenu(org.lucterios.client.application.Menu aMenu) {
		for (int index = 0; index < aMenu.getMenuComponentCount(); index++) {
			if (org.lucterios.client.application.Menu.class.isInstance(aMenu
					.getMenuComponent(index))) {
				org.lucterios.client.application.Menu current_menu = (org.lucterios.client.application.Menu) aMenu
						.getMenuComponent(index);
				if (current_menu.getIconName().length() > 0)
					mSubActions.add(new ToolButtonCollection(mOwner, this,
							current_menu));
				else
					setMenu(current_menu);
			}
			if (org.lucterios.client.application.MenuItem.class
					.isInstance(aMenu.getMenuComponent(index))) {
				org.lucterios.client.application.MenuItem current_menu = (org.lucterios.client.application.MenuItem) aMenu
						.getMenuComponent(index);
				if (current_menu.mAction.getIconName().length() > 0) {
					String key_string = current_menu.mAction.getTitle();
					key_string += Tools.getKeyString(current_menu.getAccelerator());
					mSubActions.add(new ToolButton(current_menu.mAction,
							current_menu.mAction.getTitle(),
							current_menu.mDescription, key_string,
							current_menu.mAction.getIcon()));
				}
			}
		}
	}

	public int count() {
		return mSubActions.size();
	}

	public ToolButton getActions(int index) {
		if ((index >= 0) && (index < count()))
			return (ToolButton) mSubActions.get(index);
		else
			return null;
	}

	public ToolButton getButton() {
		return mButton;
	}

	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(this);
	}

	public void run() {
		mOwner.changeMainAction(this);
	}

}
