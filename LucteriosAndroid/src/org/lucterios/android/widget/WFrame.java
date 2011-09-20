package org.lucterios.android.widget;


import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GuiFormList;
import org.lucterios.ui.GUIActionListener;

import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;

public class WFrame extends WForm implements GUIFrame {

	private FrameVisitor mFrameVisitor=null;
	private WFormList mFormList;
	
	public WFrame(WGenerator generator) {
		super("",generator);
		mFormList=new WFormList((WGenerator) this.getGenerator());
	}

    @Override
    public void onStart() {
        super.onStart();
		if (mFrameVisitor!=null) {
			mFrameVisitor.execute(this);
	    }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    private Menu mRootMenu=null;
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	mRootMenu=menu;
    	return true;
    }
	
	public void setFrameVisitor(FrameVisitor frameVisitor) {
		mFrameVisitor=frameVisitor;
	}
	
	private boolean mIsActive=true;
	public void setActive(boolean aIsActive) {
		mIsActive=aIsActive;
		super.setActive(aIsActive);
	}

	public boolean isActive() {
		return mIsActive;
	}
	
	public GuiFormList getFormList() {
		return mFormList;
	}

	public GUIMenu addMenu(boolean isNode) {
		MenuItem menuItem;
		if (isNode)
			menuItem=(MenuItem) mRootMenu.addSubMenu("");
		else
			menuItem=mRootMenu.add("");			
		return new WMenu(menuItem);
	}
	
	public GUIMenu getMenu(int index) {
		return new WMenu(mRootMenu.getItem(index));
	}

	public int getMenuCount() {
		return mRootMenu.size();
	}

	public void moveMenu(int beginExtraMenu) {
		// TODO Auto-generated method stub
		
	}

	public void removeMenu(int menuIdx) {
		mRootMenu.removeItem(menuIdx);
	}

	public void Maximise() {}

	public void pack() { }

	public void addWindowClose(GUIActionListener guiActionListener) {}

	private WDialog mLastNewDialog=null;
	public void showNewDialog(WDialog aDialog) {
		mLastNewDialog=aDialog;
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog result;
		if ((id==0) && (mLastNewDialog!=null)) {
			result=mLastNewDialog;
			mLastNewDialog=null;
		}
		else
			result=super.onCreateDialog(id);
		return result;
	}
	
}
