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

package org.lucterios.utils.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class TitleMoveabled extends JPanel implements MouseMotionListener,MouseListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JWindow mOwner;
	private JLabel mTitle;

	public TitleMoveabled(JWindow aOwner) 
	{
		super();
		mOwner=aOwner;
		setLayout(new BorderLayout());
		mTitle=new JLabel();
		mTitle.setFont(new Font("serif",Font.ITALIC,9));
		mTitle.setBackground(Color.darkGray);
		mTitle.setHorizontalAlignment(SwingConstants.CENTER);
		mTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		mTitle.addMouseListener(this);
		mTitle.addMouseMotionListener(this);
		add(mTitle,BorderLayout.CENTER);
	}

	public void setTitle(String aTitle)
	{
		mTitle.setText(aTitle);
	}

	private int mOffsetX=0;
	private int mOffsetY=0;
	public void mouseDragged(MouseEvent event)
	{
		Point p=event.getPoint();
		SwingUtilities.convertPointToScreen(p,mTitle);
		mOwner.setLocation(mOffsetX+p.x,mOffsetY+p.y);
	}

	private void changeMouse(Cursor aCur)
	{
		mTitle.setCursor(aCur);
		setCursor(aCur);
		mOwner.setCursor(aCur);
	}
	
	public void mousePressed(MouseEvent event)
	{
		Point p=event.getPoint();
		SwingUtilities.convertPointToScreen(p,mTitle);
		mOffsetX=mOwner.getLocation().x-p.x;
		mOffsetY=mOwner.getLocation().y-p.y;
		Cursor cur=Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
		changeMouse(cur);
	}

	public void mouseReleased(MouseEvent event)
	{
		Cursor cur=Cursor.getDefaultCursor();
		changeMouse(cur);
	}
	
	public void mouseMoved(MouseEvent event){}

	public void mouseClicked(MouseEvent event){}

	public void mouseEntered(MouseEvent event){}

	public void mouseExited(MouseEvent event){}
}

