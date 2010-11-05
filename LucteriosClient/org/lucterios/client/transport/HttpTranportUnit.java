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

import javax.swing.ImageIcon;

import org.lucterios.client.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;

import junit.framework.TestCase;

public class HttpTranportUnit extends TestCase {
	HttpTransport http_transport;

	protected void setUp() throws Exception {
		super.setUp();
		http_transport = new HttpTransportImpl();
	}
	
	private MapContext getParam(String aText){
		MapContext result=new MapContext();
		result.put(HttpTransport.POST_VARIABLE,aText);
		return result;
	}

	protected void tearDown() throws Exception {
		http_transport.closeConnection();
		http_transport = null;
		super.tearDown();
	}

	public void testConnection() {
		assertEquals("Server connect a", "", http_transport.getServerHost());
		assertEquals("Path connect a", "", http_transport.getRootPath());
		assertEquals("Port connect a", 0, http_transport.getCurrentPort());
		assertEquals("Session initial", "", http_transport.getSession());

		http_transport.connectToServer("projets.lucterios.org", "lucterios", 80, false);
		http_transport.setSession("ABCDEF12345");
		assertEquals("Server connect b", "projets.lucterios.org", http_transport
				.getServerHost());
		assertEquals("Path connect b", "/lucterios", http_transport.getRootPath());
		assertEquals("Port connect b", 80, http_transport.getCurrentPort());
		assertEquals("Session final", "ABCDEF12345", http_transport
				.getSession());
	}

	public void testStatic() {
		http_transport.connectToServer("projets.lucterios.org", "lucterios", 80, false);
		http_transport.setSession("ABCDEF12345");

		HttpTransport new_http_transport = new HttpTransportImpl();
		assertEquals("Server connect b", "projets.lucterios.org", new_http_transport
				.getServerHost());
		assertEquals("Path connect b", "/lucterios", new_http_transport
				.getRootPath());
		assertEquals("Port connect b", 80, new_http_transport.getCurrentPort());
		assertEquals("Session final", "ABCDEF12345", new_http_transport
				.getSession());
	}

	public void testImage() {
		http_transport.connectToServer("projets.lucterios.org", "lucterios", 80, false);
		ImageIcon null_icon = http_transport.getIcon(null,0);
		assertTrue("Null icon", null_icon == null);

		ImageIcon empty_icon = http_transport.getIcon("",0);
		assertTrue("Empty icon", empty_icon == null);

		ImageIcon add_icon = http_transport.getIcon("images/add.png",0);
		assertTrue("Add icon", add_icon != null);
		assertEquals("Add Height", 22, add_icon.getIconHeight());
		assertEquals("Add Width", 22, add_icon.getIconWidth());
	}

	public void testActions() throws LucteriosException {
		http_transport.connectToServer("projets.lucterios.org", "lucterios", 80, false);
		String xml_retour;
		xml_retour = http_transport.transfertXMLFromServer(getParam("<REQUETE extension='CORE' action='menu'></REQUETE>"));
		xml_retour = xml_retour.replaceAll("\n", "");
		System.out.println("1er reponse:" + xml_retour);
		assertEquals(
				"1er reponse",
				"<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Auth' source_extension='CORE' source_action='authentification'><![CDATA[NEEDAUTH]]></REPONSE></REPONSES>",
				xml_retour);

		xml_retour = http_transport.transfertXMLFromServer(getParam("<REQUETE extension='common' action='authentification'><PARAM name='login'>admin</PARAM><PARAM name='pass'>admin</PARAM></REQUETE>"));
		xml_retour = xml_retour.replaceAll("\n", "");
		System.out.println("2eme reponse:" + xml_retour);
		assertTrue("2eme reponse size", 140 <= xml_retour.length());
		assertEquals(
				"2eme reponse",
				"<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Auth' source_extension='CORE' source_action='authentification'>",
				xml_retour.substring(0, 140));
		int pos_in = xml_retour.indexOf("<PARAM name='ses' type='str'>");
		int pos_out = xml_retour.indexOf("</PARAM>", pos_in);
		String session = xml_retour.substring(pos_in + 29, pos_out);
		System.out.println("Session:" + session);
		http_transport.setSession(session);

		xml_retour = http_transport.transfertXMLFromServer(getParam("<REQUETE extension='CORE' action='menu'></REQUETE>"));
		xml_retour = xml_retour.replaceAll("\n", "");
		System.out.println("3eme reponse:" + xml_retour);
		assertTrue("3eme reponse size", 128 <= xml_retour.length());
		assertEquals(
				"3eme reponse:"+xml_retour.substring(0, 256),
				"<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Menu' source_extension='CORE' source_action='menu'>",
				xml_retour.substring(0, 128));
	}

	public void testActionsSecurity() throws LucteriosException {
		http_transport.connectToServer("projets.lucterios.org", "lucterios", 443, true);
		String xml_retour;
		xml_retour = http_transport.transfertXMLFromServer(getParam("<REQUETE extension='CORE' action='menu'></REQUETE>"));
		xml_retour = xml_retour.replaceAll("\n", "");
		System.out.println("1er reponse:" + xml_retour);
		assertEquals(
				"1er reponse",
				"<?xml version='1.0' encoding='ISO-8859-1'?><REPONSES><REPONSE observer='CORE.Auth' source_extension='CORE' source_action='authentification'><![CDATA[NEEDAUTH]]></REPONSE></REPONSES>",
				xml_retour);
	}
}
