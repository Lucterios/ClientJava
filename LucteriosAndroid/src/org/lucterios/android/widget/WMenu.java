package org.lucterios.android.widget;

import org.lucterios.android.graphic.AndroidImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class WMenu implements GUIMenu,GUIAction {

	private MenuItem mMenu;
	private GUIActionListener mActionListener;
	private String mDescription;
	private int mTag;
	private String mName;

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
		return this;
	}

	public GUIActionListener getActionListener() {
		return mActionListener;
	}

	public String getDescription() {
		return mDescription;
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
		return mName;
	}

	public int getTag() {
		return mTag;
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
		setText(action.getTitle());
		setMenuImage(action.getIcon(), true);
		setActionListener(action);
		setMnemonic(action.getMnemonic());
	}

	public void setActionListener(GUIActionListener actionListener) {
		mActionListener=actionListener;
		mMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				mActionListener.actionPerformed();
				return true;
			}
		});
	}

	public void setDescription(String description) {
		mDescription=description;
	}

	public void setEnabled(boolean b) {
		mMenu.setEnabled(b);
	}

	public void setMenuImage(AbstractImage image, boolean showIcon) {
		mMenu.setIcon(AndroidImage.getDrawable(image));
	}

	public void setMnemonic(char c) {
		mMenu.setAlphabeticShortcut(c);
	}

	public void setName(String name) {
		mName=name;
	}

	public void setTag(int tag) {
		mTag=tag;
	}

	public void setText(String string) {
		mMenu.setTitle(string);
	}

	public void setVisible(boolean isVisible) {
		mMenu.setVisible(isVisible);
	}

	public void actionPerformed() {
		mActionListener.actionPerformed();
	}

	public AbstractImage getIcon() {
		return getMenuImage();
	}

	public String getKeyStroke() {
		return getAcceleratorText();
	}

	public char getMnemonic() {
		return mMenu.getAlphabeticShortcut();
	}

	public String getTitle() {
		return getText();
	}

}
