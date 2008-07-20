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

import java.util.Map;
import java.util.TreeMap;

import org.lucterios.client.transport.HttpTransportStub;
import org.lucterios.utils.LucteriosException;

import junit.framework.TestCase;

public class ObserverFactoryUnit extends TestCase {
	private ObserverFactory mObserverFactory;
	private HttpTransportStub mHttpTransport;

	protected void setUp() throws Exception {
		super.setUp();
		ObserverStub.ObserverName = "ObserverStub";

		mHttpTransport = new HttpTransportStub();
		mObserverFactory = new ObserverFactoryImpl();
		mObserverFactory.setHttpTransport(mHttpTransport);
		mObserverFactory.clearObserverList();

		mObserverFactory.AddObserver("CORE.Auth", ObserverStub.class);
		mObserverFactory.AddObserver("Core.Menu", ObserverStub.class);
		mObserverFactory.AddObserver("Core.DialogBox", ObserverStub.class);
		mObserverFactory.AddObserver("Core.Custom", ObserverStub.class);
	}

	public void testCallAction() throws LucteriosException {
		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sûre de réinitialiser ce modèle?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>";

		Observer obs = mObserverFactory.callAction("CORE",
				"printmodel_APAS_reinit", new TreeMap());
		assertTrue("Observer", obs != null);
		assertEquals("Action", "printmodel_APAS_reinit", obs.getSourceAction());
		assertEquals("Extension", "CORE", obs.getSourceExtension());
		assertEquals("Context NB", 2, obs.getContext().size());
		assertEquals("Context 1", "107", (String) obs.getContext().get(
				"print_model"));
		assertEquals("Context 2", "YES", (String) obs.getContext().get(
				"CONFIRME"));
		assertEquals(
				"Content",
				"<TEXT type='2'><![CDATA[Etes-vous s�re de r�initialiser ce mod�le?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS>",
				obs.getContentText());

		assertEquals(
				"XmlParam",
				"<REQUETE extension='CORE' action='printmodel_APAS_reinit'></REQUETE>",
				mHttpTransport.XmlParam);
	}

	public void testRefresh() throws LucteriosException {
		ObserverStub.ObserverName = "Core.DialogBox";
		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sûre de réinitialiser ce modèle?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>";

		ObserverStub obs = new ObserverStub();
		obs.setSource("CORE", "printmodel_APAS_reinit");
		obs.setContent(null);
		obs.mContext = new TreeMap();
		obs.mContext.clear();
		obs.mContext.put("print_model", "107");
		obs.mContext.put("CONFIRME", "YES");

		mObserverFactory.callAction(obs.getSourceExtension(), obs
				.getSourceAction(), obs.getContext(), obs);

		assertEquals("Action", "printmodel_APAS_reinit", obs.getSourceAction());
		assertEquals("Extension", "CORE", obs.getSourceExtension());
		assertEquals("Context NB", 2, obs.getContext().size());
		assertEquals("Context 1", "107", (String) obs.getContext().get(
				"print_model"));
		assertEquals("Context 2", "YES", (String) obs.getContext().get(
				"CONFIRME"));
		assertEquals(
				"Content",
				"<TEXT type='2'><![CDATA[Etes-vous s�re de r�initialiser ce mod�le?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS>",
				obs.getContentText());

		assertEquals(
				"XmlParam",
				"<REQUETE extension='CORE' action='printmodel_APAS_reinit'><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM><PARAM name='print_model'><![CDATA[107]]></PARAM></REQUETE>",
				mHttpTransport.XmlParam);
	}

	public void testCallActionWithParam() throws LucteriosException {
		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.DialogBox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sûre de réinitialiser ce modèle?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>";

		Map params = new TreeMap();
		params.put("print_model", "107");
		params.put("CONFIRME", "YES");
		Observer obs = mObserverFactory.callAction("CORE",
				"printmodel_APAS_reinit", params);
		assertEquals("Action", "ObserverStub", obs.getObserverName());
		assertEquals(
				"XmlParam",
				"<REQUETE extension='CORE' action='printmodel_APAS_reinit'><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM><PARAM name='print_model'><![CDATA[107]]></PARAM></REQUETE>",
				mHttpTransport.XmlParam);
	}

	public void testCallActionBadXmlReceived() {
		mHttpTransport.XmlReceved = "Error php in truc.inc.php line 123";

		try {
			Observer obs = mObserverFactory.callAction("CORE",
					"printmodel_APAS_reinit", new TreeMap());
			assertTrue(obs == null);
			assertTrue(false);
		} catch (LucteriosException e) {
			assertEquals("Erreur de parsing xml", e.getMessage());
		}
	}

	public void testCallActionBadObserver() {
		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='Core.BialogDox' source_extension='CORE' source_action='printmodel_APAS_reinit'><CONTEXT><PARAM name='print_model'><![CDATA[107]]></PARAM><PARAM name='CONFIRME'><![CDATA[YES]]></PARAM></CONTEXT><TEXT type='2'><![CDATA[Etes-vous sûre de réinitialiser ce modèle?]]></TEXT><ACTIONS><ACTION icon='images/ok.png' extension='CORE' action='printmodel_APAS_reinit'><![CDATA[Oui]]></ACTION><ACTION icon='images/cancel.png'><![CDATA[Non]]></ACTION></ACTIONS></REPONSE></REPONSES>";

		try {
			Observer obs = mObserverFactory.callAction("CORE",
					"printmodel_APAS_reinit", new TreeMap());
			assertTrue(obs == null);
			assertTrue(false);
		} catch (LucteriosException e) {
			assertEquals("Observeur 'Core.BialogDox' inconnu", e.getMessage());
		}
	}

	public void testAuthentification() throws LucteriosException {
		ObserverStub.ObserverName = "CORE.Auth";

		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Auth' source_extension='CORE' source_action='authentification'><![CDATA[NEEDAUTH]]></REPONSE></REPONSES>";
		assertFalse("Bad", mObserverFactory.setAuthentification("abc", "123"));
		assertEquals("Session Bad", "", mHttpTransport.getSession());
		assertEquals(
				"param Bad",
				"<REQUETE extension='common' action='authentification'><PARAM name='login'><![CDATA[abc]]></PARAM><PARAM name='pass'><![CDATA[123]]></PARAM></REQUETE>",
				mHttpTransport.XmlParam);

		mHttpTransport.XmlReceved = "<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Auth' source_extension='CORE' source_action='authentification'><CONNECTION></CONNECTION><PARAM name='ses' type='str'>admin1176588477</PARAM><![CDATA[OK]]></REPONSE></REPONSES>";
		assertTrue("OK", mObserverFactory.setAuthentification("admin", "admin"));
		assertEquals("Session OK", "admin1176588477", mHttpTransport
				.getSession());
		assertEquals(
				"param OK",
				"<REQUETE extension='common' action='authentification'><PARAM name='login'><![CDATA[admin]]></PARAM><PARAM name='pass'><![CDATA[admin]]></PARAM></REQUETE>",
				mHttpTransport.XmlParam);
	}
}
