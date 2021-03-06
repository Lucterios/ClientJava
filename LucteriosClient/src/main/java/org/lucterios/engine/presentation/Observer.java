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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIForm;
import org.lucterios.gui.GUIObject;
import org.lucterios.gui.NotifyFrameObserver;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public interface Observer extends NotifyFrameObserver {
	
	@SuppressWarnings("serial")
	public class MapContext extends TreeMap<String,Object> {
		public String toString(){
			String result="";
			for (Iterator<?> iterator = entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
				String key = (String) entry.getKey();
				Object value_obj = entry.getValue();
				String value = value_obj.toString();
				result = result + key + "='" + value + "',";
			}
			return result;
		}
	}
	
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

	public void show(String aTitle, GUIForm new_frame) throws LucteriosException;

	public void show(String aTitle, GUIDialog aGUI) throws LucteriosException;

	public GUIForm getGUIFrame();

	public GUIDialog getGUIDialog();

	public void setActive(boolean aIsActive);

	public void close(boolean aMustRefreshParent);

	public void refresh() throws LucteriosException;

	public MapContext getParameters(String aActionId, int aUnique, boolean aCheckNull) throws LucteriosException;

	public void setNameComponentFocused(String aNameComponentFocused);

	public GUIObject getGUIObject();
}
