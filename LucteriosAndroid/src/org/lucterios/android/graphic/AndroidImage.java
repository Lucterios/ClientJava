package org.lucterios.android.graphic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.lucterios.gui.AbstractImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AndroidImage extends AbstractImage {

	public AndroidImage(){
		super(new byte[]{});
	}
	
	public AndroidImage(byte[] imageData) {
		super(imageData);
	}	
	
	public AndroidImage(Bitmap mBitmap) {
		super();
		mObject=mBitmap;
	}	
	
	public AndroidImage(Drawable icon) {
		super();
		int width=icon.getBounds().right-icon.getBounds().left;
		int height=icon.getBounds().bottom-icon.getBounds().top;
		mObject= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas((Bitmap)mObject); 
		icon.setBounds(0, 0, width, height); 
		icon.draw(canvas);	
	}

	public BitmapDrawable getDrawable(){
		return new BitmapDrawable((Bitmap)mObject);
	}
	
	@Override
	protected void initialize(byte[] imageData) {
		mObject=BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
	}
	
	@Override
	public int getHeight() {
		if (mObject!=null)
			return ((Bitmap)mObject).getHeight();
		return 0;
	}

	@Override
	public int getWidth() {
		if (mObject!=null)
			return ((Bitmap)mObject).getWidth();
		return 0;
	}

	@Override
	public boolean load(File file) {
		try {
			InputStream is=new FileInputStream(file);
			mObject=BitmapFactory.decodeStream(is);
			return (mObject!=null);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean load(URL url) {
		try {
			mObject=BitmapFactory.decodeStream(url.openStream());
			return (mObject!=null);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public AbstractImage resize(int height, int width) {
		Bitmap aIcon=(Bitmap)mObject;
    	if ((aIcon!=null) && (height>0) && (width>0) && (height!=aIcon.getHeight()) && (width!=aIcon.getWidth())){
    		int w = aIcon.getWidth();
    		int h = aIcon.getHeight();
    		float scaleWidth = ((float) width) / w;
    		float scaleHeight = ((float) height) / h;
    		Matrix matrix = new Matrix();
    		matrix.postScale(scaleWidth, scaleHeight);
    		Bitmap resizedBitmap = Bitmap.createBitmap(aIcon, 0, 0, w, h, matrix, false);    	
    		return new AndroidImage(resizedBitmap);
    	}
    	else
    		return this;
	}

	@Override
	public AbstractImage resizeIcon(int aHeight, boolean aOnlyIfBigger) {
		Bitmap aIcon=(Bitmap)mObject;
    	if ((aIcon!=null) && (!aOnlyIfBigger || (aIcon.getHeight()>aHeight))){
			int scaledW = (int)(aHeight * (double)aIcon.getWidth()/(double)aIcon.getHeight());
    		return resize(aHeight, scaledW);
    	}
    	else
    		return this;
	}

}
