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

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.lucterios.Print.GUI.BrowserObserver;
import org.lucterios.Print.GUI.EditorObserver;
import org.lucterios.Print.GUI.PreviewObserver;
import org.lucterios.Print.GUI.PropertyObserver;
import org.lucterios.form.JAdvancePanel;

/**
 *
 * @author  lg
 */
public class MainPrintPanel extends JAdvancePanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MainPrintPanel() 
    {
        initComponents();
        mPrintManager=new org.lucterios.Print.Observer.PrintManager();
        initialize();
    }
    
    private void initComponents() 
    {
        leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        rigthSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        rigthSplit.setResizeWeight(1);
        bottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        bottomSplit.setResizeWeight(1);

        setLayout(new java.awt.BorderLayout());

        leftSplit.setRightComponent(bottomSplit);
        bottomSplit.setTopComponent(rigthSplit);

        add(leftSplit, java.awt.BorderLayout.CENTER);

    }
    
    public void setOpaqueChild(boolean isOpaque){
    	setOpaqueOfSpliter(bottomSplit,isOpaque);
    	setOpaqueOfSpliter(leftSplit,isOpaque);
    	setOpaqueOfSpliter(rigthSplit,isOpaque);
    }

    private void setOpaqueOfSpliter(JSplitPane spliter,boolean isOpaque)
    {
    	spliter.setOpaque(isOpaque);
    	JComponent cmp;
    	cmp=(JComponent)spliter.getTopComponent();
    	if (cmp!=null) cmp.setOpaque(isOpaque);
    	cmp=(JComponent)spliter.getBottomComponent();
    	if (cmp!=null) cmp.setOpaque(isOpaque);   	
    }

    private JSplitPane bottomSplit;
    private JSplitPane leftSplit;
    private JSplitPane rigthSplit;

    private org.lucterios.Print.Observer.PrintManager mPrintManager;
    public org.lucterios.Print.Observer.PrintManager getManager()
    {
       return mPrintManager;
    }
    
    private void initialize()
    {
        leftSplit.setTopComponent(new BrowserObserver(mPrintManager));
        rigthSplit.setTopComponent(new PreviewObserver(mPrintManager));
        bottomSplit.setBottomComponent(new EditorObserver(mPrintManager));
        rigthSplit.setBottomComponent(new PropertyObserver(mPrintManager));
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
