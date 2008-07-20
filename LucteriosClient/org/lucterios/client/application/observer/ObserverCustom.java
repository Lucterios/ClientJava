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

import java.util.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.Button;
import org.lucterios.client.application.comp.Cmponent;
import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.JAdvancePanel;

public class ObserverCustom extends ObserverAbstract implements Runnable {
	public SimpleParsing mActions;
	public CustomManager mCustomManager;
	private JScrollPane mScrollbar = null;
	private Button mDefaultBtn = null;

	protected Object mSynchronizedObj = new Object();

	public ObserverCustom() {
		super();
		mCustomManager = new CustomManager(this);
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		SimpleParsing xml_component = mContent.getFirstSubTag("COMPONENTS");
		mCustomManager.init(xml_component);
		mActions = mContent.getFirstSubTag("ACTIONS");
	}

	public String getObserverName() {
		return "Core.Custom";
	}

	public byte getType() {
		return ObserverConstant.TYPE_BOTH;
	}

	private String mNameComponentFocused = null;

	public void setNameComponentFocused(String aNameComponentFocused) {
		synchronized (mSynchronizedObj) {
			mNameComponentFocused = aNameComponentFocused;
		}
	}

	private void getfocusToMainCmponent() {
		synchronized (mSynchronizedObj) {
			mGUIContainer.setFocusable(false);
			if (mGUIFrame != null)
				mGUIFrame.requestFocus();
			if (mGUIDialog != null)
				mGUIDialog.requestFocus();
			Cmponent new_Cmponent_focused = null;
			if (mNameComponentFocused != null)
				new_Cmponent_focused = mCustomManager
						.getCmponentName(mNameComponentFocused);
			else
				new_Cmponent_focused = mCustomManager
						.getFirstCmponentFocusabled();
			if (new_Cmponent_focused != null) {
				System.out.printf(
						"focusManagement:request focus Cmponent:%s\n",
						new Object[] { new_Cmponent_focused.getName() });
				new_Cmponent_focused.requestFocus();
			} else
				System.out.print("focusManagement:no request focus Cmponent\n");
		}
	}

	Container mGUIContainer = null;

	private void fillPanel() throws LucteriosException {
		synchronized (mSynchronizedObj) {
			if (mGUIContainer != null) {
				mGUIContainer.setLayout(new BorderLayout());
				mScrollbar = (javax.swing.JScrollPane) CustomManager
						.getComponentByName(mGUIContainer, "components");
				JPanel mPnlBtn = (JPanel) CustomManager.getComponentByName(
						mGUIContainer, "buttons");

				if (mScrollbar == null) {
					mScrollbar = new javax.swing.JScrollPane();
					mScrollbar.setName("components");
					mScrollbar.setFocusable(false);
					mScrollbar.setViewportView(mCustomManager);
					GridBagConstraints cnt = new GridBagConstraints();
					cnt.gridy = 0;
					cnt.fill = GridBagConstraints.BOTH;
					cnt.weightx = 1;
					cnt.weighty = 1;
					mGUIContainer.remove(mScrollbar);
				}
				mGUIContainer.add(mScrollbar, BorderLayout.CENTER);
				if (mPnlBtn == null) {
					mPnlBtn = new JAdvancePanel();
					((JAdvancePanel) mPnlBtn).setFontImage(Toolkit
							.getDefaultToolkit().getImage(
									this.getClass().getResource(
											"ObserverFont.jpg")),
							JAdvancePanel.TEXTURE);
					mPnlBtn.setFocusable(false);
					mPnlBtn.setName("buttons");
					mPnlBtn.setLayout(new GridBagLayout());
					GridBagConstraints cnt = new GridBagConstraints();
					cnt.gridy = 1;
					cnt.fill = GridBagConstraints.BOTH;
					cnt.weightx = 0;
					cnt.weighty = 0;
					mGUIContainer.remove(mPnlBtn);
				}
				mGUIContainer.add(mPnlBtn, BorderLayout.PAGE_END);
				Button.fillPanelByButton(mPnlBtn, this, Singletons.Factory(),
						mActions, true);
				mDefaultBtn = null;
				for (int btn_idx = 0; (mDefaultBtn == null)
						&& (btn_idx < mPnlBtn.getComponentCount()); btn_idx++)
					if (Button.class.isInstance(mPnlBtn.getComponent(btn_idx)))
						mDefaultBtn = (Button) mPnlBtn.getComponent(btn_idx);

				mCustomManager.fillComponents();
				java.awt.Dimension size = mGUIContainer.getSize();
				mGUIContainer.setSize(size);
			}
		}
	}

	public void run() {
		System.out.print("RUN\n");
		if (mScrollbar != null) {
			JScrollBar vert = mScrollbar.getVerticalScrollBar();
			vert.setValue(vert.getMinimum());
			JScrollBar hori = mScrollbar.getHorizontalScrollBar();
			hori.setValue(hori.getMinimum());
		}
		if (mGUIDialog != null)
			mGUIDialog.toFront();
		if (mGUIFrame != null)
			mGUIFrame.toFront();
		getfocusToMainCmponent();
		setActive(true);
		mCustomManager.initialComponents();
		mCustomManager.runJavaScripts();		
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		String old_name_component_focused = mNameComponentFocused;
		try {
			if (mGUIContainer != null)
				fillPanel();
			if (mGUIFrame != null) {
				if (aTitle != null)
					mGUIFrame.setTitle(getTitle());
				mGUIFrame.getRootPane().setDefaultButton(mDefaultBtn);
				java.awt.Dimension size = mGUIFrame.getSize();
				java.awt.Dimension pref_size = mGUIFrame.getPreferredSize();
				mGUIFrame.setPreferredSize(size);
				mGUIFrame.pack();
				mGUIFrame.setPreferredSize(pref_size);
				mGUIFrame.setSize(size);
				mGUIFrame.Change();
				mGUIFrame.toFront();
			}
			if (mGUIDialog != null) {
				if (aTitle != null)
					mGUIDialog.setTitle(getTitle());
				mGUIDialog.getRootPane().setDefaultButton(mDefaultBtn);
				java.awt.Dimension size = mGUIDialog.getSize();
				java.awt.Dimension pref_size = mGUIDialog.getPreferredSize();
				mGUIDialog.setPreferredSize(size);
				mGUIDialog.pack();
				mGUIDialog.setPreferredSize(pref_size);
				mGUIDialog.setSize(size);
				mGUIDialog.toFront();
			}
		} finally {
			setNameComponentFocused(old_name_component_focused);
			SwingUtilities.invokeLater(this);
		}
	}

	public void show(String aTitle, Form aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIFrame = aGUI;
		if (mGUIFrame != null) {
			mGUIContainer = mGUIFrame.getContentPane();
			if (aTitle != null)
				mGUIFrame.setTitle(getTitle());
			fillPanel();
			mGUIFrame.getRootPane().setDefaultButton(mDefaultBtn);
			mGUIFrame.setNotifyFrameObserver(this);
			java.awt.Dimension size = mGUIFrame.getSize();
			mGUIFrame.pack();
			mGUIFrame.setSize(size);
			mGUIFrame.setVisible(true);
			mGUIFrame.Change();
			SwingUtilities.invokeLater(this);
		}
	}

	public void show(String aTitle, Dialog aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIDialog = aGUI;
		if (mGUIDialog != null) {
			mGUIContainer = mGUIDialog.getContentPane();
			if (aTitle != null)
				mGUIDialog.setTitle(getTitle());
			fillPanel();
			mGUIDialog.getRootPane().setDefaultButton(mDefaultBtn);
			mGUIDialog.setNotifyFrameClose(this);
			mGUIDialog.pack();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension dialog = mGUIDialog.getSize();
			mGUIDialog.setLocation((screen.width - dialog.width) / 2,
					(screen.height - dialog.height) / 2);
			SwingUtilities.invokeLater(this);
			mGUIDialog.setVisible(true);
		}
	}

	public Map getParameters(String aActionId, int aSelect, boolean aCheckNull) {
		if (!aCheckNull || checkCompoundEmpty()) {
			Map requete = new TreeMap();
			if (mGUIContainer != null) {
				requete.putAll(mContext);
				for (int cmp_idx = 0; cmp_idx < mCustomManager
						.getCmponentCount(); cmp_idx++) {
					Cmponent cmp = mCustomManager.getCmponents(cmp_idx);
					if (cmp != null)
						requete.putAll(cmp.getRequete(aActionId));
				}
			}
			return requete;
		} else
			return null;
	}

	private boolean checkCompoundEmpty() {
		Cmponent cmp = null;
		int cmp_idx = 0;
		while ((cmp == null) && (cmp_idx < mCustomManager.getCmponentCount())) {
			cmp = mCustomManager.getCmponents(cmp_idx);
			if (!cmp.isEmpty()) {
				cmp_idx++;
				cmp = null;
			}
		}
		if (cmp != null) {
			String msg_text = "Ce champ est obligatoire!";
			if (cmp.Description.length() > 0)
				try {
					msg_text = "le champ '"
							+ java.net.URLDecoder.decode(
									cmp.Description.trim(), "iso-8859-1")
							+ "' est obligatoire!";
				} catch (java.io.UnsupportedEncodingException e) {
					msg_text = "Ce champ est obligatoire!";
				}
			JOptionPane.showMessageDialog(null, msg_text, getTitle(),
					JOptionPane.WARNING_MESSAGE);
			cmp.forceFocus();
			return false;
		} else
			return true;
	}

	public void setActive(boolean aIsActive) {
		synchronized (mSynchronizedObj) {
			if (mGUIDialog != null)
				mGUIDialog.setActive(aIsActive);
			if (mGUIFrame != null)
				mGUIFrame.setActive(aIsActive);
			if (mGUIContainer != null) {
				mGUIContainer.setEnabled(aIsActive);
				for (int cmp_idx = 0; cmp_idx < mCustomManager
						.getCmponentCount(); cmp_idx++) {
					Cmponent cmp = mCustomManager.getCmponents(cmp_idx);
					if (cmp != null)
						cmp.setEnabled(aIsActive);
				}
				JPanel mPnlBtn = (JPanel) CustomManager.getComponentByName(
						mGUIContainer, "buttons");
				if (mPnlBtn != null) {
					mPnlBtn.setEnabled(aIsActive);
					for (int btn_idx = 0; btn_idx < mPnlBtn.getComponentCount(); btn_idx++)
						if (Button.class.isInstance(mPnlBtn
								.getComponent(btn_idx))) {
							Button btn = (Button) mPnlBtn.getComponent(btn_idx);
							btn.setEnabled(aIsActive);
						}
				}
			}
		}
	}

	boolean closed = false;

	public void close() {
		if (!closed) {
			closed = true;
			if (mGUIDialog != null)
				mGUIDialog.dispose();
			if (mGUIFrame != null)
				mGUIFrame.dispose();
			super.close();
		}
	}

	public Cmponent get(String aName) {
		return mCustomManager.getCmponentName(aName);
	}
}
