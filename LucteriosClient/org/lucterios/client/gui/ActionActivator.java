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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.lucterios.client.ToolsPanel;
import org.lucterios.utils.Tools;
import org.lucterios.graphic.HtmlLabel;
import org.lucterios.form.JAdvancePanel;

public class ActionActivator extends JAdvancePanel implements ActionListener,
		MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ActionListener mActionListener = null;
	private String mText;
	private String mDescription;
	private javax.swing.ImageIcon mIcon;

	public ActionActivator(ActionListener aActionListener, String aText,
			String aDescription, String aToolTip, javax.swing.ImageIcon aIcon) {
		super();
		mActionListener = aActionListener;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = org.lucterios.graphic.Tools.resizeIcon(aIcon, 32, true);
		initial(aToolTip);
	}

	public ActionActivator(String aText, String aDescription,
			javax.swing.ImageIcon aIcon) {
		super();
		mActionListener = null;
		mText = aText;
		mDescription = Tools.convertLuctoriosFormatToHtml(aDescription);
		mIcon = org.lucterios.graphic.Tools.resizeIcon(aIcon, 64, true);
		initial("");
	}

	public String toString() {
		return mText;
	}

	public ActionListener getActionListener() {
		return mActionListener;
	}

	HtmlLabel mtext = null;
	HtmlLabel mdescription = null;
	JLabel mImage = null;

	private void initial(String aToolTip) {
		GridBagConstraints cnt;
		setLayout(new GridBagLayout());
		addMouseListenerByComponent(this);
		setToolTipNotEmpty(aToolTip, this);

		if (mActionListener == null)
			setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));

		mImage = new JLabel(mIcon);
		mImage.setAlignmentX(0.5f);
		mImage.setAlignmentY(0.5f);
		setToolTipNotEmpty(aToolTip, mImage);
		mImage.setOpaque(false);
		cnt = new GridBagConstraints();
		cnt.gridx = 0;
		cnt.gridy = 1;
		cnt.gridheight = 2;
		cnt.insets = new Insets(0, 2, 2, 2);
		cnt.fill = GridBagConstraints.BOTH;
		cnt.weightx = 0;
		cnt.weighty = 0;
		this.add(mImage, cnt);
		addMouseListenerByComponent(mImage);

		mtext = new HtmlLabel();
		mtext.setForeground(ToolsPanel.FORE_GROUND);
		mtext.setEditable(false);
		mtext.setAlignmentY(0.5f);
		setToolTipNotEmpty(aToolTip, mtext);
		mtext.setOpaque(false);
		if (mActionListener != null)
			mtext.setText("<h3><b>" + mText + "<b></h3>");
		else
			mtext.setText("<center><h2><b>" + mText + "<b></h2></center>");
		cnt = new GridBagConstraints();
		cnt.gridx = 1;
		cnt.gridy = 1;
		if (mDescription.length() == 0)
			cnt.gridheight = 2;
		cnt.fill = GridBagConstraints.HORIZONTAL;
		cnt.weightx = 1;
		cnt.weighty = 0;
		this.add(mtext, cnt);
		addMouseListenerByComponent(mtext);

		if (mDescription.length() != 0) {
			mdescription = new HtmlLabel();
			mdescription.setForeground(ToolsPanel.FORE_GROUND);
			mdescription.setEditable(false);
			mdescription.setAlignmentY(0.75f);
			if (mActionListener != null)
				mdescription.setText("<h5>" + mDescription + "</h5>");
			else
				mdescription.setText("<center><h3>" + mDescription
						+ "</h3></center>");
			setToolTipNotEmpty(aToolTip, mdescription);
			mdescription.setOpaque(false);
			cnt = new GridBagConstraints();
			cnt.gridx = 1;
			cnt.gridy = 2;
			cnt.fill = GridBagConstraints.BOTH;
			cnt.weightx = 1;
			cnt.weighty = 0;
			this.add(mdescription, cnt);
			addMouseListenerByComponent(mdescription);
		}
	}

	private void addMouseListenerByComponent(JComponent comp) {
		if (mActionListener != null)
			comp.addMouseListener(this);
	}

	private void setToolTipNotEmpty(String aToolTip, JComponent comp) {
		if (aToolTip.length() > 0) {
			aToolTip = Tools.replace(aToolTip, "{[newline}]", "\n");
			comp.setToolTipText(aToolTip);
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	long mLastTimeActionRunning = 0;

	public void runAction() {
		long new_time = System.currentTimeMillis();
		if (((mLastTimeActionRunning == 0) || ((new_time - mLastTimeActionRunning) > 2000))
				&& (mActionListener != null)) {
			mLastTimeActionRunning = new_time;
			mActionListener.actionPerformed(null);
		}
	}

	boolean mIsEntered = false;

	public void mouseEntered(MouseEvent e) {
		mIsEntered = true;
		if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
			Cursor cur = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			setCursor(cur);
		}
	}

	public void mouseExited(MouseEvent e) {
		mIsEntered = false;
		if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	public void setCursor(Cursor aCursor) {
		super.setCursor(aCursor);
		if (mImage != null)
			mImage.setCursor(aCursor);
		if (mtext != null)
			mtext.setCursor(aCursor);
		if (mdescription != null)
			mdescription.setCursor(aCursor);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		runAction();
	}

	public void actionPerformed(ActionEvent e) {
		//
	}

}
