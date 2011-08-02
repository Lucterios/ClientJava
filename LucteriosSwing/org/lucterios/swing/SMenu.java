package org.lucterios.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.lucterios.graphic.MenuNode;
import org.lucterios.graphic.MenuItem;
import org.lucterios.graphic.SwingImage;
import org.lucterios.graphic.Tools;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

public class SMenu implements GUIMenu {

	private JMenuItem mMenu;

	public SMenu(boolean isNode) {
		if (isNode)
			mMenu=new MenuNode();
		else
			mMenu=new MenuItem();
	}
	
	public SMenu(JMenuItem menu) {
		mMenu=menu;
	}

	public boolean isNode() {
		return JMenu.class.isInstance(mMenu);
	}
	
	public boolean isPoint(){
		return MenuItem.class.isInstance(mMenu);
	}
	
	public JMenuItem getJMenuItem(){
		return mMenu;
	}
	
	public GUIMenu addMenu(boolean isNode) {
		SMenu menu=null;
		if (isNode()) {
			menu=new SMenu(isNode);
			((JMenu)mMenu).add(menu.getJMenuItem());
			return menu;
		}
		else
			return null;
	}

	public String getDescription() {
		String result="";
		if (MenuItem.class.isInstance(mMenu)) {
			result=((MenuItem)mMenu).getDescription();
		}
		else if (MenuNode.class.isInstance(mMenu)) {
			result=((MenuNode)mMenu).getDescription();
		}
		if (result==null)
			result="";
		return result;
	}

	public GUIMenu getMenu(int index) {
		if (getMenuCount()>index) {
			Component comp=((JMenu)mMenu).getMenuComponent(index);
			if (JMenuItem.class.isInstance(comp))
				return new SMenu((JMenuItem)comp);
			else
				return new SMenu(null);
		}
		else
			return null;
	}

	public int getMenuCount() {
		if (isNode()) 
			return ((JMenu)mMenu).getMenuComponentCount();
		else
			return 0;
	}

	public void setAction(final GUIAction action) {
		if (MenuItem.class.isInstance(mMenu)) {
			((MenuItem)mMenu).setActionItem(action);
		}
		else {
			mMenu.setText(action.getTitle());
			setMenuImage(action.getIcon(),true);
		}
	}

	public void setDescription(String description) {
		if (MenuItem.class.isInstance(mMenu)) {
			((MenuItem)mMenu).setDescription(description);
		}
	}

	public void addSeparator() {
		if (isNode()) {
			((JMenu)mMenu).addSeparator();
		}		
	}

	public String getName() {
		return mMenu.getName();
	}

	public void setMenuImage(AbstractImage image,boolean showIcon) {
		if (image==null)
			image=AbstractImage.Null;
		if (MenuItem.class.isInstance(mMenu)) {
			((MenuItem)mMenu).setImage(image);
		}
		else if (MenuNode.class.isInstance(mMenu)) {
			((MenuNode)mMenu).setImage(image);
		}		
		if (showIcon && SwingImage.class.isInstance(image))
			mMenu.setIcon((ImageIcon)image.resizeIcon(24, true).getData());
	}
	
	public void setVisible(boolean isVisible) {
		mMenu.setVisible(isVisible);
	}
	
	public String getAcceleratorText() {
		if (MenuItem.class.isInstance(mMenu)) {
			return Tools.getKeyString(((MenuItem)mMenu).getAccelerator());
		}
		else
			return "";
	}

	public GUIAction getAction() {
		if (MenuItem.class.isInstance(mMenu)) {
			return ((MenuItem)mMenu).getActionItem();
		}
		else
			return null;
	}

	public GUIActionListener getActionListener() {
		if (MenuItem.class.isInstance(mMenu)) {
			return ((MenuItem)mMenu).getActionItem();
		}
		else
			return null;
	}

	public AbstractImage getMenuImage() {
		if (MenuItem.class.isInstance(mMenu)) {
			return ((MenuItem)mMenu).getImage();
		}
		else if (MenuNode.class.isInstance(mMenu)) {
			return ((MenuNode)mMenu).getImage();
		}		
		return AbstractImage.Null;
	}

	public String getText() {
		return mMenu.getText();
	}

	public void remove(int index) {
		if (isNode()) {
			((JMenu)mMenu).remove(index);
		}		
	}

	public void setActionListener(final GUIActionListener actionListener) {
		mMenu.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				actionListener.actionPerformed();
			}
		});		
	}

	public void setEnabled(boolean enabled) {
		mMenu.setEnabled(enabled);
	}

	public void setMnemonic(char c) {
		mMenu.setMnemonic(c);
	}

	public void setName(String name) {
		mMenu.setName(name);		
	}

	public void setText(String text) {
		mMenu.setText(text);
	}

	public int getTag() {
		int tag=0;
		if (MenuItem.class.isInstance(mMenu)) {
			tag=((MenuItem)mMenu).getTag();
		}
		else if (MenuNode.class.isInstance(mMenu)) {
			tag=((MenuNode)mMenu).getTag();
		}		
		return tag;
	}

	public void setTag(int tag) {
		if (MenuItem.class.isInstance(mMenu)) {
			((MenuItem)mMenu).setTag(tag);
		}
		else if (MenuNode.class.isInstance(mMenu)) {
			((MenuNode)mMenu).setTag(tag);
		}		
	}
	
}
