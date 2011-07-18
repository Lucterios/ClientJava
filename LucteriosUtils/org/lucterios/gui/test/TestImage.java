package org.lucterios.gui.test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.lucterios.gui.AbstractImage;
import org.lucterios.utils.Tools;

public class TestImage extends AbstractImage {

	@Override
	public int getHeight() {
		return intValue(18);
	}

	@Override
	public int getWidth() {
		return intValue(22);
	}
	
	private int intValue(int begin){
		int ret=0;
		for(int step=begin;step<(begin+4);step++)
			ret=ret*256+mImageData[step];
		return ret;
	}

	private byte[] mImageData=new byte[]{}; 
	@Override
	protected void initialize(byte[] imageData) {
		mImageData=imageData;
	}

	@Override
	public boolean load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			String value=Tools.parseISToString(fis);
			initialize(value.getBytes());
			return true;
		} catch (Exception e) {
			return false;	
		}
	}

	@Override
	public boolean load(URL url) {
		return false;
	}

}
