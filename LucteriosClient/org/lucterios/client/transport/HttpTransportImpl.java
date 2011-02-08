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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Set;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.engine.transport.HttpTransportAbstract;
import org.lucterios.engine.transport.TransportException;
import org.lucterios.utils.Logging;
import org.lucterios.utils.LucteriosException;

public class HttpTransportImpl extends HttpTransportAbstract {

	private org.apache.commons.httpclient.HttpClient m_Cnx = null;

	public HttpTransportImpl() {
		super();
		Protocol.registerProtocol("https",new Protocol("https", new EasySSLProtocolSocketFactory(),443));
		m_Cnx = new org.apache.commons.httpclient.HttpClient();
	}

	private HostConfiguration getHostCnx() {
		HostConfiguration host_cnx = new HostConfiguration();
		if ((!"".equals( mProxyServer )) && (mProxyPort != 0))
			host_cnx.setProxy(mProxyServer, mProxyPort);
		return host_cnx;
	}

	private PostMethod method = null;
	private HeadMethod header = null;

	protected void downloadFinished() {
		if (method != null)
			method.releaseConnection();
		method = null;
		if (header != null)
			header.releaseConnection();
		header = null;
	}

	protected InputStream transfertFileFromServer(String aWebFile, MapContext aParams)
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
								.getReasonPhrase()+":"+path_url.toString(),
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

	private String getMethodByParameters(MapContext aParams, java.net.URL aPathUrl) throws FileNotFoundException {
		String param_txt = "";
		method = new PostMethod(aPathUrl.toString());
		if (aParams != null) {
			Set<String> keys = aParams.keySet();
			Part[] parts = new Part[keys.size()];
			int data_idx=0;
			for (String key:keys) {
				Object obj=aParams.get(key);
				if (File.class.isInstance(obj))
					parts[data_idx++]=new FilePart(key,(File)obj);
				else
					parts[data_idx++]=new StringPart(key,obj.toString());		
				param_txt += obj.toString();
			}
			method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		}
		return param_txt;
	}

	public int getFileLength(String aWebFile) throws LucteriosException {
		Logging.getInstance().writeLog("### HEADER ###",aWebFile,2);
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

}
