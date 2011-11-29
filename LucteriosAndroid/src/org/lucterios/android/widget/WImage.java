package org.lucterios.android.widget;

import java.net.URL;

import org.lucterios.android.graphic.AndroidImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIImage;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.widget.ImageView;

public class WImage extends ImageView implements GUIImage {

	private String mName;

	private WContainer mOwner;
	public WImage(Context context, WContainer owner) {
		super(context);
		mOwner=owner;
	}

	public AbstractImage getImage() {
		if (getDrawable()!=null)
			return new AndroidImage(getDrawable());
		else
			return AbstractImage.Null;
	}

	public void setImage(AbstractImage image) {
		setImageDrawable(AndroidImage.getDrawable(image));
	}

	public void setImage(URL image) {
		AndroidImage img=new AndroidImage();
		img.load(image);
		setImage(img);
	}

	public void setSize(int width, int height) {
		setMaxHeight(height);
		setMaxWidth(width);
	}

	public void clearFocusListener(){}


	public void addActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub

	}

	public void addFocusListener(GUIFocusListener l) {
		// TODO Auto-generated method stub

	}

	public int getBackgroundColor() {
		return 0;
	}

	public String getName() {
		return mName;
	}

	public GUIComponent getOwner() {
		return mOwner;
	}

	public boolean isActive() {
		if (mOwner!=null)
			return mOwner.isActive();
		else
			return false;
	}

	public boolean isVisible() {
		return true;
	}

	public void removeActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub

	}

	public void removeFocusListener(GUIFocusListener l) {
		// TODO Auto-generated method stub

	}

	public void repaint() {	}

	public void requestFocusGUI() {	}

	public void setActiveMouseAction(boolean isActive) {
		// TODO Auto-generated method stub

	}

	public void setName(String name) {
		mName=name;
	}

	public void setToolTipText(String toolTip) { }

	public void setVisible(boolean visible) { }

	public void setNbClick(int mNbClick) { }
}
