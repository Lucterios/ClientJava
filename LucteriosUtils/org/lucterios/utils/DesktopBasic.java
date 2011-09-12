package org.lucterios.utils;

import org.lucterios.gui.GUIGenerator;
import org.lucterios.utils.Tools.InfoDescription;

public class DesktopBasic extends DesktopInterface {

	@Override
	public int[] getCoord(GUIGenerator generator) {
		return new int[]{0,0,0,0};
	}

	@Override
	public void initApplicationsSetting(IniFileManager aApplicationsSettingFile) {
	}

	@Override
	public void launch(String aUrl) throws LucteriosException {
	}

	@Override
	public void openFile(String aUrl) throws LucteriosException {
	}

	@Override
	public void printFilePDF(String aUrl) throws LucteriosException {
	}

	@Override
	public void setInfoDescription(InfoDescription applicationDescription) {
	}

	@Override
	public void throwException(Exception e) {
	}

}
