package org.lucterios.client.gui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.graphic.Tools;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;

public class ToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ICON_SIZE = 16;

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

	public void refresh(GUIFrame aFrame,int begin,int end) {
		removeAll();
		for (int index = begin; (aFrame != null) && (index < (aFrame.getMenuCount()-end)); index++)
			if (aFrame.getMenu(index).isNode()) {
				mAdded = false;
				GUIMenu current_menu = aFrame.getMenu(index);
				parseSubMenu(current_menu);
				if (mAdded)
					addSeparator();
			}
		for (int idx = 0; idx < mActions.size(); idx++)
			newAction(mActions.get(idx), null);
	}

	private void parseSubMenu(GUIMenu aMenu) {
		for (int index = 0; index < aMenu.getMenuCount(); index++) {
			GUIMenu current_menu = aMenu.getMenu(index);
			if (current_menu.isNode()) {
				parseSubMenu(current_menu);
			}
			if (current_menu.isPoint()) {
				if (current_menu.getIcon().getData()!=null) {
					String keytext = current_menu.getAcceleratorText();
					if ((keytext != null) && (keytext.length()>0))
						newButton(current_menu);
				}
			}
		}
	}

	private void newButton(GUIMenu aMenu) {
		newAction(aMenu.getAction(), KeyStroke.getKeyStroke(aMenu.getAcceleratorText()));
		mAdded = true;
	}

	private void newAction(GUIAction aAction, KeyStroke aKey) {
		JButton button = new JButton();
		String key_string = aAction.getTitle();
		key_string += Tools.getKeyString(aKey);
		button.setToolTipText(key_string);
		button.addActionListener((ActionListener) aAction);
		ImageIcon icon=null;
		if (aAction.getIcon()!=null)
			icon=(ImageIcon)aAction.getIcon().getData();
		button.setIcon(Tools.resizeIcon(icon, ICON_SIZE, true));
		add(button);
		mAdded = true;
	}

}
