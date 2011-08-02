package org.lucterios.gui;

import java.io.File;
import java.net.URL;

public abstract class AbstractImage {
	
	public final static AbstractImage Null=new AbstractImage(){
		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public void initialize(byte[] imageData) {}

		@Override
		public boolean load(File file) {
			return false;
		}

		@Override
		public boolean load(URL url) {
			return false;
		}

		@Override
		public AbstractImage resizeIcon(int aHeight, boolean aOnlyIfBigger) {
			return this;
		}
		
	};
	
	protected Object mObject; 
	
	public AbstractImage(){
		this((AbstractImage)null);
	}
	
	public AbstractImage(AbstractImage image) {
		mObject=null;
	}

	public AbstractImage(byte[] imageData) {
		this((AbstractImage)null);
		initialize(imageData);
	}

	public Object getData(){
		return mObject;
	}
	
	public boolean isNull(){
		return (this==AbstractImage.Null) || (mObject==null); 
	}

	public abstract boolean load(File file);
	
	public abstract boolean load(URL url);
	
	protected abstract void initialize(byte[] imageData);

	public abstract int getHeight();
	
	public abstract int getWidth();

	public abstract AbstractImage resizeIcon(int aHeight,boolean aOnlyIfBigger);

}
