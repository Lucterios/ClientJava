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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.JAdvancePanel;

public class ToolButton extends JAdvancePanel implements ActionListener,
		MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static javax.swing.Icon PUNAISE_IN = null;
	public static javax.swing.Icon PUNAISE_OUT = null;

	private ActionListener mActionListener = null;
	private String mText;
	private String mDescription;
	private javax.swing.ImageIcon mIcon;

	public ToolButton(ActionListener aActionListener, String aText,
			String aDescription, String aToolTip, javax.swing.ImageIcon aIcon) {
		super();
		mActionListener = aActionListener;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = org.lucterios.utils.graphic.Tools.resizeIcon(aIcon, 64, true);
		initial(false, aToolTip);
	}

	private ToolButton(String aText, String aDescription, ImageIcon aIcon) {
		mActionListener = null;
		mText = aText;
		mDescription = aDescription;
		mIcon = org.lucterios.utils.graphic.Tools.resizeIcon(aIcon, 64, true);
		initial(false, "");
	}

	public ToolButton(String aText, String aDescription, String aToolTip,
			javax.swing.ImageIcon aIcon) {
		super();
		mActionListener = this;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = org.lucterios.utils.graphic.Tools.resizeIcon(aIcon, 64, true);
		initial(false, aToolTip);
	}

	public ToolButton(ActionListener aActionListener, String aToolTip,
			javax.swing.ImageIcon aIcon) {
		super();
		mActionListener = aActionListener;
		mText = null;
		mDescription = null;
		mIcon = aIcon;
		initial(true, aToolTip);
	}

	public ToolButton getBannierButton() {
		return new ToolButton(mText, mDescription, mIcon);
	}

	public String toString() {
		return mText;
	}

	public ActionListener getActionListener() {
		return mActionListener;
	}

	JLabel mPunaise = null;
	JEditorPane mtext = null;
	JEditorPane mdescription = null;
	boolean mSmall = false;

	private void initial(boolean aSmall, String aToolTip) {
		GridBagConstraints cnt;
		setLayout(new GridBagLayout());
		mSmall = aSmall;
		changeOpaque();
		addMouseListenerByComponent(this);
		setToolTipNotEmpty(aToolTip, this);
		addPunaiseIcon(aToolTip);

		JLabel image = new JLabel(mIcon);
		image.setAlignmentX(0.5f);
		image.setAlignmentY(0.5f);
		setToolTipNotEmpty(aToolTip, image);
		image.setOpaque(false);
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 1;
		cnt.gridheight = 2;
		cnt.insets = new Insets(0, 10, 10, 10);
		cnt.fill = GridBagConstraints.BOTH;
		cnt.weightx = 0;
		cnt.weighty = 0;
		this.add(image, cnt);
		addMouseListenerByComponent(image);

		if (aSmall) {
			setMinimumSize(new Dimension(ToolsPanel.SIZE_SMALL,
					ToolsPanel.SIZE_SMALL));
			setMaximumSize(new Dimension(ToolsPanel.SIZE_SMALL,
					ToolsPanel.SIZE_SMALL));
			setPreferredSize(new Dimension(ToolsPanel.SIZE_SMALL,
					ToolsPanel.SIZE_SMALL));
		} else {
			mtext = new JEditorPane();
			mtext.setForeground(ToolsPanel.FORE_GROUND);
			mtext.setEditable(false);
			mtext.setContentType("text/html");
			mtext.setAlignmentY(0.5f);
			setToolTipNotEmpty(aToolTip, mtext);
			mtext.setOpaque(false);
			if (mActionListener != null)
				mtext.setText("<center><font size=5><b>" + mText
						+ "<b></font></center>");
			else
				mtext.setText("<center><font size=7><b>" + mText
						+ "<b></font></center>");
			cnt = new GridBagConstraints();
			cnt.gridx = 1;
			cnt.gridy = 1;
			if (mDescription.length() == 0)
				cnt.gridheight = 2;
			cnt.fill = GridBagConstraints.HORIZONTAL;
			cnt.weightx = 1;
			cnt.weighty = 1;
			this.add(mtext, cnt);
			addMouseListenerByComponent(mtext);

			if (mDescription.length() != 0) {
				mdescription = new JEditorPane();
				mdescription.setForeground(ToolsPanel.FORE_GROUND);
				mdescription.setEditable(false);
				mdescription.setAlignmentY(0.75f);
				mdescription.setContentType("text/html");
				if (mActionListener != null)
					mdescription.setText("<font size=3>" + mDescription
							+ "</font>");
				else
					mdescription.setText("<center><font size=5>" + mDescription
							+ "</font></center>");
				setToolTipNotEmpty(aToolTip, mdescription);
				mdescription.setOpaque(false);
				cnt = new GridBagConstraints();
				cnt.gridx = 1;
				cnt.gridy = 2;
				cnt.fill = GridBagConstraints.BOTH;
				cnt.weightx = 1;
				cnt.weighty = 1;
				this.add(mdescription, cnt);
				addMouseListenerByComponent(mdescription);
			}
			int size_x = getSizeX();
			setMinimumSize(new Dimension(size_x, ToolsPanel.SIZEY));
			setMaximumSize(new Dimension(size_x, ToolsPanel.SIZEY));
			setPreferredSize(new Dimension(size_x, ToolsPanel.SIZEY));
		}
	}

	private void addPunaiseIcon(String aToolTip) {
		GridBagConstraints cnt;
		if (!mSmall && (mActionListener != null) && (PUNAISE_IN != null)
				&& (PUNAISE_OUT != null)) {
			mPunaise = new JLabel(PUNAISE_IN);
			mPunaise.setOpaque(false);
			cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.gridy = 0;
			cnt.insets = new Insets(5, 0, 0, 0);
			cnt.gridwidth = 2;
			cnt.fill = GridBagConstraints.NONE;
			cnt.weightx = 0;
			cnt.weighty = 0;
			this.add(mPunaise, cnt);
			setToolTipNotEmpty(aToolTip, mPunaise);
			addMouseListenerByComponent(mPunaise);
		}
	}

	private int getSizeX() {
		int size_x = ToolsPanel.SIZEX;
		if (mActionListener == null)
			size_x = 2 * ToolsPanel.SIZEX;
		return size_x;
	}

	private void changeOpaque() {
		if ((mSmall) || (mActionListener == null)) {
			setOpaque(false);
		} else {
			setOpaque(true);
			setBackground(ToolsPanel.BACK_GROUND);
		}
	}

	private void addMouseListenerByComponent(JComponent comp) {
		if (mActionListener != null)
			comp.addMouseListener(this);
	}

	private void setToolTipNotEmpty(String aToolTip, JComponent comp) {
		if (aToolTip.length() > 0)
			comp.setToolTipText(aToolTip);
	}

	boolean mClicked = false;

	public void mouseClicked(MouseEvent e) {
	}

	long mLastTimeActionRunning = 0;

	public void runAction() {
		long new_time=System.currentTimeMillis();
		if (((mLastTimeActionRunning == 0) || ((new_time-mLastTimeActionRunning)>2000)) && (mActionListener != null))
		{
			mLastTimeActionRunning=new_time;
			mActionListener.actionPerformed(null);
		}
	}

	boolean mIsEntered = false;

	public void mouseEntered(MouseEvent e) {
		mIsEntered = true;
		if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals( getCursor() )) {
			Cursor cur = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			setCursor(cur);
		}
	}

	public void mouseExited(MouseEvent e) {
		mIsEntered = false;
		if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals( getCursor() )) {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public void setCursor(Cursor aCursor) {
		if (mIsEntered && (aCursor.getType() != Cursor.WAIT_CURSOR))
			aCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		if (aCursor.equals( Cursor.getDefaultCursor() ))
			mClicked = false;
		super.setCursor(aCursor);
		if (mtext != null)
			mtext.setCursor(aCursor);
		if (mdescription != null)
			mdescription.setCursor(aCursor);
	}

	private void changeSelect(boolean aSelected) {
		mClicked = aSelected;
		Color select_color;
		if (aSelected)
			select_color = Color.LIGHT_GRAY;
		else
			select_color = ToolsPanel.BACK_GROUND;
		setBackground(select_color);
		if (mtext != null)
			mtext.setBackground(select_color);
		if (mdescription != null)
			mdescription.setBackground(select_color);
	}

	public void mousePressed(MouseEvent e) {
		changeSelect(true);
	}

	public void mouseReleased(MouseEvent e) {
		changeSelect(false);
		runAction();
	}

	public void actionPerformed(ActionEvent e) {
		//
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!mSmall && (mActionListener != null)) {
			g.setColor(this.getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			if (mClicked) {
				traceButtonFrame(g, Color.DARK_GRAY, Color.WHITE);
				if (mPunaise != null)
					mPunaise.setIcon(PUNAISE_OUT);
			} else {
				traceButtonFrame(g, Color.LIGHT_GRAY, Color.DARK_GRAY);
				if (mPunaise != null)
					mPunaise.setIcon(PUNAISE_IN);
			}
		}
	}
		
}
