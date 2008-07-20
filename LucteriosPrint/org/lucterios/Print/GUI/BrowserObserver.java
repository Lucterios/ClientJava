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

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.*;

import org.lucterios.Print.Data.*;
import org.lucterios.Print.Observer.*;
import org.lucterios.Print.resources.Resources;

public class BrowserObserver extends javax.swing.JPanel implements PrintObserverAbstract 
{  
	class IconNodeRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,
		      boolean sel, boolean expanded, boolean leaf, int row,
		      boolean hasFocus) {

		    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
		        row, hasFocus);

		    DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
		    Object node_value=node.getUserObject();
		    
		    Icon icon = null;
            if (PrintImage.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("image.png"));
            else if (PrintText.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("text.png"));
            else if (PrintTable.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("table.png"));
            else if (PrintColumn.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("colonne.png"));
            else if (PrintRow.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("ligne.png"));
            else if (PrintCell.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("cellule.png"));
            else if (PrintPage.class.isInstance(node_value))
            	icon = new ImageIcon(Resources.class.getResource("model.png"));
            else if (PrintArea.class.isInstance(node_value))
            	icon = getIconFromArea(node_value);
            else if (PrintVector.class.isInstance(node_value))
            	icon = getIconFromVector(node_value);
		    setIconNoNull(icon);
		    return this;
		  }

		private void setIconNoNull(Icon icon) {
			if (icon != null) 
		      setIcon(icon);
		}

		private Icon getIconFromVector(Object nodeValue) {
			Icon icon;
			if (((PrintVector)nodeValue).name.equalsIgnoreCase("Colonnes"))
				icon = new ImageIcon(Resources.class.getResource("columns.png"));
			else if (((PrintVector)nodeValue).name.equalsIgnoreCase("Lignes"))
			    icon = new ImageIcon(Resources.class.getResource("rows.png"));
			else
				icon = new ImageIcon(Resources.class.getResource("vector.png"));
			return icon;
		}

		private Icon getIconFromArea(Object nodeValue) {
			Icon icon;
			if (((PrintArea)nodeValue).name.equalsIgnoreCase("body"))
				icon = new ImageIcon(Resources.class.getResource("page.png"));
			else
				icon = new ImageIcon(Resources.class.getResource("area.png"));
			return icon;
		}
	}	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PrintManager mPrintManager;
    public BrowserObserver(PrintManager aPrintManager) 
    {
        super();
        mPrintManager=aPrintManager;
        mPrintManager.addObserver(this);
        initComponents();
        refresh();
        MouseListener popupListener = new org.lucterios.utils.graphic.PopupListener(mnBrowser);
        treeBrowser.addMouseListener(popupListener);
        treeBrowser.putClientProperty("JTree.lineStyle","Angled");
        treeBrowser.setCellRenderer(new IconNodeRenderer());
        treeBrowser.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    
    private void initComponents() {
        mnBrowser = new javax.swing.JPopupMenu();
        mnDelete = new javax.swing.JMenuItem();
        mnAdd = new javax.swing.JMenu();
        mnText = new javax.swing.JMenuItem();
        mnImage = new javax.swing.JMenuItem();
        mnTable = new javax.swing.JMenuItem();
        mnPage = new javax.swing.JMenuItem();
        mnColumns = new javax.swing.JMenuItem();
        mnRows = new javax.swing.JMenuItem();
        scrBrowser = new javax.swing.JScrollPane();
        treeBrowser = new javax.swing.JTree();

        mnBrowser.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                mnBrowserPopupMenuWillBecomeVisible(evt);
            }
        });

        mnDelete.setMnemonic('s');
        mnDelete.setText("Supprimer");
        mnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteActionPerformed(evt);
            }
        });
        mnBrowser.add(mnDelete);

        mnAdd.setMnemonic('a');
        mnAdd.setText("Ajouter...");

        mnText.setMnemonic('t');
        mnText.setText("Text");
        mnText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTextActionPerformed(evt);
            }
        });
        mnAdd.add(mnText);

        mnImage.setMnemonic('i');
        mnImage.setText("Image");
        mnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnImageActionPerformed(evt);
            }
        });
        mnAdd.add(mnImage);

        mnTable.setMnemonic('b');
        mnTable.setText("Table");
        mnTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnTableActionPerformed(evt);
            }
        });
        mnAdd.add(mnTable);

        mnColumns.setMnemonic('c');
        mnColumns.setText("Colonne");
        mnColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnColumnsActionPerformed(evt);
            }
        });
        mnAdd.add(mnColumns);

        mnRows.setMnemonic('l');
        mnRows.setText("Ligne");
        mnRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRowsActionPerformed(evt);
            }
        });
        mnAdd.add(mnRows);

        mnPage.setMnemonic('p');
        mnPage.setText("Page");
        mnPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPageActionPerformed(evt);
            }
        });
        mnAdd.add(mnPage);

        mnBrowser.add(mnAdd);

        setLayout(new java.awt.GridBagLayout());
        setMinimumSize(new java.awt.Dimension(100, 200));
        setPreferredSize(new java.awt.Dimension(150, 300));
        treeBrowser.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeBrowserValueChanged(evt);
            }
        });
        scrBrowser.setViewportView(treeBrowser);
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets=new Insets(5,5,5,5);
        add(scrBrowser, gridBagConstraints);
    }

    private void mnBrowserPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) 
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

    private void treeBrowserValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeBrowserValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeBrowser.getLastSelectedPathComponent();
        if ((node!=null) && (PrintAbstract.class.isInstance(node.getUserObject())))
            mPrintManager.setCurrent((PrintAbstract)node.getUserObject());
        else
            mPrintManager.setCurrent(null);
    }

    private void mnPageActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_PAGE);
    }

    private void mnImageActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_IMAGE);
    }

    private void mnTextActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_TEXT);
    }

    private void mnTableActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_TABLE);
    }

    private void mnColumnsActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_COL);
    }

    private void mnRowsActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.addItem(PrintManager.OBJECT_ROW);
    }

    private void mnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        mPrintManager.deleteCurrent();
    }

    private javax.swing.JMenu mnAdd;
    private javax.swing.JPopupMenu mnBrowser;
    private javax.swing.JMenuItem mnDelete;
    private javax.swing.JMenuItem mnImage;
    private javax.swing.JMenuItem mnPage;
    private javax.swing.JMenuItem mnText;
    private javax.swing.JMenuItem mnTable;
    private javax.swing.JMenuItem mnColumns;
    private javax.swing.JMenuItem mnRows;
    private javax.swing.JScrollPane scrBrowser;
    private javax.swing.JTree treeBrowser;

    private void addTableItem(DefaultMutableTreeNode tbl_node,PrintTable tbl)
    {
        DefaultMutableTreeNode col_node = new DefaultMutableTreeNode(tbl.columns);
        for(int ic=0;ic<tbl.columns.size();ic++)
        {
            DefaultMutableTreeNode new_node=new DefaultMutableTreeNode(tbl.columns.get(ic));
            col_node.add(new_node);
        }
        tbl_node.add(col_node);
        DefaultMutableTreeNode row_node = new DefaultMutableTreeNode(tbl.rows);
        for(int ir=0;ir<tbl.rows.size();ir++)
        {
            PrintRow row=(PrintRow)tbl.rows.get(ir);
            DefaultMutableTreeNode subrow_node=new DefaultMutableTreeNode(row);
            for(int ic=0;ic<row.cell.size();ic++)
            {
                DefaultMutableTreeNode cell_node=new DefaultMutableTreeNode(row.cell.get(ic));
                subrow_node.add(cell_node);
            }
            row_node.add(subrow_node);
        }
        tbl_node.add(row_node);        
    }
    
    private void addAreaSubNodes(DefaultMutableTreeNode area_node,PrintArea area_obj)
    {
        for(int i=0;i<area_obj.size();i++)
        {
            DefaultMutableTreeNode new_node=new DefaultMutableTreeNode(area_obj.get(i));
            if (PrintTable.class.isInstance(area_obj.get(i)))
                addTableItem(new_node,(PrintTable)area_obj.get(i));
            area_node.add(new_node);
        }
    }

    private void addSubNodes(DefaultMutableTreeNode area_parent_node,PrintArea area_obj)
    {
        DefaultMutableTreeNode area_node=new DefaultMutableTreeNode(area_obj);
        addAreaSubNodes(area_node,area_obj);
        area_parent_node.add(area_node);
    }

    public void refresh()
    {
        PrintPage page=mPrintManager.getPage();
        DefaultMutableTreeNode root_node = new DefaultMutableTreeNode(page);
        addSubNodes(root_node,page.header);
        addSubNodes(root_node,page.left);
        DefaultMutableTreeNode body_node;
        body_node = new DefaultMutableTreeNode(page.body);
        root_node.add(body_node);
        for(int i=0;i<page.body.size();i++)
            addSubNodes(body_node,(PrintArea)page.body.get(i));
        addSubNodes(root_node,page.bottom);
        addSubNodes(root_node,page.rigth);
        treeBrowser.setModel(new DefaultTreeModel(root_node));
    }
    
    public TreeNode findObject(PrintAbstract obj_select) 
    {
        TreeNode current_node = null;
        if (obj_select!=null)
        {
            TreeNode root = (TreeNode)treeBrowser.getModel().getRoot();
            if (PrintPage.class.isInstance(obj_select))
            {
                current_node=root;
            }
            else if (obj_select.isPage())
            {
                current_node=root.getChildAt(2);
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
                TreeNode parent_node=findObject(obj_select.getOwner());
                current_node=parent_node.getChildAt(0);
            }
            else if (obj_select.isRow())
            {
                TreeNode parent_node=findObject(obj_select.getOwner());
                current_node=parent_node.getChildAt(1);
            }
        }
        return current_node;
    }

	private TreeNode getCell(PrintAbstract objectSelect) 
	{
		TreeNode currentNode=null;
		PrintVector cells=(PrintVector)objectSelect.getOwner();
		TreeNode parent_node=findObject(cells.getOwner());
		int idx=cells.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    currentNode=parent_node.getChildAt(idx);
		return currentNode;
	}

	private TreeNode getColumn(PrintAbstract objectSelect) 
	{
		TreeNode current_node=null;
		PrintVector cols=(PrintVector)objectSelect.getOwner();
		TreeNode parent_node=findObject(cols);
		int idx=cols.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    current_node=parent_node.getChildAt(idx);
		return current_node;
	}

	private TreeNode getContentArea(PrintAbstract objectSelect)
	{
		TreeNode current_node=null;
		PrintArea area=(PrintArea)objectSelect.getOwner();
		TreeNode parent_node=findObject(area);
		int idx=area.indexOf(objectSelect);
		if ((parent_node!=null) && (idx>=0))
		    current_node=parent_node.getChildAt(idx);
		else
		    System.out.println("+++ find cmp "+objectSelect+" owner"+objectSelect.getOwner()+" "+idx);
		return current_node;
	}

	private TreeNode getPageArea(PrintAbstract objectSelect, TreeNode root) 
	{
		TreeNode current_node=null;
		int idx=-1;
		PrintPage page=mPrintManager.getPage();
		TreeNode parent_node=root;
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
		    current_node=parent_node.getChildAt(idx);
		else
		    System.out.println("+++ find area "+objectSelect+" owner"+objectSelect.getOwner()+" "+idx);
		return current_node;
	}

    public void changeCurrentEvent()
    {
        TreeNode current=findObject(mPrintManager.getCurrent());
        if (current!=null)
        {
            TreePath current_path=new TreePath(((DefaultMutableTreeNode)current).getPath());
            treeBrowser.getSelectionModel().setSelectionPath(current_path);
            treeBrowser.expandPath(current_path);
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
