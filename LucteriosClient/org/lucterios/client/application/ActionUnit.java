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

package org.lucterios.client.application;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.engine.presentation.ObserverConstant;
import org.lucterios.engine.presentation.ObserverFactoryMock;
import org.lucterios.engine.presentation.ObserverStub;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.transport.HttpTransportStub;
import org.lucterios.utils.SimpleParsing;

import junit.framework.TestCase;

public class ActionUnit extends TestCase {
	private Action mAction;

	protected void setUp() throws Exception {
		super.setUp();
		ActionImpl.mWindowGenerator = new WindowGeneratorFeak();
		mAction = new ActionImpl();
		Singletons.HttpTransportClass = HttpTransportStub.class;
		ObserverStub.mParameters.clear();
		ObserverStub.LastActionId = "";
		ObserverStub.LastSelect = ActionConstantes.SELECT_NONE;
		ObserverStub.mShow = false;
		ObserverStub.mTitle = "";

		ObserverFactoryMock.LastExtension = "";
		ObserverFactoryMock.LastAction = "";
		ObserverFactoryMock.LastParam = null;
	}

	private void sleepOneTime() {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testEmpty() {
		SimpleParsing action = new SimpleParsing();
		mAction.initialize(null, null, action);

		assertEquals("Titre", "", mAction.getTitle());
		assertEquals("Mnemonic", 0, mAction.getMnemonic());
		assertEquals("Extension", "", mAction.getExtension());
		assertEquals("Action", "", mAction.getAction());
		assertEquals("Icon", null, mAction.getIcon());
		assertEquals("Modal", ActionConstantes.FORM_NOMODAL, mAction
				.getFormType());
		assertEquals("Close", true, mAction.getClose());
		assertEquals("Unique", ActionConstantes.SELECT_NONE, mAction
				.getSelect());
	}

	public void testAction() {
		SimpleParsing action = new SimpleParsing();
		action.parse("<ACTION icon='images/edit.png' extension='CORE' action='extension_params_APAS_modifier' close='0' modal='1' unique='1'><![CDATA[_Modifier]]></ACTION>");
		mAction.initialize(null, null, action);

		assertEquals("Titre", "Modifier", mAction.getTitle());
		assertEquals("Mnemonic", 'M', mAction.getMnemonic());
		assertEquals("Extension", "CORE", mAction.getExtension());
		assertEquals("Action", "extension_params_APAS_modifier", mAction
				.getAction());
		assertEquals("Icon", null, mAction.getIcon());
		assertEquals("Modal", ActionConstantes.FORM_MODAL, mAction
				.getFormType());
		assertEquals("Close", false, mAction.getClose());
		assertEquals("Unique", ActionConstantes.SELECT_NONE, mAction
				.getSelect());
	}

	public void testMenu() {
		SimpleParsing action = new SimpleParsing();
		action
				.parse("<MENU id='Im_pressionsauvegardees' extension='CORE' action='finalreport_APAS_list'><![CDATA[Im_pression sauvegardees]]></MENU>");
		mAction.initialize(null, null, action);

		assertEquals("ID", "Im_pressionsauvegardees", mAction.getID());
		assertEquals("Titre", "Impression sauvegardees", mAction.getTitle());
		assertEquals("Mnemonic", 'p', mAction.getMnemonic());
		assertEquals("Extension", "CORE", mAction.getExtension());
		assertEquals("Action", "finalreport_APAS_list", mAction.getAction());
		assertEquals("Icon", null, mAction.getIcon());
		assertEquals("Modal", ActionConstantes.FORM_NOMODAL, mAction
				.getFormType());
		assertEquals("Close", true, mAction.getClose());
		assertEquals("Unique", ActionConstantes.SELECT_NONE, mAction
				.getSelect());
	}

	public void testSimple() {
		ObserverStub.mType = ObserverConstant.TYPE_NONE;
		ObserverFactoryMock fact = new ObserverFactoryMock();
		ObserverFactoryMock.NewObserver = new ObserverStub();

		SimpleParsing action = new SimpleParsing();
		action
				.parse("<ACTION extension='CORE' action='extension_params_APAS_modifier' close='0' modal='1'><![CDATA[_Modifier]]></ACTION>");
		mAction.initialize(null, fact, action);
		mAction.actionPerformed();
		sleepOneTime();

		assertEquals("extension", "CORE", ObserverFactoryMock.LastExtension);
		assertEquals("action", "extension_params_APAS_modifier",
				ObserverFactoryMock.LastAction);
		assertEquals("Params", 0, ObserverFactoryMock.LastParam.size());
		assertTrue("Show", ObserverStub.mShow);
		assertEquals("Title", "Modifier", ObserverStub.mTitle);
		assertTrue("Dialog",
				ObserverFactoryMock.NewObserver.getGUIDialog() == null);
		assertTrue("Frame",
				ObserverFactoryMock.NewObserver.getGUIFrame() == null);
	}

	/*
	 * public void testNewDialog() throws LucteriosException {
	 * ObserverStub.mType = ObserverConstant.TYPE_BOTH; ObserverFactoryMock fact
	 * = new ObserverFactoryMock(); ObserverFactoryMock.NewObserver = new
	 * ObserverStub();
	 * 
	 * SimpleParsing action = new SimpleParsing(); action.parse(
	 * "<ACTION extension='CORE' action='extension_params_APAS_modifier' close='0' modal='1'><![CDATA[_Modifier]]></ACTION>"
	 * ); mAction.initialize(null, fact, action); mAction.actionPerformed(null);
	 * sleepOneTime();
	 * 
	 * assertEquals("extension", "CORE", ObserverFactoryMock.LastExtension);
	 * assertEquals("action", "extension_params_APAS_modifier",
	 * ObserverFactoryMock.LastAction); assertEquals("Params", 0,
	 * ObserverFactoryMock.LastParam.size()); assertEquals("Modal",
	 * ActionConstantes.FORM_MODAL, mAction .getFormType()); assertTrue("Show",
	 * ObserverStub.mShow); assertEquals("Title", "Modifier",
	 * ObserverStub.mTitle); assertTrue("Frame",
	 * ObserverFactoryMock.NewObserver.getGUIFrame() == null);
	 * assertTrue("Dialog", ObserverFactoryMock.NewObserver.getGUIDialog() !=
	 * null); assertTrue("Visible",
	 * ObserverFactoryMock.NewObserver.getGUIDialog() .isVisible());
	 * 
	 * ObserverFactoryMock.NewObserver.close(true); }
	 * 
	 * public void testNewFrame() throws LucteriosException { ObserverStub.mType
	 * = ObserverConstant.TYPE_BOTH; ObserverFactoryMock fact = new
	 * ObserverFactoryMock(); ObserverFactoryMock.NewObserver = new
	 * ObserverStub();
	 * 
	 * SimpleParsing action = new SimpleParsing(); action.parse(
	 * "<ACTION extension='CORE' action='extension_params_APAS_modifier' close='0' modal='0'><![CDATA[_Modifier]]></ACTION>"
	 * ); mAction.initialize(null, fact, action); mAction.actionPerformed(null);
	 * sleepOneTime();
	 * 
	 * assertEquals("extension", "CORE", ObserverFactoryMock.LastExtension);
	 * assertEquals("action", "extension_params_APAS_modifier",
	 * ObserverFactoryMock.LastAction); assertEquals("Params", 0,
	 * ObserverFactoryMock.LastParam.size()); assertEquals("Modal",
	 * ActionConstantes.FORM_NOMODAL, mAction .getFormType());
	 * assertTrue("Show", ObserverStub.mShow); assertEquals("Title", "Modifier",
	 * ObserverStub.mTitle); assertTrue("Dialog",
	 * ObserverFactoryMock.NewObserver.getGUIDialog() == null);
	 * assertTrue("Frame", ObserverFactoryMock.NewObserver.getGUIFrame() !=
	 * null); assertTrue("Visible",
	 * ObserverFactoryMock.NewObserver.getGUIFrame() .isVisible());
	 * 
	 * ObserverFactoryMock.NewObserver.close(true); }
	 */

	public void testCloseParent() {
		ObserverStub obs_parent = new ObserverStub();
		ObserverStub.mParameters.put("abc", "123");

		ObserverStub.mType = ObserverConstant.TYPE_NONE;
		ObserverFactoryMock fact = new ObserverFactoryMock();
		ObserverFactoryMock.NewObserver = new ObserverStub();

		SimpleParsing action = new SimpleParsing();
		action
				.parse("<ACTION extension='CORE' action='extension_params_APAS_modifier' close='1' modal='1'><![CDATA[_Modifier]]></ACTION>");
		mAction.initialize(obs_parent, fact, action);
		mAction.actionPerformed();
		sleepOneTime();

		assertEquals("extension", "CORE", ObserverFactoryMock.LastExtension);
		assertEquals("action", "extension_params_APAS_modifier",
				ObserverFactoryMock.LastAction);
		assertEquals("Params", 1, ObserverFactoryMock.LastParam.size());
		assertEquals("Param 0", "123", ObserverFactoryMock.LastParam.get("abc"));
		assertEquals("Close", true, mAction.getClose());
		assertEquals("Unique", ActionConstantes.SELECT_NONE, mAction
				.getSelect());
		assertTrue("Show", ObserverStub.mShow);
		assertEquals("Title", "Modifier", ObserverStub.mTitle);
		assertTrue("Parent",
				ObserverFactoryMock.NewObserver.getParent() == null);

		assertTrue("Close", obs_parent.mClose);
		assertEquals("Select", ActionConstantes.SELECT_NONE,
				ObserverStub.LastSelect);
	}

	public void testNoCloseParent() {
		ObserverStub obs_parent = new ObserverStub();
		ObserverStub.mParameters.put("xyz", "456");
		ObserverStub.mParameters.put("ijk", "987");

		ObserverStub.mType = ObserverConstant.TYPE_NONE;
		ObserverFactoryMock fact = new ObserverFactoryMock();
		ObserverFactoryMock.NewObserver = new ObserverStub();

		SimpleParsing action = new SimpleParsing();
		action
				.parse("<ACTION extension='CORE' action='extension_params_APAS_modifier' close='0' modal='1' unique='0'><![CDATA[_Modifier]]></ACTION>");
		mAction.initialize(obs_parent, fact, action);
		mAction.actionPerformed();
		sleepOneTime();

		assertEquals("extension", "CORE", ObserverFactoryMock.LastExtension);
		assertEquals("action", "extension_params_APAS_modifier",
				ObserverFactoryMock.LastAction);
		assertEquals("Params", 2, ObserverFactoryMock.LastParam.size());
		assertEquals("Param 0", "456", ObserverFactoryMock.LastParam.get("xyz"));
		assertEquals("Param 1", "987", ObserverFactoryMock.LastParam.get("ijk"));
		assertEquals("Close", false, mAction.getClose());
		assertEquals("Unique", ActionConstantes.SELECT_SINGLE, mAction
				.getSelect());
		assertTrue("Show", ObserverStub.mShow);
		assertEquals("Title", "Modifier", ObserverStub.mTitle);
		assertTrue("Parent", ObserverFactoryMock.NewObserver.getParent()
				.equals(obs_parent));

		assertFalse("Close", obs_parent.mClose);
		assertEquals("Select", ActionConstantes.SELECT_SINGLE,
				ObserverStub.LastSelect);
	}

	public void testOnlyCloseParent() {
		ObserverStub obs_parent = new ObserverStub();
		ObserverStub.mParameters.put("abc", "123");

		ObserverStub.mType = ObserverConstant.TYPE_NONE;
		ObserverFactoryMock fact = new ObserverFactoryMock();
		ObserverFactoryMock.NewObserver = null;

		SimpleParsing action = new SimpleParsing();
		action.parse("<ACTION close='1' modal='0'><![CDATA[_Close]]></ACTION>");
		mAction.initialize(obs_parent, fact, action);
		mAction.actionPerformed();
		sleepOneTime();

		assertEquals("extension", "", ObserverFactoryMock.LastExtension);
		assertEquals("action", "", ObserverFactoryMock.LastAction);
		assertTrue("Params", ObserverFactoryMock.LastParam == null);
		assertEquals("Close", true, mAction.getClose());
		assertEquals("Modal", ActionConstantes.FORM_NOMODAL, mAction
				.getFormType());
		assertEquals("Unique", ActionConstantes.SELECT_NONE, mAction
				.getSelect());
		assertFalse("Show", ObserverStub.mShow);
		assertEquals("Title", "", ObserverStub.mTitle);

		assertTrue("Close", obs_parent.mClose);
	}
}
