package org.lucterios.android.widget;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.ui.GUIActionListener;

import android.content.Context;
import android.webkit.WebView;

public class WHyperText extends WebView implements GUIHyperText {

	private String mText;
	private String mName;
	private GUIComponent mOwner;

	public void addFocusListener(GUIFocusListener l){}

	public void removeFocusListener(GUIFocusListener l){}
	
	
	public WHyperText(Context context, WContainer owner) {
		super(context);
		mOwner=owner;
	}

	public String getTextString() {
		return mText;
	}

	public void setTextString(String text) {
		mText=text;
		loadData(text, "text/html", "utf-8");
	}

	public boolean isVisible() {
		return true;
	}
	
	public void setVisible(boolean visible) { }

	public void addActionListener(GUIActionListener l) {
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

	public void removeActionListener(GUIActionListener l) {
		// TODO Auto-generated method stub
		
	}

	public void repaint() {
		// TODO Auto-generated method stub
		
	}

	public void requestFocusGUI() {
		// TODO Auto-generated method stub
		
	}

	public void setActiveMouseAction(boolean isActive) {
		// TODO Auto-generated method stub
		
	}

	public void setName(String name) {
		mName=name;
	}

	public void setToolTipText(String toolTip) { }

	public void setNbClick(int mNbClick) { }

	public String getToolTipText() {
		return "";
	}

	public void setHyperLink(String string) {
		// TODO Auto-generated method stub
		
	}

}
