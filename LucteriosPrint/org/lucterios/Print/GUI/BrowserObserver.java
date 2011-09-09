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

import java.net.URL;

import org.lucterios.Print.Data.*;
import org.lucterios.Print.Observer.*;
import org.lucterios.Print.resources.Resources;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUITree;
import org.lucterios.gui.GUITreeNode;
import org.lucterios.gui.GUIParam.ReSizeMode;
import org.lucterios.ui.GUIActionListener;

public class BrowserObserver implements PrintObserverAbstract 
{  
	private static final long serialVersionUID = 1L;
	private PrintManager mPrintManager;
	private GUIContainer mContainer;

    private GUIMenu mnAdd;
    private GUIMenu mnDelete;
    private GUIMenu mnImage;
    private GUIMenu mnPage;
    private GUIMenu mnText;
    private GUIMenu mnTable;
    private GUIMenu mnColumns;
    private GUIMenu mnRows;
    private GUITree treeBrowser;
	
    public BrowserObserver(PrintManager aPrintManager, GUIContainer guiContainer) 
    {
        super();
        mContainer=guiContainer;
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        initComponents();
        refresh();
    }
    
    private void initComponents() {
    	GUIParam param = new GUIParam(0,0);
    	param.setReSize(ReSizeMode.RSM_BOTH);
    	param.setPad(5);
        treeBrowser = mContainer.createTree(param);
        treeBrowser.addActionListener(new GUIActionListener() {
			public void actionPerformed() {
                treeBrowserValueChanged();
			}
		});
        treeBrowser.setImagePaths(new URL[]{
    		Resources.class.getResource("image.png"), //0
        	Resources.class.getResource("text.png"),  //1
        	Resources.class.getResource("table.png"), //2
        	Resources.class.getResource("colonne.png"), //3
        	Resources.class.getResource("ligne.png"), //4
        	Resources.class.getResource("cellule.png"), //5
        	Resources.class.getResource("model.png"), //6
			Resources.class.getResource("page.png"), //7
			Resources.class.getResource("area.png"), //8
			Resources.class.getResource("columns.png"), //9
		    Resources.class.getResource("rows.png"), //10
			Resources.class.getResource("vector.png"), //11
        });
        
        treeBrowser.clearPopupMenu();       
        treeBrowser.setMenuOpenningListenner(new GUIActionListener() {		
			public void actionPerformed() {
                mnBrowserPopupMenuWillBecomeVisible();
			}
        });

        mnDelete=treeBrowser.newPopupMenu(false);
        mnDelete.setMnemonic('s');
        mnDelete.setText("Supprimer");
        mnDelete.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnDeleteActionPerformed();
            }
        });

        mnAdd=treeBrowser.newPopupMenu(true);
        mnAdd.setMnemonic('a');
        mnAdd.setText("Ajouter...");

        mnText=mnAdd.addMenu(false);
        mnText.setMnemonic('t');
        mnText.setText("Text");
        mnText.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnTextActionPerformed();
            }
        });

        mnImage=mnAdd.addMenu(false);
        mnImage.setMnemonic('i');
        mnImage.setText("Image");
        mnImage.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnImageActionPerformed();
            }
        });

        mnTable=mnAdd.addMenu(false);
        mnTable.setMnemonic('b');
        mnTable.setText("Table");
        mnTable.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnTableActionPerformed();
            }
        });

        mnColumns=mnAdd.addMenu(false);
        mnColumns.setMnemonic('c');
        mnColumns.setText("Colonne");
        mnColumns.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnColumnsActionPerformed();
            }
        });

        mnRows=mnAdd.addMenu(false);
        mnRows.setMnemonic('l');
        mnRows.setText("Ligne");
        mnRows.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnRowsActionPerformed();
            }
        });

        mnPage=mnAdd.addMenu(false);
        mnPage.setMnemonic('p');
        mnPage.setText("Page");
        mnPage.setActionListener(new GUIActionListener() {
            public void actionPerformed() {
                mnPageActionPerformed();
            }
        });
        mContainer.setMinimumSize(150, 200);
    }

    private void mnBrowserPopupMenuWillBecomeVisible() 
    {
        PrintAbstract current=mPrintManager.getCurrent();
        if (current!=null)
        {
            if (PrintArea.class.isInstance(current)) 
            {
                mnAdd.setVisible(true);
                mnDelete.setVisible("body".equals( ((PrintArea)current).name ));
                mnImage.setVisible(true);
                mnPage.setVisible(false);
                mnTable.setVisible(true);
                mnText.setVisible(true);
                mnRows.setVisible(false);
                mnColumns.setVisible(false);
            }
            else if (PrintPage.class.isInstance(current))
            {
                mnAdd.setVisible(false);
                mnDelete.setVisible(false);
            }             
            else if (PrintTable.class.isInstance(current))
            {
                mnAdd.setVisible(true);
                mnDelete.setVisible(true);
                mnImage.setVisible(false);
                mnPage.setVisible(false);
                mnTable.setVisible(false);
                mnText.setVisible(false);
                mnRows.setVisible(false);
                mnColumns.setVisible(false);
            } 
            else if (current.isPage())
            {
                mnAdd.setVisible(true);
                mnDelete.setVisible(false);
                mnImage.setVisible(false);
                mnPage.setVisible(true);
                mnTable.setVisible(false);
                mnText.setVisible(false);
                mnRows.setVisible(false);
                mnColumns.setVisible(false);
            }            
            else if (current.isColumn())
            {
                mnAdd.setVisible(true);
                mnDelete.setVisible(false);
                mnImage.setVisible(false);
                mnPage.setVisible(false);
                mnTable.setVisible(false);
                mnText.setVisible(false);
                mnRows.setVisible(false);
                mnColumns.setVisible(true);
            }            
            else if (current.isRow())
            {
                mnAdd.setVisible(true);
                mnDelete.setVisible(false);
                mnImage.setVisible(false);
                mnPage.setVisible(false);
                mnTable.setVisible(false);
                mnText.setVisible(false);
                mnRows.setVisible(true);
                mnColumns.setVisible(false);
            }
            else
            {
                mnAdd.setVisible(false);
                mnDelete.setVisible(true);
            }
        }
        else
        {
            mnAdd.setVisible(false);
            mnDelete.setVisible(false);
        }
    }

    private void treeBrowserValueChanged() {
        Object nodeObj = treeBrowser.getLastSelectedObject();
        if ((nodeObj!=null) && (PrintAbstract.class.isInstance(nodeObj)))
            mPrintManager.setCurrent((PrintAbstract)nodeObj);
        else
            mPrintManager.setCurrent(null);
    }

    private void mnPageActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_PAGE);
    }

    private void mnImageActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_IMAGE);
    }

    private void mnTextActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_TEXT);
    }

    private void mnTableActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_TABLE);
    }

    private void mnColumnsActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_COL);
    }

    private void mnRowsActionPerformed() {
        mPrintManager.addItem(PrintManager.OBJECT_ROW);
    }

    private void mnDeleteActionPerformed() {
        mPrintManager.deleteCurrent();
    }

    private void addTableItem(GUITreeNode tbl_node,PrintTable tbl)
    {
        GUITreeNode col_node = tbl_node.newNode(tbl.columns);
        selectIconToNode(col_node);
        for(int ic=0;ic<tbl.columns.size();ic++)
        	selectIconToNode(col_node.newNode(tbl.columns.get(ic)));

        GUITreeNode row_node = tbl_node.newNode(tbl.rows);
        selectIconToNode(row_node);
        for(int ir=0;ir<tbl.rows.size();ir++)
        {
            PrintRow row=(PrintRow)tbl.rows.get(ir);
            GUITreeNode subrow_node=row_node.newNode(row);
            selectIconToNode(subrow_node);
            for(int ic=0;ic<row.cell.size();ic++)
            	selectIconToNode(subrow_node.newNode(row.cell.get(ic)));
        }
    }
    
    private void addAreaSubNodes(GUITreeNode area_node,PrintArea area_obj)
    {
        for(int i=0;i<area_obj.size();i++)
        {
        	GUITreeNode new_node=area_node.newNode(area_obj.get(i));
            if (PrintTable.class.isInstance(area_obj.get(i)))
                addTableItem(new_node,(PrintTable)area_obj.get(i));
            selectIconToNode(new_node);
        }
    }

    private void addSubNodes(GUITreeNode area_parent_node,PrintArea area_obj)
    {
        GUITreeNode area_node=area_parent_node.newNode(area_obj);
        addAreaSubNodes(area_node,area_obj);
        selectIconToNode(area_node);
    }

    public void refresh()
    {
        PrintPage page=mPrintManager.getPage();
        GUITreeNode root_node = treeBrowser.newRootNode(page);
        selectIconToNode(root_node);
        addSubNodes(root_node,page.header);
        addSubNodes(root_node,page.left);
        GUITreeNode body_node;
        body_node = root_node.newNode(page.body);
        selectIconToNode(body_node);
        for(int i=0;i<page.body.size();i++)
            addSubNodes(body_node,(PrintArea)page.body.get(i));
        addSubNodes(root_node,page.bottom);
        addSubNodes(root_node,page.rigth);
    }
    
    private void selectIconToNode(GUITreeNode node){
    	Object node_value=node.getUserObject();
	    int iconIndex = -1;
        if (PrintImage.class.isInstance(node_value))
        	iconIndex = 0;
        else if (PrintText.class.isInstance(node_value))
        	iconIndex = 1;
        else if (PrintTable.class.isInstance(node_value))
        	iconIndex = 2;
        else if (PrintColumn.class.isInstance(node_value))
        	iconIndex = 3;
        else if (PrintRow.class.isInstance(node_value))
        	iconIndex = 4;
        else if (PrintCell.class.isInstance(node_value))
        	iconIndex = 5;
        else if (PrintPage.class.isInstance(node_value))
        	iconIndex = 6;
        else if (PrintArea.class.isInstance(node_value)) {
			if (((PrintArea)node_value).name.equalsIgnoreCase("body"))
				iconIndex = 7;
			else
				iconIndex = 8;
        }
        else if (PrintVector.class.isInstance(node_value)) {
			if (((PrintVector)node_value).name.equalsIgnoreCase("Colonnes"))
				iconIndex = 9;
			else if (((PrintVector)node_value).name.equalsIgnoreCase("Lignes"))
				iconIndex = 10;
			else
				iconIndex = 11;
        }
        node.setIconIndex(iconIndex);
    }
    
    public GUITreeNode findObject(PrintAbstract obj_select) 
    {
        GUITreeNode current_node = null;
        if (obj_select!=null)
        {
            GUITreeNode root = treeBrowser.getRoot();
            if (PrintPage.class.isInstance(obj_select))
            {
                current_node=root;
            }
            else if (obj_select.isPage())
            {
                current_node=root.getNode(2);
            }
            else if (PrintArea.class.isInstance(obj_select))
            {
                current_node = getPageArea(obj_select, root);
            }
            else if (PrintArea.class.isInstance(obj_select.getOwner()))
            {
                current_node = getContentArea(obj_select);
            }
            else if (PrintColumn.class.isInstance(obj_select))
            {
                current_node = getColumn(obj_select);
            }
            else if (PrintRow.class.isInstance(obj_select))
            {
                current_node = getColumn(obj_select);
            }
            else if (PrintCell.class.isInstance(obj_select))
            {
                current_node = getCell(obj_select);
            }
            else if (obj_select.isColumn())
            {
                GUITreeNode parent_node=findObject(obj_select.getOwner());
                current_node=parent_node.getNode(0);
            }
            else if (obj_select.isRow())
            {
                GUITreeNode parent_node=findObject(obj_select.getOwner());
                current_node=parent_node.getNode(1);
            }
        }
        return current_node;
    }

	private GUITreeNode getCell(PrintAbstract objectSelect) 
	{
		GUITreeNode currentNode=null;
		PrintVector cells=(PrintVector)objectSelect.getOwner();
		GUITreeNode parent_node=findObject(cells.getOwner());
		int idx=cells.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    currentNode=parent_node.getNode(idx);
		return currentNode;
	}

	private GUITreeNode getColumn(PrintAbstract objectSelect) 
	{
		GUITreeNode current_node=null;
		PrintVector cols=(PrintVector)objectSelect.getOwner();
		GUITreeNode parent_node=findObject(cols);
		int idx=cols.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    current_node=parent_node.getNode(idx);
		return current_node;
	}

	private GUITreeNode getContentArea(PrintAbstract objectSelect)
	{
		GUITreeNode current_node=null;
		PrintArea area=(PrintArea)objectSelect.getOwner();
		GUITreeNode parent_node=findObject(area);
		int idx=area.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    current_node=parent_node.getNode(idx);
		else
		    System.out.println("+++ find cmp "+objectSelect+" owner"+objectSelect.getOwner()+" "+idx);
		return current_node;
	}

	private GUITreeNode getPageArea(PrintAbstract objectSelect, GUITreeNode root) 
	{
		GUITreeNode current_node=null;
		int idx=-1;
		PrintPage page=mPrintManager.getPage();
		GUITreeNode parent_node=root;
		if (objectSelect.equals(page.header))
		    idx=0;
		else if (objectSelect.equals(page.left))
		    idx=1;
		else if (objectSelect.equals(page.bottom))
		    idx=3;
		else if (objectSelect.equals(page.rigth))
		    idx=4;
		else
		{
		    parent_node=findObject(mPrintManager.getPage().body);
		    idx=mPrintManager.getPage().body.indexOf(objectSelect);
		}
		if ((parent_node!=null) && (idx>=0))
		    current_node=parent_node.getNode(idx);
		else
		    System.out.println("+++ find area "+objectSelect+" owner"+objectSelect.getOwner()+" "+idx);
		return current_node;
	}

    public void changeCurrentEvent()
    {
    	GUITreeNode current=findObject(mPrintManager.getCurrent());
        if (current!=null) {
        	treeBrowser.setSelectNodeAndExpand(current);
        }
    }

    public void addItemEvent()
    {
        refresh();
    }

    public void ModifyEvent(){}

    public void beforeChangeCurrentEvent(){}

    public void deleteCurrentEvent()
    {
        refresh();
    }    
}
