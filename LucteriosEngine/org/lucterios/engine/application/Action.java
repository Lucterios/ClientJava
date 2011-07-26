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

import java.util.ArrayList;

import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.ui.GUIAction;
import org.lucterios.utils.SimpleParsing;

public interface Action extends GUIAction  {
	
	@SuppressWarnings("serial")
	public class ActionList extends ArrayList<Action> {}
	
	public void initialize(Observer aOwner, ObserverFactory aFactory,
			SimpleParsing aXml);

	public void initialize(Observer aOwner, ObserverFactory aFactory,
			String aTitle, String aExtension, String aAction);

	public Observer getOwner();
	
	public void setOwner(Observer aOwner);	

	public String getID();

	public String getExtension();

	public String getAction();

	public int getFormType();

	public boolean getClose();

	public int getSelect();

	public boolean getCheckNull();

	public boolean getUsedContext();

	public void setFormType(int aFormType);

	public void setClose(boolean aClose);

	public void setSelect(int aSelect);

	public void setCheckNull(boolean aCheckNull);

	public void setUsedContext(boolean aUsedContext);

	public void setKeyStroke(String aKey);

	public void runAction(MapContext aParam);
}
