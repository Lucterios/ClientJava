package org.lucterios.mock;

import java.net.URL;

import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIImage;
import org.lucterios.gui.test.TestImage;

public class MockImage extends MockComponent implements GUIImage {

	private TestImage mImage=new TestImage();

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public MockImage(GUIComponent aOwner){
        super(aOwner);
	}
	
	public AbstractImage getImage() {		
		return mImage;
	}

	public void setImage(AbstractImage image) {
		if (image instanceof TestImage) {
			mImage=(TestImage)image;
		}
	}

	public void setImage(URL image) {
		TestImage img=new TestImage();
		img.load(image);
		setImage(img);
	}

	public void setSize(int width, int height) { }
		
}
