package org.lucterios.engine.gui;

import org.lucterios.utils.Tools;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class CategoryPanel {

	private static final int SEP_HEIGHT = 5;

	private static final int ACTIVATOR_PAS_SIZE = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int offset_width = 20;

	private static final int max_width = 10;

	private static final int activator_width = 300;

	private GUIMenu mMenu = null;

	private int pos_y;

	private int pos_x;

	private int num;

	private int nb_col;

	private GUIContainer mContainer=null;

	public GUIContainer getContainer() {
		return mContainer;
	}

	public void dispose() {
		mContainer.removeAll();
		mMenu = null;
	}

	public GUIMenu getMenu() {
		if (mMenu != null)
			return mMenu;
		else
			return null;
	}

	public CategoryPanel(GUIMenu currentMenu) {
		mMenu = currentMenu;
		pos_y = 5;
		pos_x = 0;
		num = 0;
	}

	public void initialize(GUIContainer container) {
		mContainer=container;
	}
	
	private void addActivator(int x, int y, int width, ActionActivator activator) {
		GUIParam param=new GUIParam(x,y,width,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
		param.setPad(ACTIVATOR_PAS_SIZE);
		activator.initialize(mContainer.createContainer(ContainerType.CT_NORMAL,param));
	}

	private void addSeparator(int x, int y) {
		if (x > 0) {
			GUILabel sep = mContainer.createLabel(new GUIParam(0,y,x,1,ReSizeMode.RSM_VERTICAL,FillMode.FM_BOTH));
			sep.setSize(x * offset_width, SEP_HEIGHT);
		}
	}

	public void refreshButtons(int aWidth) {
		GUIMenu aMenu = getMenu();
		if (aMenu != null) {
			try {
				nb_col = Math.max(1, (int) aWidth / activator_width);
				pos_y = 5;
				pos_x = 0;
				num = 0;
				mContainer.removeAll();
				fillTitle();
				refreshMenus(aMenu);
				addSeparator(max_width, pos_y + 1);
			} finally {
				mContainer.repaint();
			}
		}
	}

	private void fillTitle() {
		String descript = "";
		if (getMenu() != null)
			descript = getMenu().getDescription();
		GUIParam param=new GUIParam(0,0,max_width,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_BOTH);
		param.setPad(3);
		GUIHyperText mdescription = mContainer.createHyperText(param);
		mdescription.setTextString("<center><h1><u><b>"
				+ Tools.convertLuctoriosFormatToHtml(descript)
				+ "</b></u></h1></center>");
	}

	private void refreshMenus(GUIMenu aMenu) {
		for (int index = 0; index < aMenu.getMenuCount(); index++) {
			if (aMenu.getMenu(index).isNode()) {
				GUIMenu current_menu = aMenu.getMenu(index);
				if (!current_menu.getMenuImage().isNull()) {
					if (num != 0)
						pos_y++;
					num = 0;
					//addSeparator(pos_x, pos_y);
					addActivator(pos_x, pos_y++, max_width - pos_x,
							new ActionActivator(current_menu.getText(),current_menu.getDescription(), 
									current_menu.getMenuImage()));
					pos_x++;
					refreshMenus(current_menu);
					pos_x--;
				} else
					refreshMenus(current_menu);
			}
			if (aMenu.getMenu(index).isPoint()) {
				GUIMenu current_menu = aMenu.getMenu(index);
				if (!current_menu.getMenuImage().isNull()) {
					String key_string = current_menu.getText();
					key_string += current_menu.getAcceleratorText();
					/*if (num == 0)
						addSeparator(pos_x, pos_y);*/
					addActivator(pos_x + num * (max_width - pos_x) / nb_col,
							pos_y, (max_width - pos_x) / nb_col,
							new ActionActivator(current_menu.getActionListener(),
									current_menu.getText(),current_menu.getDescription(),key_string,current_menu.getMenuImage()));
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
