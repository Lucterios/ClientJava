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
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;

public class ToogleManagerPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RefreshButtonPanel mRefreshButtonPanel;

	private ToogleManager mToogleManager;

	private GUIContainer mContainer;

	private Observer mParent = null;

	public GUIContainer getContainer() {
		return mContainer;
	}

	public ToogleManagerPanel(RefreshButtonPanel refreshButtonPanel, Observer aParent) {
		super();
		mRefreshButtonPanel = refreshButtonPanel;
		mParent = aParent;
	}

	public void initialize(GUIContainer container) {
		this.mContainer = container;
		mToogleManager = new ToogleManager(mParent);
		mToogleManager.initialize(mContainer);
	}

	public void clearTools() {
		mToogleManager.clear();
	}

	public void setMainMenuBar(GUIFrame aframe) {
		for (int index = 0; (aframe != null) && (index < aframe.getMenuCount()); index++) {
			GUIMenu current_menu = aframe.getMenu(index);
			if (current_menu.isNode() && (current_menu.getTag() == 0)) {
				if (current_menu.getMenuImage().isNull() && (current_menu.getText().length() == 0)) {
					mToogleManager.addMenu(current_menu);
				}
			}
		}
		Singletons.getWindowGenerator().invokeLater(this);
		mRefreshButtonPanel.reorganize();
	}
	
	public boolean hasToggle(){
		return mToogleManager.getToggleCount() > 0;
	}

	public void run() {
		mToogleManager.showToggles();
		mContainer.setVisible(mToogleManager.getToggleCount() > 0);
	}

}
