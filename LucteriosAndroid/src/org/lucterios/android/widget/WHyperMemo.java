package org.lucterios.android.widget;

import org.lucterios.gui.GUIHyperMemo;

import android.content.Context;

public class WHyperMemo extends WMemo implements GUIHyperMemo {

	public WHyperMemo(Context context, WContainer owner) {
		super(context, owner);
	}

	public void addSpecialMenus(String menuName, Object[] variables,
			String tagBegin, String tagEnd) {
		// TODO Auto-generated method stub

	}

	public void addStandardPopupMenu(boolean withFileAction) {
		// TODO Auto-generated method stub

	}

	public void load(String source) {
		setText(source);
	}

	public String save() {
		return getValue();
	}

}
