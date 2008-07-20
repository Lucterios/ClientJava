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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.lucterios.Print.Data.PrintAbstract;


public abstract class PrintPanelAbstract extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public static final double Ratio=75;
	
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
	
	public PrintPanelAbstract(ObserverCallBack observer)
	{
		super();
		addMouseListener(this);
		mObserver=observer;
		setLayout(null);
		setBackground(Color.WHITE);
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
			setBackground(Color.LIGHT_GRAY);
			setBorder(new LineBorder(Color.RED,1));
		}
		else
		{
			setBackground(Color.WHITE);
			setBorder(new LineBorder(Color.BLACK,1));
		}
	}
	
	public abstract void activate();
	
	public void mouseClicked(MouseEvent e) 
	{
		activate();
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}
