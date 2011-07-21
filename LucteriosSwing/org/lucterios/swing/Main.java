package org.lucterios.swing;

import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.test.DialogExample;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GUIDialog mDialog=new SGenerator().newDialog(null);
		mDialog.setDialogVisitor(new DialogExample());
		mDialog.setVisible(true);
	}

}
