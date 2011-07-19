package org.lucterios.style;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;

import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.utils.Tools.DefaultThemeCallBack;

import com.pagosoft.plaf.PlafOptions;
import com.pagosoft.plaf.ThemeFactory;

public class ThemeMenu extends JRadioButtonMenuItem implements ActionListener {

	public interface LookAndFeelCallBack {
		public Component[] getComponentsForLookAndFeel();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MetalTheme mTheme;

	public LookAndFeelCallBack CallBack = null;

	private static DefaultThemeCallBack gDefaultThemeCallBack=null;
	
	public static void setDefaultThemeCallBack(DefaultThemeCallBack defaultThemeCallBack) {
		ThemeMenu.gDefaultThemeCallBack = defaultThemeCallBack;
	}

	public static DefaultThemeCallBack getDefaultThemeCallBack() {
		return gDefaultThemeCallBack;
	}

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
		int default_num = 0;
		if (gDefaultThemeCallBack!=null)
			default_num = gDefaultThemeCallBack.getDefaultValue();
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
		if ((default_num != -1) && (gDefaultThemeCallBack!=null)) {
			gDefaultThemeCallBack.setDefaultValue(default_num);
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
