package org.lucterios.utils;

import java.awt.Insets;


public interface DesktopInterface {

	public abstract void initApplicationsSetting(
			IniFileManager aApplicationsSettingFile);

	public abstract void launch(String aUrl) throws LucteriosException;

	public abstract Insets getInsets();

}