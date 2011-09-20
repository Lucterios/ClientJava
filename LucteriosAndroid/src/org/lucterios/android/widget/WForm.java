package org.lucterios.android.widget;

import org.lucterios.android.R;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIGenerator;
import org.lucterios.gui.NotifyFrameChange;
import org.lucterios.gui.NotifyFrameList;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.utils.LucteriosException;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

public class WForm extends Activity implements GUIForm {

	private FormVisitor mFormVisitor=null;
	private WContainer mContainer;
	private WGenerator mGenerator;

	private NotifyFrameList mNotifyFrameList = null;
	private NotifyFrameChange mNotifyFrameChange = null;
	private NotifyFrameObserver mNotifyFrameObserver = null;
	
	public WForm(String aActionId,WGenerator generator) {
		super();
		mGenerator=generator;
	}

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContainer=new WContainer(this, ContainerType.CT_NORMAL,null);
	}

	@Override
    public void onStart() {
        super.onStart();
		addContentView(mContainer, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if (mFormVisitor!=null) {
			mFormVisitor.execute(this);
	    }
    }
	
	public void activate() {
		if (mNotifyFrameList != null)
			mNotifyFrameList.selectFrame(this);
	}

	public void setActive(boolean aIsActive) {
		mContainer.setActive(aIsActive);
		if (mGenerator.getFrame() != null)
			mGenerator.getFrame().setActive(aIsActive);
	}
	
	public GUIContainer getContainer() {
		return mContainer;
	}

	public GUIGenerator getGenerator() {
		return mGenerator;
	}

	public AbstractImage getImage() {
		return AbstractImage.Null;
	}

	public String getName() {
		return "";
	}
	
	public String getTextTitle() {
		return getTitle().toString();
	}	

	public void setFormVisitor(FormVisitor formVisitor) {
		mFormVisitor=formVisitor;
	}

	public void setImage(AbstractImage image) {
		
	}

	public void setNotifyFrameChange(NotifyFrameChange aNotifyFrameChange) {
		mNotifyFrameChange=aNotifyFrameChange;
	}

	public void setNotifyFrameList(NotifyFrameList aNotifyFrameList) {
		mNotifyFrameList=aNotifyFrameList;
	}

	public void setNotifyFrameObserver(NotifyFrameObserver aNotifyFrameObserver) {
		mNotifyFrameObserver=aNotifyFrameObserver;
	}

	public void setTextTitle(String title) {
		setTitle(title);
	}

	public void Change() {
		if (mNotifyFrameChange != null)
			mNotifyFrameChange.Change();
	}

	public void Close() {
		if (mNotifyFrameObserver != null) {
			mNotifyFrameObserver.close(true);
			mNotifyFrameObserver = null;
		}
		if (mNotifyFrameList != null) {
			mNotifyFrameList.removeFrame(this);
			mNotifyFrameList = null;
		}
		Change();
		mNotifyFrameChange = null;
	}


	public void refresh() {
		try {
			if (mNotifyFrameObserver != null)
				mNotifyFrameObserver.refresh();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void setSelected(boolean aSelected) { }

	public void setSize(int width, int height) { }
	
	public void toFront() {	}

	public void dispose() {	}

	public void refreshSize() { }

	public void requestFocus() { }

	public void setDefaultButton(GUIButton defaultBtn) { }

	public void setLocation(int x, int y) { }
	
}
