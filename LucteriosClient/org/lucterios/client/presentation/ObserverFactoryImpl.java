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

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.lucterios.client.transport.HttpTransport;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;

public class ObserverFactoryImpl implements ObserverFactory {
	private HttpTransport mTransport = null;
	static private Map mObservers = new TreeMap();

	public ObserverFactoryImpl() {
		super();
	}

	public void setHttpTransport(HttpTransport aTransport) {
		mTransport = aTransport;
	}

	public HttpTransport getHttpTransport() {
		return mTransport;
	}

	public void clearObserverList() {
		mObservers.clear();
	}

	public void AddObserver(String aObserverName, Class aObserver) {
		mObservers.put(aObserverName, aObserver);
	}

	public boolean setAuthentification(String aLogin, String aPassWord)
			throws LucteriosException {
		boolean res = false;
		Map param = new TreeMap();
		param.put("login", aLogin);
		param.put("pass", aPassWord);
		Observer aut = callAction("common", "authentification", param, null);
		if ("CORE.Auth".equals( aut.getObserverName() )) {
			SimpleParsing xml_aut = aut.getContent();
			SimpleParsing xml_param = xml_aut.getFirstSubTag("PARAM");
			if (xml_param != null) {
				String session = xml_param.getText();
				res = (session != null);
				if (res) {
					mTransport.setSession(session);
					if (!session.equals( "" ))
						WatchDog.runWatchDog(mTransport);
					else
						WatchDog.runWatchDog(null);
				}
			}
			aut.show("");
		}
		return res;
	}

	public Observer callAction(String aExtension, String aAction, Map aParam)
			throws LucteriosException {
		return callAction(aExtension, aAction, aParam, null);
	}

	private String m_XMLParameters="";
	protected Map convertParameters(String aExtension, String aAction, Map aParam) {
		Map result=new TreeMap();
		m_XMLParameters = "<REQUETE extension='" + aExtension + "' action='" + aAction + "'>";

		for (Iterator iterator = aParam.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String key = (String) entry.getKey();
			Object value_obj = entry.getValue();
			if (File.class.isInstance(value_obj))
				result.put(key,value_obj);
			String value = value_obj.toString();
			m_XMLParameters = m_XMLParameters + "<PARAM name='" + key + "'><![CDATA[" + value + "]]></PARAM>";
		}
		m_XMLParameters = m_XMLParameters + "</REQUETE>";
		result.put(HttpTransport.POST_VARIABLE,m_XMLParameters);
		return result;
	}

	private Map getContext(SimpleParsing aReponse) {
		Map context = new TreeMap();
		SimpleParsing xmlcontext = aReponse.getFirstSubTag("CONTEXT");
		if (xmlcontext != null) {
			SimpleParsing xmlparams[] = xmlcontext.getSubTag("PARAM");
			for (int index = 0; index < xmlparams.length; index++)
				context.put(xmlparams[index].getAttribut("name"),
						xmlparams[index].getText());
		}
		return context;
	}

	protected Observer factoryObserver(String aObserverName, String aParamTxt,
			String aXmlText) throws LucteriosException {
		Observer res_obs = null;
		Class observ = (Class) mObservers.get(aObserverName);
		try {
			res_obs = (Observer) observ.newInstance();
		} catch (InstantiationException e) {
			throw new LucteriosException("Observeur non créé", aParamTxt,
					aXmlText, e);
		} catch (IllegalAccessException e) {
			throw new LucteriosException("Observeur non créé", aParamTxt,
					aXmlText, e);
		}
		return res_obs;
	}

	public Observer callAction(String aExtension, String aAction, Map aParam,
			Observer aObserver) throws LucteriosException {
		if (mTransport == null)
			throw new LucteriosException("Transport null");
		Observer res_obs = null;
		String xml_text = mTransport.transfertXMLFromServer(convertParameters(aExtension, aAction, aParam));
		SimpleParsing parse = new SimpleParsing();
		if (parse.parse(xml_text)) {
			SimpleParsing rep = parse.getFirstSubTag("REPONSE");
			String observer_name = (rep!=null)?rep.getAttribut("observer"):"";
			if (mObservers.containsKey(observer_name)) {
				String source_extension = (rep!=null)?rep.getAttribut("source_extension"):"";
				String source_action = (rep!=null)?rep.getAttribut("source_action"):"";
				if (aObserver == null) {
					res_obs = factoryObserver(observer_name, m_XMLParameters,
							xml_text);
					res_obs.setSource(source_extension, source_action);
				} else {
					res_obs = aObserver;
					if (!res_obs.getObserverName().equals(observer_name)
							|| "".equals( source_action )
							|| "".equals( source_extension )) {
						if (observer_name.equalsIgnoreCase("CORE.Auth")
								|| observer_name
										.equalsIgnoreCase("CORE.Exception")) {
							res_obs = factoryObserver(observer_name, m_XMLParameters,
									xml_text);
							res_obs.setSource(source_extension, source_action);
						} else
							throw new LucteriosException(
									"Erreur de parsing xml", m_XMLParameters,
									xml_text);
					}
				}
				if (rep!=null) {
					res_obs.setCaption(rep.getCDataOfFirstTag("TITLE"));
					res_obs.setContext(getContext(rep));
					res_obs.setContent(rep);
				}
			}
			else
				throw new LucteriosException("Observeur '" + observer_name
						+ "' inconnu", m_XMLParameters, xml_text);
		} else
			throw new LucteriosException("Erreur de parsing xml", m_XMLParameters,
					xml_text);
		return res_obs;
	}

	public void refreshAction(Observer aObserver) throws LucteriosException {
		if (mTransport == null)
			throw new LucteriosException("Transport null");
		String xml_text = mTransport.transfertXMLFromServer(convertParameters(aObserver.getSourceExtension(),
				aObserver.getSourceAction(), aObserver.getContext()));
		SimpleParsing parse = new SimpleParsing();
		if (parse.parse(xml_text)) {
			SimpleParsing rep = parse.getFirstSubTag("REPONSE");
			String observer_name = rep.getAttribut("observer");
			String source_extension = rep.getAttribut("source_extension");
			String source_action = rep.getAttribut("source_action");
			if (aObserver.getObserverName().equals(observer_name)
					&& aObserver.getSourceAction().equals(source_action)
					&& aObserver.getSourceExtension().equals(source_extension)) {
				aObserver.setContext(getContext(rep));
				aObserver.setContent(rep);
				aObserver.show(null);
			} else {
				if (observer_name.equalsIgnoreCase("CORE.Auth")
						|| observer_name.equalsIgnoreCase("CORE.Exception")) {
					Observer res_obs = factoryObserver(observer_name,
							m_XMLParameters, xml_text);
					res_obs.setSource(source_extension, source_action);
					res_obs.setContext(getContext(rep));
					res_obs.setContent(rep);
					res_obs.show(null);
				} else
					throw new LucteriosException("Observeur non respecté",
							m_XMLParameters, xml_text);
			}
		} else
			throw new LucteriosException("Erreur de parsing xml", m_XMLParameters,
					xml_text);
	}
}
