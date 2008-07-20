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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Font;

import javax.swing.JLabel;

import org.lucterios.Print.Data.*;


public class PrintPanelObject extends PrintPanelAbstract
{	
	private static final long serialVersionUID = 1L;
	
	private JLabel mText; 

	private PrintContainer mPrintContainer; 
	
	public PrintPanelObject(PrintPanelBase.ObserverCallBack observer)
	{
		super(observer);
		setLayout(new GridBagLayout());
		mPrintContainer=null;
		mText=new JLabel();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(mText,gridBagConstraints);
	}
	
	public void setPrintContainer(PrintContainer printContainer)
	{
		mPrintContainer=printContainer;
		mText.setText(mPrintContainer.toString());
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
		setBounds((int)(mPrintContainer.left*echelle/PrintPanelBase.Ratio),(int)(mPrintContainer.top*echelle/PrintPanelBase.Ratio), (int)(mPrintContainer.width*echelle/PrintPanelBase.Ratio), (int)(mPrintContainer.height*echelle/PrintPanelBase.Ratio));
		Font font=new Font("Helvetica",0,(int)(6*echelle/PrintPanelBase.Ratio));
		mText.setFont(font);
		mText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		mText.setVerticalAlignment(javax.swing.JTextField.TOP);		
	}

	public void activate() 
	{
		mObserver.selectObject(mPrintContainer);
	}
}
