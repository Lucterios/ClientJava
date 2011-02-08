package org.lucterios.Print.GUI;

import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import org.lucterios.Print.Data.PrintAbstract;
import org.lucterios.Print.Data.PrintCell;
import org.lucterios.Print.Data.PrintColumn;
import org.lucterios.Print.Data.PrintText;
import org.lucterios.Print.Observer.PrintManager;
import org.lucterios.Print.Observer.PrintObserverAbstract;
import org.lucterios.graphic.LucteriosEditor;

public class EditorObserver extends JPanel implements PrintObserverAbstract {

	private static final long serialVersionUID = 1L;
	private static final String MENU_SPECIAL="Variables";
	private static final String TAG_BEGIN="[{";
	private static final String TAG_END="}]";

    protected LucteriosEditor mFormedEditor=null;
	private PrintManager mPrintManager;

	public EditorObserver(PrintManager aPrintManager) 
    {
        super();
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        setLayout(new GridBagLayout());
        mFormedEditor=new LucteriosEditor(false);
        mFormedEditor.setMinimumSize(new java.awt.Dimension(50,50));
        mFormedEditor.setPreferredSize(new java.awt.Dimension(100,100));
        mFormedEditor.addStandardPopupMenu(false);
        mFormedEditor.setOpaque(false);
        mFormedEditor.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				propertyFocusLost();
			}
        });
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(mFormedEditor, gridBagConstraints);
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
        	repaint();       
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
