package org.lucterios.engine.gui;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

public class ToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ICON_SIZE = 16;

	private boolean mAdded;

	private ActionList mActions;
	private GUIContainer mContainer;

	public GUIContainer getContainer() {
		return mContainer;
	}
	
	public ToolBar() {
		mActions = new ActionList();
	}

	public void initialize(GUIContainer container) {
		this.mContainer = container;
	}
	
	public void addAction(Action aNewAction) {
		mActions.add(aNewAction);
	}

	public void refresh(GUIFrame aFrame) {
		mContainer.removeAll();
		for (int index = 0; (aFrame != null) && (index < aFrame.getMenuCount()); index++)
			if (aFrame.getMenu(index).isNode() && (aFrame.getMenu(index).getTag()==0)) {
				mAdded = false;
				GUIMenu current_menu = aFrame.getMenu(index);
				parseSubMenu(current_menu);
				if (mAdded)
					addSeparator();
			}
		for (int idx = 0; idx < mActions.size(); idx++)
			newAction(mActions.get(idx), null);
		mContainer.createLabel(new GUIParam(mContainer.count(),0));
		double size=ICON_SIZE * 1.8;
		mContainer.setSize((int)(size*mActions.size()),(int)size);
	}

	private void addSeparator() {
		GUIParam param=new GUIParam(mContainer.count(),0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
		param.setPad(3);
		param.setPrefSizeX(2);
		GUILabel sep = mContainer.createLabel(param);
		sep.setTextString("");
	}

	private void parseSubMenu(GUIMenu aMenu) {
		for (int index = 0; index < aMenu.getMenuCount(); index++) {
			GUIMenu current_menu = aMenu.getMenu(index);
			if (current_menu.isNode()) {
				parseSubMenu(current_menu);
			}
			if (current_menu.isPoint()) {
				if (!current_menu.getMenuImage().isNull()) {
					String keytext = current_menu.getAcceleratorText();
					if ((keytext != null) && (keytext.length()>0))
						newButton(current_menu);
				}
			}
		}
	}

	private void newButton(GUIMenu aMenu) {
		newAction(aMenu.getAction(), aMenu.getAcceleratorText());
		mAdded = true;
	}

	private void newAction(GUIAction aAction, String aKey) {
		GUIParam param=new GUIParam(mContainer.count(),0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
		param.setPrefSizeX((int)(ICON_SIZE * 1.5));
		param.setPrefSizeY((int)(ICON_SIZE * 1.5));
		GUIButton button = mContainer.createButton(param);
		String key_string = aAction.getTitle();
		key_string += aKey;
		button.setToolTipText(key_string);
		button.addActionListener((GUIActionListener) aAction);
		button.setImage(aAction.getIcon().resizeIcon(ICON_SIZE, true));
		mAdded = true;
	}

}
