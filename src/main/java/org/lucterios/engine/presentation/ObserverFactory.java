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

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.engine.transport.HttpTransport;
import org.lucterios.utils.LucteriosException;

public interface ObserverFactory {
	public void setHttpTransport(HttpTransport aTransport);

	public HttpTransport getHttpTransport();

	public void clearObserverList();

	public void AddObserver(String aObserverName, Class<?> aObserver);

	public boolean setAuthentification(String aLogin, String aPassWord)
			throws LucteriosException;

	public Observer callAction(String aExtension, String aAction, MapContext aParam)
			throws LucteriosException;

	public Observer callAction(String aExtension, String aAction, MapContext aParam,
			Observer aObserver) throws LucteriosException;
}
