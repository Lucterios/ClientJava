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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.LucteriosException;

public class WebLabel extends LinkLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WebLabel(String aUrl) 
	{
		super(aUrl);
		initWeb();
	}

	public WebLabel() 
	{
		super();
		initWeb();
	}
	
	private void initWeb(){
		setLinker(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
					DesktopTools desktop=new DesktopTools();
					desktop.launch(mUrl);
				} catch (LucteriosException e1) 
				{
					ExceptionDlg.throwException(e1);
				}
			}
		});
	}
}