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

package org.lucterios.client.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.IDialog;
import org.lucterios.gui.IForm;

public class ActionImpl implements Action, ActionListener, javax.swing.Action {
	static public WindowGenerator mWindowGenerator = null;

	public final static char MNEMONIC_CHAR = '_';

	private Observer mOwner = null;

	private ObserverFactory mFactory = null;

	private String mID = "";

	private String mTitle;

	private char mMnemonic = 0;

	private String mExtension = "";

	private String mAction = "";

	private String mIcon = "";

	private int mSizeIcon = 0;

	private String mKeyStroke = "";

	private int mFormType = ActionConstantes.FORM_NOMODAL;

	private boolean mClose = true;

	private int mSelect = ActionConstantes.SELECT_NONE;

	private boolean mCheckNull = true;

	private boolean mUsedContext = false;

	public ActionImpl() {
		super();
	}

	public void initialize(Observer aOwner, ObserverFactory aFactory,
			SimpleParsing aXml) {
		initialize(aOwner, aFactory, aXml.getText(), aXml
				.getAttribut("extension"), aXml.getAttribut("action"));
		if (!aXml.getAttribut("id").equals(""))
			mID = aXml.getAttribut("id");
		mFormType = aXml.getAttributInt("modal", ActionConstantes.FORM_NOMODAL);
		mClose = (aXml.getAttributInt("close", 1) != 0);
		mSelect = aXml.getAttributInt("unique", ActionConstantes.SELECT_NONE);
		mIcon = aXml.getAttribut("icon");
		mSizeIcon = aXml.getAttributInt("sizeicon", 0);
		mKeyStroke = aXml.getAttribut("shortcut");
	}

	public void initialize(Observer aOwner, ObserverFactory aFactory,
			String aTitle, String aExtension, String aAction) {
		mOwner = aOwner;
		mFactory = aFactory;
		mTitle = aTitle;
		int pos = mTitle.indexOf(MNEMONIC_CHAR);
		if ((pos != -1) && (mTitle.length() > 0))
			mMnemonic = mTitle.charAt(mTitle.indexOf(MNEMONIC_CHAR) + 1);
		while ((pos = mTitle.indexOf(MNEMONIC_CHAR)) != -1)
			mTitle = mTitle.substring(0, pos) + mTitle.substring(pos + 1);

		mExtension = aExtension;
		mAction = aAction;
		mID = (new Integer(hashCode())).toString();
	}

	public void setOwner(Observer aOwner) {
		mOwner = aOwner;
	}

	public Observer getOwner() {
		return mOwner;
	}

	public String getID() {
		return mID;
	}

	public String getTitle() {
		return mTitle;
	}

	public char getMnemonic() {
		return mMnemonic;
	}

	public String getExtension() {
		return mExtension;
	}

	public String getAction() {
		return mAction;
	}

	public String getIconName() {
		return mIcon;
	}

	public AbstractImage getIcon() {
		return Singletons.Transport().getIcon(mIcon, mSizeIcon);
	}

	public String getKeyStroke() {
		return mKeyStroke;
	}

	public int getFormType() {
		return mFormType;
	}

	public void setFormType(int aFormType) {
		mFormType = aFormType;
	}

	public boolean getCheckNull() {
		return mCheckNull;
	}

	public void setCheckNull(boolean aCheckNull) {
		mCheckNull = aCheckNull;
	}

	public boolean getUsedContext() {
		return mUsedContext;
	}

	public void setUsedContext(boolean aUsedContext) {
		mUsedContext = aUsedContext;
	}

	public boolean getClose() {
		return mClose;
	}

	public void setClose(boolean aClose) {
		mClose = aClose;
	}

	public int getSelect() {
		return mSelect;
	}

	public void setSelect(int aSelect) {
		mSelect = aSelect;
	}

	public void setKeyStroke(String aKey) {
		mKeyStroke = aKey;
	}

	public void showObserver(Observer aObs) {
		Logging.getInstance().writeLog("@@@ showObserver @@@", "BEGIN", 2);
		aObs.eventForEnabled(true);
		try {
			try {
				IDialog owner_dialog = null;
				IForm owner_frame = null;
				int form_type = getFormType(aObs);
				if ((mOwner != null)
						&& (form_type != ActionConstantes.FORM_REFRESH)) {
					if (mClose) {
						if (mOwner.getParent() != null) {
							owner_dialog = mOwner.getParent().getGUIDialog();
							owner_frame = mOwner.getParent().getGUIFrame();
						}
					} else {
						owner_dialog = mOwner.getGUIDialog();
						owner_frame = mOwner.getGUIFrame();
					}
				}
				showObserver(aObs, owner_dialog, owner_frame, form_type);
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
			}
		} finally {
			aObs.eventForEnabled(false);
		}
		Logging.getInstance().writeLog("@@@ showObserver @@@", "END", 2);
	}

	private int getFormType(Observer aObs) {
		int form_type = mFormType;
		if ((form_type != ActionConstantes.FORM_REFRESH)
				&& (aObs.getType() == ObserverConstant.TYPE_DIALOG))
			form_type = ActionConstantes.FORM_MODAL;
		return form_type;
	}

	private void showObserver(Observer aObs, final IDialog owner_dialog,
			final IForm owner_frame, int form_type) throws LucteriosException {
		IDialog new_dialog = null;
		IForm new_frame = null;
		if (aObs.getType() != ObserverConstant.TYPE_NONE) {
			switch (form_type) {
			case ActionConstantes.FORM_NOMODAL:
				new_dialog = null;
				new_frame = mWindowGenerator.newFrame(mID);
				break;
			case ActionConstantes.FORM_MODAL:
				new_dialog = null;
				new_frame = null;
				if (((aObs.getType() == ObserverConstant.TYPE_BOTH))
						|| (aObs.getType() == ObserverConstant.TYPE_DIALOG))
					new_dialog = mWindowGenerator.newDialog(owner_dialog,
							owner_frame);
				else
					new_frame = mWindowGenerator.newFrame(mID);
				break;
			case ActionConstantes.FORM_REFRESH:
				new_dialog = null;
				new_frame = null;
				break;
			default:
				break;
			}
		}

		if (new_frame != null)
			aObs.show(mTitle, new_frame);
		else if (new_dialog != null)
			aObs.show(mTitle, new_dialog);
		else
			aObs.show(mTitle);
	}

	public void runAction(MapContext aParam) {
		try {
			Logging.getInstance().writeLog("@@@ runAction @@@", "BEGIN", 2);
			Observer old_observrer = null;
			if (mFormType == ActionConstantes.FORM_REFRESH)
				old_observrer = getOwner();
			final Observer obs = mFactory.callAction(mExtension, mAction,
					aParam, old_observrer);

			if (!mUsedContext && (mOwner != null)) {
				if ((mClose) || (mFormType == ActionConstantes.FORM_REFRESH))
					obs.setParent(mOwner.getParent());
				else
					obs.setParent(mOwner);
			}

			if (SwingUtilities.isEventDispatchThread())
				showObserver(obs);
			else
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						showObserver(obs);
					}
				});
		} catch (final LucteriosException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ExceptionDlg.throwException(e);
				}
			});
		}
		Logging.getInstance().writeLog("@@@ runAction @@@", "END", 2);
	}

	private MapContext getParameters() throws LucteriosException {
		MapContext param;
		if (mOwner != null) {
			if (mUsedContext)
				param = mOwner.getContext();
			else
				param = mOwner.getParameters(mID, mSelect, mCheckNull);
		} else
			param = new MapContext();
		return param;
	}

	private boolean mustPerforme() {
		if (!mEnabled)
			return false;
		if ("".equals(mExtension) || "".equals(mAction)) {
			if ((mOwner != null) && (mClose))
				mOwner.close(true);
			return false;
		}
		return true;
	}

	public void actionPerformed(ActionEvent aEvent) {
		actionPerformed();
	}

	public void actionPerformed() {
		if (mustPerforme())
			try {
				final MapContext param = getParameters();
				if (param != null) {
					if ((mOwner != null) && (mClose)) {
						mOwner.close(false);
					}
					if (mOwner != null) {
						mOwner.setActive(false);
					}
					Thread thd = new Thread() {
						public void run() {
							try {
								runAction(param);
							} finally {
								if (mOwner != null) {
									mOwner.setActive(true);
								}
							}
						}
					};
					thd.start();
				}
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
			}
	}

	private boolean mEnabled = true;

	public void setEnabled(boolean aEnabled) {
		mEnabled = aEnabled;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public Object getValue(String arg0) {
		return null;
	}

	public void putValue(String arg0, Object arg1) {
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

}
