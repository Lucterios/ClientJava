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

package org.lucterios.engine.application.comp;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing; 
import org.lucterios.graphic.ExceptionDlg; 
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class Cmponent {
	static final long serialVersionUID = 1L;

	public static int CmponentCount = 0;

	protected FillMode mFill;
	protected SimpleParsing mXmlItem;
	protected WeakReference<Observer> mObsCustom;
	public String Description;
	public String JavaScript;
	private double mWeightx;
	private double mWeighty;
	public boolean mNeeded;
	public int X;
	public int Y;
	public int H;
	public int W;
	public int HMin=0;
	public int VMin=0;
	public int HMax;
	public int VMax;

	private String mName="";

	protected GUIContainer mPanel=null;
	protected GUIParam mParam=null;
	
	public Cmponent() {
		super();
		CmponentCount++;
		mFill = FillMode.FM_HORIZONTAL;
		setWeightx(0.0);
		setWeighty(0.0);
		mNeeded = false;
		mParam=new GUIParam(0,0);
	}

	public void setWeightx(double mWeightx) {
		this.mWeightx = mWeightx;
	}

	public double getWeightx() {
		return mWeightx;
	}

	public void setWeighty(double mWeighty) {
		this.mWeighty = mWeighty;
	}

	public double getWeighty() {
		return mWeighty;
	}

	protected void finalize() throws Throwable {
		CmponentCount--;
		super.finalize();
	}

	public void close() {
		mXmlItem = null;
		mObsCustom = null;
	}

	public void fillActions(ActionList atns) {

	}

	public boolean isEmpty() {
		return false; // mNeeded
	}

	public void forceFocus() {
		mPanel.requestFocusGUI();
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
		mPanel.setEnabled(aEnabled);
		mEnabled = aEnabled;
	}

	public void setName(String name) {
		this.mName=name;
	}

	public String getName() {
		return this.mName;
	}

	public void initPanel(GUIContainer panel, Observer aObsCustom,
			SimpleParsing aXmlItem){
		mPanel=panel;
		mObsCustom = new WeakReference<Observer>(aObsCustom);
		mXmlItem = aXmlItem;
		mPanel.setObject(this);
		initComponent();
	}
		
	public void init(GUIContainer aOwnerPanel, Observer aObsCustom,
			SimpleParsing aXmlItem) {
		setName(aXmlItem.getAttribute("name"));
		Description = aXmlItem.getAttribute("description");
		X = aXmlItem.getAttributeInt("x", -1);
		Y = aXmlItem.getAttributeInt("y", -1);
		W = aXmlItem.getAttributeInt("colspan", 1);
		H = aXmlItem.getAttributeInt("rowspan", 1);
		HMin = aXmlItem.getAttributeInt("HMin", HMin);
		VMin = aXmlItem.getAttributeInt("VMin", VMin);
		HMax = aXmlItem.getAttributeInt("HMax", Integer.MAX_VALUE);
		VMax = aXmlItem.getAttributeInt("VMax", Integer.MAX_VALUE);
		GUIParam param=new GUIParam(X,Y); 
		param.setW(W);
		param.setH(H);
		param.setFill(mFill);
		param.setWeight(getWeightx(),getWeighty());
		param.setPad(1);
		if ((HMin > 0) && (VMin > 0)) {
			param.setPrefSizeX(HMin);
			param.setPrefSizeY(VMin);
		}
		mNeeded = (aXmlItem.getAttributeInt("needed", 0) == 1);
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
		initPanel(aOwnerPanel.createContainer(ContainerType.CT_NORMAL, param),
				aObsCustom,aXmlItem);
	}

	private boolean mFirstRefresh = true;

	public void setValue(SimpleParsing aXmlItem) {
		if (mFirstRefresh || !mXmlItem.equals(aXmlItem)) {
			mXmlItem = aXmlItem;
			refreshComponent();
			mFirstRefresh = false;
		}
	}

	public void setValue(String aValue) {
		SimpleParsing item = new SimpleParsing();
		if (item.parse(aValue)) {
			mFirstRefresh = true;
			setValue(item);
			initialize();
			mPanel.setVisible(true);
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

	public static Cmponent getParentOfControle(GUIComponent aComponent) {
		Cmponent cmp = null;		
		if (aComponent != null)
			if (GUIContainer.class.isInstance(aComponent)) {
				
				if (Cmponent.class.isInstance(((GUIContainer)aComponent).getObject()))
					cmp = (Cmponent)((GUIContainer)aComponent).getObject();
			}
			if (cmp==null)
				cmp = getParentOfControle(aComponent.getOwner());
		return cmp;
	}
	
	public abstract MapContext getRequete(String aActionIdent)
			throws LucteriosException;

	protected abstract void initComponent();

	protected abstract void refreshComponent();

	public void initialize() {
	}
	
}
