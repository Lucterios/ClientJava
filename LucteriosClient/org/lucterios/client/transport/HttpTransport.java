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

package org.lucterios.client.transport;

import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;

import org.lucterios.utils.LucteriosException;

public interface HttpTransport {
	public String getSession();

	public void setSession(String aSession);

	public int getCurrentPort();

	public String getRootPath();

	public String getServerHost();

	public void setProxy(String aProxyServer, int aProxyPort);

	public void connectToServer(String aServerHost, String aRootPath, int aPort);

	public void closeConnection();

	public String transfertXMLFromServer(String aXmlParam)
			throws LucteriosException;

	public String transfertFileFromServerString(String aWebFile, Map aParams)
			throws LucteriosException;

	public ImageIcon getIcon(String aIconName);

	public void saveFiles(String aServerFileName, String aLocalFileName)
			throws LucteriosException;

	public void openPageInWebBrowser(String pageName) throws LucteriosException;

	public URL getUrl();

	public int getFileLength(String aWebFile) throws LucteriosException;
}