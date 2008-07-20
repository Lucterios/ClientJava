package org.lucterios.client.utils;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

import org.lucterios.utils.graphic.ExceptionDlg;

public class LookAndFeelMenuItem extends JRadioButtonMenuItem implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public interface LookAndFeelCallBack {
		public Component[] getComponentsForLookAndFeel();
	}

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
