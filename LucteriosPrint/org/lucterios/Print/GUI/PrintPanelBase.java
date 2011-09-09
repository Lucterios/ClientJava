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

import org.lucterios.Print.Data.*;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;


public class PrintPanelBase extends PrintPanelAbstract
{
	private static final long serialVersionUID = 1L;
	
	public PrintPanelBase(ObserverCallBack observer,GUIContainer container)
	{
		super(observer,container);
		mPrintPage=null;
	}
	
	private int mPrintType;
	public void setPrintType(int printType)
	{
		mPrintType=printType;
	}

	private int mBodyNum=0;
	public void setBodyNum(int bodyNum)
	{
		if (bodyNum!=-1)
		{
			boolean refreshing=(mBodyNum!=bodyNum);
			mBodyNum=bodyNum;
			if (refreshing)
			{
				initialize();
				refresh();
			}
		}
	}
	
	private PrintPage mPrintPage;
	public void setPrintObject(PrintPage printObject)
	{
		mPrintPage=printObject;
	}
	
	public PrintArea getPrintArea()
	{
		switch(mPrintType)
		{
			case CENTER:
				return (PrintArea)mPrintPage.body.get(mBodyNum);
			case NORTH:
				return mPrintPage.header;
			case SOUTH:
				return mPrintPage.bottom;
			case EAST:
				return mPrintPage.rigth;
			case WEST:
				return mPrintPage.left;
			case PAGE:
				return null;
			default: 
				//TODO: Implement 'default' statement
				break;
		}
		return null;
	}

	public void refresh()
	{
		super.refresh();
		for(int index=0;index<mContainer.count();index++)
		{
			GUIComponent cmp=mContainer.get(index);
			if (GUIContainer.class.isInstance(cmp) && PrintPanelObject.class.isInstance(((GUIContainer)cmp).getObject()))
			{
				PrintPanelObject current_obj=(PrintPanelObject)((GUIContainer)cmp).getObject();
				current_obj.refresh();
			}
		}
	}
	
	public void initialize()
	{
		int idx=0;
		while(idx<mContainer.count())
		{
			GUIComponent cmp=mContainer.get(idx);
			if (GUIContainer.class.isInstance(cmp) && PrintPanelObject.class.isInstance(((GUIContainer)cmp).getObject()))
				mContainer.remove(idx);
			else
				idx++;
		}
		PrintArea area=getPrintArea();
		if (area!=null)
			for(int index=0;index<area.size();index++)
				if (PrintContainer.class.isInstance(area.get(index)))
				{
					PrintPanelObject new_obj=new PrintPanelObject(mObserver,mContainer.createContainer(ContainerType.CT_NORMAL, new GUIParam(0,0)));
					new_obj.setPrintContainer((PrintContainer)area.get(index));
					new_obj.setSizeEchelle(mEchelle);
				}
	}

	public void setVisitObject(PrintAbstract printObject)
	{
		if (mPrintType==PAGE)
			setActive(mPrintPage.equals( printObject ));
		else
			setActive(getPrintArea().equals( printObject ));
		for(int index=0;index<mContainer.count();index++)
		{
			GUIComponent cmp=mContainer.get(index);
			if (GUIContainer.class.isInstance(cmp) && PrintPanelObject.class.isInstance(((GUIContainer)cmp).getObject()))
			{
				PrintPanelObject current_obj=(PrintPanelObject)((GUIContainer)cmp).getObject();
				current_obj.setVisit(printObject);
			}
		}
	}
	
	public void setSizeEchelle(int echelle)
	{
		super.setSizeEchelle(echelle);
		double width=0;
		double height=0;
		switch(mPrintType)
		{
			case CENTER:
				width=mPrintPage.page_width-mPrintPage.margin_left-mPrintPage.margin_right-mPrintPage.left.extent-mPrintPage.rigth.extent;
				height=mPrintPage.page_height-mPrintPage.margin_top-mPrintPage.margin_bottom-mPrintPage.header.extent-mPrintPage.bottom.extent;
				break;
			case NORTH:
				width=mPrintPage.page_width-mPrintPage.margin_left-mPrintPage.margin_right;
				height=mPrintPage.header.extent;
				break;
			case SOUTH:
				width=mPrintPage.page_width-mPrintPage.margin_left-mPrintPage.margin_right;
				height=mPrintPage.bottom.extent;
				break;
			case EAST:
				width=mPrintPage.rigth.extent;
				height=mPrintPage.page_height-mPrintPage.margin_top-mPrintPage.margin_bottom-mPrintPage.header.extent-mPrintPage.bottom.extent; 
				break;
			case WEST:
				width=mPrintPage.left.extent;
				height=mPrintPage.page_height-mPrintPage.margin_top-mPrintPage.margin_bottom-mPrintPage.header.extent-mPrintPage.bottom.extent;
				break;
			case PAGE:
				mContainer.setBorder((int)(mPrintPage.margin_top*echelle/Ratio), (int)(mPrintPage.margin_left*echelle/Ratio), (int)(mPrintPage.margin_bottom*echelle/Ratio), (int)(mPrintPage.margin_right*echelle/Ratio),0xFFFFFF);
				width=mPrintPage.page_width;
				height=mPrintPage.page_height;
				break;
			default: 
				//TODO: Implement 'default' statement
				break;
		}
        mContainer.setSize((int)(width*echelle/Ratio),(int)(height*echelle/Ratio));
        mContainer.setMinimumSize((int)(width*echelle/Ratio),(int)(height*echelle/Ratio));
        mContainer.setMaximumSize((int)(width*echelle/Ratio),(int)(height*echelle/Ratio));
		for(int index=0;index<mContainer.count();index++)
		{
			GUIComponent cmp=mContainer.get(index);
			if (GUIContainer.class.isInstance(cmp) && PrintPanelObject.class.isInstance(((GUIContainer)cmp).getObject()))
			{
				PrintPanelObject current_obj=(PrintPanelObject)((GUIContainer)cmp).getObject();
				current_obj.setSizeEchelle(echelle);
			}
		}
	}

	public void activate() 
	{
		PrintArea area=getPrintArea();
		if (area!=null)
			mObserver.selectObject(area);
		else
			mObserver.selectObject(mPrintPage);
	}
}
