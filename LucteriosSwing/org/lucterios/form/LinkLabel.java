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

package org.lucterios.form;

import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.lucterios.graphic.HtmlLabel;

public class LinkLabel extends HtmlLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String mUrl;
	protected String mValue;
	protected ActionListener mLinker=null;
	public LinkLabel(String aUrl) 
	{
		super();
		initial();
		setURL(aUrl,aUrl);
	}

	public LinkLabel() 
	{
		super();
		initial();
		setURL("","");
	}

	public void setLinker(ActionListener aLinker) {
		mLinker=aLinker;
		setURL(mUrl,mValue);
	}
	
	protected void initial()
	{
        setEditable(false);
        setFocusable(false);        
		addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent event)
			{
				if (mLinker!=null) {
					Cursor cur=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					setCursor(cur);
				}
			}
			public void mouseExited(MouseEvent event)
			{
				Cursor cur=Cursor.getDefaultCursor();
				setCursor(cur);
			}
			
			public void mouseClicked(MouseEvent e)
			{
				if (mLinker!=null) mLinker.actionPerformed(null);
			}
		});
	}
	
	public void setURL(String aUrl,String aValue)
	{
		mUrl=aUrl;
		mValue=aValue;
		if (mLinker!=null) {
			setToolTipText(mUrl);
			setText("<font size='-1' color='blue'><u><center>"+mValue+"</center></u></font>");
		}
		else  {
			setToolTipText(null);
			setText("<center>"+mValue+"</center>");
		}			
	}

}
