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

package org.lucterios.Print.Observer;

import java.util.*;

import org.lucterios.Print.Data.*;

/**
 *
 * @author lg
 */
public class PrintManager
{
    public static final int OBJECT_PAGE=1;
    public static final int OBJECT_IMAGE=2;
    public static final int OBJECT_TEXT=3;
    public static final int OBJECT_TABLE=4;
    public static final int OBJECT_COL=5;
    public static final int OBJECT_ROW=6;

    /** Creates a new instance of PrintManager */
    public PrintManager()
    {
        mPrintPage=new PrintPage();
        mCurrent=null;
        mPrintObservers=new ArrayList();
    }

    private ArrayList mPrintObservers;
    public void clearObservers()
    {
        mPrintObservers.clear();
    }

    public void addObserver(PrintObserverAbstract new_observer)
    {
        mPrintObservers.add(new_observer);
    }

    private PrintPage mPrintPage;
    public PrintPage getPage()
    {
        return mPrintPage;
    }

    public int getBodyNumber()
    {
    	PrintAbstract obj=getCurrent();
    	while ((obj!=null) && !PrintArea.class.isInstance(obj))
    		obj=obj.getOwner();
        return mPrintPage.body.indexOf(obj);
    }
    
    private PrintAbstract mCurrent;
    public PrintAbstract getCurrent()
    {
        return mCurrent;
    }

    private boolean change_current_flag=true;
    public void setCurrent(PrintAbstract new_current)
    {
        if (change_current_flag)
        {
            change_current_flag=false;
            try
            {
                for(int index=0;index<mPrintObservers.size();index++)
                {
                    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
                    obs.beforeChangeCurrentEvent();
                }                
                mCurrent=new_current;
                System.out.println("$$ setCurrent $$"+mCurrent);
                for(int index=0;index<mPrintObservers.size();index++)
                {
                    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
                    obs.changeCurrentEvent();
                }                
            }
            finally
            {
                change_current_flag=true;
            }
        }
    }

    public void refresh()
    {
        System.out.println("$$ refresh $$");
        for(int index=0;index<mPrintObservers.size();index++)
        {
            PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
            obs.refresh();
        }
    }

    public void deleteCurrent()
    {
        boolean notify=false;
        if (mCurrent!=null)
        {
            if (PrintArea.class.isInstance(mCurrent) && "body".equals( ((PrintArea)mCurrent).name ))
            {
                mPrintPage.body.remove(mCurrent);
                mCurrent=null;
                notify=true;
            }
            else if (PrintContainer.class.isInstance(mCurrent))
            {
                System.out.println("$$ deleteCurrent $$");
                PrintArea owner=(PrintArea)mCurrent.getOwner();
                owner.remove(mCurrent);
                mCurrent=owner;
                notify=true;
            }
            else if (PrintColumn.class.isInstance(mCurrent))
            {
                PrintTable tbl=(PrintTable)(mCurrent.getOwner().getOwner());
                tbl.removeCol((PrintColumn)mCurrent);
                mCurrent=tbl;
                notify=true;
            }
            else if (PrintRow.class.isInstance(mCurrent))
            {
                PrintTable tbl=(PrintTable)(mCurrent.getOwner().getOwner());
                tbl.removeRow((PrintRow)mCurrent);
                mCurrent=tbl;
                notify=true;
            }
        }
        if (notify)
        {
            for(int index=0;index<mPrintObservers.size();index++)
            {
                PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
                obs.deleteCurrentEvent();
            }
            setCurrent(mCurrent);
        }
    }

    public void addItem(int objectId)
    {
        System.out.println("$$ addItem "+objectId+" $$");
        if (isPage(objectId))
            addArea();
        else if (isColumn(objectId))
            addColInCurrentTable();
        else if (isRow(objectId))
            addRowInCurrentTable();
        else if (PrintArea.class.isInstance(mCurrent))
            addObjectItem(objectId);
    }

	private boolean isRow(int objectId) {
		return mCurrent.isRow() && (objectId==PrintManager.OBJECT_ROW);
	}

	private boolean isColumn(int objectId) {
		return mCurrent.isColumn() && (objectId==PrintManager.OBJECT_COL);
	}

	private boolean isPage(int objectId) {
		return mCurrent.isPage() && (objectId==PrintManager.OBJECT_PAGE);
	}

	private void addArea() {
		PrintArea new_area=new PrintArea();
		mPrintPage.body.add(new_area);
		for(int index=0;index<mPrintObservers.size();index++)
		{
		    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
		    obs.addItemEvent();
		}
		setCurrent(new_area);
	}

	private void addColInCurrentTable() {
		PrintTable tbl=(PrintTable)mCurrent.getOwner();
		PrintColumn new_col=tbl.newCol();
		for(int index=0;index<mPrintObservers.size();index++)
		{
		    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
		    obs.addItemEvent();
		}
		setCurrent(new_col);
	}

	private void addRowInCurrentTable() 
	{
		PrintTable tbl=(PrintTable)mCurrent.getOwner();
		PrintRow new_row=tbl.newRow();
		for(int index=0;index<mPrintObservers.size();index++)
		{
		    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
		    obs.addItemEvent();
		}
		setCurrent(new_row);
	}

	private void addObjectItem(int objectId) 
	{
		PrintContainer new_item=null;
		switch(objectId)
		{
		    case PrintManager.OBJECT_TEXT:
		        new_item=new PrintText();
		        break;
		    case PrintManager.OBJECT_IMAGE:
		        new_item=new PrintImage();
		        break;
		    case PrintManager.OBJECT_TABLE:
		        new_item=new PrintTable();
		        break;
			default: 
				//TODO: Implement 'default' statement
				break;
		}
		if (new_item!=null)
		{
		    ((PrintArea)mCurrent).add(new_item);
		    new_item.setOwner(mCurrent);
		    for(int index=0;index<mPrintObservers.size();index++)
		    {
		        PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
		        obs.addItemEvent();
		    }
		    setCurrent(new_item);            
		}
	}

    private boolean refreshing=false;
    public void DataChanged()
    {
        if (!refreshing)
        {
            refreshing=true;
            try
            {
                for(int index=0;index<mPrintObservers.size();index++)
                {
                    PrintObserverAbstract obs=(PrintObserverAbstract)mPrintObservers.get(index);
                    obs.ModifyEvent();
                }
            }
            finally
            {
                refreshing=false;
            }
        }
    }
}
