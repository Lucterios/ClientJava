package org.lucterios.client.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import org.lucterios.client.application.Menu;
import org.lucterios.client.application.MenuItem;
import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.HtmlLabel;
import org.lucterios.utils.graphic.JAdvancePanel;

public class CategoryPanel extends JAdvancePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int offset_width=50;
	private static final int max_width=10;
	private static final int nb_col=2;
	
	private Menu mMenu;
	private int pos_y;
	private int pos_x;
	private int num;

	public CategoryPanel(Menu current_menu){
		super();		
		setLayout(new java.awt.GridBagLayout());
		mMenu=current_menu;
		fillTitle();
		pos_y=5;
		pos_x=0;
		num=0;
		setMenu(mMenu);
		addSeparator(max_width,pos_y+1);
	}

	private void fillTitle() {
		HtmlLabel mdescription = new HtmlLabel();
		mdescription.setEditable(false);
		mdescription.setAlignmentY(0.75f);
		mdescription.setText("<center><font size=7><u><b>" + Tools.convertLuctoriosFormatToHtml(mMenu.mDescription) + "</b></u></font></center>");
		mdescription.setOpaque(false);
		GridBagConstraints cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 0;
		cnt.gridwidth = max_width;
		cnt.fill = GridBagConstraints.BOTH;
		cnt.insets = new Insets(0, 0, 20, 0);
		cnt.weightx = 1;
		cnt.weighty = 0;
		this.add(mdescription, cnt);
	}
	
	public void setCursor(Cursor aCursor) {
		super.setCursor(aCursor);
		for (int idx = 0; idx < getComponentCount(); idx++)
			getComponent(idx).setCursor(aCursor);
	}

	private void addActivator(int x, int y, int width, ActionActivator activator) {
		GridBagConstraints cnt = new GridBagConstraints();
		cnt.gridx = x;
		cnt.gridy = y;
		cnt.gridwidth = width;
		cnt.fill = GridBagConstraints.BOTH;
		cnt.anchor = GridBagConstraints.WEST;
		cnt.weightx = 1;
		cnt.weighty = 0;
		cnt.insets = new Insets(0, 0, 10, 5);
		this.add(activator,cnt);
	}

	private void addSeparator(int x, int y) {
		if (x>0) {
			GridBagConstraints cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.gridy = y;
			cnt.gridwidth = x;
			cnt.fill = GridBagConstraints.BOTH;
			cnt.weightx = 0;
			cnt.weighty = 1;
			JLabel sep=new JLabel();
			sep.setMaximumSize(new Dimension(x*offset_width,10));
			sep.setMinimumSize(new Dimension(x*offset_width,10));
			sep.setPreferredSize(new Dimension(x*offset_width,10));
			sep.setOpaque(false);
			this.add(sep, cnt);
		}
	}
	
	public void setMenu(Menu aMenu) {
		for (int index = 0; index < aMenu.getMenuComponentCount(); index++) {
			if (Menu.class.isInstance(aMenu.getMenuComponent(index))) {				
				Menu current_menu = (Menu)aMenu.getMenuComponent(index);
				if (current_menu.getIconName().length() > 0) {
					if (num!=0)
						pos_y++;
					num=0;
					addSeparator(pos_x,pos_y);
					addActivator(pos_x,pos_y++,max_width-pos_x,new ActionActivator(current_menu.getText(),current_menu.mDescription,current_menu.getMenuIcon()));
					pos_x++;
					setMenu(current_menu);
					pos_x--;
				}
				else
					setMenu(current_menu);
			}
			if (MenuItem.class.isInstance(aMenu.getMenuComponent(index))) {
				MenuItem current_menu = (MenuItem) aMenu.getMenuComponent(index);
				if (current_menu.mAction.getIconName().length() > 0) {
					String key_string = current_menu.mAction.getTitle();
					key_string += Tools.getKeyString(current_menu.getAccelerator());
					if (num==0) 
						addSeparator(pos_x,pos_y);
					addActivator(pos_x+num*(max_width-pos_x)/nb_col,pos_y,(max_width-pos_x)/nb_col,new ActionActivator(current_menu.mAction,
							current_menu.mAction.getTitle(),
							current_menu.mDescription, key_string,
							current_menu.mAction.getIcon()));
					num++;
					if (num>=nb_col) {
						num=0;
						pos_y++;
					}
				}
			}
		}
	}

}
