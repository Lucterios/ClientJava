package org.lucterios.gui.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.lucterios.gui.AbstractImage;

public class TestImage extends AbstractImage {

	public TestImage() {
		super();
		initialize(new byte[]{});
	}
	
	public TestImage(byte[] dataImage) {
		super();
		initialize(dataImage);
	}

	@Override
	public int getHeight() {
		return intValue(16);
	}

	@Override
	public int getWidth() {
		return intValue(20);
	}
	
	private int intValue(int begin){
		int ret=0;
		if ((begin+4)<mImageData.length) {
			for(int step=begin;step<(begin+4);step++)
				ret=ret*256+(((int)mImageData[step]) & 0x0FF);
		}
		return ret;
	}

	private byte[] mImageData=new byte[]{};
	
	
	public int getInternalSize() {
		return mImageData.length;
	}

	public String getInternalString() {
		String res="";
		for(byte val:mImageData) {
			res+=String.format("%2h ",((int)val) & 0x00FF);
		}
		return res;
	}
	
	@Override
	protected void initialize(byte[] imageData) {
		mImageData=imageData;
	}

	@Override
	public boolean load(File file) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(file);
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = fis.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			fis.close();
			buffer.flush();
			initialize(buffer.toByteArray());
			return true;
		} catch (Exception e) {
			return false;	
		}
	}

	@Override
	public boolean load(URL url) {
		try {
			File file=new File(url.toURI());
			return load(file);
		} catch (Exception e) {
			return false;	
		}
	}

	@Override
	public AbstractImage resizeIcon(int aHeight, boolean aOnlyIfBigger) {
		return this;
	}

	@Override
	public AbstractImage resize(int height, int width) {
		return this;
	}

}
