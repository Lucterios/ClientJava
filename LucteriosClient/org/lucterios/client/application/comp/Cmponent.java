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

package org.lucterios.client.application.comp;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import javax.swing.*;

import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing; 
import org.lucterios.graphic.ExceptionDlg; 
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class Cmponent extends JPanel {
	static final long serialVersionUID = 1L;

	public static int CmponentCount = 0;

	public GridBagConstraints mGdbConp;
	protected int mFill;
	protected SimpleParsing mXmlItem;
	protected WeakReference<Observer> mObsCustom;
	public String Description;
	public String JavaScript;
	public double mWeightx;
	public double mWeighty;
	public boolean mNeeded;
	public int X;
	public int Y;
	public int H;
	public int W;
	public int HMin;
	public int VMin;
	public int HMax;
	public int VMax;

	public Cmponent() {
		super();
		CmponentCount++;
		mFill = GridBagConstraints.BOTH;
		mWeightx = 0.0;
		mWeighty = 0.0;
		mNeeded = false;
		setOpaque(false);
		setFocusable(false);
	}

	protected void finalize() throws Throwable {
		CmponentCount--;
		super.finalize();
	}

	public void close() {
		mGdbConp = null;
		mXmlItem = null;
		mObsCustom = null;
	}

	public void fillActions(ActionList atns) {

	}

	public boolean isEmpty() {
		return false; // mNeeded
	}

	public void forceFocus() {
		requestFocus();
	}

	public boolean isFocusable() {
		return true;
	}

	protected boolean mEnabled = true;

	protected SimpleParsing getXmlItem() {
		return mXmlItem;
	}

	protected Observer getObsCustom() {
		if (mObsCustom != null)
			return mObsCustom.get();
		else
			return null;
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		mEnabled = aEnabled;
	}

	public void init(JPanel aOwnerPanel, Observer aObsCustom,
			SimpleParsing aXmlItem) {
		mObsCustom = new WeakReference<Observer>(aObsCustom);
		mXmlItem = aXmlItem;
		setName(aXmlItem.getAttribut("name"));
		Description = aXmlItem.getAttribut("description");
		initComponent();
		mGdbConp = new GridBagConstraints();
		X = aXmlItem.getAttributInt("x", GridBagConstraints.RELATIVE);
		Y = aXmlItem.getAttributInt("y", GridBagConstraints.RELATIVE);
		W = aXmlItem.getAttributInt("colspan", GridBagConstraints.REMAINDER);
		H = aXmlItem.getAttributInt("rowspan", GridBagConstraints.REMAINDER);
		HMin = aXmlItem.getAttributInt("HMin", 0);
		VMin = aXmlItem.getAttributInt("VMin", 0);
		HMax = aXmlItem.getAttributInt("HMax", Integer.MAX_VALUE);
		VMax = aXmlItem.getAttributInt("VMax", Integer.MAX_VALUE);
		if (aOwnerPanel != null) {
			mGdbConp.gridx = X;
			mGdbConp.gridy = Y;
			mGdbConp.gridwidth = W;
			mGdbConp.gridheight = H;
			mGdbConp.fill = mFill;
			mGdbConp.anchor = GridBagConstraints.NORTH;
			mGdbConp.weightx = mWeightx;
			mGdbConp.weighty = mWeighty;
			mGdbConp.insets = new Insets(1, 1, 1, 1);
			if ((HMin > 0) && (VMin > 0)) {
				this.setMinimumSize(new Dimension(HMin, VMin));
				this.setPreferredSize(new Dimension(HMin, VMin));
			}
			if ((HMax > 0) && (VMax > 0))
				this.setMaximumSize(new Dimension(HMax, VMax));
			aOwnerPanel.add(this, mGdbConp);
		}
		mNeeded = (aXmlItem.getAttributInt("needed", 0) == 1);
		try {
			JavaScript = java.net.URLDecoder.decode(aXmlItem
					.getCDataOfFirstTag("JavaScript"), "utf-8");
			if (JavaScript.length() > 0)
				Logging.getInstance()
						.writeLog("==> JavaScript '" + getName() + "' ==>",
								JavaScript, 2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			JavaScript = "";
		}
	}

	private boolean mFirstRefresh = true;

	public void setValue(SimpleParsing aXmlItem) throws LucteriosException {
		if (mFirstRefresh || !mXmlItem.equals(aXmlItem)) {
			mXmlItem = aXmlItem;
			refreshComponent();
			mFirstRefresh = false;
		}
	}

	public void setValue(String aValue) throws LucteriosException {
		SimpleParsing item = new SimpleParsing();
		if (item.parse(aValue)) {
			mFirstRefresh = true;
			setValue(item);
			initialize();
			setVisible(true);
		}
	}

	public String getValue() {
		try {
			Object val = getRequete("").get(getName());
			if (val != null)
				return val.toString();
			else
				return "";
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
			return "";
		}
	}

	public static Cmponent getParentOfControle(Component aComponent) {
		Cmponent cmp = null;
		if (aComponent != null)
			if (Cmponent.class.isInstance(aComponent.getParent()))
				cmp = (Cmponent) aComponent.getParent();
			else
				cmp = getParentOfControle(aComponent.getParent());
		return cmp;
	}

	public void runJavaScript() throws LucteriosException {
		if (JavaScript.length() > 0) {
			try {
				Context cx = Context.enter();
				try {
					Scriptable scope = cx.initStandardObjects();
					Object wrappedCurrent = Context.javaToJS(this, scope);
					ScriptableObject.putProperty(scope, "current",
							wrappedCurrent);
					Object wrappedParent = Context.javaToJS(getObsCustom(),
							scope);
					ScriptableObject
							.putProperty(scope, "parent", wrappedParent);
					Object result = cx.evaluateString(scope, JavaScript,
							"<js>", 1, null);
					Logging.getInstance().writeLog(
							"<== JavaScript '" + getName() + "' <==",
							Context.toString(result), 2);
				} finally {
					Context.exit();
				}
			} catch (EcmaError e) {
				throw new LucteriosException("Erreur dans un script",
						JavaScript, "", e);
			}
		}
	}

	public abstract MapContext getRequete(String aActionIdent)
			throws LucteriosException;

	protected abstract void initComponent();

	protected abstract void refreshComponent() throws LucteriosException;

	public void initialize() throws LucteriosException {
	}

}
