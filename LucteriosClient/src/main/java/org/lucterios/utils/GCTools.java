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

package org.lucterios.utils;

import java.util.Date;

import org.lucterios.ui.GUIActionListener;

public class GCTools {
    
    private final static int TIME_TO_ORDER=2*1000; // 2sec 

    private static Date postOrderGCdate = null;
    public static void postOrderGC(){
    	long current_time=new Date().getTime(); 
    	postOrderGCdate=new Date(current_time+TIME_TO_ORDER);
    }       
    
    public static GUIActionListener createOrderGCAction(){
    	GUIActionListener action=new GUIActionListener(){
			public void actionPerformed() {
				if (postOrderGCdate!=null) {
					long current_time=new Date().getTime();
					if (postOrderGCdate.getTime()<current_time){
						org.lucterios.utils.Tools.clearGC();
						postOrderGCdate=null;
					}
				}
				
			}
    	};
    	return action;
    }

}
