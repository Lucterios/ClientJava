package org.lucterios.utils;

import java.net.URL;

import org.lucterios.gui.AbstractImage;

public interface DesktopInterface {

	static final public String ASSOCIATION_SECTION="Application";
	
	public abstract void initApplicationsSetting(
			IniFileManager aApplicationsSettingFile);

	public abstract void launch(String aUrl) throws LucteriosException;
	
	public abstract void openFile(String aUrl) throws LucteriosException;
	
	public abstract void printFilePDF(String aUrl) throws LucteriosException;

	public abstract int[] getCoord();
	
	public abstract int[] getScreenSize();

	public abstract AbstractImage CreateImage(URL url);
	
	public abstract void throwException(Exception e);
	
}