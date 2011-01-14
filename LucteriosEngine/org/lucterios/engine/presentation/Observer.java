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

import java.util.TreeMap;

import org.lucterios.engine.utils.IDialog;
import org.lucterios.engine.utils.IForm;
import org.lucterios.engine.utils.NotifyFrameObserver;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public interface Observer extends NotifyFrameObserver {
	
	@SuppressWarnings("serial")
	public class MapContext extends TreeMap<String,Object> {}
	
	public String getObserverName();

	public void setParent(Observer aParent);

	public Observer getParent();

	public void setSource(String aExtension, String aAction);

	public String getSourceExtension();

	public String getSourceAction();

	public void setContext(MapContext aContext);

	public MapContext getContext();

	public void setContent(SimpleParsing aContent);

	public void setCaption(String aCaption);

	public SimpleParsing getContent();

	public String getContentText();
	
	public byte getType();

	public String getTitle();

	public void show(String aTitle) throws LucteriosException;

	public void show(String aTitle, IForm new_frame) throws LucteriosException;

	public void show(String aTitle, IDialog aGUI) throws LucteriosException;

	public IForm getGUIFrame();

	public IDialog getGUIDialog();

	public void setActive(boolean aIsActive);

	public void close(boolean aMustRefreshParent);

	public void refresh() throws LucteriosException;

	public MapContext getParameters(String aActionId, int aUnique, boolean aCheckNull) throws LucteriosException;

	public void setNameComponentFocused(String aNameComponentFocused);
}
