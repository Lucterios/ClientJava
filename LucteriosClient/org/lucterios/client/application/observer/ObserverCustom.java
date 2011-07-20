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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;

import java.lang.ref.WeakReference;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.Button;
import org.lucterios.client.application.comp.Cmponent;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.form.JAdvancePanel;
import org.lucterios.graphic.Tools;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;

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
		if (mCustomManager != null) {
			SimpleParsing xml_component = mContent.getFirstSubTag("COMPONENTS");
			getCustomManager().init(xml_component);
			mActions = mContent.getFirstSubTag("ACTIONS");
		}
	}

	private CustomManager getCustomManager() {
		CustomManager resValue;
		resValue = mCustomManager;
		if (resValue == null) {
			resValue = new CustomManager(this);
			mCustomManager = resValue;
		}
		return resValue;
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
			if (mGUIContainer != null) {
				mGUIContainer.setFocusable(false);
				if (getGUIFrame() != null)
					getGUIFrame().requestFocus();
				if (getGUIDialog() != null)
					getGUIDialog().requestFocus();
				Cmponent new_Cmponent_focused = null;
				if (mNameComponentFocused != null)
					new_Cmponent_focused = getCustomManager().getCmponentName(
							mNameComponentFocused);
				else
					new_Cmponent_focused = getCustomManager()
							.getFirstCmponentFocusabled();
				if (new_Cmponent_focused != null) {
					System.out.printf(
							"focusManagement:request focus Cmponent:%s\n",
							new Object[] { new_Cmponent_focused.getName() });
					new_Cmponent_focused.requestFocus();
				} else
					System.out
							.print("focusManagement:no request focus Cmponent\n");
			}
		}
	}

	Container mGUIContainer = null;

	public Container getGUIContainer() {
		return mGUIContainer;
	}

	public void setGUIContainer(Container container) {
		mGUIContainer = container;
	}

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
					mScrollbar.setViewportView(getCustomManager());
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

				getCustomManager().fillComponents();
				java.awt.Dimension size = mGUIContainer.getSize();
				mGUIContainer.setSize(size);
			}
			super.setContent(null);
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
		if (getGUIDialog() != null)
			getGUIDialog().toFront();
		if (getGUIFrame() != null)
			getGUIFrame().toFront();
		getfocusToMainCmponent();
		setActive(true);
		mGUIContainer.setVisible(true);
		if ((mCustomManager != null) && (getCustomManager() != null)) {
			getCustomManager().initialComponents();
			getCustomManager().runJavaScripts();
		}
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		String old_name_component_focused = mNameComponentFocused;
		try {
			if (mGUIContainer != null)
				fillPanel();
			if (getGUIFrame() != null) {
				if (aTitle != null)
					getGUIFrame().setTitle(getTitle());
				((RootPaneContainer) getGUIFrame()).getRootPane()
						.setDefaultButton(mDefaultBtn);
				getGUIFrame().refreshSize();
			}
			if (getGUIDialog() != null) {
				if (aTitle != null)
					getGUIDialog().setTitle(getTitle());
				((RootPaneContainer) getGUIDialog()).getRootPane()
						.setDefaultButton(mDefaultBtn);
				getGUIDialog().refreshSize();
			}
		} finally {
			setNameComponentFocused(old_name_component_focused);
			SwingUtilities.invokeLater(this);
		}
		Tools.postOrderGC();
	}

	public void show(String aTitle, GUIForm aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIFrame = new WeakReference<GUIForm>(aGUI);
		if (getGUIFrame() != null) {
			mGUIContainer = ((RootPaneContainer) getGUIFrame())
					.getContentPane();
			if (aTitle != null)
				getGUIFrame().setTitle(getTitle());
			fillPanel();
			getGUIFrame().setNotifyFrameObserver(this);
			((RootPaneContainer) getGUIFrame()).getRootPane().setDefaultButton(
					mDefaultBtn);
			getGUIFrame().refreshSize();
			getGUIFrame().setVisible(true);
			SwingUtilities.invokeLater(this);
		}
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIDialog = new WeakReference<GUIDialog>(aGUI);
		if (getGUIDialog() != null) {
			mGUIContainer = ((RootPaneContainer) getGUIDialog())
					.getContentPane();
			if (aTitle != null)
				getGUIDialog().setTitle(getTitle());
			fillPanel();
			getGUIDialog().setNotifyFrameClose(this);
			((RootPaneContainer) getGUIDialog()).getRootPane()
					.setDefaultButton(mDefaultBtn);
			((Window) getGUIDialog()).pack();
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension dialog = ((Window) getGUIDialog()).getSize();
			getGUIDialog().setLocation((screen.width - dialog.width) / 2,
					(screen.height - dialog.height) / 2);
			getGUIDialog().setSize((int) (dialog.width * 1.05),
					(int) (dialog.height * 1.05));
			SwingUtilities.invokeLater(this);
			getGUIDialog().setVisible(true);
		}
	}

	public MapContext getParameters(String aActionId, int aSelect,
			boolean aCheckNull) throws LucteriosException {
		if (!aCheckNull || checkCompoundEmpty()) {
			MapContext requete = new MapContext();
			if (mGUIContainer != null) {
				requete.putAll(mContext);
				for (int cmp_idx = 0; cmp_idx < getCustomManager()
						.getCmponentCount(); cmp_idx++) {
					Cmponent cmp = getCustomManager().getCmponents(cmp_idx);
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
		while ((cmp == null)
				&& (cmp_idx < getCustomManager().getCmponentCount())) {
			cmp = getCustomManager().getCmponents(cmp_idx);
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
			if (getGUIDialog() != null)
				getGUIDialog().setActive(aIsActive);
			if (getGUIFrame() != null)
				getGUIFrame().setActive(aIsActive);
			if (mGUIContainer != null) {
				mGUIContainer.setEnabled(aIsActive);
				for (int cmp_idx = 0; cmp_idx < getCustomManager()
						.getCmponentCount(); cmp_idx++) {
					Cmponent cmp = getCustomManager().getCmponents(cmp_idx);
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
			if (getParent() != null) {
				getParent().setActive(aIsActive);
			}
		}
	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			for (int cmp_idx = 0; cmp_idx < getCustomManager()
					.getCmponentCount(); cmp_idx++)
				getCustomManager().getCmponents(cmp_idx).close();
			closed = true;
			getCustomManager().close();
			if (mGUIContainer != null) {
				mGUIContainer.removeAll();
				mGUIContainer = null;
			}
			if (getGUIDialog() != null)
				getGUIDialog().dispose();
			if (getGUIFrame() != null)
				getGUIFrame().dispose();
			mCustomManager = null;
			mScrollbar = null;
			mDefaultBtn = null;
			mActions = null;
			super.close(aMustRefreshParent);
			Tools.postOrderGC();
		}
	}

	public Cmponent get(String aName) {
		return getCustomManager().getCmponentName(aName);
	}

}
