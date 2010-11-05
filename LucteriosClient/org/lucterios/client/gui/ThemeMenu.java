package org.lucterios.client.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.utils.CharcoalTheme;
import org.lucterios.client.utils.EmeraldTheme;
import org.lucterios.utils.graphic.ExceptionDlg;

import com.pagosoft.plaf.PlafOptions;
import com.pagosoft.plaf.ThemeFactory;

public class ThemeMenu extends JRadioButtonMenuItem implements ActionListener {

	private static final int THEME_DEFAULT = 4;

	private static final String DEFAULT_THEME = "DefaultTheme";

	private static final String PREFERENCE = "Preference";

	public interface LookAndFeelCallBack {
		public Component[] getComponentsForLookAndFeel();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MetalTheme mTheme;

	public LookAndFeelCallBack CallBack = null;

	public ThemeMenu(MetalTheme aTheme, boolean aSelected,
			LookAndFeelCallBack aCallBack) {
		super(aTheme.getName(), aSelected);
		mTheme = aTheme;
		CallBack = aCallBack;
		addActionListener(this);
	}

	public void actionPerformed(java.awt.event.ActionEvent ae) {
		try {
			setDefaultTheme(mTheme);
			PlafOptions.setCurrentTheme(mTheme);
			PlafOptions.setAsLookAndFeel();
			PlafOptions.updateAllUIs();
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

	public static MetalTheme[] Global_Themes = new MetalTheme[] {
			new DefaultMetalTheme(), new CharcoalTheme(), new EmeraldTheme(), 
			ThemeFactory.GOLD, ThemeFactory.GRAY, ThemeFactory.GREEN,
			ThemeFactory.RUBY, ThemeFactory.WIN, ThemeFactory.YELLOW };

	public static MetalTheme getDefaultTheme() {
		int default_num = Singletons.LucteriosSettingFile.getValueSectionInt(
				PREFERENCE, DEFAULT_THEME, THEME_DEFAULT);
		if ((default_num > Global_Themes.length) || (default_num < 0))
			default_num = 0;
		return Global_Themes[default_num];
	}

	public static void setDefaultTheme(MetalTheme aTheme) {
		int default_num = -1;
		for (int theme_id = 0; (default_num == -1)
				&& (theme_id < Global_Themes.length); theme_id++) {
			if (Global_Themes[theme_id].equals(aTheme))
				default_num = theme_id;
		}
		if (default_num != -1) {
			Singletons.LucteriosSettingFile.setValueSectionInt(PREFERENCE,
					DEFAULT_THEME, default_num);
			try {
				Singletons.LucteriosSettingFile.save();
			} catch (IOException e) { }
		}
	}

	public static void initializedTheme() {
		try {
			UIManager.setLookAndFeel("com.pagosoft.plaf.PgsLookAndFeel");
			PlafOptions.setCurrentTheme(getDefaultTheme());
			PlafOptions.setAsLookAndFeel();
			PlafOptions.updateAllUIs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JMenu getThemeMenu(LookAndFeelCallBack aCallBack) {
		JMenu LookFeelMenuItem = new JMenu("Thèmes");
		javax.swing.ButtonGroup bg = new javax.swing.ButtonGroup();

		MetalTheme default_theme = getDefaultTheme();
		for (MetalTheme theme : Global_Themes) {
			javax.swing.JRadioButtonMenuItem item = new ThemeMenu(theme, theme
					.equals(default_theme), aCallBack);
			bg.add(item);
			LookFeelMenuItem.add(item);
		}
		return LookFeelMenuItem;
	}
}
