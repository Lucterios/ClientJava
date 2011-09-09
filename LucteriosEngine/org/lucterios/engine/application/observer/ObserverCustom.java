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

package org.lucterios.engine.application.observer;

import java.lang.ref.WeakReference;

import org.lucterios.engine.application.comp.Cmponent;
import org.lucterios.engine.gui.GraphicTool;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.GCTools;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIForm.FormVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class ObserverCustom extends ObserverAbstract implements Runnable, DialogVisitor, FormVisitor {
	public SimpleParsing mActions;
	public CustomManager mCustomManager;
	private GUIContainer mScrollbar = null;
	private GUIContainer mPnlBtn = null;
	private GUIButton mDefaultBtn = null;

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
				if (getGUIFrame() != null)
					getGUIFrame().requestFocus();
				if (getGUIDialog() != null)
					getGUIDialog().requestFocus();
				Cmponent new_Cmponent_focused = null;
				if (mNameComponentFocused != null)
					new_Cmponent_focused = getCustomManager().getCmponentName(mNameComponentFocused);
				else
					new_Cmponent_focused = getCustomManager().getFirstCmponentFocusabled();
				if (new_Cmponent_focused != null) {
					System.out.printf("focusManagement:request focus Cmponent:%s\n",
							new Object[] { new_Cmponent_focused.getName() });
					new_Cmponent_focused.forceFocus();
				} else
					System.out.print("focusManagement:no request focus Cmponent\n");
			}
		}
	}

	GUIContainer mGUIContainer = null;

	public GUIContainer getGUIContainer() {
		return mGUIContainer;
	}

	public void setGUIContainer(GUIContainer container) {
		mGUIContainer = container;
	}

	private void fillPanel() {
		synchronized (mSynchronizedObj) {
			if (mGUIContainer != null) {
				if (mScrollbar == null) {
					mScrollbar = mGUIContainer.createContainer(ContainerType.CT_SCROLL, new GUIParam(0,0));
					mCustomManager.setContainer(mScrollbar);
				}
				if (mPnlBtn == null) {
					mPnlBtn = mGUIContainer.createContainer(ContainerType.CT_NORMAL, new GUIParam(0,1,1,1,ReSizeMode.RSM_HORIZONTAL,FillMode.FM_HORIZONTAL));
				}
				mDefaultBtn = GraphicTool.fillPanelByButton(mPnlBtn, this, Singletons.Factory(),mActions, true);
				getCustomManager().fillComponents();
			}
			super.setContent(null);
		}
	}

	public void run() {
		System.out.print("RUN\n");
		if (mScrollbar != null) {
			mScrollbar.setMinimumScroll();
		}
		if (getGUIDialog() != null)
			getGUIDialog().toFront();
		if (getGUIFrame() != null)
			getGUIFrame().toFront();
		getfocusToMainCmponent();
		setActive(true);
		if (mGUIContainer!=null)
			mGUIContainer.setVisible(true);
		if ((mCustomManager != null) && (getCustomManager() != null)) {
			getCustomManager().initialComponents();
			getCustomManager().runJavaScripts();
		}
	}

	public void show(String aTitle) {
		super.show(aTitle);
		String old_name_component_focused = mNameComponentFocused;
		try {
			if (mGUIContainer != null)
				fillPanel();
			if (getGUIFrame() != null) {
				if (aTitle != null)
					getGUIFrame().setTitle(getTitle());
				getGUIFrame().setDefaultButton(mDefaultBtn);
				getGUIFrame().refreshSize();
			}
			if (getGUIDialog() != null) {
				if (aTitle != null)
					getGUIDialog().setTitle(getTitle());
				getGUIDialog().setDefaultButton(mDefaultBtn);
				getGUIDialog().refreshSize();
			}
		} finally {
			setNameComponentFocused(old_name_component_focused);
			Singletons.getWindowGenerator().invokeLater(this);
		}
		GCTools.postOrderGC();
	}

	public void show(String aTitle, GUIForm aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIFrame = new WeakReference<GUIForm>(aGUI);
		if (getGUIFrame() != null) {
			mGUIContainer = getGUIFrame().getContainer();
			getGUIFrame().setFormVisitor(this);
			getGUIFrame().setVisible(true);
		}
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		super.show(aTitle);
		mGUIDialog = new WeakReference<GUIDialog>(aGUI);
		if (getGUIDialog() != null) {
			mGUIContainer = getGUIDialog().getContainer();
			getGUIDialog().setDialogVisitor(this);
			getGUIDialog().setVisible(true);
		}
	}
	
	public void execute(GUIDialog dialog) {
		if (getTitle() != null)
			getGUIDialog().setTitle(getTitle());
		fillPanel();
		getGUIDialog().setNotifyFrameClose(this);
		getGUIDialog().setDefaultButton(mDefaultBtn);
		Singletons.getWindowGenerator().invokeLater(this);
	}

	public void execute(GUIForm form) {
		if (getTitle() != null)
			getGUIFrame().setTitle(getTitle());
		fillPanel();
		getGUIFrame().setNotifyFrameObserver(this);
		getGUIFrame().setDefaultButton(mDefaultBtn);
		Singletons.getWindowGenerator().invokeLater(this);
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
			Singletons.getWindowGenerator().showMessageDialog(msg_text, getTitle());
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
				for (int cmp_idx = 0; cmp_idx < getCustomManager().getCmponentCount(); cmp_idx++) {
					Cmponent cmp = getCustomManager().getCmponents(cmp_idx);
					if (cmp != null)
						cmp.setEnabled(aIsActive);
				}
				if (mPnlBtn != null) {
					mPnlBtn.setEnabled(aIsActive);
					for (int btn_idx = 0; btn_idx < mPnlBtn.count(); btn_idx++)
						if (GUIButton.class.isInstance(mPnlBtn.get(btn_idx))) {
							GUIButton btn = (GUIButton) mPnlBtn.get(btn_idx);
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
			GCTools.postOrderGC();
		}
	}

	public Cmponent get(String aName) {
		return getCustomManager().getCmponentName(aName);
	}

	public void closing() { }

}
