package org.lucterios.swing;

import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.test.DialogExample;
import org.lucterios.gui.test.DialogSimple;
import org.lucterios.style.ThemeMenu;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThemeMenu.initializedTheme();
		GUIDialog mDialog=new SGenerator().newDialog(null);
		if (args.length>0)
			mDialog.setDialogVisitor(new DialogExample());
		else
			mDialog.setDialogVisitor(new DialogSimple());
		mDialog.setVisible(true);
	}

}
