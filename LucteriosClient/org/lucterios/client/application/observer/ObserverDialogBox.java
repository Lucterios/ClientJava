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

package org.lucterios.client.application.observer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.*;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.lucterios.client.application.Button;
import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.JAdvancePanel;

public class ObserverDialogBox extends ObserverAbstract {
	public String mText;
	public int mType;
	public SimpleParsing mActions;

	public String getObserverName() {
		return "Core.DialogBox";
	}

	public byte getType() {
		return ObserverConstant.TYPE_DIALOG;
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		SimpleParsing xml_text = mContent.getFirstSubTag("TEXT");
		mText = xml_text.getCData();
		mType = xml_text.getAttributInt("type", 0);
		mActions = mContent.getFirstSubTag("ACTIONS");
	}

	JPanel mPnlBtn;
	Container mGUIContainer = null;

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		if ((mGUIDialog != null) && (mGUIContainer != null)) {
			if (aTitle != null)
				mGUIDialog.setTitle(getTitle());
			mGUIContainer.removeAll();
			mGUIContainer.setLayout(new GridBagLayout());

			JAdvancePanel main_Pnl = new JAdvancePanel();
			main_Pnl.setFontImage(Toolkit.getDefaultToolkit().getImage(
					this.getClass().getResource("ObserverFont.jpg")),
					JAdvancePanel.TEXTURE);
			main_Pnl.setLayout(new GridBagLayout());

			JLabel lbl_img;
			GridBagConstraints gdbConstr_Img;
			JEditorPane lbl_message;
			GridBagConstraints gdbConstr_lbl;
			GridBagConstraints gdbConstr_pnl;
			lbl_img = new JLabel();
			lbl_message = new JEditorPane();
			mPnlBtn = new JPanel();
			mPnlBtn.setOpaque(false);
			gdbConstr_Img = new GridBagConstraints();
			gdbConstr_lbl = new GridBagConstraints();
			gdbConstr_pnl = new GridBagConstraints();

			lbl_img.setOpaque(false);
			lbl_img.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_img.setHorizontalTextPosition(SwingConstants.CENTER);
			gdbConstr_Img.gridx = 0;
			gdbConstr_Img.gridy = 0;
			gdbConstr_pnl.weightx = 0;
			gdbConstr_pnl.weighty = 1;
			gdbConstr_lbl.gridwidth = 1;
			main_Pnl.add(lbl_img, gdbConstr_Img);

			lbl_message.setOpaque(false);
			lbl_message.setEditable(false);
			lbl_message.setFocusable(false);
			lbl_message.setContentType("text/html");
			// lbl_message.setBackground(mGUIDialog.getBackground());
			gdbConstr_lbl.gridx = 1;
			gdbConstr_lbl.gridy = 0;
			gdbConstr_lbl.gridwidth = GridBagConstraints.REMAINDER;
			gdbConstr_lbl.fill = GridBagConstraints.BOTH;
			gdbConstr_pnl.weightx = 1;
			gdbConstr_pnl.weighty = 1;
			main_Pnl.add(lbl_message, gdbConstr_lbl);

			Button.fillPanelByButton(mPnlBtn, this, Singletons.Factory(),
					mActions, true);
			gdbConstr_pnl.gridx = 0;
			gdbConstr_pnl.gridy = 1;
			gdbConstr_pnl.gridwidth = GridBagConstraints.REMAINDER;
			gdbConstr_pnl.weightx = 1;
			gdbConstr_pnl.weighty = 0;
			main_Pnl.add(mPnlBtn, gdbConstr_pnl);

			GridBagConstraints cnt = new GridBagConstraints();
			cnt.gridx = 0;
			cnt.gridy = 0;
			cnt.fill = GridBagConstraints.BOTH;
			cnt.weightx = 1;
			cnt.weighty = 1;
			mGUIContainer.add(main_Pnl, cnt);

			switch (mType) {
			case 2: {
				lbl_img.setIcon(Singletons.Transport().getIcon(
						"images/confirm.png"));
				break;
			}
			case 3: {
				lbl_img.setIcon(Singletons.Transport().getIcon(
						"images/warning.png"));
				break;
			}
			case 4: {
				lbl_img.setIcon(Singletons.Transport().getIcon(
						"images/error.png"));
				break;
			}
			default: {
				lbl_img.setIcon(Singletons.Transport().getIcon(
						"images/info.png"));
				break;
			}
			}
			mText = Tools.convertLuctoriosFormatToHtml(mText);
			lbl_message.setText(mText);
		}
	}

	public void show(String aTitle, Form new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, Dialog aGUI) throws LucteriosException {
		mGUIDialog = aGUI;
		mGUIContainer = aGUI.getContentPane();
		show(aTitle);
		mGUIDialog.setNotifyFrameClose(this);
		mGUIDialog.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		mGUIDialog.setLocation((screen.width - mGUIDialog.getSize().width) / 2,
				(screen.height - mGUIDialog.getSize().height) / 2);
		mGUIDialog.setResizable(false);
		mGUIDialog.setVisible(true);
	}

	public Map getParameters(String aActionId, int aSelect, boolean aCheckNull) {
		return mContext;
	}

	public void setActive(boolean aIsActive) {
		mGUIDialog.setActive(aIsActive);
		mPnlBtn.setEnabled(aIsActive);
	}

	boolean closed = false;

	public void close() {
		if (!closed) {
			closed = true;
			if (mGUIDialog != null)
				mGUIDialog.dispose();
			super.close();
		}
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}