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

package org.lucterios.engine.presentation;

import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.graphic.ExceptionDlg;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.utils.SimpleParsing;

public class ObserverStub extends ObserverAbstract {
	static public String ObserverName = "ObserverStub";
	static public byte mType = ObserverConstant.TYPE_BOTH;

	public ObserverStub() {
		super();
	}

	public String getObserverName() {
		return ObserverName;
	}

	public byte getType() {
		return mType;
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		mContent = aContent;
	}

	public SimpleParsing getContent() {
		return mContent;
	}

	static public boolean mShow = false;
	static public String mTitle = "";

	public void show(String aTitle) {
		mShow = true;
		mTitle = aTitle;
		try{
			if (getGUIFrame() != null)
				getGUIFrame().setVisible(true);
			if (getGUIDialog() != null)
				getGUIDialog().setVisible(true);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
	}

	public void show(String aTitle, GUIForm aGUI) {
		mGUIFrame = aGUI;
		show(aTitle);
	}

	public void show(String aTitle, GUIDialog aGUI) {
		mGUIDialog = aGUI;
		show(aTitle);
	}

	public boolean mRefresh = false;

	public void refresh() {
		mRefresh = true;
	}

	public boolean mClose = false;

	public void close(boolean aMustRefreshParent) {
		mClose = true;
		try{
			if (getGUIFrame() != null)
				getGUIFrame().setVisible(false);
			if (getGUIDialog() != null)
				getGUIDialog().setVisible(false);
		}catch (Exception e) {
			ExceptionDlg.throwException(e);
		}
		super.close(aMustRefreshParent);
	}

	static public MapContext mParameters = new MapContext();
	static public String LastActionId = "";
	static public int LastSelect = ActionConstantes.SELECT_NONE;
	static public boolean LastCheckNull = false;

	public MapContext getParameters(String aActionId, int aSelect, boolean aCheckNull) {
		LastActionId = aActionId;
		LastSelect = aSelect;
		LastCheckNull = aCheckNull;
		return mParameters;
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}
}
