package org.lucterios.android.widget;

import java.io.File;
import java.net.URL;

import org.lucterios.android.graphic.AndroidImage;
import org.lucterios.android.widget.resources.ImgResources;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.GUIObject;
import org.lucterios.gui.GUIWindows;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

public class WGenerator implements GUIGenerator {

	public AbstractImage CreateImage(URL url) {
		AbstractImage img=new AndroidImage();
		img.load(url);
		return img;
	}

	public AbstractImage CreateImage(byte[] dataImage) {
		return new AndroidImage(dataImage);
	}

	public int[] getDefaultInsets() {
		return new int[]{0,0,0,0};
	}

	public WFrame mFrame=null;
	public GUIFrame getFrame() {
		if (mFrame==null)
			mFrame=new WFrame(this);
		return mFrame;
	}
	public void setFrame(WFrame frame) {
		mFrame=frame;
	}

	public int[] getScreenSize() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		mFrame.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return new int[]{displaymetrics.widthPixels,displaymetrics.heightPixels};
	}

	public void invokeAndWait(Runnable runnable) {
		mFrame.runOnUiThread(runnable);
	}

	public void invokeLater(Runnable runnable) {
		mFrame.runOnUiThread(runnable);
	}

	public boolean isEventDispatchThread() {
		return true;
	}

	public void runSubThread(Runnable runnable) {
		Thread th=new Thread(runnable);
		th.start();
	}	
	
	public GUIDialog newDialog(GUIFrame aOwnerFrame) {
		WDialog result;
		WFrame current_frame;
		if (aOwnerFrame!=null)
			current_frame=(WFrame)aOwnerFrame;
		else
			current_frame=mFrame;
		result=new WDialog(current_frame,this);
		result.setIcon(AndroidImage.getDrawable(current_frame.getImage()));
		return result;
	}

	public GUIDialog newDialog(GUIDialog aOwnerDialog, GUIForm aOwnerFrame) {
		WDialog result;
		if (aOwnerDialog!=null) {
			result=new WDialog(((WDialog)aOwnerDialog).getContext(),this);
			result.setIcon(((WDialog)aOwnerDialog).getIcon());
		}
		else {		
			WFrame current_frame;
			if (aOwnerFrame!=null)
				current_frame=(WFrame)aOwnerFrame;
			else
				current_frame=mFrame;
			result=new WDialog(current_frame,this);
			result.setIcon(AndroidImage.getDrawable(current_frame.getImage()));
		}
		return result;
	}

	public GUIForm newForm(String aActionId) {
		return new WForm(aActionId,this);
	}

	public GUIWindows newWindows() {
		return new WWindows(this);
	}

	public File selectOpenFileDialog(FileFilter filter, GUIObject aGUIOwner) {
		// TODO Auto-generated method stub
		return null;
	}

	public File selectSaveFileDialog(FileFilter filter, GUIObject aGUIOwner,
			String aDefaultFileName) {
		// TODO Auto-generated method stub
		return null;
	}

	protected boolean mResult=false;
	public boolean showConfirmDialog(String message, String title) {
		mResult=false;
		AlertDialog alertDialog = new AlertDialog.Builder(mFrame).create();  
	    alertDialog.setTitle(title);  
	    alertDialog.setMessage(message);
	    alertDialog.setIcon(new BitmapDrawable(ImgResources.class.getResourceAsStream("confirm.png")));
	    alertDialog.setButton("Oui", new DialogInterface.OnClickListener() {  
	      public void onClick(DialogInterface dialog, int which) {
	    	  mResult=true;
	        return;  
	    } });
	    alertDialog.setButton("Non", new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int which) {  
		    	  mResult=false;
		        return;  
		    } });
	    alertDialog.show();
	    return mResult;
	}

	public void showErrorDialog(String message, String title) {
		AlertDialog alertDialog = new AlertDialog.Builder(mFrame).create();  
	    alertDialog.setTitle(title);  
	    alertDialog.setMessage(message);  
	    alertDialog.setIcon(new BitmapDrawable(ImgResources.class.getResourceAsStream("error.png")));
	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {  
	      public void onClick(DialogInterface dialog, int which) {  
	        return;  
	    } });
	    alertDialog.show();
	}

	public void showMessageDialog(String message, String title) {
		AlertDialog alertDialog = new AlertDialog.Builder(mFrame).create();  
	    alertDialog.setTitle(title);  
	    alertDialog.setMessage(message);  
	    alertDialog.setIcon(new BitmapDrawable(ImgResources.class.getResourceAsStream("info.png")));
	    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {  
	      public void onClick(DialogInterface dialog, int which) {  
	        return;  
	    } });
	    alertDialog.show();
	}
	
}
