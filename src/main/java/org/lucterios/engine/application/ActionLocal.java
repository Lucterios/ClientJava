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

package org.lucterios.engine.application;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.AbstractImage;
import org.lucterios.ui.GUIActionListener;
import org.lucterios.utils.SimpleParsing;

public class ActionLocal implements Action, GUIActionListener {

	private String mTitle = "";
	private char mMenomnic = 0;
	private AbstractImage mIcon = null;
	private GUIActionListener mAction = null;
	private String mKey = "";

	public ActionLocal(String aTitle, char aMenomnic, AbstractImage aIcon,
			GUIActionListener aAction, String aKey) {
		super();
		mTitle = aTitle;
		mMenomnic = aMenomnic;
		mIcon = aIcon;
		mAction = aAction;
		if (aKey != null)
			mKey = aKey;
	}

	public void setOwner(Observer aOwner) {
	}

	public String getAction() {
		return "";
	}

	public boolean getCheckNull() {
		return false;
	}

	public boolean getClose() {
		return false;
	}

	public String getExtension() {
		return "";
	}

	public int getFormType() {
		return ActionConstantes.FORM_NOMODAL;
	}

	public String getID() {
		return "";
	}

	public AbstractImage getIcon() {
		return mIcon;
	}

	public String getKeyStroke() {
		return mKey;
	}

	public char getMnemonic() {
		return mMenomnic;
	}

	public Observer getOwner() {
		return null;
	}

	public int getSelect() {
		return ActionConstantes.SELECT_NONE;
	}

	public String getTitle() {
		return mTitle;
	}

	public boolean getUsedContext() {
		return false;
	}

	public void initialize(Observer owner, ObserverFactory factory,
			SimpleParsing xml) {
	}

	public void initialize(Observer owner, ObserverFactory factory,
			String title, String extension, String action) {
	}

	public void runAction(MapContext param) {
		actionPerformed();
	}

	public void setCheckNull(boolean checkNull) {
	}

	public void setClose(boolean close) {
	}

	public void setFormType(int formType) {
	}

	public void setSelect(int select) {
	}

	public void setUsedContext(boolean usedContext) {
	}

	public void setKeyStroke(String key) {
		mKey = key;
	}

	public void actionPerformed() {
		if (mAction != null)
			Singletons.getWindowGenerator().invokeLater(new Runnable() {
				public void run() {
					Singletons.getWindowGenerator().getFrame().setActive(false);
					try {
						mAction.actionPerformed();
					} finally {
						Singletons.getWindowGenerator().getFrame().setActive(true);
					}
				}
			});

	}

	public Object getValue(String arg0) {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public void putValue(String arg0, Object arg1) {
	}

	public void setEnabled(boolean arg0) {
	}

}
