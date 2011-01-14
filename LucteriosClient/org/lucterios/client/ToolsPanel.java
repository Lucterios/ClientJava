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

package org.lucterios.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.ActionLocal;
import org.lucterios.client.gui.RefreshButtonPanel;
import org.lucterios.engine.application.Action;
import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.graphic.JAdvancePanel;
import org.lucterios.utils.graphic.NavigatorBar;

public class ToolsPanel extends JAdvancePanel implements Runnable,
		ActionListener, ToolButtonCollection.ChangeAction {

	public static final int SIZE_SMALL = 50;
	public static final int SIZEX = 320;
	public static final int SIZEY = 140;
	public static final int SPACING = 5;
	public static final Color BACK_GROUND = Color.WHITE;
	public static final Color FORE_GROUND = Color.BLACK;

	public static final int TOP_LEFT = 0;
	public static final int TOP_RIGHT = 1;
	public static final int BOTTOM_LEFT = 2;
	public static final int BOTTOM_RIGHT = 3;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JScrollPane mScrollbar;
	JAdvancePanel mPanelContains;
	NavigatorBar mNavigatorBar;
	Thread mWaitingThread;
	int mNbHorizontalButton;
	int mNbMainButton;

	public JPanel getMainPanel() {
		return mPanelContains;
	}

	RefreshButtonPanel mRefreshButtonPanel;

	public ToolsPanel(RefreshButtonPanel aRefreshButtonPanel) {
		super();
		ToolButton.PUNAISE_IN = new ImageIcon(this.getClass().getResource(
				"resources/punaiseIn.png"));
		ToolButton.PUNAISE_OUT = new ImageIcon(this.getClass().getResource(
				"resources/punaiseOut.png"));

		Toolkit tkt = Toolkit.getDefaultToolkit();
		setFontImage(tkt.getImage(this.getClass().getResource(
				"resources/MainFont.jpg")), TEXTURE);
		mRefreshButtonPanel = aRefreshButtonPanel;
		setLayout(new java.awt.GridBagLayout());
		setBorder(BorderFactory.createLineBorder(FORE_GROUND));
		setBackground(BACK_GROUND);
		GridBagConstraints cnt;
		mPanelContains = new JAdvancePanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				traceButtonFrame(g, Color.DARK_GRAY, Color.WHITE);
			}
		};
		mPanelContains.setFontImage(tkt.getImage(this.getClass().getResource(
				"resources/liege.jpg")), JAdvancePanel.STRETCH);
		mPanelContains.setLayout(new java.awt.GridBagLayout());
		mPanelContains.setBackground(BACK_GROUND);
		mScrollbar = new JScrollPane(mPanelContains);
		mScrollbar.setBorder(BorderFactory.createLineBorder(BACK_GROUND));
		JAdvancePanel scroll_panel = new JAdvancePanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				traceButtonFrame(g, Color.LIGHT_GRAY, Color.DARK_GRAY);
			}
		};
		scroll_panel.setOpaque(false);
		scroll_panel.setLayout(new java.awt.GridBagLayout());
		cnt = new GridBagConstraints();
		cnt.weightx = 1;
		cnt.weighty = 1;
		cnt.fill = GridBagConstraints.BOTH;
		cnt.insets = new Insets(3, 3, 3, 3);
		scroll_panel.add(mScrollbar, cnt);

		cnt = new GridBagConstraints();
		cnt.gridx = 1;
		cnt.gridy = 3;
		cnt.weightx = 1;
		cnt.weighty = 1;
		cnt.fill = GridBagConstraints.BOTH;
		cnt.insets = new Insets(0, 3, 0, 3);
		add(scroll_panel, cnt);

		mNavigatorBar = new NavigatorBar();
		mNavigatorBar.setOpaque(false);

		Insets insets = DesktopTools.instance().getInsets();
		Dimension screen = tkt.getScreenSize();
		int width = (int) (screen.getWidth() - insets.left - insets.right);
		int height = (int) (screen.getHeight() - insets.top - insets.bottom);

		setMinimumSize(new Dimension(SIZEX / 2, 0));
		setPreferredSize(new Dimension(width - SIZE_SMALL, height - 1 * SIZEY));

		SwingUtilities.invokeLater(this);
		mReturnAction = new ActionLocal("Retour", 'R', new ImageIcon(this
				.getClass().getResource("resources/return.png")), this, null);
		setAllVisible(false);
	}

	private void setAllVisible(boolean aIsVisible) {
		for (int index = 0; index < this.getComponentCount(); index++)
			this.getComponent(index).setVisible(aIsVisible);
	}

	public void run() {
		JScrollBar vert = mScrollbar.getVerticalScrollBar();
		vert.setValue(vert.getMinimum());
		JScrollBar hori = mScrollbar.getHorizontalScrollBar();
		hori.setValue(hori.getMinimum());
		repaint();
	}

	GridBagConstraints mBlankConstraints = null;

	public void clearTools() {
		mCurrentButton = null;
		Toolkit kit = Toolkit.getDefaultToolkit();
		Insets insets = DesktopTools.instance().getInsets();
		Dimension screen = kit.getScreenSize();
		int width = (int) (screen.getWidth() - insets.left - insets.right);
		mNbHorizontalButton = (width - 3 * SIZE_SMALL) / SIZEX;

		mNbMainButton = 0;
		mPanelContains.removeAll();
		for (int index = 0; index < mCoinActions.length; index++)
			if (mCoinActions[index] != null)
				remove(mCoinActions[index]);
		setAllVisible(false);
		GridBagConstraints cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 1;
		cnt.gridwidth = 10;
		cnt.weightx = 0;
		cnt.weighty = 0;
		cnt.fill = GridBagConstraints.BOTH;
		cnt.insets = new Insets(0, 0, 3, 0);
		mPanelContains.add(mNavigatorBar, cnt);
		mNavigatorBar.clear();
	}

	private void addPanel(JPanel aPanel, int aX, int aY, boolean aIsCoin,
			boolean aIsApplication) {
		GridBagConstraints cnt = new GridBagConstraints();
		if (aIsCoin) {
			cnt.gridx = aX;
			cnt.gridy = aY;
			cnt.fill = GridBagConstraints.NONE;
			cnt.insets = new Insets(0, 0, 0, 0);
			cnt.weightx = 0.0;
			cnt.weighty = 0.0;
			add(aPanel, cnt);
		} else if (aIsApplication) {
			cnt.gridx = aX;
			cnt.gridy = aY;
			cnt.weightx = 1.0;
			cnt.weighty = 0.0;
			cnt.gridwidth = 10;
			cnt.insets = new Insets(0, 0, SPACING, 0);
			cnt.fill = GridBagConstraints.NONE;
			mPanelContains.add(aPanel, cnt);
		} else {
			cnt.gridx = aX;
			cnt.gridy = aY + 2;
			cnt.insets = new Insets(SPACING, 0, SPACING, 0);
			cnt.fill = GridBagConstraints.NONE;
			cnt.weightx = 1.0;
			cnt.weighty = 1.0;
			mPanelContains.add(aPanel, cnt);
		}
	}

	public void setCursor(Cursor aCursor) {
		super.setCursor(aCursor);
		for (int idx = 0; idx < mPanelContains.getComponentCount(); idx++)
			mPanelContains.getComponent(idx).setCursor(aCursor);
	}

	Action mReturnAction;

	public Action getReturnAction() {
		return mReturnAction;
	}

	ToolButton[] mCoinActions = new ToolButton[4];

	public void setCoinAction(Action aAction, int aPosition) {
		ToolButton btn = new ToolButton((ActionListener)aAction, 
				aAction.getTitle(), aAction.getIcon());
		setCoinAction(btn, aPosition);
		btn.setVisible(mNavigatorBar.getComponentCount() > 0);
		mCoinActions[aPosition] = btn;
	}

	private void setCoinAction(ToolButton aToolButton, int aPosition) {
		if (aToolButton != null)
			switch (aPosition) {
			case TOP_LEFT:
				addPanel(aToolButton, 0, 0, true, false);
				break;
			case TOP_RIGHT:
				addPanel(aToolButton, 2, 0, true, false);
				break;
			case BOTTOM_LEFT:
				addPanel(aToolButton, 0, 4, true, false);
				break;
			case BOTTOM_RIGHT:
				addPanel(aToolButton, 2, 4, true, false);
				break;
				default: 
					//TODO: Implement 'default' statement
					break;
			}
	}

	String mApplicationText = "";
	String mApplicationDescription = "";
	javax.swing.ImageIcon mApplicationIcon = null;

	public void setApplication(String aText, javax.swing.ImageIcon aIcon) {
		String[] text = aText.split(" - ");
		mApplicationText = "";
		mApplicationDescription = "";
		if (text.length>0)
			mApplicationText = text[0];
		if (text.length>1)
			mApplicationDescription = text[1];
		mApplicationIcon = aIcon;
	}

	ToolButtonCollection mCurrentButton = null;

	private void addButtonList() {
		boolean find_nb_int = false;
		for (int nb = mNbHorizontalButton; !find_nb_int && (nb > 1); nb--) {
			if ((mCurrentButton.count() % nb) == 0) {
				find_nb_int = true;
				mNbHorizontalButton = nb;
			}
		}

		for (int index = 0; index < mCurrentButton.count(); index++) {
			int x = (index % mNbHorizontalButton);
			int y = (index / mNbHorizontalButton);
			addPanel(mCurrentButton.getActions(index), x, y, false, false);
		}
		mNbMainButton = mCurrentButton.count();
		int nb_ligne = ((mNbMainButton - 1) / mNbHorizontalButton) + 1;
		if (((nb_ligne + 1) * (SIZEY + 2 * SPACING)
				+ (mCurrentButton.getHeight() + SPACING) + mNavigatorBar
				.getHeight()) < mScrollbar.getHeight()) {
			GridBagConstraints cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.gridy = 100;
			cnt.fill = GridBagConstraints.NONE;
			cnt.gridwidth = mNbHorizontalButton;
			cnt.weightx = 1.0;
			cnt.weighty = 1.0;
			mPanelContains.add(new ToolButton(null, "", "", "", null), cnt);
		}
	}

	private void finishRepaint() {
		ToolButtonCollection item_btn = mCurrentButton;
		while ((item_btn != null)) {
			mNavigatorBar.addLink(item_btn);
			item_btn = item_btn.getButtonsParent();
		}
		if (mNavigatorBar.getNbLink() == 1)
			mNavigatorBar.clear();

		setAllVisible(mNavigatorBar.getComponentCount() > 0);

		if (mCurrentButton != null) {
			addButtonList();
			addPanel(mCurrentButton.getBannierButton(), 0, 0, false, true);
		}

		for (int index = 0; index < mCoinActions.length; index++) {
			if (mCoinActions[index].getActionListener().equals( mReturnAction )) {
				if ((mCurrentButton != null)
						&& (mCurrentButton.getButtonsParent() != null))
					setCoinAction(mCoinActions[index], index);
			} else
				setCoinAction(mCoinActions[index], index);
		}
		mRefreshButtonPanel.refreshSize();
	}

	public void setMainMenuBar(JMenuBar aMenuBar) {
		changeMainAction(new ToolButtonCollection(this, mApplicationText,
				mApplicationDescription, mApplicationIcon, aMenuBar));
		mRefreshButtonPanel.reorganize();
	}

	public void changeMainAction(ToolButtonCollection aAction) {
		mRefreshButtonPanel.setActive(false);
		try {
			clearTools();
			if (aAction != null)
				mCurrentButton = aAction;
			finishRepaint();
		} finally {
			mRefreshButtonPanel.setActive(true);
		}
		SwingUtilities.invokeLater(this);
	}

	public String toString() {
		return "Accueil";
	}

	public void actionPerformed(ActionEvent e) {
		changeMainAction(mCurrentButton.getButtonsParent());
	}

}
