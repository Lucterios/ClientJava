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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.Menu;
import org.lucterios.client.resources.Resources;
import org.lucterios.utils.graphic.JAdvancePanel;

public class MainPanel extends JAdvancePanel implements Runnable,
		MouseListener, ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Color BACK_GROUND = Color.WHITE;

	public static final Color FORE_GROUND = Color.BLACK;

	RefreshButtonPanel mRefreshButtonPanel;

	private JTabbedPane mtabs;

	private ToogleManager mToogleManager;

	private JSplitPane split;

	private double mDividerLocation = -1;

	public MainPanel(RefreshButtonPanel refreshButtonPanel) {
		super();
		Toolkit tkt = Toolkit.getDefaultToolkit();
		setFontImage(tkt.getImage(Resources.class.getResource("MainFont.jpg")),
				TEXTURE);
		mRefreshButtonPanel = refreshButtonPanel;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(FORE_GROUND));
		setBackground(BACK_GROUND);
		mtabs = new JTabbedPane();
		mtabs.addComponentListener(this);
		mToogleManager = new ToogleManager(getFontImage());
		split = new JSplitPane();
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mToogleManager);
		split.setRightComponent(mtabs);
		split.setOneTouchExpandable(true);
		GridBagConstraints cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 0;
		cnt.weightx = 1;
		cnt.weighty = 1;
		cnt.fill = GridBagConstraints.BOTH;
		add(split, cnt);
		mtabs.addMouseListener(this);
	}

	public void clearTools() {
		if (mDividerLocation != -1)
			mDividerLocation = (1.0 * split.getDividerLocation())
					/ split.getSize().width;
		else
			mDividerLocation = 0.25;
		split.setVisible(false);
		split.setDividerLocation(0);
		mtabs.removeAll();
		mToogleManager.clear();
	}

	public void setCursor(Cursor aCursor) {
		super.setCursor(aCursor);
		for (int idx = 0; idx < mtabs.getComponentCount(); idx++)
			if (JScrollPane.class.isInstance(mtabs.getComponent(idx))) {
				JScrollPane scroll = (JScrollPane) mtabs.getComponent(idx);
				scroll.getViewport().getView().setCursor(aCursor);
			}
	}

	public void setMainMenuBar(JMenuBar aMenuBar) {
		repaint();
		for (int index = 0; (aMenuBar != null)
				&& (index < aMenuBar.getMenuCount()); index++)
			if (Menu.class.isInstance(aMenuBar.getMenu(index))) {
				Menu current_menu = (Menu) aMenuBar.getMenu(index);
				if (current_menu.getIconName().length() > 0) {
					CategoryPanel new_subpanel = new CategoryPanel(current_menu);
					new_subpanel.setFontImage(this.getFontImage(), TEXTURE);
					ImageIcon icon = org.lucterios.utils.graphic.Tools
							.resizeIcon(current_menu.getMenuIcon(), 32, true);
					mtabs.addTab(current_menu.getText(), icon, new JScrollPane(
							new_subpanel));
				} else if (current_menu.getText().length() == 0) {
					mToogleManager.addMenu(current_menu);
				}
			}
		SwingUtilities.invokeLater(this);
		mRefreshButtonPanel.reorganize();
	}

	public void run() {
		mToogleManager.showToggles();
		if (mToogleManager.getToggleCount() > 0) {
			split.setDividerLocation((int) (mDividerLocation * split.getSize().width));
			mToogleManager.setVisible(true);
		} else {
			mToogleManager.setVisible(false);
			split.setDividerLocation(0);
		}
		lastDividerLocation = split.getDividerLocation();
		invokeReinitScrollBar();
		split.setVisible(true);
		repaint();
	}

	public void componentResized(ComponentEvent aEvent) {
		for (int idx = 0; idx < mtabs.getComponentCount(); idx++)
			if (JScrollPane.class.isInstance(mtabs.getComponent(idx))) {
				JScrollPane scroll = (JScrollPane) mtabs.getComponent(idx);
				CategoryPanel subpanel = (CategoryPanel) scroll.getViewport().getView();
				subpanel.refreshButtons(mtabs.getSize().width);
			}
		invokeReinitScrollBar();
	}

	private int lastDividerLocation = 0;

	public void mouseClicked(MouseEvent aEvent) {
		if (aEvent.getClickCount() == 2) {
			if (mToogleManager.isVisible())
				lastDividerLocation = split.getDividerLocation();
			mToogleManager.setVisible(!mToogleManager.isVisible());
			if (mToogleManager.isVisible())
				split.setDividerLocation(lastDividerLocation);
			invokeReinitScrollBar();
		}
	}

	private void invokeReinitScrollBar() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				for (int idx = 0; idx < mtabs.getComponentCount(); idx++)
					if (JScrollPane.class.isInstance(mtabs.getComponent(idx))) {
						JScrollPane scroll = (JScrollPane) mtabs.getComponent(idx);
						JScrollBar vert = scroll.getVerticalScrollBar();
						vert.setValue(vert.getMinimum());
						JScrollBar hori = scroll.getHorizontalScrollBar();
						hori.setValue(hori.getMinimum());
					}
			}
		});
	}

	public void mouseEntered(MouseEvent aEvent) {
	}

	public void mouseExited(MouseEvent aEvent) {
	}

	public void mousePressed(MouseEvent aEvent) {
	}

	public void mouseReleased(MouseEvent aEvent) {
	}

	public void componentHidden(ComponentEvent aEvent) {
	}

	public void componentMoved(ComponentEvent aEvent) {
	}

	public void componentShown(ComponentEvent aEvent) {
	}

}
