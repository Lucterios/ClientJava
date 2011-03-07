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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.AbstractImage;
import org.lucterios.utils.DesktopInterface;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;

public abstract class HttpTransportAbstract implements HttpTransport {
	final static public String ENCODE = "utf-8";
	final static String MANAGER_FILE = "coreIndex.php";
	final static String AUTH_REQUETE="<REQUETE extension='common' action='authentification'>";
	final static String AUTH_PARAM="<PARAM name='ses' type='str'>";
	
	static private String mServerHost = "";
	static private int mCurrentPort = 0;
	static private String mRootPath = "";
	static private String mSession = "";

	private ImageCache imageCache = null;
	private DesktopInterface mDesktop=null;

	static protected String mProxyServer = "";
	static protected int mProxyPort = 0;
	static protected boolean mSecurity=false;
	
	public HttpTransportAbstract() {
		super();
		imageCache = new ImageCache(this);
	}

	public void setDesktop(DesktopInterface desktop){
		mDesktop=desktop;
	}

	public void connectToServer(String aServerHost, String aRootPath, int aPort, boolean aSecurity) {
		mCurrentPort = aPort;
		mServerHost = aServerHost;
		mRootPath = aRootPath;
		mSecurity = aSecurity;
		if ((mRootPath.length() > 0) && (mRootPath.charAt(0) != '/'))
			mRootPath = "/" + mRootPath;
	}

	public void setProxy(String aProxyServer, int aProxyPort) {
		mProxyServer = aProxyServer;
		mProxyPort = aProxyPort;
	}

	public void closeConnection() {
		mSession = "";
		mServerHost = "";
		mCurrentPort = 0;
		mRootPath = "";
		mProxyServer = "";
		mProxyPort = 0;
		mSecurity=false;
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

	public boolean getSecurity() {
		return mSecurity;
	}

	public String getProtocol() {
		if (mSecurity)
			return "https";
		else
			return "http";
	}
	
	public String getRootPath() {
		return mRootPath;
	}

	public String getServerHost() {
		return mServerHost;
	}

	public URL getUrl() {
		return getUrl("");
	}

	protected java.net.URL getUrl(String aWebName) {
		if (aWebName != null) {
			try {
				if (aWebName.startsWith("http://") || aWebName.startsWith("https://"))
					return new java.net.URL(aWebName);
				if ((aWebName.length() > 0) && (aWebName.charAt(0) != '/')
						&& (mRootPath.length() > 0)
						&& (mRootPath.charAt(mRootPath.length() - 1) != '/'))
					aWebName = "/" + aWebName;
				return new java.net.URL(getProtocol(), mServerHost, mCurrentPort,
						mRootPath + aWebName);
			} catch (MalformedURLException e) {
				return null;
			}
		} else
			return null;
	}

	public AbstractImage getIcon(String aIconName,int aSize)  {
		if ((aIconName != null) && (aIconName.length() > 0)) {
			if (imageCache.isInCache(aIconName,aSize)) {
				return imageCache.getImage(aIconName);
			}
			else {
				AbstractImage icon_result = AbstractImage.Null;
				try {
					try {
						Logging.getInstance().writeLog("### TELECHARGEMENT ###",aIconName,2);
						InputStream reponse = transfertFileFromServer(aIconName, new MapContext());
						icon_result=imageCache.addImage(aIconName, reponse);
					} catch (Exception e) {
						imageCache.addDummy(aIconName);
						e.printStackTrace();
					}
				} finally {
					downloadFinished();
				}
				return icon_result;
			}
		} else {
			return AbstractImage.Null;
		}
	}

	protected String parseISToString(java.io.InputStream is, boolean withRefresh)
			throws IOException {
		java.io.DataInputStream din = new java.io.DataInputStream(is);
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[2048];
		for (int n; (n = din.read(b)) != -1;)
			out.append(new String(b, 0, n));
		return out.toString();
	}

	protected abstract void downloadFinished();

	protected abstract InputStream transfertFileFromServer(String aWebFile, MapContext aParams) throws LucteriosException;

	public abstract int getFileLength(String aWebFile) throws LucteriosException;
	
	public String transfertFileFromServerString(String aWebFile, MapContext aParams)
			throws LucteriosException {
		String input_reponse = null;
		synchronized (mSynchronizedObj) {
			try {
				InputStream reponse = transfertFileFromServer(aWebFile, aParams);
				if (reponse != null) {
					int repeat_loop_index = 3;
					while (repeat_loop_index > 0) {
						try {
							input_reponse = parseISToString(reponse, true);
							repeat_loop_index = 0;
						} catch (java.io.IOException e) {
							repeat_loop_index--;
							if (repeat_loop_index <= 0)
								throw new LucteriosException(e.toString(), "",
										"", e);
						}
					}
				}
			} finally {
				downloadFinished();
			}
		}
		return input_reponse;
	}

	public void saveFiles(String aServerFileName, String aLocalFileName)
			throws LucteriosException {
		synchronized (mSynchronizedObj) {
			try {
				InputStream is = transfertFileFromServer(aServerFileName, null);
				Tools.saveFileStream(new File(aLocalFileName),is);
			} finally {
				downloadFinished();
			}
		}
	}

	protected Object mSynchronizedObj = new Object();

	public String transfertXMLFromServer(MapContext aXmlParam) throws LucteriosException {
		String data = "";
		synchronized (mSynchronizedObj) {
			String xml_param = "<?xml version='1.0' encoding='" + ENCODE
					+ "'?>";
			xml_param = xml_param + "<REQUETES>\n";
			if (!"".equals( mSession ) && (!aXmlParam.containsKey(POST_VARIABLE) || !aXmlParam.get(POST_VARIABLE).toString().startsWith(AUTH_REQUETE))) {
				xml_param = xml_param + AUTH_REQUETE;
				xml_param = xml_param + AUTH_PARAM + mSession + "</PARAM>";
				xml_param = xml_param + "</REQUETE>";
			}
			if (aXmlParam.containsKey(POST_VARIABLE))
				xml_param = xml_param + aXmlParam.get(POST_VARIABLE).toString();
			xml_param = xml_param + "</REQUETES>";
			Logging.getInstance().setInText(xml_param);
	        try
	        {
	        	aXmlParam.put(POST_VARIABLE,java.net.URLEncoder.encode(xml_param,ENCODE));
	        }
	        catch(java.io.UnsupportedEncodingException e)
	        {
	            throw new LucteriosException(e.toString(),e);
	        }
			try {
				data = "<?xml version='1.0' encoding='ISO-8859-1'?>";
				data = data	+ transfertFileFromServerString(MANAGER_FILE, aXmlParam);
			} catch (TransportException e) {
				throw new LucteriosException("<b>Le serveur ne répond pas.</b>" +
						"<br>Vérifiez la connection réseau et les configurations de l'outil.",e);
			}
			try {
				data = java.net.URLDecoder.decode(data.trim(), ENCODE);
			} catch (java.io.UnsupportedEncodingException e) {
				throw new LucteriosException(e.toString(), e);
			}
			if ((data.length() > 0) && (data.charAt(0) != '<'))
				throw new TransportException(data,TransportException.TYPE_HTTP, 2, xml_param, data);
			Logging.getInstance().setOutText(data);
		}
		return data;
	}

	public void openPageInWebBrowser(String pageName) throws LucteriosException {
		java.net.URL page_url = getUrl(pageName);
		mDesktop.launch(page_url.toString());
	}
}
