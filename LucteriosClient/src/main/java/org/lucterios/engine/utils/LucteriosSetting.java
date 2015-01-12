package org.lucterios.engine.utils;

import java.io.IOException;

import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.Tools.DefaultThemeCallBack;

public class LucteriosSetting extends IniFileManager implements DefaultThemeCallBack {
	private static final long serialVersionUID = 1L;

	public LucteriosSetting(String aIniFileName) throws IOException {
		super(aIniFileName);
	}

	private static final int THEME_DEFAULT = 4;

	private static final String DEFAULT_THEME = "DefaultTheme";

	private static final String PREFERENCE = "Preference";

	public int getDefaultValue() {
		return getValueSectionInt(PREFERENCE, DEFAULT_THEME, THEME_DEFAULT);
	}

	public void setDefaultValue(int value) {
		setValueSectionInt(PREFERENCE,DEFAULT_THEME, value);
		try {
			save();
		} catch (IOException e) {
		}
	}
}
