package org.lucterios.utils;

import java.io.File;

public interface DesktopInterface {

	static final public String ASSOCIATION_SECTION="Application";
	
	public interface FileFilter{
		public boolean accept(File aFile);
		public String getDescription();		
	}
	
	public abstract void initApplicationsSetting(
			IniFileManager aApplicationsSettingFile);

	public abstract void launch(String aUrl) throws LucteriosException;
	
	public abstract void openFile(String aUrl) throws LucteriosException;
	
	public abstract void printFilePDF(String aUrl) throws LucteriosException;

	public abstract int[] getCoord();
	
	public abstract int[] getScreenSize();

	public abstract void throwException(Exception e);
	
	public abstract File selectOpenFileDialog(final FileFilter filter,final Object aGUIOwner);	
}