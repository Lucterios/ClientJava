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

public class HttpTransportStub implements HttpTransport {
	static private String mServerHost = "";
	static private int mCurrentPort = 0;
	static private String mRootPath = "";
	static private String mSession = "";

	public HttpTransportStub() {
		super();
	}

	public void connectToServer(String aServerHost, String aRootPath, int aPort) {
		mCurrentPort = aPort;
		mServerHost = aServerHost;
		mRootPath = aRootPath;
		if ((mRootPath.length() > 0) && (mRootPath.charAt(0) != '/'))
			mRootPath = "/" + mRootPath;
	}

	public void setProxy(String aProxyServer, int aProxyPort) {
	}

	public void closeConnection() {
		mSession = "";
		mServerHost = "";
		mCurrentPort = 0;
		mRootPath = "";
	}

	public String getSession() {
		return mSession;
	}

	public void setSession(String aSession) {
		mSession = aSession;
	}

	public int getCurrentPort() {
		return mCurrentPort;
	}

	public String getRootPath() {
		return mRootPath;
	}

	public String getServerHost() {
		return mServerHost;
	}

	public ImageIcon IconName = null;

	public ImageIcon getIcon(String aIconName) {
		return IconName;
	}

	public String XmlParam = "";
	public String XmlReceved = "";
	public LucteriosException NextException = null;

	public String transfertXMLFromServer(String aXmlParam)
			throws LucteriosException {
		XmlParam = aXmlParam;
		if (NextException != null)
			throw NextException;
		return XmlReceved;
	}

	public String transfertFileFromServerString(String aWebFile, Map aParams)
			throws LucteriosException {
		XmlParam = aWebFile;
		return XmlReceved;
	}

	public void saveFiles(String aServerFileName, String aLocalFileName)
			throws LucteriosException {
	}

	public void openPageInWebBrowser(String pageName) throws LucteriosException {
		// TODO Auto-generated method stub

	}

	public URL getUrl() {
		return null;
	}

	public int getFileLength(String webFile) throws LucteriosException {
		return 0;
	}
}