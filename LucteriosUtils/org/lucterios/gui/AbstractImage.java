package org.lucterios.gui;

import java.io.File;
import java.net.URL;

public abstract class AbstractImage {
	
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

	public abstract boolean load(File file);
	
	public abstract boolean load(URL url);
	
	protected abstract void initialize(byte[] imageData);

	public abstract int getHeight();
	
	public abstract int getWidth();

}
