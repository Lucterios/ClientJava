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

import java.io.IOException;

import org.lucterios.client.transport.HttpTransport;
import org.lucterios.client.transport.HttpTransportImpl;
import org.lucterios.client.utils.LucteriosConfiguration;

public class Singletons {
	
	public interface ApplicationTerminate{
		void exit();
	}
	
	static public ApplicationTerminate AppTerminate=null;
	
	static public Class HttpTransportClass=null;

	static public HttpTransport Transport() {
		HttpTransport new_transport = null;
		try {
			new_transport = (HttpTransport) HttpTransportClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new_transport;
	}

	static private ObserverFactory mFactory=null;

	static public ObserverFactory Factory() {
		mFactory.setHttpTransport(Transport());
		return mFactory;
	}

	static public LucteriosConfiguration Configuration = null;

	static public void initalize() throws IOException {
		HttpTransportClass = HttpTransportImpl.class;
		Configuration = new LucteriosConfiguration();
		mFactory = new ObserverFactoryImpl();
	}
	
	static public void exit(){
		if (AppTerminate!=null)
			AppTerminate.exit();
		System.exit(0);
	}
}
