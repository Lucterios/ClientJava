package org.lucterios.swing;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.SMenuItem;
import org.lucterios.graphic.SMenuNode;
import org.lucterios.graphic.PopupListener;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUITree;
import org.lucterios.gui.GUITreeNode;
import org.lucterios.ui.GUIActionListener;

public class STree extends JScrollPane implements GUITree {

	class IconNodeRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;
		private URL[] mUrls;
		
		public IconNodeRenderer(URL[] urls){
			super();
			mUrls=urls;
		}
		
		public Component getTreeCellRendererComponent(JTree tree, Object value,
			      boolean sel, boolean expanded, boolean leaf, int row,boolean hasFocus) {

		    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
		        row, hasFocus);
		    if (STreeNode.class.isInstance(value)) {
			    STreeNode node=(STreeNode)value;	    
			    Icon icon = null;
			    int index=node.getIconIndex();
			    if ((index>=0) && (index<mUrls.length))
			    	icon = new ImageIcon(mUrls[index]);
				if (icon != null) 
				      setIcon(icon);
		    }
		    return this;
		  }
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PopupListener popupListener;
	private CursorMouseListener mCursorMouseListener;   
	private FocusListenerList mFocusListener=new FocusListenerList();
	private JTree mTree;
	
	public void clearFocusListener() {
		mFocusListener.clear();
	}
	
	public void addActionListener(GUIActionListener l) {
		mCursorMouseListener.add(l);
	}

	public void removeActionListener(GUIActionListener l) {
		mCursorMouseListener.remove(l);
	}

	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public STree(GUIComponent aOwner){
		super();
        mOwner=aOwner;		
        mCursorMouseListener=new CursorMouseListener(mTree,this);
        mTree=new JTree();
        mTree.addFocusListener(mFocusListener);
        mTree.addMouseListener(mCursorMouseListener);
        mTree.setBackground(Color.WHITE);
        mTree.setOpaque(false);
        mTree.setDragEnabled(true);
        mTree.putClientProperty("JTree.lineStyle","Angled");
        mTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		popupListener = new PopupListener();
		popupListener.addEditionMenu(false);
		mTree.addMouseListener(popupListener);
		setFocusable(false);
		setViewportView(mTree);
        mTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
            	mCursorMouseListener.actionPerformed(null);
            }
        });
	}

	public void clearPopupMenu() {
		popupListener.getPopup().removeAll();
	}
	
	public void setMenuOpenningListenner(final GUIActionListener actionListener) {
		popupListener.getPopup().addPopupMenuListener(new PopupMenuListener() {
	        public void popupMenuCanceled(PopupMenuEvent evt) {
	        }
	        public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {
	        }
	        public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
	        	actionListener.actionPerformed();
	        }
		});
	}
	
	public GUIMenu newPopupMenu(boolean isNode) {
		JMenuItem menu;
		if (isNode)
			menu=new SMenuNode();
		else
			menu=new SMenuItem();		
		popupListener.getPopup().add(menu);
		return new SMenu(menu);
	}
	
	public void setImagePaths(URL[] Urls){
		mTree.setCellRenderer(new IconNodeRenderer(Urls));
	}

	public GUITreeNode newRootNode(Object obj){
		STreeNode result=new STreeNode(obj);
		mTree.setModel(new javax.swing.tree.DefaultTreeModel(result));
		return result;
	}
	
	public Object getLastSelectedObject() {
		Object result=null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)mTree.getLastSelectedPathComponent();
        if (node!=null)
        	result=node.getUserObject();
		return result;
	}

	public GUITreeNode getRoot(){
		return (GUITreeNode)mTree.getModel().getRoot();
	}
	
	public void setSelectNodeAndExpand(GUITreeNode current){
    	javax.swing.tree.TreePath current_path=new javax.swing.tree.TreePath(((STreeNode)current).getPath());
        mTree.getSelectionModel().setSelectionPath(current_path);
        mTree.expandPath(current_path);
	}
	
	private GUIComponent mOwner;
	public GUIComponent getOwner() {
		return mOwner;
	}

	public boolean isActive() {
		return getOwner().isActive();
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}
	
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
