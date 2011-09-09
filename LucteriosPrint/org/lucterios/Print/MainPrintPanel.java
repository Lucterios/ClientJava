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

package org.lucterios.Print;

import org.lucterios.Print.GUI.BrowserObserver;
import org.lucterios.Print.GUI.EditorObserver;
import org.lucterios.Print.GUI.PreviewObserver;
import org.lucterios.Print.GUI.PropertyObserver;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;

/**
 *
 * @author  lg
 */
public class MainPrintPanel  {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GUIContainer mContainer;
	
	public MainPrintPanel(GUIContainer container) 
    {
		super();
		mContainer=container;
        initComponents();
        mPrintManager=new org.lucterios.Print.Observer.PrintManager();
        initialize();
    }
	    
    private void initComponents() 
    {
        leftSplit = mContainer.createContainer(ContainerType.CT_SPLITER, new GUIParam(0,0));
        leftSplit.setSpliteOrientation(true);
       
        bottomSplit = leftSplit.getSplite(ContainerType.CT_SPLITER, true);
        bottomSplit.setSpliteOrientation(false);

        rigthSplit = bottomSplit.getSplite(ContainerType.CT_SPLITER, false); 
        rigthSplit.setSpliteOrientation(true);
    }
    
    private GUIContainer bottomSplit;
    private GUIContainer leftSplit;
    private GUIContainer rigthSplit;

    private org.lucterios.Print.Observer.PrintManager mPrintManager;
    public org.lucterios.Print.Observer.PrintManager getManager()
    {
       return mPrintManager;
    }
    
    private void initialize()
    {
    	new BrowserObserver(mPrintManager,leftSplit.getSplite(ContainerType.CT_NORMAL, false));
        new PreviewObserver(mPrintManager,rigthSplit.getSplite(ContainerType.CT_NORMAL,false));
        new EditorObserver(mPrintManager,bottomSplit.getSplite(ContainerType.CT_NORMAL, true));
        new PropertyObserver(mPrintManager,rigthSplit.getSplite(ContainerType.CT_NORMAL, true));
        rigthSplit.setDividerLocation(650);
        bottomSplit.setDividerLocation(600);
    }

    public void getData(String aModal,String aXmlData) throws org.xml.sax.SAXException,java.io.IOException
    {
        if (aModal.trim().length()==0)
            aModal="<model></model>";
        getManager().getPage().Load(aModal);
        if (aXmlData.trim().length()>0)
            getManager().getPage().setXML(aXmlData);
        getManager().refresh();
    }

}
