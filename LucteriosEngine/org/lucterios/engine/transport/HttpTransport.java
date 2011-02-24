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

package org.lucterios.engine.transport;

import java.net.URL;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.AbstractImage;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.LucteriosException;

public interface HttpTransport {
	public final static String POST_VARIABLE = "XMLinput";

	public void setDesktop(DesktopInterface desktop);
	
	public String getSession();

	public void setSession(String aSession);

	public int getCurrentPort();

	public String getRootPath();

	public String getServerHost();

	public boolean getSecurity();

	public String getProtocol();
	
	public void setProxy(String aProxyServer, int aProxyPort);

	public void connectToServer(String aServerHost, String aRootPath, int aPort, boolean aSecurity);

	public void closeConnection();

	public String transfertXMLFromServer(MapContext aParams) throws LucteriosException;

	public String transfertFileFromServerString(String aWebFile, MapContext aParams)
			throws LucteriosException;

	public AbstractImage getIcon(String aIconName,int aSize);

	public void saveFiles(String aServerFileName, String aLocalFileName)
			throws LucteriosException;

	public void openPageInWebBrowser(String pageName) throws LucteriosException;

	public URL getUrl();

	public int getFileLength(String aWebFile) throws LucteriosException;
}
