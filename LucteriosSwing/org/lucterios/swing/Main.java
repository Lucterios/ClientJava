package org.lucterios.swing;

import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.test.DialogExample;
import org.lucterios.style.ThemeMenu;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThemeMenu.initializedTheme();
		GUIDialog mDialog=new SGenerator().newDialog(null);
		mDialog.setDialogVisitor(new DialogExample());
		mDialog.setVisible(true);
	}

}
