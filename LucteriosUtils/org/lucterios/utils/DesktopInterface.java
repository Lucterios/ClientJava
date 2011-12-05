package org.lucterios.utils;

import org.lucterios.gui.GUIGenerator;
import org.lucterios.utils.Tools.InfoDescription;

public abstract class DesktopInterface {

	static final public String ASSOCIATION_SECTION="Application";
	
	protected static DesktopInterface gInstance=null;
	public static DesktopInterface getInstance() {
		return gInstance;
	}
	
	private String mTempPath=".";	
	
	public String getTempPath() {
		return mTempPath;
	}

	public void setTempPath(String tempPath) {
		this.mTempPath = tempPath;
	}

	public abstract void initApplicationsSetting(
			IniFileManager aApplicationsSettingFile);

	public abstract void launch(String aUrl) throws LucteriosException;
	
	public abstract void openFile(String aUrl) throws LucteriosException;
	
	public abstract void printFilePDF(String aUrl) throws LucteriosException;

	public abstract int[] getCoord(GUIGenerator generator);

	public abstract void throwException(Exception e);
	
	public abstract void setInfoDescription(InfoDescription applicationDescription);

}