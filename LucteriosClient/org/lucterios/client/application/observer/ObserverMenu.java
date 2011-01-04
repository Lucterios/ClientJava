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

import org.lucterios.client.application.Menu;
import org.lucterios.client.application.Menu.FrameControle;
import org.lucterios.client.presentation.ObserverAbstract;
import org.lucterios.client.presentation.ObserverConstant;
import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.utils.IForm;
import org.lucterios.client.utils.IDialog;
import org.lucterios.utils.LucteriosException;

public class ObserverMenu extends ObserverAbstract {
	static public FrameControle Main = null;

	public ObserverMenu() {
		super();
	}

	public String getObserverName() {
		return "Core.Menu";
	}

	public byte getType() {
		return ObserverConstant.TYPE_NONE;
	}

	public void show(String aTitle) throws LucteriosException {
		super.show(aTitle);
		Menu.fillMenuInFrame(Main, this, Singletons.Factory(), mContent.getFirstSubTag("MENUS"));
	}

	public void show(String aTitle, IForm new_frame) throws LucteriosException {
		throw new LucteriosException("Not in Frame");
	}

	public void show(String aTitle, IDialog aGUI) throws LucteriosException {
		throw new LucteriosException("Not in Dialog");
	}

	public void close(boolean aMustRefreshParent) {
		Main.setActive(false);
	}

	public void setActive(boolean aIsActive) {
		Main.setActive(aIsActive);
	}

	public void setNameComponentFocused(String aNameComponentFocused) {
	}

}
