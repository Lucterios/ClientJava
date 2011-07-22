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
package org.lucterios.graphic;

import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class NavigatorBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUIContainer mPanel;

	public NavigatorBar(GUIContainer panel)
	{
		mPanel=panel;
		mPanel.setSize(30,30);
		clear();
	}
	
	private int mNbLink;
	public int getNbLink(){
		return mNbLink;
	}
	
	public void clear()
	{
		mPanel.removeAll();
		mNbLink=0;
        mPanel.createLabel(new GUIParam(100,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
        mPanel.createLabel(new GUIParam(0,0,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH));
	}

	public void addLink(String text,GUIActionListener aLinker){
		GUIHyperText new_link=mPanel.createHyperText(new GUIParam(100-mPanel.count(),0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
        if (mNbLink!=0) {
    		new_link.addActionListener(aLinker);
    		new_link.setTextString(text);
    		new_link.setHyperLink("");
    		GUILabel new_label=mPanel.createLabel(new GUIParam(100-mPanel.count(),0,1,1,ReSizeMode.RSM_NONE,FillMode.FM_NONE));
    		new_label.setTextString(" > ");
        }       
        mNbLink++;
	}

	public GUIContainer getOwner() {
		return mPanel;
	}
	
}
