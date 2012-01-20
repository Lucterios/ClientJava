/*
*    This file is part of Lucterios.
*
*    Lucterios is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Lucterios is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with Lucterios; if not, write to the Free Software
*    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*
*	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
*/

package org.lucterios.Print.GUI;

import org.lucterios.Print.Data.PrintAbstract;
import org.lucterios.Print.Data.PrintPage;
import org.lucterios.Print.Data.PrintVector;
import org.lucterios.Print.Observer.PrintManager;
import org.lucterios.Print.Observer.PrintObserverAbstract;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUISpinEdit;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;


public class PreviewObserver implements PrintObserverAbstract,PrintPanelBase.ObserverCallBack 
{
	private PrintManager mPrintManager;
	private GUIContainer mContainer;
    /** Creates new form BrowserObserver 
     * @param guiContainer */
    public PreviewObserver(PrintManager aPrintManager, GUIContainer guiContainer) 
    {
        super();
        mContainer=guiContainer;
        mRefreshing=false;
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        initComponents();
        initSheet();
        initPage();        
        refresh();
    }

    private GUIContainer mTopPanel; 
    private GUILabel mlblEchelle;
    private GUILabel mlblPages;
    private GUISpinEdit mEchelles;
    private GUICombo mPages;
    private GUIContainer mMain; 

    private PrintPanelBase mSheet; 
    private PrintPanelBase mHeader; 
    private PrintPanelBase mFooter; 
    private PrintPanelBase mRight; 
    private PrintPanelBase mLeft; 
    private PrintPanelBase mBody; 

    
    private void initComponents() 
    {
        GUIParam param;
    	
        param=new GUIParam(0,0);
        param.setFill(FillMode.FM_HORIZONTAL);
        param.setReSize(ReSizeMode.RSM_NONE);
        mTopPanel=mContainer.createContainer(ContainerType.CT_NORMAL, param);
    	
        param=new GUIParam(0,0);
        param.setFill(FillMode.FM_NONE);
        param.setReSize(ReSizeMode.RSM_NONE);
        param.setPad(5);
        mlblEchelle = mTopPanel.createLabel(param);
        mlblEchelle.setTextString("Echelle");
        mlblEchelle.setStyle(1);

        param=new GUIParam(1,0);
        param.setFill(FillMode.FM_NONE);
        mEchelles=mTopPanel.createSpinEdit(param); 
        mEchelles.init(100,1,1000);
        mEchelles.setName("mEchelles");
        mEchelles.addActionListener(new GUIActionListener(){
			public void actionPerformed() {
				changeEchelle();
			}
		});

        param=new GUIParam(2,0);
        param.setFill(FillMode.FM_BOTH);
        param.setReSize(ReSizeMode.RSM_BOTH);
        mTopPanel.createLabel(param);
        
        param=new GUIParam(3,0);
        param.setFill(FillMode.FM_NONE);
        param.setReSize(ReSizeMode.RSM_NONE);
        param.setPad(2);
        mlblPages = mTopPanel.createLabel(param);
        mlblPages.setTextString("Pages");
        mlblPages.setStyle(1);

        param=new GUIParam(4,0);
        param.setFill(FillMode.FM_NONE);
        param.setPrefSizeY(20);
        param.setPrefSizeX(150);
        mPages= mTopPanel.createCombo(param);
        mPages.setName("mPages");
        mPages.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
				changePageSelected();
			}
        });

        param=new GUIParam(6,0);
        param.setFill(FillMode.FM_BOTH);
        param.setReSize(ReSizeMode.RSM_BOTH);
        mTopPanel.createLabel(param);
        
        param=new GUIParam(0,1);
        mMain=mContainer.createContainer(ContainerType.CT_SCROLL, param);
        mMain.setBackgroundColor(0x808080);
        mMain.setBorder(1, 1, 1, 1, 0x000000);

        mContainer.setSize(600, 600);
        mContainer.setMinimumSize(200, 200);
    }

    private void addBorder(int x, int y, int width)
    {
        GUIParam param=new GUIParam(x,y,width,1);
        param.setFill(FillMode.FM_NONE);
        param.setReSize(ReSizeMode.RSM_HORIZONTAL);
        param.setPad(5);
	    mMain.createLabel(param).setBackgroundColor(0xFF0000);
    }
    
    private void initSheet()
    {
    	GUIParam param;
    	
    	
    	param=new GUIParam(1,1,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE);
    	GUIContainer sheetCont=mMain.createContainer(ContainerType.CT_NORMAL,param);
        mSheet=new PrintPanelBase(this,sheetCont);
        
        GUIContainer newContainer; 
        param=new GUIParam(0,0,3,1);
        newContainer=sheetCont.createContainer(ContainerType.CT_NORMAL,param);
		newContainer.setLayoutIsGrid(false);
        mHeader=new PrintPanelBase(this,newContainer);
        
        param=new GUIParam(0,2,3,1);
        newContainer=sheetCont.createContainer(ContainerType.CT_NORMAL,param);
		newContainer.setLayoutIsGrid(false);
        mFooter=new PrintPanelBase(this,newContainer);
        
        param=new GUIParam(2,1);
        newContainer=sheetCont.createContainer(ContainerType.CT_NORMAL,param);
		newContainer.setLayoutIsGrid(false);
        mRight=new PrintPanelBase(this,newContainer);
        
        param=new GUIParam(0,1);
        newContainer=sheetCont.createContainer(ContainerType.CT_NORMAL,param);
		newContainer.setLayoutIsGrid(false);
        mLeft=new PrintPanelBase(this,newContainer);
        
        param=new GUIParam(1,1);
        newContainer=sheetCont.createContainer(ContainerType.CT_NORMAL,param);
		newContainer.setLayoutIsGrid(false);
        mBody=new PrintPanelBase(this,newContainer);

        addBorder(0,0,3);
        addBorder(0,2,3);
        addBorder(0,1,1);
        addBorder(2,1,1);
    }

    private void initPage()
    {
        PrintPage page=mPrintManager.getPage();
        mSheet.setPrintType(PrintPanelBase.PAGE);
        mHeader.setPrintType(PrintPanelBase.NORTH);
        mFooter.setPrintType(PrintPanelBase.SOUTH);
        mRight.setPrintType(PrintPanelBase.EAST);
        mLeft.setPrintType(PrintPanelBase.WEST);
        mBody.setPrintType(PrintPanelBase.CENTER);

        mSheet.setPrintObject(page);
	    mHeader.setPrintObject(page);
	    mFooter.setPrintObject(page);
	    mRight.setPrintObject(page); 
	    mLeft.setPrintObject(page);
	    mBody.setPrintObject(page);
    }

	public void refresh() 
	{
		PrintPage page=mPrintManager.getPage();
		mRefreshing=true;
		try
		{
			mPages.removeAllElements();
			PrintVector page_list=page.body;
			for(int index=0;index<page_list.size();index++)
				mPages.addElement(page_list.get(index));
		}
		finally
		{
			mRefreshing=false;
		}
		initialize();
		changePageSelected();
		changeEchelle();
	}

	public void beforeChangeCurrentEvent() {}

	public void changeCurrentEvent() 
	{
		mRefreshing=true;
		try
		{
			int num=mPrintManager.getBodyNumber();
			if (num!=-1)
				mPages.setSelectedIndex(num);
		    mBody.setBodyNum(num); 
		}
		finally
		{
			mRefreshing=false;
		}
		mSheet.setVisitObject(mPrintManager.getCurrent());
	    mHeader.setVisitObject(mPrintManager.getCurrent());
	    mFooter.setVisitObject(mPrintManager.getCurrent());
	    mRight.setVisitObject(mPrintManager.getCurrent()); 
	    mLeft.setVisitObject(mPrintManager.getCurrent()); 
	    mBody.setVisitObject(mPrintManager.getCurrent());
	    ModifyEvent();
	}

	public void addItemEvent() 
	{
        refresh();
	}

	public void deleteCurrentEvent() 
	{
        refresh();
	}

	public void initialize() 
	{
		mSheet.initialize();
	    mHeader.initialize();
	    mFooter.initialize();
	    mRight.initialize(); 
	    mLeft.initialize(); 
	    mBody.initialize(); 
	}
	
	public void ModifyEvent() 
	{
		mSheet.refresh();
	    mHeader.refresh();
	    mFooter.refresh();
	    mRight.refresh(); 
	    mLeft.refresh(); 
	    mBody.refresh(); 
	}

	private boolean mRefreshing;
	private void changePageSelected()
	{
		if (!mRefreshing)
		{
			mRefreshing=true;
			try
			{
		        PrintAbstract body=(PrintAbstract)mPages.getSelectedItem();
				mPrintManager.setCurrent(body);
			}
			finally
			{
				mRefreshing=false;
			}
		}
	}
	
	private void changeEchelle()
	{
		int num=(int)mEchelles.getNumber();
		mSheet.setSizeEchelle(num);
	    mHeader.setSizeEchelle(num);
	    mFooter.setSizeEchelle(num);
	    mRight.setSizeEchelle(num); 
	    mLeft.setSizeEchelle(num); 
	    mBody.setSizeEchelle(num); 
	    changeCurrentEvent();
	}

	PrintPanelAbstract mCurrent;
	public void setCurrent(PrintPanelAbstract newCurrent) 
	{
		mCurrent=newCurrent;		
	}
	
	public void selectObject(PrintAbstract printObject)
	{
		mPrintManager.setCurrent(printObject);
	}
}

