package org.lucterios.android.widget;

import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.utils.Tools;

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
		setText(Tools.replace(source, "{[newline]}", "\n"));
	}

	public String save() {
		return getValue();
	}

}
