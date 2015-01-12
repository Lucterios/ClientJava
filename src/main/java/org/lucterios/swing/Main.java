package org.lucterios.swing;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.test.FrameTest;
import org.lucterios.style.ThemeMenu;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThemeMenu.initializedTheme();
		ExceptionDlg.mGenerator=new SGenerator();
		GUIFrame mDialog=ExceptionDlg.mGenerator.getFrame();
		mDialog.setFrameVisitor(new FrameTest());
		mDialog.setVisible(true);
	}

}
