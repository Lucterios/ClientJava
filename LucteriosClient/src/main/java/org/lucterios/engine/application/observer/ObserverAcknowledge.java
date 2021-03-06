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

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.presentation.ObserverAbstract;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class ObserverAcknowledge extends ObserverAbstract {

	protected Action mRedirectAction = null;

	public Action getRedirectAction() {
		return mRedirectAction;
	}
	
	public String getObserverName() {
		return "Core.Acknowledge";
	}

	public void setContent(SimpleParsing aContent) {
		super.setContent(aContent);
		SimpleParsing act = mContent.getFirstSubTag("ACTION");
		if (act != null) {
			mRedirectAction = new ActionImpl();
			mRedirectAction.initialize(this, Singletons.Factory(), act);
			mRedirectAction.setClose(true);
		} else
			mRedirectAction = null;
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) {
		super.show(aTitle);
		setActive(true);
		if (mRedirectAction != null)
			mRedirectAction.actionPerformed();
		close(mRedirectAction == null);
	}

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public MapContext getParameters(String aActionId, int aSelect,
			boolean aCheckNull) throws LucteriosException {
		MapContext requete = new MapContext();
		requete.putAll(mContext);
		return requete;
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}

}
