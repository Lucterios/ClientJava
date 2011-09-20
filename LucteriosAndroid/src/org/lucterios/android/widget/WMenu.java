package org.lucterios.android.widget;

import org.lucterios.android.graphic.AndroidImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

import android.view.Menu;
import android.view.MenuItem;

public class WMenu implements GUIMenu {

	private MenuItem mMenu;
	
	public WMenu(MenuItem menuItem) {
		mMenu=menuItem;
	}

	public GUIMenu addMenu(boolean isNode) {
		if (isNode()) {
			MenuItem menuItem;
			if (isNode)
				menuItem=(MenuItem)((Menu)mMenu).addSubMenu("");
			else
				menuItem=((Menu)mMenu).add("");			
			return new WMenu(menuItem);
		}
		else
			return null;
	}

	public void addSeparator() {
		if (isNode()) {
			((Menu)mMenu).add("");
		}
	}

	public String getAcceleratorText() {
		return "";
	}

	public GUIAction getAction() {
		return null;
	}

	public GUIActionListener getActionListener() {
		return null;
	}

	public String getDescription() {
		return "";
	}

	public GUIMenu getMenu(int index) {
		if (isNode()) {
			return new WMenu(((Menu)mMenu).getItem(index));
		}
		else
			return null;
	}

	public int getMenuCount() {
		if (isNode()) {
			return ((Menu)mMenu).size();
		}
		else
			return 0;
	}

	public AbstractImage getMenuImage() {
		return new AndroidImage(mMenu.getIcon());
	}

	public String getName() {
		return "";
	}

	public int getTag() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getText() {
		return mMenu.getTitle().toString();
	}

	public boolean isNode() {
		return (Menu.class.isInstance(mMenu));
	}

	public boolean isPoint() {
		return false;
	}

	public void removeAll() {
		if (isNode())
			((Menu)mMenu).clear();
	}

	public void removeMenu(int index) {
		if (isNode())
			((Menu)mMenu).removeItem(index);
	}

	public void setAction(GUIAction action) {
		// TODO Auto-generated method stub

	}

	public void setActionListener(GUIActionListener actionListener) {
		// TODO Auto-generated method stub

	}

	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	public void setEnabled(boolean b) {
		mMenu.setEnabled(b);
	}

	public void setMenuImage(AbstractImage image, boolean showIcon) {
		mMenu.setIcon(((AndroidImage)image).getDrawable());
	}

	public void setMnemonic(char c) {
		// TODO Auto-generated method stub

	}

	public void setName(String string) {
		// TODO Auto-generated method stub

	}

	public void setTag(int tag) {
		// TODO Auto-generated method stub

	}

	public void setText(String string) {
		mMenu.setTitle(string);
	}

	public void setVisible(boolean isVisible) {
		mMenu.setVisible(isVisible);
	}

}
