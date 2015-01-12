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

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


public class PopupListener extends MouseAdapter 
{
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undo = new UndoManager();
    
    private Hashtable<Object,Action> commands=null;
    private Hashtable<String,JMenuItem> menuItems=null;

	class UndoHandler implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
    }
    class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				System.out.println("Unable to undo: " + ex);
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if(undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
			}
			else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
    }
    class RedoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				System.out.println("Unable to redo: " + ex);
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if(undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
			}
			else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
    }
    class ActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (Action.NAME.equals( e.getPropertyName() )) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if ("enabled".equals( propertyName )) {
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }
    }

    javax.swing.JPopupMenu mPopupMenu;

    public PopupListener(javax.swing.JPopupMenu popupMenu) 
    {
    	mPopupMenu = popupMenu;
        init();
    }

    public PopupListener() 
    {
    	mPopupMenu = new JPopupMenu();
    	mPopupMenu.setFocusable(false);
        init();
    }
    
    private void init(){
		menuItems = new Hashtable<String,JMenuItem>();
		commands = new Hashtable<Object,Action>();
	    undoAction=new UndoAction();
	    redoAction=new RedoAction();
    }

    public void addEditionMenu(boolean recreate) {
    	setActions(new Action[]{undoAction,redoAction});
    	mPopupMenu.add(createMenuItem("Undo","Annuler",recreate,"ctrl+Z"));
    	mPopupMenu.add(createMenuItem("Redo","Recommencer",recreate,"ctrl+Y"));		
    	mPopupMenu.addSeparator();
    	mPopupMenu.add(createMenuItem("cut-to-clipboard","Couper",recreate,"ctrl+ins"));
    	mPopupMenu.add(createMenuItem("copy-to-clipboard","Copier",recreate,"shift+ins"));
    	mPopupMenu.add(createMenuItem("paste-from-clipboard","Coller",recreate));
    	mPopupMenu.addSeparator();
    }
    
    public void setActions(Action[] actions){
		for (int i = 0; i < actions.length; i++)
		{
			Action a = actions[i];
			commands.put(a.getValue(Action.NAME), a);
		}
    }

    public void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
		undoAction.setEnabled(true);
		redoAction.setEnabled(true);
    }

    public JMenuItem getMenuItem(String cmd) {
		return (JMenuItem) menuItems.get(cmd);
    }
    
    public Action getAction(String name)
    {
        return (Action)(commands.get(name));
    }
    
    protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
		return new ActionChangedListener(b);
    }

    public JMenuItem createMenuItem(String cmd,String name,boolean recreate)
    {
    		return createMenuItem(cmd,name,recreate,null);
    }
    
    public JMenuItem createMenuItem(String cmd,String name,boolean recreate,String shortcut)
    {
    	JMenuItem new_menu;
    	if (!recreate && menuItems.containsKey(cmd))
    		new_menu=(JMenuItem)menuItems.get(cmd);
    	else
    	{
			JMenuItem mi = new JMenuItem(name);
			mi.setActionCommand(cmd);
			if (shortcut!=null)
				mi.setAccelerator(KeyStroke.getKeyStroke(shortcut));
			Action a = getAction(cmd);
			if (a != null) {
				mi.addActionListener(a);
				a.addPropertyChangeListener(createActionChangeListener(mi));
				mi.setEnabled(a.isEnabled());
			} else {
				mi.setEnabled(false);
			}
			if (!recreate)
				menuItems.put(cmd, mi);
			new_menu=mi;
    	}
    	return new_menu;
    }
    
    public void mousePressed(MouseEvent e) 
    {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) 
    {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) 
    {
        if (e.isPopupTrigger()) {
        	mPopupMenu.show(e.getComponent(),
                        e.getX(), e.getY());
        }
    }

    public UndoManager getUndo() {
    	return undo;
    }
    
    public javax.swing.JPopupMenu getPopup(){
    	return mPopupMenu;
    }
    
}
