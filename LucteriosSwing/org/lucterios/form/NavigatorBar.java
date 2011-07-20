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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class NavigatorBar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NavigatorBar()
	{
		super();
		setLayout(new java.awt.GridBagLayout());
		setPreferredSize(new Dimension(30,30));
		clear();
	}
	
	private int mNbLink;
	public int getNbLink(){
		return mNbLink;
	}
	
	public void clear()
	{
		removeAll();
		mNbLink=0;
		GridBagConstraints cnt;
        JLabel new_label;

        new_label=new JLabel();
		cnt = new GridBagConstraints();
        cnt.gridx = 100;
        cnt.gridy = 0;
    	cnt.weightx = 1;
        cnt.fill = GridBagConstraints.BOTH;
        add(new_label,cnt);
        new_label=new JLabel();
		cnt = new GridBagConstraints();
        cnt.gridx = 0;
        cnt.gridy = 0;
    	cnt.weightx = 1;
        cnt.fill = GridBagConstraints.BOTH;
        add(new_label,cnt);
	}

	public void addLink(ActionListener aLinker){
		LinkLabel new_link=new LinkLabel(aLinker.toString());
		new_link.setOpaque(false);
		GridBagConstraints cnt;

        if (mNbLink!=0) {
    		new_link.setLinker(aLinker);
	        JLabel new_label=new JLabel();
	        new_label.setOpaque(false);
	        new_label.setText(" > ");
			cnt = new GridBagConstraints();
	        cnt.gridx = 100-getComponentCount();
	        cnt.gridy = 0;
	        cnt.fill = GridBagConstraints.BOTH;
	        add(new_label,cnt);
        }
		
		cnt = new GridBagConstraints();
        cnt.gridx = 100-getComponentCount();
        cnt.gridy = 0;
       	cnt.weightx = 0;
        cnt.fill = GridBagConstraints.BOTH;
        add(new_link,cnt);
        
        mNbLink++;
	}
	
}
