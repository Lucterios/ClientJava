package org.lucterios.Print.GUI;

import org.lucterios.Print.Data.PrintAbstract;
import org.lucterios.Print.Data.PrintCell;
import org.lucterios.Print.Data.PrintColumn;
import org.lucterios.Print.Data.PrintText;
import org.lucterios.Print.Observer.PrintManager;
import org.lucterios.Print.Observer.PrintObserverAbstract;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIHyperMemo;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIComponent.GUIFocusListener;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class EditorObserver implements PrintObserverAbstract {

	private static final String MENU_SPECIAL="Variables";
	private static final String TAG_BEGIN="[{";
	private static final String TAG_END="}]";

    protected GUIHyperMemo mFormedEditor=null;
	private PrintManager mPrintManager;
	private GUIContainer mContainer;

	public EditorObserver(PrintManager aPrintManager, GUIContainer guiContainer) 
    {
        super();
        mContainer=guiContainer;
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        GUIParam param;

        param=new GUIParam(0,0);
        param.setReSize(ReSizeMode.RSM_BOTH);
        mFormedEditor=mContainer.createHyperMemo(param);
        mFormedEditor.addStandardPopupMenu(false);
        mFormedEditor.addFocusListener(new GUIFocusListener(){
			public void focusLost(GUIComponent origine, GUIComponent target) {
				propertyFocusLost();
			}
        });      
        mContainer.setMinimumSize(100, 100);
        refresh();
    }

    private boolean mDataChanging=false;
    public void refresh() 
    {
		if (!mDataChanging)
		try
		{
	        PrintAbstract mCurrent=mPrintManager.getCurrent();
	        if (PrintText.class.isInstance(mCurrent))
	        {
	            String source=((PrintText)mCurrent).getText();
	        	mFormedEditor.addSpecialMenus(MENU_SPECIAL,((PrintText)mCurrent).getSelection(),TAG_BEGIN,TAG_END);
	            mFormedEditor.load(source);
	        	mFormedEditor.setEnabled(true);
	        }
	        else if (PrintColumn.class.isInstance(mCurrent))
	        {
	            String source=((PrintColumn)mCurrent).cell.getText();
	        	mFormedEditor.addSpecialMenus(MENU_SPECIAL,((PrintColumn)mCurrent).getSelection(),TAG_BEGIN,TAG_END);
	            mFormedEditor.load(source);
	        	mFormedEditor.setEnabled(true);
	        }
	        else if (PrintCell.class.isInstance(mCurrent))
	        {
	            String source=((PrintCell)mCurrent).getText();
	        	mFormedEditor.addSpecialMenus(MENU_SPECIAL,((PrintCell)mCurrent).getSelection(),TAG_BEGIN,TAG_END);
	            mFormedEditor.load(source);
	        	mFormedEditor.setEnabled(true);
	        }
	        else
	        	clear();
        	mContainer.repaint();       
		}
		finally
		{
			mDataChanging=false;
		}		
	}

	public void beforeChangeCurrentEvent() 
	{
        PrintAbstract mCurrent=mPrintManager.getCurrent();
        if (PrintText.class.isInstance(mCurrent) && (mFormedEditor!=null))
            ((PrintText)mCurrent).setText(mFormedEditor.save());
        else if (PrintColumn.class.isInstance(mCurrent) && (mFormedEditor!=null))
            ((PrintColumn)mCurrent).cell.setText(mFormedEditor.save());
        else if (PrintCell.class.isInstance(mCurrent) && (mFormedEditor!=null))
            ((PrintCell)mCurrent).setText(mFormedEditor.save());
        mPrintManager.DataChanged();
	}

	public void changeCurrentEvent() {
        refresh();
	}

	public void addItemEvent() {
		clear();
	}

	public void deleteCurrentEvent() {
		clear();
	}

	public void clear() {
	    mFormedEditor.load("");
    	mFormedEditor.addSpecialMenus(MENU_SPECIAL,null,TAG_BEGIN,TAG_END);
		mFormedEditor.setEnabled(false);
	}
	
	public void ModifyEvent() {
        refresh();
	}
	
	protected void propertyFocusLost() 
	{
		if (!mDataChanging)
		try
		{
			mDataChanging=true;
			beforeChangeCurrentEvent();	
		}
		finally
		{
			mDataChanging=false;
		}		
	}
	
}
