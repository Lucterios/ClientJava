package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class MockComponent implements GUIComponent {
	
	private GUIComponent mOwner;
	public MockComponent(GUIComponent owner){
		super();
		mOwner=owner;
	}
	
	private Object mParam=null;

	public Object getParam() {
		return mParam;
	}

	public void setParam(Object param) {
		this.mParam = param;
	}
	
	public void addActionListener(GUIActionListener l) {}

	public void addFocusListener(GUIFocusListener l) {}

	public void removeActionListener(GUIActionListener l) {	}

	public void removeFocusListener(GUIFocusListener l) {}
	
	private int mColor=0;
	public int getBackgroundColor() {
		return mColor;
	}

	public void setBackgroundColor(int color) {
		mColor=color;
	}
	
	private String mName="";
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName=name;
	}
	
	public GUIComponent getOwner() {
		return mOwner;
	}

	private boolean mActive=true;
	public boolean isActive() {
		return mActive;
	}
	public void setActive(boolean aActive) {
		mActive=aActive;
	}	
	
	private boolean mEnabled=true;
	public boolean isEnabled() {
		return mEnabled;
	}
	public void setEnabled(boolean enabled) {
		mEnabled=enabled;
	}

	private boolean mVisible=false;
	public boolean isVisible() {
		return mVisible;
	}

	public void setVisible(boolean visible) {
		mVisible=visible;
	}
	
	public void repaint() { }

	public void requestFocus() { }

	public void setActiveMouseAction(boolean isActive) { }


	public void setToolTipText(String toolTip) {

	}

}
