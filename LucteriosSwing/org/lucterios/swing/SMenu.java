package org.lucterios.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.lucterios.graphic.SMenuNode;
import org.lucterios.graphic.SMenuItem;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIMenu;
import org.lucterios.ui.GUIAction;
import org.lucterios.ui.GUIActionListener;

public class SMenu implements GUIMenu {

	private JMenuItem mMenu;

	public SMenu(boolean isNode) {
		if (isNode)
			mMenu=new SMenuNode();
		else
			mMenu=new SMenuItem();
	}
	
	public SMenu(JMenuItem menu) {
		mMenu=menu;
	}

	public boolean isNode() {
		return JMenu.class.isInstance(mMenu);
	}
	
	public boolean isPoint(){
		return SMenuItem.class.isInstance(mMenu);
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
		if (SMenuItem.class.isInstance(mMenu)) {
			result=((SMenuItem)mMenu).getDescription();
		}
		else if (SMenuNode.class.isInstance(mMenu)) {
			result=((SMenuNode)mMenu).getDescription();
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
		if (SMenuItem.class.isInstance(mMenu)) {
			((SMenuItem)mMenu).setActionItem(action);
		}
		else {
			mMenu.setText(action.getTitle());
			setMenuImage(action.getIcon(),true);
		}
	}

	public void setDescription(String description) {
		if (SMenuItem.class.isInstance(mMenu)) {
			((SMenuItem)mMenu).setDescription(description);
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
		if (SMenuItem.class.isInstance(mMenu)) {
			((SMenuItem)mMenu).setImage(image);
		}
		else if (SMenuNode.class.isInstance(mMenu)) {
			((SMenuNode)mMenu).setImage(image);
		}		
		if (showIcon && SwingImage.class.isInstance(image))
			mMenu.setIcon((ImageIcon)image.resizeIcon(24, true).getData());
	}
	
	public void setVisible(boolean isVisible) {
		mMenu.setVisible(isVisible);
	}
	
	public String getAcceleratorText() {
		if (SMenuItem.class.isInstance(mMenu)) {
			return getKeyString(((SMenuItem)mMenu).getAccelerator());
		}
		else
			return "";
	}

	public GUIAction getAction() {
		if (SMenuItem.class.isInstance(mMenu)) {
			return ((SMenuItem)mMenu).getActionItem();
		}
		else
			return null;
	}

	public GUIActionListener getActionListener() {
		if (SMenuItem.class.isInstance(mMenu)) {
			return ((SMenuItem)mMenu).getActionItem();
		}
		else
			return null;
	}

	public AbstractImage getMenuImage() {
		if (SMenuItem.class.isInstance(mMenu)) {
			return ((SMenuItem)mMenu).getImage();
		}
		else if (SMenuNode.class.isInstance(mMenu)) {
			return ((SMenuNode)mMenu).getImage();
		}		
		return AbstractImage.Null;
	}

	public String getText() {
		return mMenu.getText();
	}

	public void removeMenu(int index) {
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
		if (SMenuItem.class.isInstance(mMenu)) {
			tag=((SMenuItem)mMenu).getTag();
		}
		else if (SMenuNode.class.isInstance(mMenu)) {
			tag=((SMenuNode)mMenu).getTag();
		}		
		return tag;
	}

	public void setTag(int tag) {
		if (SMenuItem.class.isInstance(mMenu)) {
			((SMenuItem)mMenu).setTag(tag);
		}
		else if (SMenuNode.class.isInstance(mMenu)) {
			((SMenuNode)mMenu).setTag(tag);
		}		
	}

	public void removeAll() {
		if (isNode()) {
			((JMenu)mMenu).removeAll();
		}		
	}

    static public String getKeyString(KeyStroke aKey)
    {
		String key_string = "";
		if (aKey != null)
			key_string += " [" + aKey.toString() + "]";
		key_string=org.lucterios.utils.Tools.replace(key_string,"pressed"," ");
		return key_string;
    }
	
}
