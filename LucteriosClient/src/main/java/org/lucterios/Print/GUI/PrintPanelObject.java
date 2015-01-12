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
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;


public class PrintPanelObject extends PrintPanelAbstract
{	
	private GUILabel mText; 

	private PrintContainer mPrintContainer; 
	
	public PrintPanelObject(PrintPanelBase.ObserverCallBack observer,GUIContainer container)
	{
		super(observer,container);
		mPrintContainer=null;
		mText=mContainer.createLabel(new GUIParam(0,0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_BOTH));
	}
	
	public void setPrintContainer(PrintContainer printContainer)
	{
		mPrintContainer=printContainer;
		mText.setTextString(mPrintContainer.toString());
		setSizeEchelle(mEchelle);
	}
	
	public void setVisit(PrintAbstract printObject)
	{
		setActive(mPrintContainer.equals( printObject ));
		if (!mActive && PrintTable.class.isInstance(mPrintContainer))
		{
			PrintTable table=(PrintTable)mPrintContainer;
			setActive(table.columns.equals( printObject ));
			if (!mActive)
				setActive(table.columns.equals( printObject ));
			for(int index=0;!mActive && (index<table.columns.size());index++)
				setActive(table.columns.get(index).equals( printObject ));
			for(int index=0;!mActive && (index<table.rows.size());index++)
			{
				PrintRow row=(PrintRow)table.rows.get(index);
				setActive(row.equals( printObject ));
				for(int indexbis=0;!mActive && (indexbis<row.cell.size());indexbis++)
					setActive(row.cell.get(indexbis).equals( printObject ));
			}
		}
	}
	
	public void setSizeEchelle(int echelle)
	{
		super.setSizeEchelle(echelle);
		mContainer.setBounds((int)(mPrintContainer.left*echelle/PrintPanelBase.Ratio),(int)(mPrintContainer.top*echelle/PrintPanelBase.Ratio), (int)(mPrintContainer.width*echelle/PrintPanelBase.Ratio), (int)(mPrintContainer.height*echelle/PrintPanelBase.Ratio));
		mText.setFontSize((int)(6*echelle/PrintPanelBase.Ratio));
	}

	public void activate() 
	{
		mObserver.selectObject(mPrintContainer);
	}
}
