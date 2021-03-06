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

package org.lucterios.engine;

import org.lucterios.engine.transport.HttpTranportUnit;
import org.lucterios.engine.presentation.ObserverFactoryUnit;
import org.lucterios.engine.application.ActionUnit;
import org.lucterios.engine.application.observer.ObserverUnit;
import org.lucterios.utils.Logging;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTest {
	public static Test suite() {
		Logging.getInstance().setDebugLevel("DEBUG");
		TestSuite suite = new TestSuite("Client");
		suite.addTest(new TestSuite(HttpTranportUnit.class));
		suite.addTest(new TestSuite(ObserverFactoryUnit.class));
		suite.addTest(new TestSuite(ActionUnit.class));
		suite.addTest(new TestSuite(ObserverUnit.class));
		return suite;
	}
}
