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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import org.lucterios.utils.DesktopTools;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.Tools;

public class HttpTransportImpl implements HttpTransport {
	final static public String ENCODE = "utf-8";
	final static String MANAGER_FILE = "coreIndex.php";
	final static String POST_VARIABLE = "XMLinput";

	static private String mServerHost = "";
	static private int mCurrentPort = 0;
	static private String mRootPath = "";
	static private String mSession = "";

	static private String mProxyServer = "";
	static private int mProxyPort = 0;

	private org.apache.commons.httpclient.HttpClient m_Cnx = null;
	private ImageCache imageCache = null;

	public HttpTransportImpl() {
		super();
		m_Cnx = new org.apache.commons.httpclient.HttpClient();
		imageCache = new ImageCache(this);
	}

	public void connectToServer(String aServerHost, String aRootPath, int aPort) {
		mCurrentPort = aPort;
		mServerHost = aServerHost;
		mRootPath = aRootPath;
		if ((mRootPath.length() > 0) && (mRootPath.charAt(0) != '/'))
			mRootPath = "/" + mRootPath;
	}

	public void setProxy(String aProxyServer, int aProxyPort) {
		mProxyServer = aProxyServer;
		mProxyPort = aProxyPort;
	}

	private HostConfiguration getHostCnx() {
		HostConfiguration host_cnx = new HostConfiguration();
		if ((!"".equals( mProxyServer )) && (mProxyPort != 0))
			host_cnx.setProxy(mProxyServer, mProxyPort);
		return host_cnx;
	}

	public void closeConnection() {
		mSession = "";
		mServerHost = "";
		mCurrentPort = 0;
		mRootPath = "";
		mProxyServer = "";
		mProxyPort = 0;
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

	public URL getUrl() {
		return getUrl("");
	}

	private java.net.URL getUrl(String aWebName) {
		if (aWebName != null) {
			try {
				if (aWebName.startsWith("http://"))
					return new java.net.URL(aWebName);
				if ((aWebName.length() > 0) && (aWebName.charAt(0) != '/')
						&& (mRootPath.length() > 0)
						&& (mRootPath.charAt(mRootPath.length() - 1) != '/'))
					aWebName = "/" + aWebName;
				return new java.net.URL("http", mServerHost, mCurrentPort,
						mRootPath + aWebName);
			} catch (MalformedURLException e) {
				return null;
			}
		} else
			return null;
	}

	public ImageIcon getIcon(String aIconName)  {
		if ((aIconName != null) && (aIconName.length() > 0)) {
			if (imageCache.isInCache(aIconName))
				return imageCache.getImage(aIconName);
			else {
				ImageIcon icon_result = null;
				try {
					try {
						InputStream reponse = transfertFileFromServer(aIconName, new TreeMap());
						icon_result=imageCache.addImage(aIconName, reponse);
					} catch (LucteriosException e) {
						e.printStackTrace();
					}
				} finally {
					downloadFinished();
				}
				return icon_result;
			}
		} else {
			return null;
		}
	}

	private String parseISToString(java.io.InputStream is, boolean withRefresh)
			throws IOException {
		java.io.DataInputStream din = new java.io.DataInputStream(is);
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[2048];
		for (int n; (n = din.read(b)) != -1;)
			out.append(new String(b, 0, n));
		return out.toString();
	}

	private PostMethod method = null;
	private HeadMethod header = null;

	private void downloadFinished() {
		if (method != null)
			method.releaseConnection();
		method = null;
		if (header != null)
			header.releaseConnection();
		header = null;
	}

	private InputStream transfertFileFromServer(String aWebFile, Map aParams)
			throws LucteriosException {
		downloadFinished();
		java.net.URL path_url = getUrl(aWebFile);
		InputStream data = null;
		String param_txt = "";
		int repeat_loop_index = 3;
		while (repeat_loop_index > 0) {
			try {
				param_txt = getMethodByParameters(aParams, path_url);

				// Execute the POST method
				int statusCode = m_Cnx.executeMethod(getHostCnx(), method);
				if (statusCode == HttpStatus.SC_OK) {
					data = method.getResponseBodyAsStream();
					if (data != null)
						repeat_loop_index = 0;
					else
						repeat_loop_index--;
				} else {
					repeat_loop_index--;
					if ((statusCode != HttpStatus.SC_NOT_FOUND)
							|| (repeat_loop_index <= 0))
						throw new TransportException(method.getStatusLine()
								.getReasonPhrase(),
								TransportException.TYPE_HTTP, statusCode,
								param_txt, "");
				}
			} catch (java.net.ConnectException ce) {
				repeat_loop_index--;
				checkException(data, repeat_loop_index, param_txt, ce, "");
			} catch (java.net.UnknownHostException uhe) {
				repeat_loop_index--;
				checkException(data, repeat_loop_index, param_txt, uhe,
						"Serveur inconnu:");
			} catch (IOException ioe) {
				repeat_loop_index--;
				checkException(data, repeat_loop_index, param_txt, ioe, "");
			}
		}
		checkException(data, repeat_loop_index, param_txt, null, "");
		return data;
	}

	private void checkException(InputStream data, int repeat_loop_index,
			String param_txt, Exception except, String prefixText)
			throws LucteriosException {
		if ((data == null) && (repeat_loop_index == 0)) {
			if (except == null)
				throw new LucteriosException("Réponse null", param_txt, "");
			else if (SocketException.class.isInstance(except))
				throw new TransportException(except.toString(),
						TransportException.TYPE_STANDARD, 0, param_txt, "",
						except);
			else if (IOException.class.isInstance(except))
				throw new LucteriosException(except.toString(), param_txt, "",
						except);
			else
				throw new TransportException(prefixText + except.getMessage(),
						TransportException.TYPE_HTTP, 0, param_txt, "", except);
		}
	}

	private String getMethodByParameters(Map aParams, java.net.URL aPathUrl) {
		String param_txt = "";
		method = new PostMethod(aPathUrl.toString());
		if (aParams != null) {
			Set keys = aParams.keySet();
			for (int data_idx = 0; data_idx < keys.size(); data_idx++) {
				String key = (String) keys.toArray()[data_idx];
				method.addParameter(key, (String) aParams.get(key));
				param_txt += (String) aParams.get(key);
			}
		}
		return param_txt;
	}

	public int getFileLength(String aWebFile) throws LucteriosException {
		int size = 0;
		String value="";
		java.net.URL path_url = getUrl(aWebFile);
		int repeat_loop_index = 3;
		while (repeat_loop_index > 0) {
			try {
				header=new HeadMethod(path_url.toString());
				try {
					// Execute the HEADER method
					int statusCode = m_Cnx.executeMethod(getHostCnx(), header);
					if (statusCode == HttpStatus.SC_OK) {
						value = header.getResponseHeader("Content-Length").getValue();
						if (value != null) {
							size=Integer.parseInt(value);
							repeat_loop_index = 0;
						}
						else
							repeat_loop_index--;
					} else {
						repeat_loop_index--;
						if ((statusCode != HttpStatus.SC_NOT_FOUND)
								|| (repeat_loop_index <= 0))
							throw new TransportException(header.getStatusLine()
									.getReasonPhrase(),
									TransportException.TYPE_HTTP, statusCode,
									"", "");
					}
				} finally {
					downloadFinished();
				}
			} catch (java.net.ConnectException ce) {
				repeat_loop_index--;
				throw new LucteriosException("Erreur de Header",aWebFile,value,ce);
			} catch (java.net.UnknownHostException uhe) {
				repeat_loop_index--;
				throw new LucteriosException("Erreur de Header",aWebFile,value,uhe);
			} catch (IOException ioe) {
				repeat_loop_index--;
				throw new LucteriosException("Erreur de Header",aWebFile,value,ioe);
			}
		}		
		return size;
	}
	
	public String transfertFileFromServerString(String aWebFile, Map aParams)
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

	public String transfertXMLFromServer(String aXmlParam)
			throws LucteriosException {
		String data = "";
		synchronized (mSynchronizedObj) {
			String xml_param = "<?xml version='1.0' encoding='" + ENCODE
					+ "'?>";
			xml_param = xml_param + "<REQUETES>\n";
			if (!"".equals( mSession )) {
				xml_param = xml_param
						+ "<REQUETE extension='common' action='authentification'>";
				xml_param = xml_param + "<PARAM name='ses' type='str'>"
						+ mSession + "</PARAM>";
				xml_param = xml_param + "</REQUETE>";
			}
			xml_param = xml_param + aXmlParam;
			xml_param = xml_param + "</REQUETES>";
			Logging.getInstance().setInText(xml_param);

			Map params = new TreeMap();
			params.put(POST_VARIABLE, xml_param);
			//params.put("nourlencode","");
			try {
				data = "<?xml version='1.0' encoding='ISO-8859-1'?>";
				data = data	+ transfertFileFromServerString(MANAGER_FILE, params);
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
				throw new TransportException(data,TransportException.TYPE_HTTP, 2, aXmlParam, data);
			Logging.getInstance().setOutText(data);
		}
		return data;
	}

	public void openPageInWebBrowser(String pageName) throws LucteriosException {
		java.net.URL page_url = getUrl(pageName);
		DesktopTools desktop = new DesktopTools();
		desktop.launch(page_url.toString());
	}
}
