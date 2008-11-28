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

package org.lucterios.client.presentation;

import java.util.Map;

import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.client.utils.Form.NotifyFrameObserver;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public interface Observer extends NotifyFrameObserver {
	public String getObserverName();

	public void setParent(Observer aParent);

	public Observer getParent();

	public void setSource(String aExtension, String aAction);

	public String getSourceExtension();

	public String getSourceAction();

	public void setContext(Map aContext);

	public Map getContext();

	public void setContent(SimpleParsing aContent);

	public void setCaption(String aCaption);

	public SimpleParsing getContent();

	public String getContentText();
	
	public byte getType();

	public String getTitle();

	public void show(String aTitle) throws LucteriosException;

	public void show(String aTitle, Form new_frame) throws LucteriosException;

	public void show(String aTitle, Dialog aGUI) throws LucteriosException;

	public Form getGUIFrame();

	public Dialog getGUIDialog();

	public void setActive(boolean aIsActive);

	public void close(boolean aMustRefreshParent);

	public void refresh() throws LucteriosException;

	public Map getParameters(String aActionId, int aUnique, boolean aCheckNull);

	public void setNameComponentFocused(String aNameComponentFocused);
}
