/**
 * 
 */
package org.lucterios.client.presentation;

import java.util.Map;

import org.lucterios.client.transport.HttpTransport;
import org.lucterios.utils.LucteriosException;

/**
 * @author lag
 * 
 */
public class ObserverFactoryMock implements ObserverFactory {
	public ObserverFactoryMock() {
		super();
	}

	public void setHttpTransport(HttpTransport aTransport) {
	}

	public void clearObserverList() {
	}

	public void AddObserver(String aObserverName, Class<? extends Observer> aObserver) {
	}

	public HttpTransport getHttpTransport() {
		return null;
	}

	static public String LastLogin = "";
	static public String LastPassWord = "";
	static public boolean NewAuthentification = true;

	public boolean setAuthentification(String aLogin, String aPassWord)
			throws LucteriosException {
		LastLogin = aLogin;
		LastPassWord = aPassWord;
		return NewAuthentification;
	}

	public Observer callAction(String aExtension, String aAction, Map<String,Object> aParam)
			throws LucteriosException {
		return callAction(aExtension, aAction, aParam, null);
	}

	static public String LastExtension = "";
	static public String LastAction = "";
	static public Map<String,Object> LastParam = null;
	static public Observer OldObserver = null;
	static public Observer NewObserver = null;

	public Observer callAction(String aExtension, String aAction, Map<String,Object> aParam,
			Observer aObserver) throws LucteriosException {
		OldObserver = aObserver;
		LastExtension = aExtension;
		LastAction = aAction;
		LastParam = aParam;
		return NewObserver;
	}
}
