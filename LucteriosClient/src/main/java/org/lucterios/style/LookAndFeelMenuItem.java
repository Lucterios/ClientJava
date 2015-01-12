package org.lucterios.style;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.style.ThemeMenu.LookAndFeelCallBack;

public class LookAndFeelMenuItem extends JRadioButtonMenuItem implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String classe;
	public LookAndFeelCallBack CallBack = null;

	public LookAndFeelMenuItem(String aName, String aClasse,
			LookAndFeelCallBack aCallBack) {
		super(aName, aClasse.equals(javax.swing.UIManager
				.getSystemLookAndFeelClassName()));
		classe = aClasse;
		CallBack = aCallBack;
		addActionListener(this);
	}

	public void actionPerformed(java.awt.event.ActionEvent ae) {
		try {
			javax.swing.UIManager.setLookAndFeel(classe);
			if (CallBack != null) {
				Component[] cmps = CallBack.getComponentsForLookAndFeel();
				for (int cmp_i = 0; cmp_i < cmps.length; cmp_i++)
					javax.swing.SwingUtilities
							.updateComponentTreeUI(cmps[cmp_i]);
			}
		} catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}
}
