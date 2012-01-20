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
import org.lucterios.gui.GUIContainer;
import org.lucterios.ui.GUIActionListener;

public abstract class PrintPanelAbstract implements GUIActionListener
{
	public static final double Ratio=50;
	
	public static final int PAGE=0;
	public static final int CENTER=1;
	public static final int NORTH=2;
	public static final int SOUTH=3;
	public static final int EAST=4;
	public static final int WEST=5;
	
	public interface ObserverCallBack
	{
		void setCurrent(PrintPanelAbstract newCurrent);
		void selectObject(PrintAbstract printObject);
	}

	protected ObserverCallBack mObserver;
	protected GUIContainer mContainer;
	
	public PrintPanelAbstract(ObserverCallBack observer,GUIContainer container)
	{
		super();
		mContainer=container;
		mContainer.setObject(this);
		mContainer.setMouseClickAction(this);
		mObserver=observer;
		mContainer.setBackgroundColor(0xFFFFFF);
		setActive(false);
	}
	
	public void refresh()
	{
		setSizeEchelle(mEchelle);
	}	

	protected int mEchelle=1;
	public void setSizeEchelle(int echelle)
	{
		mEchelle=echelle;
	}
	
	protected boolean mActive;
	public void setActive(boolean active)
	{
		mActive=active;
		if (mActive)
		{
			mContainer.setBackgroundColor(0xC0C0C0);
			mContainer.setBorder(1,1,1,1,0xFF0000);
		}
		else
		{
			mContainer.setBackgroundColor(0xFFFFFF);
			mContainer.setBorder(1,1,1,1,0x000000);
		}
	}
	
	public abstract void activate();
	
	public void actionPerformed() 
	{
		activate();
	}
}
