package org.lucterios.client.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import org.lucterios.utils.Tools;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.gui.GUIMenu;
import org.lucterios.form.JAdvancePanel;

public class CategoryPanel extends JAdvancePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int offset_width = 50;

	private static final int max_width = 10;

	private static final int activator_width = 400;

	private GUIMenu mMenu = null;

	private int pos_y;

	private int pos_x;

	private int num;

	private int nb_col;

	public void dispose() {
		removeAll();
		mMenu = null;
	}

	public GUIMenu getMenu() {
		if (mMenu != null)
			return mMenu;
		else
			return null;
	}

	public CategoryPanel(GUIMenu currentMenu) {
		super();
		setLayout(new java.awt.GridBagLayout());
		mMenu = currentMenu;
		pos_y = 5;
		pos_x = 0;
		num = 0;
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
		this.add(activator, cnt);
	}

	private void addSeparator(int x, int y) {
		if (x > 0) {
			GridBagConstraints cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.gridy = y;
			cnt.gridwidth = x;
			cnt.fill = GridBagConstraints.BOTH;
			cnt.weightx = 0;
			cnt.weighty = 1;
			JLabel sep = new JLabel();
			sep.setMaximumSize(new Dimension(x * offset_width, 10));
			sep.setMinimumSize(new Dimension(x * offset_width, 10));
			sep.setPreferredSize(new Dimension(x * offset_width, 10));
			sep.setOpaque(false);
			this.add(sep, cnt);
		}
	}

	public void refreshButtons(int aWidth) {
		GUIMenu aMenu = getMenu();
		if (aMenu != null) {
			setVisible(false);
			try {
				nb_col = Math.max(1, (int) aWidth / activator_width);
				pos_y = 5;
				pos_x = 0;
				num = 0;
				removeAll();
				fillTitle();
				refreshMenus(aMenu);
				addSeparator(max_width, pos_y + 1);
			} finally {
				setVisible(true);
				repaint();
			}
		}
	}

	private void fillTitle() {
		String descript = "";
		if (getMenu() != null)
			descript = getMenu().getDescription();
		HtmlLabel mdescription = new HtmlLabel();
		mdescription.setEditable(false);
		mdescription.setAlignmentY(0.75f);
		mdescription.setText("<center><h1><u><b>"
				+ Tools.convertLuctoriosFormatToHtml(descript)
				+ "</b></u></h1></center>");
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

	private void refreshMenus(GUIMenu aMenu) {
		for (int index = 0; index < aMenu.getMenuCount(); index++) {
			if (aMenu.getMenu(index).isNode()) {
				GUIMenu current_menu = aMenu.getMenu(index);
				if (current_menu.getIcon().getData()!=null) {
					if (num != 0)
						pos_y++;
					num = 0;
					addSeparator(pos_x, pos_y);
					addActivator(pos_x, pos_y++, max_width - pos_x,
							new ActionActivator(current_menu.getText(),current_menu.getDescription(), 
									current_menu.getIcon()));
					pos_x++;
					refreshMenus(current_menu);
					pos_x--;
				} else
					refreshMenus(current_menu);
			}
			if (aMenu.getMenu(index).isPoint()) {
				GUIMenu current_menu = aMenu.getMenu(index);
				if (current_menu.getIcon().getData()!=null) {
					String key_string = current_menu.getText();
					key_string += current_menu.getAcceleratorText();
					if (num == 0)
						addSeparator(pos_x, pos_y);
					addActivator(pos_x + num * (max_width - pos_x) / nb_col,
							pos_y, (max_width - pos_x) / nb_col,
							new ActionActivator(current_menu.getActionListener(),
									current_menu.getText(),current_menu.getDescription(),key_string,current_menu.getIcon()));
					num++;
					if (num >= nb_col) {
						num = 0;
						pos_y++;
					}
				}
			}
		}
	}

}
