package org.lucterios.android.graphic;

import java.net.URL;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.IniFileManager;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools.InfoDescription;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DesktopTools extends DesktopInterface{

	private Context mContext=null;
	
	private DesktopTools() {
		super();
	}
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	private static DesktopInterface mInstance=null;
	public static DesktopInterface instance() {
		if (mInstance==null)
			mInstance=new DesktopTools();
		return mInstance;
	}
	
	public int[] getCoord() {
		return new int[]{0,0,0,0};
	}

	public int[] getScreenSize() {
		if (mContext!=null) {
			 DisplayMetrics metrics = new DisplayMetrics();
			 ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
			 return new int[]{metrics.widthPixels,metrics.heightPixels};
		}
		else
			return new int[]{1,1};
	}
	
	public void initApplicationsSetting(IniFileManager aApplicationsSettingFile) {
		// TODO Auto-generated method stub

	}

	public void launch(String aUrl) throws LucteriosException {
		// TODO Auto-generated method stub

	}

	public void openFile(String aUrl) throws LucteriosException {
		// TODO Auto-generated method stub
		
	}

	public void printFilePDF(String aUrl) throws LucteriosException {
		// TODO Auto-generated method stub
		
	}

	public AbstractImage CreateImage(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	public void throwException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getCoord(GUIGenerator generator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfoDescription(InfoDescription applicationDescription) {
		// TODO Auto-generated method stub
		
	}

}
