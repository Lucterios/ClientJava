package org.lucterios.android.widget;


import org.lucterios.android.R;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.utils.LucteriosException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;

public class WDialog extends Dialog implements GUIDialog {

	private NotifyFrameObserver mNotifyFrameClose = null;
	private DialogVisitor mDialogVisitor=null;
	private WContainer mContainer;
	private WGenerator mGenerator;

	private double mPosition=0.5;
	private boolean isCreate=false;
	private String mTitle; 
	
	public WDialog(Context context,WGenerator generator) {
		super(context);
		mGenerator=generator;
		init();
	}

	public GUIDialog createDialog() {
		return new WDialog(getContext(),mGenerator);
	}
	
	public GUIGenerator getGenerator() {
		return mGenerator;
	}
	
	protected void init(){
		mContainer=new WContainer(getContext(), ContainerType.CT_NORMAL,null);
	}

	public void setDialogVisitor(DialogVisitor dialogVisitor){
		mDialogVisitor=dialogVisitor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
    public void onStart() {
        super.onStart();
		addContentView(mContainer, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (mDialogVisitor!=null) {
			mDialogVisitor.execute(this);
	    }
		this.initialPosition();
	}
	
	public void setTextTitle(String title) {
		super.setTitle(title);
		mTitle=title;
	}
	
	public void setVisible(boolean aVisible) {
		if (aVisible) {
			if (!isCreate) {
				isCreate=true;
				((WFrame)mGenerator.getFrame()).showNewDialog(this);
			}
			else
				show();
		}
		else
			hide();
	}

	public void close() {
		if (mNotifyFrameClose != null) {
			mNotifyFrameClose.close(true);
			mNotifyFrameClose = null;
		}
		mNotifyFrameClose = null;
	}

	public void dispose() {
		dismiss();
	}

	public GUIContainer getContainer() {
		return mContainer;
	}

	public void refresh() {
		try {
			if (mNotifyFrameClose != null)
				mNotifyFrameClose.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void setActive(boolean aIsActive) {	
		mContainer.setActive(aIsActive);
		if (mGenerator.getFrame() != null)
			mGenerator.getFrame().setActive(aIsActive);
	}

	public void setNotifyFrameClose(NotifyFrameObserver aNotifyFrameClose) {
		mNotifyFrameClose = aNotifyFrameClose;
	}

	public void setDefaultButton(GUIButton btnAdd) { }

	public int getSizeX() {		
		return getWindow().getAttributes().width;
	}

	public int getSizeY() {
		return getWindow().getAttributes().height;
	}

	public void setResizable(boolean isResizable) { }

	public void refreshSize() { }

	public void setSize(int width, int height) {
		getWindow().getAttributes().width=width;
		getWindow().getAttributes().height=height;
		getWindow().setAttributes(getWindow().getAttributes());
	}

	public void setLocation(int x, int y) {
		getWindow().getAttributes().x=x;
		getWindow().getAttributes().y=y;
		getWindow().setAttributes(getWindow().getAttributes());
	}

	public void initialPosition() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		setLocation((displaymetrics.widthPixels - getWindow().getAttributes().width) / 2,(int)((displaymetrics.heightPixels - getWindow().getAttributes().height)*mPosition));
	}

	public void setPosition(double position) {
		mPosition=position;
	}

	public void pack() { }

	public void requestFocus() { }

	public void toFront() {	}

	public String getTextTitle() {
		return mTitle;
	}

}
