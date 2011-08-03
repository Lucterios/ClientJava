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

package org.lucterios.client.gui;

import org.lucterios.engine.gui.CategoryPanel;
import org.lucterios.engine.gui.RefreshButtonPanel;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.ui.GUIActionListener;

public class MainPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RefreshButtonPanel mRefreshButtonPanel;

	private GUIContainer mtabs = null;

	private ToogleManager mToogleManager;

	private GUIContainer mContainer;

	private double mDividerLocation = -1;
	
	private Observer mParent=null;

	public GUIContainer getContainer() {
		return mContainer;
	}

	public MainPanel(RefreshButtonPanel refreshButtonPanel,Observer aParent) {
		super();
		mRefreshButtonPanel = refreshButtonPanel;
		mParent=aParent;
	}

	public void initialize(GUIContainer container) {
		this.mContainer = container;
		mToogleManager = new ToogleManager(mParent);
		mToogleManager.initialize(mContainer.getSplite(ContainerType.CT_NORMAL,false));
		addTabs();
	}

	private void addTabs() {
		if (mtabs == null) {
			mtabs = mContainer.getSplite(ContainerType.CT_TAB,true);
			mtabs.setMouseClickAction(new GUIActionListener() {
				public void actionPerformed() {
					doubleClickSpliter();
				}
			});
			mtabs.setResizeAction(new GUIActionListener() {
				public void actionPerformed() {
					resizeSpliter();
				}
			});
		}
	}

	private void clearTabs() {
		mContainer.removeSplite(true);
		if (mtabs != null) {
			mtabs.setMouseClickAction(null);
			mtabs.setResizeAction(null);
			mtabs.removeAll();
		}
		mtabs = null;
	}

	public void clearTools() {
		if (mDividerLocation != -1)
			mDividerLocation = (1.0 * mContainer.getDividerLocation()) / mContainer.getSizeX();
		else
			mDividerLocation = 0.25;
		mContainer.setVisible(false);
		mContainer.setDividerLocation(0);
		clearTabs();
		mToogleManager.clear();
	}

	public void setMainMenuBar(GUIFrame aframe) {
		addTabs();
		for (int index = 0; (aframe != null) && (index < aframe.getMenuCount()); index++) {
			GUIMenu current_menu = aframe.getMenu(index);
			if (current_menu.isNode() && (current_menu.getTag()==0)) {
				if (!current_menu.getMenuImage().isNull()) {
					AbstractImage icon = current_menu.getMenuImage().resizeIcon(32, true);
					GUIContainer panel=mtabs.addTab(ContainerType.CT_SCROLL,current_menu.getText(),icon);
					CategoryPanel new_subpanel = new CategoryPanel(current_menu);
					new_subpanel.initialize(panel);
					panel.setObject(new_subpanel);
				} else if (current_menu.getText().length() == 0) {
					mToogleManager.addMenu(current_menu);
				}
			}
		}
		if (mtabs.count() == 0) {
			clearTabs();
		}
		Singletons.getWindowGenerator().invokeLater(this);
		mRefreshButtonPanel.reorganize();
	}

	public void run() {
		mContainer.repaint();
		mToogleManager.showToggles();
		if (mToogleManager.getToggleCount() > 0) {
			mContainer.setDividerLocation((int) (mDividerLocation * mContainer.getSizeX()));
			mToogleManager.getContainer().setVisible(true);
		} else {
			mToogleManager.getContainer().setVisible(false);
			mContainer.setDividerLocation(0);
		}
		lastDividerLocation = mContainer.getDividerLocation();
		invokeReinitScrollBar();
		mContainer.setVisible(true);
	}

	public void resizeSpliter() {
		
		for(int idx = 0;idx<mtabs.count();idx++)
		{
			GUIContainer tab=(GUIContainer)mtabs.get(idx);
			if (CategoryPanel.class.isInstance(tab.getObject())) {
				CategoryPanel subpanel = (CategoryPanel) tab.getObject();
				subpanel.refreshButtons(mtabs.getSizeX());
			}
		}
		invokeReinitScrollBar();
	}

	private int lastDividerLocation = 0;

	public void doubleClickSpliter() {
		if (mToogleManager.getContainer().isVisible())
			lastDividerLocation = mContainer.getDividerLocation();
		mToogleManager.getContainer().setVisible(!mToogleManager.getContainer().isVisible());
		if (mToogleManager.getContainer().isVisible())
			mContainer.setDividerLocation(lastDividerLocation);
		invokeReinitScrollBar();
	}

	private void invokeReinitScrollBar() {
		Singletons.getWindowGenerator().invokeLater(new Runnable() {
			public void run() {
				if (mtabs!=null)
					for(int idx = 0;idx<mtabs.count();idx++)
					{
						GUIContainer tab=(GUIContainer)mtabs.get(idx);
						tab.setMinimumScroll();
					}
			}
		});
	}

}
