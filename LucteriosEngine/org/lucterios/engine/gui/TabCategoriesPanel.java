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

package org.lucterios.engine.gui;

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIContainer.ContainerType;

public class TabCategoriesPanel implements Runnable {

	RefreshButtonPanel mRefreshButtonPanel;

	private GUIContainer mContainer;

	public GUIContainer getContainer() {
		return mContainer;
	}

	public TabCategoriesPanel(RefreshButtonPanel refreshButtonPanel, Observer aParent) {
		super();
		mRefreshButtonPanel = refreshButtonPanel;
	}

	public void initialize(GUIContainer container) {
		this.mContainer = container;
	}

	public void clearTools() {
		mContainer.removeAll();
	}

	public void setMainMenuBar(GUIFrame aframe) {
		for (int index = 0; (aframe != null) && (index < aframe.getMenuCount()); index++) {
			GUIMenu current_menu = aframe.getMenu(index);
			if (current_menu.isNode() && (current_menu.getTag() == 0)) {
				if (!current_menu.getMenuImage().isNull()) {
					AbstractImage icon = current_menu.getMenuImage()
							.resizeIcon(32, true);
					GUIContainer panel = mContainer.addTab(ContainerType.CT_SCROLL,
							current_menu.getText(), icon);
					CategoryPanel new_subpanel = new CategoryPanel(current_menu);
					new_subpanel.initialize(panel);
					panel.setObject(new_subpanel);
				}
			}
		}
		if (mContainer.count() == 0) {
			clearTools();
		}
		mRefreshButtonPanel.reorganize();
		Singletons.getWindowGenerator().invokeLater(this);
	}

	private void invokeReinitScrollBar() {
		Singletons.getWindowGenerator().invokeLater(new Runnable() {
			public void run() {
				for (int idx = 0; idx < mContainer.count(); idx++) {
					GUIContainer tab = (GUIContainer) mContainer.get(idx);
					tab.setMinimumScroll();
				}
			}
		});
	}

	public void run() {
		Singletons.getWindowGenerator().invokeLater(new Runnable() {
			public void run() {
				for (int idx = 0; idx < mContainer.count(); idx++) {
					GUIContainer tab = (GUIContainer) mContainer.get(idx);
					if (CategoryPanel.class.isInstance(tab.getObject())) {
						CategoryPanel subpanel = (CategoryPanel) tab
								.getObject();
						subpanel.refreshButtons(mContainer.getSizeX());
					}
				}
				invokeReinitScrollBar();
			}
		});
	}	

}
